package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to mediate the transformation of data between the format of objects in Java to rows in a database. This is where we can use SQL queries to interact with the database.
 */

public class MessageDAO {
    /*
     * Method to create a message from a Message object. The object will not have a message_id.
     * It will return a Message object with a message_id if created successfully and return null if operation is not successful.
     */
    public Message insertMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if (pKeyResultSet.next()) {
                int generated_message_id = (int) pKeyResultSet.getInt("message_id");
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
