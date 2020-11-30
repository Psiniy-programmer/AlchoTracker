package technokek.alchotracker.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import technokek.alchotracker.data.AuthLiveData;


public class AuthViewModel {

    private Activity mCurrentActivity = null;
    Context context;
    AuthLiveData liveData;

    public AuthViewModel(Context context){
        this.context=context;
        liveData=new AuthLiveData();
        liveData.setAuth();
    }

    public void getActivity(Activity activity){
        mCurrentActivity=activity;
    }

    public static FirebaseAuth Auth(){
        return FirebaseAuth.getInstance();
    }

    public void signIn(String email, String password) {
        liveData.signIn(email,password).addOnCompleteListener(mCurrentActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(AuthLiveData.TAG, "signInWithEmail:success");
                    liveData.userThis = liveData.mAuth.getCurrentUser();
                } else {
                    Log.w(AuthLiveData.TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(context.getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    liveData.userThis = null;
                }

            }
        });
    }
    public FirebaseUser getUser(){
        return liveData.userThis;
    }
}
