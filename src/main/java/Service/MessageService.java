package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService () {
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

    public MessageService (MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public MessageService (MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAO();
    }

    /*
     * Method receives a message object without a message_id.
     * For a message to be valid the message_text must not be blank, is not over 255 characters, and posted_by refers to a real, existing user.
     */
    public Message createMessage(Message message) {
        // determine if the message meets the requirements
        if (message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255) {
            return null;
        }
        // check if the posted_by corresponds to an existing account_id
        Account queryAccount = this.accountDAO.getAccountById(message.posted_by);
        if (queryAccount == null) {
            return null;
        }
        // create message
        return this.messageDAO.insertMessage(message);
    }

    /*
     * Method retrieves all messages in the database.
     */
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }
    
}
