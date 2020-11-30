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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import technokek.alchotracker.data.Constants;
import technokek.alchotracker.data.RegistrLiveData;

public class RegistrViewModel {
    private Activity mCurrentActivity = null;
    Context context;
    public RegistrLiveData liveData;


    public RegistrViewModel(Context context){
        this.context=context;
        liveData =new RegistrLiveData();
        liveData.setReg();
    }


    public static FirebaseAuth Reg(){
        return FirebaseAuth.getInstance();
    }


    public static DatabaseReference DB(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public FirebaseUser getUser(){
        return liveData.userThis;
    }

    public void getActivity(Activity activity){
        mCurrentActivity=activity;
    }

    public  String getUID(){
        return liveData.userThis.getUid();
    }
    public void createAccount(final String email, String password, final String name) {
        liveData.createAccount(email, password)
                .addOnCompleteListener(mCurrentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(RegistrLiveData.TAG, "createUserWithEmail:success");
                            liveData.userThis = liveData.mAuth.getCurrentUser();
                            liveData.setUID();
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.ID).setValue(liveData.id);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.ALCHOINFO).child(Constants.EVENTSCOUNT).setValue(0);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.ALCHOINFO).child(Constants.FAVOURITEDRINK).setValue("");
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.ALCHOINFO).child(Constants.FRIENDSCOUNT).setValue(0);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.ALCHOINFO).child(Constants.PREFERENCES).child("0").setValue("");
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.AVATAR).setValue(Constants.DEFAULTPATH);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.EMAIL).setValue(email);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.FRIENDS).child(Constants.LIST).setValue("");
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.FRIENDS).child(Constants.REQUESTS).child(Constants.OUTGOING).setValue("");
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.FRIENDS).child(Constants.REQUESTS).child(Constants.INCOMING).setValue("");
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.NAME).setValue(name);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.STATISTICS).child(Constants.TOTAL).child(Constants.EVENTSCOUNT).setValue(0);
                            liveData.mDatabase.child(Constants.USERS).child(liveData.id).child(Constants.STATUS).setValue(Constants.DEFAILTSTATUS+name);
                            liveData.userThis = liveData.mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(RegistrLiveData.TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            liveData.userThis = null;
                        }
                    }
                });
    }
}
