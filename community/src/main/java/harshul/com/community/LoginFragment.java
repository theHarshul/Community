package harshul.com.community;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import harshul.com.community.Model.User;

/**
 * Created by Harshul on 3/15/2016.
 */
public class LoginFragment extends Fragment {

    private MainActivity mainActivity;
    private Firebase ref, userRef;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainActivity = ((MainActivity)getActivity());
        mainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        view =  inflater.inflate(R.layout.fragment_login, container, false);

        loginEvent();

        accountCreationEvent();

        resetPasswordEvent();

        return  view;


    }

    private void resetPasswordEvent() {
        TextView resetLink = (TextView) view.findViewById(R.id.link_forgot);
        resetLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_reset);

                Button resetButton = (Button) dialog.findViewById(R.id.btn_reset);
                resetButton.setOnClickListener(new OnClickListener(){


                    @Override
                    public void onClick(View v) {
                        String email = ((EditText) dialog.findViewById(R.id.input_resetemail)).getText().toString();
                        mainActivity.getFirebaseRef().resetPassword(email,new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                               System.out.println("password reset");
                                dialog.dismiss();
                            }
                            @Override
                            public void onError(FirebaseError firebaseError) {
                                // error encountered
                            }
                        });
                    }
                });

                dialog.show();


            }

        });
    }

    private void accountCreationEvent() {
        Button createAccountBtn = (Button)view.findViewById(R.id.btn_signup);
        createAccountBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("cfeating user");

                final String name = ((EditText) view.findViewById(R.id.input_name)).getText().toString();
                final String email = ((EditText) view.findViewById(R.id.input_email)).getText().toString();
                final String password = ((EditText) view.findViewById(R.id.input_password)).getText().toString();

                ref = mainActivity.getFirebaseRef();

                ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        userRef = ref.child("users").child(result.get("uid").toString());
                        User info = new User(name, email);
                        userRef.setValue(info);
                        mainActivity.setUser(info);
                        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                               System.out.println("User authenticated!");
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                // Authenticated failed with error firebaseError
                            }
                        };

                        mainActivity.getFirebaseRef().authWithPassword(email,password,authResultHandler);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });


            }
        });
    }

    private void loginEvent() {
        TextView loginLink = (TextView) view.findViewById(R.id.link_login);
        loginLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               System.out.println("Log in");
                mainActivity.showFirebaseLoginPrompt();
            }
        });
    }
}
