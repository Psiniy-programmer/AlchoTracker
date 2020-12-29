package technokek.alchotracker.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.Completable
import technokek.alchotracker.api.SignUpInterface
import technokek.alchotracker.data.models.MasterProfileModel


class FirebaseSource {
    private lateinit var db: DatabaseReference
    private lateinit var uid: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var listener: Query

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

    fun register(email: String, password: String, name: String, callback: SignUpInterface) =
        Completable.create { emitter ->
            this.email = email
            this.name = name
            this.password = password

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        setDefaultValue(callback)
                        emitter.onComplete()
                    } else
                        emitter.onError(it.exception!!)
                }
            }
        }

    private fun setDefaultValue(callback: SignUpInterface) {
        db = FirebaseDatabase.getInstance().reference
        uid = FirebaseAuth.getInstance().currentUser!!.uid;
        db.child(Constants.USERS).child(uid).child(Constants.ID).setValue(uid)
        db.child(Constants.USERS).child(uid).child(Constants.EVENTS).setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.ALCHOO)
            .child(Constants.DECLINELIST).setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.ALCHOO)
            .child(Constants.FINDER).setValue(Constants.FALSE)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.EVENTSCOUNT)
            .setValue(0)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO)
            .child(Constants.FAVOURITEDRINK).setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO)
            .child(Constants.FRIENDSCOUNT).setValue(0)
        db.child(Constants.USERS).child(uid).child(Constants.ALCHOINFO).child(Constants.PREFERENCES)
            .child("0").setValue("")
        db.child(Constants.USERS).child(uid).child(Constants.AVATAR)
            .setValue(Constants.DEFAULTPATH);
        db.child(Constants.USERS).child(uid).child(Constants.EMAIL).setValue(email);
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.LIST)
            .setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.REQUESTS)
            .child(
                Constants.OUTGOING
            ).setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.FRIENDS).child(Constants.REQUESTS)
            .child(
                Constants.INCOMING
            ).setValue("");
        db.child(Constants.USERS).child(uid).child(Constants.NAME).setValue(name);
        db.child(Constants.USERS).child(uid).child(Constants.STATISTICS).child(Constants.TOTAL)
            .child(
                Constants.EVENTSCOUNT
            ).setValue(0);
        db.child(Constants.USERS).child(uid).child(Constants.STATUS)
            .setValue(Constants.DEFAILTSTATUS + name);

        listener = db.child(Constants.USERS).child(uid)
        listener.addValueEventListener(SignUpListener(callback))
    }

    inner class SignUpListener(private val callback: SignUpInterface) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            try {
                MasterProfileModel(
                    snapshot.child(technokek.alchotracker.data.Constants.AVATAR).value.toString(),
                    snapshot.child(technokek.alchotracker.data.Constants.NAME).value.toString(),
                    snapshot.child(technokek.alchotracker.data.Constants.STATUS).value.toString(),
                    snapshot.child(technokek.alchotracker.data.Constants.ALCHOINFO).child(technokek.alchotracker.data.Constants.FRIENDSCOUNT).getValue(Int::class.java)!!,
                    snapshot.child(technokek.alchotracker.data.Constants.ALCHOINFO).child(technokek.alchotracker.data.Constants.EVENTSCOUNT).getValue(Int::class.java)!!,
                    snapshot.child(technokek.alchotracker.data.Constants.ALCHOINFO).child(technokek.alchotracker.data.Constants.FAVOURITEDRINK).value.toString()
                )
                callback.onSuccessStartActivity()
            } catch (e: Exception) {

            }
        }

        override fun onCancelled(error: DatabaseError) {
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser
}
