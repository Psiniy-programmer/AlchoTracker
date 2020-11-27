package technokek.alchotracker.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import technokek.alchotracker.api.ProfileApi
import technokek.alchotracker.data.models.Profile

class ProfileRepo : ProfileApi, MutableLiveData<Profile>() {
    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("users")
    private val storage = FirebaseStorage.getInstance()
    private val sRef = storage.reference

    init {
        mProfile.value = Profile()
    }

    companion object {
        private val mProfile = MutableLiveData<Profile>()
    }

    override fun getProfile(): MutableLiveData<Profile> {
        //TODO("Смотрим текущего авторизованного и получаем его из бд")
        return mProfile
    }

    override fun getProfile(uid: String): MutableLiveData<Profile> {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sRef.child("${uid}.jpg").downloadUrl.addOnSuccessListener {
                    setProfile(snapshot.child(uid), it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.toString())
            }

        })
        return mProfile
    }

    override fun setProfile(x: DataSnapshot, avatar: Uri) {
        mProfile.value?.name = x.child("name").value.toString()
        mProfile.value?.status = x.child("status").value.toString()
        mProfile.value?.friendsCount =
            x.child("info").child("friendsCount").getValue(Int::class.java)!!
        mProfile.value?.eventCount =
            x.child("info").child("eventsCount").getValue(Int::class.java)!!
        mProfile.value?.favouriteDrink = x.child("info").child("favouriteDrink").value.toString()
        mProfile.value?.avatar = avatar.toString()
        Log.d("SYKA", mProfile.value.toString())
    }

    override fun getPreferencersDrinks(): Array<ProfileApi.PreferencesDrinks> {
        val array: Array<ProfileApi.PreferencesDrinks> = arrayOf()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.toString())
            }

        })
        return array
    }
}