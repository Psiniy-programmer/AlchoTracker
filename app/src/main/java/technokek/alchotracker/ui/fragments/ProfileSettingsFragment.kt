package technokek.alchotracker.ui.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.R
import technokek.alchotracker.data.repositories.Constants.PICK_IMAGE_REQUEST
import technokek.alchotracker.ui.activity.LoginActivity
import technokek.alchotracker.ui.activity.MainActivity
import technokek.alchotracker.viewmodels.ProfileSettingsViewModel
import java.io.IOException

class ProfileSettingsFragment : Fragment() {
    private lateinit var signOutBtn: MaterialCardView
    private lateinit var inputName: TextInputEditText
    private lateinit var inputStatus: TextInputEditText
    private lateinit var inputDrink: TextInputEditText
    private lateinit var alchooSwitcher: SwitchMaterial
    private lateinit var changeAvatarBtn: ImageButton
    private lateinit var mSettingsViewModel: ProfileSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.settings_toolbar)

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.profile_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeAvatarBtn = view.findViewById(R.id.profile_settings_download_photo)
        signOutBtn = view.findViewById(R.id.sign_out_btn)
        inputName = view.findViewById(R.id.profile_settings_edit_name)
        inputStatus = view.findViewById(R.id.profile_settings_edit_status)
        inputDrink = view.findViewById(R.id.profile_settings_edit_fav)
        alchooSwitcher = view.findViewById(R.id.alchoo_switcher)

        mSettingsViewModel =
            ViewModelProvider(this)[ProfileSettingsViewModel(activity!!.application)::class.java]

        mSettingsViewModel.profileSettings.observe(viewLifecycleOwner, {
            inputName.setText(it.name)
            inputDrink.setText(it.drink)

            alchooSwitcher.isChecked = it.alchoo
        })

        changeAvatarBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select pic"), PICK_IMAGE_REQUEST)
        }

        signOutBtn.setOnClickListener {
            mSettingsViewModel.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings_save) {
            mSettingsViewModel.setName(inputName.text.toString())
            mSettingsViewModel.setStatus(inputStatus.text.toString())
            mSettingsViewModel.setDrink(inputDrink.text.toString())
            if (alchooSwitcher.isChecked)
                mSettingsViewModel.onAlchoo()
            else
                mSettingsViewModel.offAlchoo()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSettingsViewModel.setAvatar(requestCode, resultCode, data)
    }
}
