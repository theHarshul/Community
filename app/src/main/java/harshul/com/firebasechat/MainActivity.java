package harshul.com.firebasechat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

public class MainActivity extends FirebaseLoginBaseActivity {

    private Firebase mFirebaseRef;
    private FirebaseListAdapter<ChatMessage> mListAdapter;
    private boolean isLoggedIn = false;
    private EditText textEdit;
    private Button sendButton;

    @Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
        setEnabledAuthProvider(AuthProviderType.FACEBOOK);
        setEnabledAuthProvider(AuthProviderType.TWITTER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initializeEvents();
    }

    private void initializeEvents(){

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://chatfirebasedemo.firebaseio.com/");

        textEdit = (EditText) this.findViewById(R.id.text_edit);
        sendButton = (Button) this.findViewById(R.id.send_button);

        if(getAuth() != null){
            System.out.println("already logged in");
            textEdit.setEnabled(true);
            sendButton.setEnabled(true);
        }

        else{
            textEdit.setEnabled(false);
            sendButton.setEnabled(false);
        }





        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textEdit.getText().toString();
                ChatMessage message = new ChatMessage("Guest", text);
                mFirebaseRef.push().setValue(message);
                textEdit.setText("");
            }
        });

        Button loginButton = (Button) this.findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFirebaseLoginPrompt();
            }
        });

        Button logoutButton = (Button)this.findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("shoud log out");
                mFirebaseRef.unauth();
            }
        });

        final ListView listView = (ListView) this.findViewById(android.R.id.list);

        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                android.R.layout.two_line_list_item, mFirebaseRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                ((TextView)v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView)v.findViewById(android.R.id.text2)).setText(model.getText());
            }
        };
        listView.setAdapter(mListAdapter);

    }

    @Override
    protected Firebase getFirebaseRef() {
        return mFirebaseRef;
    }

    @Override
    protected void onFirebaseLoginProviderError(FirebaseLoginError firebaseLoginError) {

    }

    @Override
    protected void onFirebaseLoginUserError(FirebaseLoginError firebaseLoginError) {


    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        System.out.println("uuid: " + authData.getUid());
        textEdit.setEnabled(true);
        sendButton.setEnabled(true);


    }

    @Override
    public void onFirebaseLoggedOut() {
        System.out.println("Logged out");
        isLoggedIn = false;
        textEdit.setEnabled(false);
        sendButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListAdapter.cleanup();
    }
}


