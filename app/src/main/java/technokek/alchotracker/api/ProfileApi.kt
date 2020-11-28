package technokek.alchotracker.api

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import technokek.alchotracker.data.models.MasterProfileModel

public interface ProfileApi {

    data class PreferencesDrinks(var drink: String)

    fun getProfile(): MutableLiveData<MasterProfileModel>
    fun getProfile(uid: String): MutableLiveData<MasterProfileModel>
    fun setProfile(x: DataSnapshot, avatar: Uri)
    fun getPreferencersDrinks(): Array<PreferencesDrinks>
}