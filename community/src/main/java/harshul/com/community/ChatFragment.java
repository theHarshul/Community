package harshul.com.community;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import harshul.com.community.Model.ChatMessage;

/**
 * Created by Harshul on 3/15/2016.
 */
public class ChatFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> mListAdapter;
    private EditText textEdit;
    private ImageButton sendButton;
    private String channelName;
    private MainActivity mainActivity;
    private Firebase channelMessagesRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mainActivity = ((MainActivity)getActivity());

        channelName = channelName.replace("#",""); //firebase rules

        channelMessagesRef = mainActivity.getFirebaseRef().child("channelMessaging").child(channelName);

        final ListView listView = (ListView) view.findViewById(android.R.id.list);

        mListAdapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                android.R.layout.two_line_list_item, channelMessagesRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView author =((TextView) v.findViewById(android.R.id.text1));
                TextView message = ((TextView) v.findViewById(android.R.id.text2));

                System.out.println(author.getText().toString() + " vs " + mainActivity.getUser().getUsername());

                if(mainActivity.getUser().getUsername().equals(model.getName())){
                    author.setTextColor(Color.BLUE);
                    author.setGravity(Gravity.RIGHT);
                    message.setGravity(Gravity.RIGHT);
                }
                author.setText(model.getName());
                message.setText(model.getText());
            }
        };
        listView.setAdapter(mListAdapter);


        textEdit = (EditText) view.findViewById(R.id.text_edit);
        sendButton = (ImageButton) view.findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                ChatMessage message = new ChatMessage(mainActivity.getUser().getUsername(), text);
                channelMessagesRef.push().setValue(message);
                textEdit.setText("");
            }
        });

        //R





        return view;

    }

    public void setChannelName(String str){
        channelName = str;
    }




}

