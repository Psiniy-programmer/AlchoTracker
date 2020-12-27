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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.switchmaterial.SwitchMaterial
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.R
import technokek.alchotracker.ui.activity.LoginActivity
import technokek.alchotracker.ui.activity.MainActivity
import technokek.alchotracker.viewmodels.ProfileSettingsViewModel
import java.io.IOException

class ProfileSettingsFragment : Fragment() {
    private lateinit var changeStatusBtn: Button
    private lateinit var changeAvatarBtn: Button
    private lateinit var changeDrinkBtn: Button
    private lateinit var signOutBtn: Button
    private lateinit var inputStatus: EditText
    private lateinit var inputDrink: EditText
    private lateinit var alchooSwitcher: SwitchMaterial
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

//        changeStatusBtn = view.findViewById(R.id.change_status_btn)
//        changeAvatarBtn = view.findViewById(R.id.change_avatar_btn)
//        changeDrinkBtn = view.findViewById(R.id.change_drink_btn)
//        signOutBtn = view.findViewById(R.id.sign_out_btn)
//        inputStatus = view.findViewById(R.id.edit_status)
//        inputDrink = view.findViewById(R.id.edit_drink)
//        alchooSwitcher = view.findViewById(R.id.alchoo_switcher)
//
//        mSettingsViewModel =
//            ViewModelProvider(this)[ProfileSettingsViewModel(activity!!.application)::class.java]
//
//        mSettingsViewModel.profileSettings.observe(viewLifecycleOwner, {
//            alchooSwitcher.isChecked = it.alchoo
//        })
//
//        changeStatusBtn.setOnClickListener {
//            mSettingsViewModel.setStatus(inputStatus.text.toString())
//            inputStatus.setText("")
//        }
//
//        changeAvatarBtn.setOnClickListener {
//            val PICK_IMAGE_REQUEST = 71
//
//            val intent = Intent()
//            intent.type = "image/"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Select pic"), PICK_IMAGE_REQUEST)
//        }
//
//        changeDrinkBtn.setOnClickListener {
//            mSettingsViewModel.setDrink(inputDrink.text.toString())
//            inputDrink.setText("")
//        }
//
//        signOutBtn.setOnClickListener {
//            mSettingsViewModel.signOut()
//            startActivity(Intent(context, LoginActivity::class.java))
//        }
//
//        alchooSwitcher.setOnCheckedChangeListener { buttonView, isChecheck ->
//            if (alchooSwitcher.isChecked)
//                mSettingsViewModel.onAlchoo()
//            else
//                mSettingsViewModel.offAlchoo()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings_save) {
            // TODO save data in db
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSettingsViewModel.setAvatar(requestCode, resultCode, data)
    }
}
