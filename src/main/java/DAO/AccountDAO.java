package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to mediate the transformation of data between the format of objects in Java to rows in a database. This is where we can use SQL queries to interact with the database.
 */

public class AccountDAO {
    /*
     * Method to retrieve an account from an account_id.
     * It will return an account object if an account is found, and will return null if no account is found.
     */
    public Account getAccountById(int id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            // create a prepared statement with the given SQL string
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            // get the ResultSet from the query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // if no account is found for the username, return null;
        return null;
    }

    /*
     * Method to retrieve an account from a username.
     * It will return an account object if an account is found, and will return null if no account is found.
     */
    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            // create a prepared statement with the given SQL string
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            // get the ResultSet from the query
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // if no account is found for the username, return null;
        return null;
    }

    /*
     * Method to retrieve an account from username & password 
     * It will return an account object if an account is found, and will return null if no account is found.
     */
    public Account getAccountByUsernamePassword(String username, String password) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // return null if no account is found;
        return null;
    }

    /*
     * Method to insert an account into the Account table.
     * It will return a new account object with an account_id once the account is created or it will return null if the operation is unsuccessful
     */
    public Account insertAccount(Account account) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet pKeyResultSet = ps.getGeneratedKeys();
            if (pKeyResultSet.next()) {
                int generated_account_id = (int) pKeyResultSet.getInt("account_id");
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
