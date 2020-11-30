package technokek.alchotracker.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import technokek.alchotracker.R;
import technokek.alchotracker.data.Constants;
import technokek.alchotracker.viewmodels.RegistrViewModel;

public class StartRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText type_name_field;
    EditText type_login_field;
    EditText type_password_field;
    EditText type_password_again_field;
    Button registrationButton;
    Button buttAut;
    TextView errorhandler;


    RegistrViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        viewModel = new RegistrViewModel(this);
        buttAut = findViewById(R.id.authentification_button);
        buttAut.setOnClickListener(this);
        type_name_field = findViewById(R.id.type_name_field);
        type_login_field = findViewById(R.id.type_login_field);
        type_password_field = findViewById(R.id.type_password_field);
        type_password_again_field = findViewById(R.id.type_password_again_field);
        errorhandler=findViewById(R.id.errorHandler);
        registrationButton = findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(viewModel.getUser());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            registrationButton.setVisibility(View.GONE);
            type_name_field.setVisibility(View.GONE);
            type_login_field.setVisibility(View.GONE);
            type_password_field.setVisibility(View.GONE);
            type_password_again_field.setVisibility(View.GONE);
            errorhandler.setVisibility(View.GONE);
            buttAut.setVisibility(View.GONE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            registrationButton.setVisibility(View.VISIBLE);
            type_name_field.setVisibility(View.VISIBLE);
            type_login_field.setVisibility(View.VISIBLE);
            type_password_field.setVisibility(View.VISIBLE);
            type_password_again_field.setVisibility(View.VISIBLE);
            errorhandler.setVisibility(View.VISIBLE);
            buttAut.setVisibility(View.VISIBLE);
        }
    }

    public void errorHandler(){
        if(!type_password_field.getText().toString().equals(type_password_again_field.getText().toString())) {
            errorhandler.setText(Constants.NOTEQUALPASSWORERROR);
        }
        else if(type_name_field.getText().toString().equals("")) {
            errorhandler.setText(Constants.MISSINGNAMEERROR);
        }
        else if(type_password_field.getText().toString().length()<6)
            errorhandler.setText(Constants.SHORTPASSWORDERROR);
    }


    private Activity mCurrentActivity = null;

    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }



    @Override
    public void onClick(View v) {
        setCurrentActivity(this);
        switch (v.getId()){
            case R.id.registrationButton:
                if(!type_password_field.getText().toString().equals(type_password_again_field.getText().toString())
                        ||type_name_field.getText().toString().equals("")
                        ||(type_password_field.getText().toString().length()<6)) {
                    errorHandler();
                }
                else {
                    Thread thread = new Thread(new MyThread());
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateUI(viewModel.getUser());
                }
                break;
            case R.id.authentification_button:
                Intent intent = new Intent(this, AuthentificationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class MyThread implements Runnable{

        @Override
        public void run() {
            viewModel.getActivity(mCurrentActivity);
            viewModel.createAccount(type_login_field.getText().toString(), type_password_field.getText().toString(),type_name_field.getText().toString());
           // updateUI(viewModel.getUser());
        }
    }
}

