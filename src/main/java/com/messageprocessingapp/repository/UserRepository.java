package com.messageprocessingapp.repository;

import com.messageprocessingapp.interfaces.IUserRepository;
import com.messageprocessingapp.models.User;
import com.messageprocessingapp.utils.DBConnPool;
import com.messageprocessingapp.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository implements IUserRepository {
    private Connection conn = null;
    @Override
    public User login(String user, String password) {
        try{
            conn = DBConnPool.getConn();
            PreparedStatement ps = conn.prepareStatement("select * from users where userName = ?");
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            User loggedUser = new User();
            if(rs.next()){
                loggedUser.setUser_id(rs.getInt("user_id"));
                loggedUser.setUsername(rs.getString("userName"));
                loggedUser.setCreated_at(rs.getTimestamp("user_created_at").toString());
                loggedUser.setPassword_hash(rs.getString("password_hash"));
            }

            if(!PasswordHasher.checkPass(password, loggedUser.getPassword_hash())){
                throw new RuntimeException("Invalid credentials");
            }

            return loggedUser;

        }catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public boolean getUserByName(String username) {
        try{
            conn = DBConnPool.getConn();
            PreparedStatement ps = conn.prepareStatement("select 1 from users where userName = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }

    @Override
    public boolean createUser(String user, String password) {
        try{
            conn = DBConnPool.getConn();
            PreparedStatement ps = conn.prepareStatement("insert into users (userName, password_hash) values (?, ?)");
            ps.setString(1, user);
            ps.setString(2, PasswordHasher.hashPassword(password));
            int result = ps.executeUpdate();
            return result > 0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnPool.releaseConnection(conn);
        }
    }
}
