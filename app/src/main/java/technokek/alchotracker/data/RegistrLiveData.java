package technokek.alchotracker.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import technokek.alchotracker.viewmodels.RegistrViewModel;


public class RegistrLiveData {
    public FirebaseAuth mAuth;
    public static final String TAG = "EmailPassword";
    public DatabaseReference mDatabase;
    public FirebaseUser userThis;
    public String id;

    public void setReg(){
        mAuth= RegistrViewModel.Reg();
        userThis = mAuth.getCurrentUser();
        mDatabase = RegistrViewModel.DB();
    }
    public void setUID(){
        id=mAuth.getCurrentUser().getUid();
    }

    public Task<AuthResult> createAccount(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}
