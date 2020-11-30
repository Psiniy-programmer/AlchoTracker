package technokek.alchotracker.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import technokek.alchotracker.viewmodels.AuthViewModel;


public class AuthLiveData{
    public static final String TAG = "EmailPassword";
    public FirebaseAuth mAuth;
    public FirebaseUser userThis;

    public void setAuth(){
        mAuth= AuthViewModel.Auth();
        userThis = mAuth.getCurrentUser();
    }
    public Task<AuthResult> signIn(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password);
    }



}
