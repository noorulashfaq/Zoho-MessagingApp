package com.messageprocessingapp.repository;

import com.messageprocessingapp.interfaces.IMessageProcessRepository;
import com.messageprocessingapp.models.MessageProcess;
import com.messageprocessingapp.utils.DBConnPool;
import com.messageprocessingapp.utils.MessageEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageProcessRepository implements IMessageProcessRepository {
    private Connection conn = null;
    @Override
    public MessageProcess addMessageProcess(MessageProcess mp) {
        try{
            conn = DBConnPool.getConn();
            PreparedStatement ps = conn.prepareStatement("insert into messageprocess (user_id, message_content, content_length, message_type, priority, posted_at, thread_id, message_id) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, mp.getUser_id());
            ps.setString(2, MessageEncoder.decode(mp.getMessage_content()));
            ps.setDouble(3, mp.getContent_length());
            ps.setString(4, mp.getMessage_type());
            ps.setString(5, mp.getPriority());
            ps.setTimestamp(6, mp.getPosted_at());
            ps.setString(7, mp.getThread_id());
            ps.setInt(8, mp.getMessage_id());
            int result = ps.executeUpdate();
            if (result > 0){
                return mp;
            }
            return null;
        }catch(SQLException e){
            throw new RuntimeException();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

//    @Override
//    public boolean updateThreadId(String threadId, Timestamp p_id) {
//        try{
//            conn = DBConnPool.getConn();
//            PreparedStatement ps = conn.prepareStatement("update messageprocess set thread_id = ? where posted_at = ?");
//            ps.setString(1, threadId);
//            ps.setTimestamp(2, p_id);
//            int result = ps.executeUpdate();
//            return result > 0;
//        }catch(SQLException e){
//            throw new RuntimeException();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            DBConnPool.releaseConnection(conn);
//        }
//    }
}
