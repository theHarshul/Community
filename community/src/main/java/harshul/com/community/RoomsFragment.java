package harshul.com.community;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;

import harshul.com.community.Model.Channel;

/**
 * Created by Harshul on 3/16/2016.
 */
public class RoomsFragment extends Fragment {

    private MainActivity mainActivity;
    private Firebase ref, channelRef;
    private View view;
    private ListView lv;
    private ArrayList<String> chatRooms =new ArrayList<String>();
    private ArrayList<Channel> channels = new ArrayList<Channel>();
    FirebaseListAdapter<Channel> adapter;




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mainActivity = ((MainActivity) getActivity());
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        ref = ((MainActivity)getActivity()).getFirebaseRef();
        channelRef = ref.child("channels");

        view = inflater.inflate(R.layout.fragment_chatmenu, container, false);

        initializeListView();

        addChannelEvent();

        channelSelectedEvent();


        return view;


    }

    private void channelSelectedEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                Channel selectedChannel = (Channel) o;//As you are using Default String Adapter
                mainActivity.chatScreen("#" + selectedChannel.getName());
            }
        });
    }

    private void addChannelEvent() {
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.add_btn);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_addchannel);

                Button addButton = (Button) dialog.findViewById(R.id.btn_addChannel);
                addButton.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        String channel = ((EditText) dialog.findViewById(R.id.input_channelname)).getText().toString().trim();
                        channel = capsFirst(channel);
                        channel=channel.replaceAll("\\s","");
                        //blank channel
                        if(channel.length() <= 0){
                            Toast.makeText(getContext(), "Must input valid channel name" + "!", Toast.LENGTH_SHORT).show();
                        }
                        //channel exists
                        else if(chatRooms.contains(channel)){
                            Toast.makeText(getContext(), "Channel already exists" + "!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            channel = channel.replace("#", "");
                            //channel = "#" + channel;
                            Channel chan = new Channel(channel);
                            channels.add(chan);
                            //chatRooms.add(channel);
                            channelRef.push().setValue(chan);
                            channel = "#" + channel;

                            dialog.dismiss();
                        }


                    }
                });

                dialog.show();


            }

        });
    }



    private void initializeListView() {
        lv = (ListView)view.findViewById(R.id.chatRooms);

        adapter = new FirebaseListAdapter<Channel>(getActivity(), Channel.class,
                android.R.layout.simple_expandable_list_item_1, channelRef) {
            @Override
            protected void populateView(View v,Channel channel, int position) {
                System.out.println(channel.getName());
                ((TextView)v.findViewById(android.R.id.text1)).setText("#" + channel.getName());
            }

        };
        lv.setAdapter(adapter);

        /*

        chatRooms.add("#bulletinBoard ");
        chatRooms.add("#events");
        chatRooms.add("#emergency");
        chatRooms.add("#funny");
        chatRooms.add("#hangout");
        chatRooms.add("#ideas ");
        chatRooms.add("#startup");
        chatRooms.add("#carpool");
        chatRooms.add("#garageSale");
        chatRooms.add("#outdoorActivities");*/


        //adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_expandable_list_item_1, chatRooms);

        //lv.setAdapter(adapter);
    }

    //Channel name rules
    public String capsFirst(String str) {
        String[] words = str.split(" ");
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            if(i == 0){ret.append(words[i]);}
            else {
                ret.append(Character.toUpperCase(words[i].charAt(0)));
                ret.append(words[i].substring(1));
                if (i < words.length - 1) {
                    ret.append(' ');
                }
            }
        }
        return ret.toString();
    }


}
