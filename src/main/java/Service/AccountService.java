package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * The purpose of this service class is to sit between the web layer (controller) and the persistence layer (DAO).
 * This class can be used for tasks like checking input validity, security checkes, or saving actions made by the API to a logging file.
 */

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService () { 
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /* 
     * Method receives an account object with a username and password. 
     * For an account to be valid the username must not be blank, the password must be at least 4 characters long, and the username must be unique.
     */
    public Account createAccount(Account account) {
        // determine if the username and password meet the requirements
        if (account.getUsername().length() == 0 || account.getPassword().length() < 4) {
            return null;
        }
        // check if an account already exists for this username
        if (this.accountDAO.getAccountByUsername(account.username) != null) {
            return null;
        }
        // add the account to the database and return it. It will be null if the operation was unsuccessful
        return this.accountDAO.insertAccount(account);
    }

    public Account loginAccount(Account account) {
        // check if there is an account with this username and password & return result (account or null)
        return this.accountDAO.getAccountByUsernamePassword(account.getUsername(), account.getPassword());

    }

}
