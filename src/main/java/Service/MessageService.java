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

    /*
     * Method to retrieve a message by the message_id.
     */
    public Message getMessageById(int message_id) {
        return this.messageDAO.getMessageById(message_id);
    }

    /*
     * Method to delete a message by the message_id.
     */
    public Message deleteMessageById(int message_id) {
        // retrieve the message before deleting it
        Message messageToDelete = this.messageDAO.getMessageById(message_id);
        // if the message exists, delete it from the database
        if (messageToDelete != null) {
            this.messageDAO.deleteMessageById(message_id);
        }
        // return the messageToDelete, which will either be a Message object if it existed, or null if it never existed
        return messageToDelete;
    }

    /*
     * Method to update the message_text of a message by the message_id.
     * The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters.
     */
    public Message updateMessageById(int message_id, String message_text) {
        // check if the message is the right length
        if (message_text.length() == 0 || message_text.length() > 255) {
            return null;
        }
        // check that a message with this message_id exists
        Message messageToUpdate = this.messageDAO.getMessageById(message_id);
        if (messageToUpdate == null) {
            return null;
        }
        // update the message 
        int rowsUpdated = this.messageDAO.updateMessageById(message_id, message_text);
        // if there was a message updated then return the new message object
        if (rowsUpdated == 1) {
            // update the message object to reflect the new message_text
            messageToUpdate.setMessage_text(message_text);
            return messageToUpdate;
        }
        else {
            return null;
        }
    }

    /*
     * Method to retrieve all messages with a particular posted_by value given by the account_id. The method will return a list of Message objects, which would just be empty if there are no messages for the given account_id.
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        return this.messageDAO.getAllMessagesPostedBy(account_id);
    }
    
}
