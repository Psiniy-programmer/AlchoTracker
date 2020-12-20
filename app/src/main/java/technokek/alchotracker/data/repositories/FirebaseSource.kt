package technokek.alchotracker.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable


class FirebaseSource {
    private lateinit var db: DatabaseReference
    private lateinit var uid: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String, name:String) = Completable.create { emitter ->
        this.email = email
        this.name = name
        this.password = password

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful){
                    setDefaultValue()
                    emitter.onComplete()
                }
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun setDefaultValue() {
        db = FirebaseDatabase.getInstance().getReference()
        uid = FirebaseAuth.getInstance().currentUser!!.uid;
        db.child(Constants.USERS).child(uid).child(Constants.ID).setValue(uid)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.EVENTSCOUNT).setValue(0)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.FAVOURITEDRINK).setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.FRIENDSCOUNT).setValue(0)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.PREFERENCES).child("0").setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.AVATAR).setValue(Constants.DEFAULTPATH);
        db.child(Constants.USERS).child(uid).child(Constants.EMAIL).setValue(email);
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.LIST).setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.REQUESTS).child(
            Constants.OUTGOING).setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.REQUESTS).child(
            Constants.INCOMING).setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.NAME).setValue(name);
        db.child(Constants.USERS).child(uid).child(Constants.STATISTICS).child(Constants.TOTAL).child(
            Constants.EVENTSCOUNT).setValue(0);
        db.child(Constants.USERS).child(uid).child(Constants.STATUS).setValue(Constants.DEFAILTSTATUS+name);
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}
