package harshul.com.community;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import harshul.com.community.Model.User;

public class MainActivity extends FirebaseLoginBaseActivity {

    private Firebase ref, selectedChatref;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private String uid;
    private AuthData authData;
    private User user;
    LoginFragment firstFragment;
    RoomsFragment roomsFragment;
    ChatFragment cf;
    Context context = this;
    Query usersQuery;



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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);


        //inflate activity with homepage fragment

        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
           firstFragment = new LoginFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        addEvents();


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                onFirebaseLoggedOut();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    private void addEvents() {
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://chatfirebasedemo.firebaseio.com/");



    }



    @Override
    protected Firebase getFirebaseRef() {
        return ref;
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
        uid = authData.getUid();
        chatRooms();
        //chatScreen();

    }

    public void chatRooms(){

        if(uid == null) uid = authData.getUid();


        System.out.println("chat rooms: " + uid);
        roomsFragment = new RoomsFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.fragment_container, roomsFragment, "room");
        transaction.addToBackStack(null);
        transaction.commit();

        Firebase userRef = new Firebase("https://chatfirebasedemo.firebaseio.com/users/" + uid);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                user = snapshot.getValue(User.class);
                System.out.println(user.getEmail());
                Toast.makeText(getBaseContext(), "Welcome " + user.getUsername() + "!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void chatScreen(String str) {

        System.out.println("chat screen: " + uid);

        cf = new ChatFragment();
        cf.setChannelName(str);
        getSupportActionBar().setTitle(str);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, cf, TAG_FRAGMENT);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();




        //DataSnapshot userData = new DataSnapshot("users/" + uid);
    }

    @Override
    public void onFirebaseLoggedOut() {
        System.out.println("Logged out");
        logOutOfApp();

    }

    public void logOutOfApp(){
        //deauthorize
        ref.unauth();



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, firstFragment, TAG_FRAGMENT).addToBackStack(null);

        // Commit the transaction
        transaction.commit();


    }

    public String getUid(){
        return uid;
    }
    public void setUser(User u){user = u;}
    public void setSelectedChatref(Firebase ref){selectedChatref = ref;}
    public Firebase getSelectedChatref(){return selectedChatref;}
    public User getUser(){return user;}
}
