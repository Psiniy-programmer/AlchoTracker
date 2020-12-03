package technokek.alchotracker.ui.fragments

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.theartofdev.edmodo.cropper.CropImage
import technokek.alchotracker.R
import technokek.alchotracker.ui.activity.MainActivity
import technokek.alchotracker.ui.activity.StartRegistrationActivity
import technokek.alchotracker.viewmodels.ProfileSettingsViewModel

class ProfileSettingsFragment : Fragment() {
    private lateinit var changeStatusBtn: Button
    private lateinit var changeAvatarBtn: Button
    private lateinit var changeDrinkBtn: Button
    private lateinit var signOutBtn: Button
    private lateinit var inputStatus: EditText
    private lateinit var inputDrink: EditText
    private lateinit var mProfileViewModel: ProfileSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeStatusBtn = view.findViewById(R.id.change_status_btn)
        changeAvatarBtn = view.findViewById(R.id.change_avatar_btn)
        changeDrinkBtn = view.findViewById(R.id.change_drink_btn)
        signOutBtn = view.findViewById(R.id.sign_out_btn)
        inputStatus = view.findViewById(R.id.edit_status)
        inputDrink = view.findViewById(R.id.edit_drink)

        mProfileViewModel =
            ViewModelProvider(this)[ProfileSettingsViewModel(Application())::class.java]

        changeStatusBtn.setOnClickListener {
            mProfileViewModel.setStatus(inputStatus.text.toString())
            inputStatus.setText("")
        }

        changeAvatarBtn.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setRequestedSize(500, 500)
                .start(activity as MainActivity, this)
        }
        changeDrinkBtn.setOnClickListener {
            mProfileViewModel.setDrink(inputDrink.text.toString())
            inputDrink.setText("")
        }

        signOutBtn.setOnClickListener {
            mProfileViewModel.signOut()
            startActivity(Intent(context, StartRegistrationActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
            resultCode == RESULT_OK && data != null
        ) {
            val uri = CropImage.getActivityResult(data).uri
            mProfileViewModel.setAvatar(uri)
        }
    }
}
