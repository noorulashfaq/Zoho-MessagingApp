package com.messageprocessingapp.repository;

import com.messageprocessingapp.interfaces.IMessageRepository;
import com.messageprocessingapp.models.Message;
import com.messageprocessingapp.models.MessageResponse;
import com.messageprocessingapp.models.MessageType;
import com.messageprocessingapp.utils.DBConnPool;
import com.messageprocessingapp.utils.UserTableAssignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository implements IMessageRepository {
    private Connection conn = null;
    @Override
    public MessageResponse addMessage(Message message) {
        try{
            conn = DBConnPool.getConn();
            String tableName = UserTableAssignment.getAssignedTable(message.getUser_id(), message.getMessage_type());

            StringBuilder sb = new StringBuilder();
            sb.append("insert into ");
            sb.append(tableName);
            sb.append(" (message_type, message_content, priority, user_id, status) values ('");
            sb.append(message.getMessage_type());
            sb.append("','");
            //sb.append(MessageEncoder.getMessageMetaData(message, tableName));
            sb.append(message.getMessage_content());
            sb.append("','");
            sb.append(message.getPriority());
            sb.append("',");
            sb.append(message.getUser_id());
            sb.append(", false");
            sb.append(") returning message_id");

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sb.toString());

            MessageResponse mr = new MessageResponse();
            mr.setUser_id(-1);
            if(rs.next()) {
                mr.setMessage_id(rs.getInt(1));
                mr.setUser_id(message.getUser_id());
                mr.setSub_table_name(tableName);
            }
            return mr;
        }catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
//        } catch(SizeLimitExceededException e){
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

//    @Override
//    public boolean deleteMessage(String tableName, int messageId) {
//        try{
//            conn = DBConnPool.getConn();
//            String sql = "delete from " + tableName + " where message_id = " + messageId;
//            Statement st = conn.createStatement();
//            int result = st.executeUpdate(sql);
//            return result > 0;
//        } catch (SQLException e){
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            DBConnPool.releaseConnection(conn);
//        }
//    }

//    @Override
//    public Message getMessage(int messageId) {
//        return null;
//    }

    @Override
    public List<MessageType> getMessageType(String messageType) {
        try{
            conn = DBConnPool.getConn();
            String sql = "select * from " + messageType;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<MessageType> messageTypes = new ArrayList<>();
            while(rs.next()){
                MessageType mt = new MessageType();
                mt.setId(rs.getInt("type_id"));
                mt.setSub_table_name(rs.getString("sub_table_name"));
                messageTypes.add(mt);
            }
            return messageTypes;
        }catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public boolean checkMessageFromUserAlreadyExists(String tableName, int user_id) {
        try{
            conn = DBConnPool.getConn();
            String sql = "select count(user_id) as count_user from " + tableName + " where user_id = " + user_id;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int result = 0;
            if(rs.next()) {
                result = rs.getInt("count_user");
            }
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public int getNumberOfMessages(String subtype) {
        try{
            conn = DBConnPool.getConn();
            String sql = "select count(message_id) as no_of_messages from " + subtype;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return rs.getInt("no_of_messages");
            }else{
                return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

//    @Override
//    public Message getRowsFromTable(String tableName) {
//        try{
//            conn = DBConnPool.getConn();
//            String sql = "select * from " + tableName + " limit 1";
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            Message message = new Message();
//            if(rs.next()){
//                message.setMessage_id(rs.getInt("message_id"));
//                message.setMessage_content(rs.getString("message_content"));
//                message.setMessage_type(rs.getString("message_type"));
//                message.setPriority(rs.getString("priority"));
//                message.setUser_id(rs.getInt("user_id"));
//                message.setPosted_at(rs.getTimestamp("posted_at"));
//            }
//            return message;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            DBConnPool.releaseConnection(conn);
//        }
//    }

    @Override
    public boolean checkIfTableIsNotEmpty(String subtable) {
        try{
            conn = DBConnPool.getConn();
            String sql = "select count(message_id) as count_of_messages from " + subtable + " where status = false";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int rowCount = 0;
            if(rs.next()){
                rowCount = rs.getInt("count_of_messages");
            }
            return rowCount > 0;
        }catch(SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public Message getOldestMessage(String tableName) {
        try{
            conn = DBConnPool.getConn();
//            String sql = "select * from " + tableName + " limit 1";
            String sql = "select * from " + tableName + " where posted_at = (select min(posted_at) from " + tableName + " where status = false)";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            Message m = new Message();
            while(rs.next()){
                m.setMessage_id(rs.getInt("message_id"));
                m.setMessage_content(rs.getString("message_content"));
                m.setMessage_type(rs.getString("message_type"));
                m.setPriority(rs.getString("priority"));
                m.setUser_id(rs.getInt("user_id"));
                m.setPosted_at(rs.getTimestamp("posted_at"));
            }
            return m;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public boolean updateStatus(String tableName, int message_id) {
        try{
            conn = DBConnPool.getConn();
//            String sql = "delete from " + tableName + " where message_id = " + messageId;
            String sql = "update " + tableName + " set status = true where message_id = " + message_id;
            Statement st = conn.createStatement();
            int result = st.executeUpdate(sql);
            return result > 0;
        } catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

}
