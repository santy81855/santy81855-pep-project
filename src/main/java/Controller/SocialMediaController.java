package Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void getAllUserMessages(Context ctx) {
        String account_id = ctx.pathParam("account_id");	
        List<Message> messageList = messageService.getAllMessagesByAccountId(Integer.parseInt(account_id));
        ctx.json(messageList);
        ctx.status(200);
    }

    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");	
        Message message = om.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessageById(Integer.parseInt(message_id), message.getMessage_text());
        if (updatedMessage != null) {
            ctx.json(om.writeValueAsString(updatedMessage));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");	
        Message deleteMessage = messageService.deleteMessageById(Integer.parseInt(message_id));
        if (deleteMessage != null) {
            ctx.json(om.writeValueAsString(deleteMessage));
        } 
        ctx.status(200);
    }

    private void getMessageByIdHandler(Context ctx)throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String message_id = ctx.pathParam("message_id");	
        Message queryMessage = messageService.getMessageById(Integer.parseInt(message_id));
        if (queryMessage != null) {
            ctx.json(om.writeValueAsString(queryMessage));
        }
        ctx.status(200);
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messageList = messageService.getAllMessages();
        ctx.json(messageList);
        ctx.status(200);
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message insertedMessage = messageService.createMessage(message);
        if (insertedMessage != null) {
            ctx.json(om.writeValueAsString(insertedMessage));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.createAccount(account);
        if (addedAccount != null) {
            ctx.json(om.writeValueAsString(addedAccount));
            ctx.status(200);
        }
        else {
            ctx.status(400);
        }

    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account queryAccount = accountService.loginAccount(account);
        if (queryAccount != null) {
            ctx.json(om.writeValueAsString(queryAccount));
            ctx.status(200);
        }
        else {
            ctx.status(401);
        }
    }

}