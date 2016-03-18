package harshul.com.community.Model;

/**
 * Created by Harshul on 3/16/2016.
 */
public class ChatMessage {

    private String name;
    private String text;

    public ChatMessage() {

    }
    public ChatMessage(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
