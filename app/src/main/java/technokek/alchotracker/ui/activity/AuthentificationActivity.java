package technokek.alchotracker.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import technokek.alchotracker.R;
import technokek.alchotracker.viewmodels.AuthViewModel;

public class AuthentificationActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "EmailPassword";
    EditText type_login_field;
    EditText type_password_field;
    Button authentification;
    private AuthViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new AuthViewModel(this);
        setContentView(R.layout.autentification_layout);
        type_login_field = findViewById(R.id.type_login_field);
        type_password_field = findViewById(R.id.type_password_field);
        authentification = findViewById(R.id.authentification);
        authentification.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(viewModel.getUser());
    }

    private void updateUI(FirebaseUser user) {//live data
        if (user != null) {
            authentification.setVisibility(View.GONE);
            type_login_field.setVisibility(View.GONE);
            type_password_field.setVisibility(View.GONE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            authentification.setVisibility(View.VISIBLE);
            type_login_field.setVisibility(View.VISIBLE);
            type_password_field.setVisibility(View.VISIBLE);
        }
    }

    private Activity mCurrentActivity = null;

    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }


    @Override
    public void onClick(View v) {
        setCurrentActivity(this);
        int i = v.getId();
        if (i == R.id.authentification) {
            Thread thread = new Thread(new MyThread());
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateUI(viewModel.getUser());
    }

    class MyThread implements Runnable{

        @Override
        public void run() {
            viewModel.getActivity(mCurrentActivity);
            viewModel.signIn(type_login_field.getText().toString(), type_password_field.getText().toString());
        }
    }
}