package harshul.com.community.Model;

import java.util.ArrayList;

/**
 * Created by Harshul on 3/17/2016.
 */
public class Channel {

    private String name;
    private ArrayList<ChatMessage> chatMessages;

    public Channel(String name, ArrayList<ChatMessage> chatMessages) {
        this.name = name;
        this.chatMessages = chatMessages;
    }

    public Channel(String name) {

        this.name = name;
    }

    public Channel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
