package technokek.alchotracker.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.R
import technokek.alchotracker.api.AdminEventCallback
import technokek.alchotracker.api.AdminEventInterface
import technokek.alchotracker.api.AdminEventPushDeleteCallback
import technokek.alchotracker.data.CalendarLiveData
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.data.repositories.Constants
import technokek.alchotracker.ui.fragments.calendarfragment.utils.isEmpty
import java.io.ByteArrayOutputStream
import java.io.IOException

class AdminEventProfileFragment() : Fragment(), AdminEventInterface, AdminEventPushDeleteCallback {
    private lateinit var alkoEventPlace: MaterialTextView
    private lateinit var alkoEventStatus: MaterialTextView
    private lateinit var priceNumber: MaterialTextView
    private lateinit var changeEventPhoto: MaterialCardView
    private lateinit var changeEventStatus: MaterialCardView
    private lateinit var changeEventDrinks: MaterialCardView
    private lateinit var eventMembers: MaterialCardView
    private lateinit var deleteEvent: MaterialCardView
    private lateinit var eventAvatarView: ImageView

    private var checkedItem = 0
    private lateinit var drinks: Array<String>
    private lateinit var statusDialog: MaterialAlertDialogBuilder
    private lateinit var drinksDialog: MaterialAlertDialogBuilder
    private lateinit var thisDrinks:MutableList<String>

    private lateinit var calendarModel: CalendarModel
    private lateinit var callback: AdminEventCallback


    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.admin_event_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alkoEventPlace = view.findViewById(R.id.event_name)
        alkoEventStatus = view.findViewById(R.id.admin_event_status_text)
        priceNumber = view.findViewById(R.id.price_number)
        changeEventPhoto = view.findViewById(R.id.event_photo)
        changeEventStatus = view.findViewById(R.id.event_status_change_btn)
        changeEventDrinks = view.findViewById(R.id.event_drinks_btn)
        eventMembers = view.findViewById(R.id.event_members_btn)
        deleteEvent = view.findViewById(R.id.delete_event_btn)
        eventAvatarView = view.findViewById(R.id.event_profile_avatar)

        activity?.title = resources.getString(R.string.alko_event_toolbar)

        if (arguments != null) {
            val args = AdminEventProfileFragmentArgs.fromBundle(requireArguments())
            calendarModel = args.calendarModel
            thisDrinks = calendarModel.drinks.split(",") as MutableList<String>
            drinksDialog = buildDrinksDialog(calendarModel = calendarModel)
            statusDialog = buildStatusDialog(calendarModel = calendarModel)
            Picasso.get().load(calendarModel.avatar).into(eventAvatarView)
            setFragmentData(calendarModel)
        }
    }

    private fun setFragmentData(calendarModel: CalendarModel) {
        alkoEventPlace.text = calendarModel.eventPlace.place
        alkoEventStatus.text = calendarModel.status
        priceNumber.text = calendarModel.eventPlace.price
        drinks = calendarModel.drinks.split(",").toTypedArray()
        if (drinks.size == 0) {
            drinks[0] = "No drinks added"
        }

        changeEventStatus.setOnClickListener {
                statusDialog.show()
        }
        changeEventDrinks.setOnClickListener {
            drinksDialog.show()
        }
        /*
        Слава, это твоя хуня
         */
        eventMembers.setOnClickListener {
            val navController =
                activity?.let { it -> Navigation.findNavController(it, R.id.content) }

            val action
                    = AdminEventProfileFragmentDirections
                .actionAdminEventProfileToMembersFragment(calendarModel.id)
            navController?.navigate(action)
        }
        changeEventPhoto.setOnClickListener {
            //TODO why doesnt show?
            Toast.makeText(requireContext(), Constants.LONG_CLICK_PHOTO_CHANGE_HINT, Toast.LENGTH_LONG).show()
        }
        changeEventPhoto.setOnLongClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select pic"),
            Constants.PICK_IMAGE_REQUEST)
            return@setOnLongClickListener true
        }
        deleteEvent.setOnClickListener {
            //TODO why doesnt show?
            Toast.makeText(context, "Are you sure? Long click to confirm",
            Toast.LENGTH_SHORT).show()
        }
        deleteEvent.setOnLongClickListener {
            pushDeleteEventAsAdmin()
            return@setOnLongClickListener true
        }
    }

    private fun createBottomSheetDrinksDialog(calendarModel: CalendarModel) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val ll = requireView().findViewById<LinearLayout>(R.id.drinks_container)
        val bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.drinks_popup_dialog_layout,
                ll
            )
        bottomSheetDialog.setContentView(bottomSheetView)

        val etDrinks = bottomSheetDialog.findViewById<EditText>(R.id.pop_up_drinks_add)
        val buttonSubmit = bottomSheetDialog.findViewById<Button>(R.id.button_submit_drinks)
        buttonSubmit!!.setOnClickListener {
            if (isEmpty(etDrinks!!)) {
                Toast.makeText(this.context, "Fill in the field!", Toast.LENGTH_LONG).show()
            } else {
                //TODO push new drinks to BD
                val newDrinks = thisDrinks.joinToString(separator = ",") + "," + etDrinks.text.toString()
                thisDrinks = newDrinks.split(",") as MutableList<String>
                drinks = thisDrinks.toTypedArray()
                renewDrinksDialog(drinks)
                drinksDialog = buildDrinksDialog(calendarModel)
                pushDrinksToBD(newDrinks, calendarModel.id)
                bottomSheetDialog.dismiss()
            }
        }


        bottomSheetDialog.show()
    }

    private fun buildDrinksDialog(calendarModel: CalendarModel): MaterialAlertDialogBuilder {
        var booleans: BooleanArray = BooleanArray(thisDrinks.size) {
            false
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(Constants.DRINKS)
            .setMultiChoiceItems(thisDrinks.toTypedArray(), booleans) {
                    dialog, which, checked ->
                //TODO we need fill array
                booleans[which] = checked
                Log.d("Checked:", booleans[which].toString())
            }
            .setPositiveButton("Add") {
                    dialog, which ->
                dialog.dismiss()
                createBottomSheetDrinksDialog(calendarModel)
            }
            .setNegativeButton("Delete chosen") {
                dialog, which ->
                Log.d("Checked:", booleans.toString())
                booleans.forEachIndexed { index, b ->
                    if (booleans[index] == true) {
                        thisDrinks[index] = ""
                    }
                }
                thisDrinks.removeAll {
                    it == ""
                }
                booleans = BooleanArray(thisDrinks.size) {
                    false
                }
                pushDrinksToBD(thisDrinks.toTypedArray().joinToString(separator = ","),
                eventNumber = calendarModel.id)
                renewDrinksDialog(thisDrinks.toTypedArray(), booleans)
                dialog.dismiss()
            }
            .setPositiveButtonIcon(resources.getDrawable(R.drawable.add_event))
            .setBackground(resources.getDrawable(R.drawable.bg_status_dialog))
    }

    private fun buildStatusDialog(calendarModel: CalendarModel): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(Constants.EVENT_STATUS)
            .setNeutralButton(Constants.CANCEL_BTN_TEXT) {
                    dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton(Constants.OK_BTN_TEXT) {
                    dialog, which ->
                alkoEventStatus.text = statuses[checkedItem]
                pushStatusToDB(statuses[checkedItem], calendarModel.id)
                dialog.dismiss()
            }
            .setSingleChoiceItems(statuses, checkedItem) {
                    dialog, which ->
                checkedItem = which

            }
            .setBackground(resources.getDrawable(R.drawable.bg_status_dialog))
    }

    private fun renewDrinksDialog(newDrinks: Array<String>) {
        drinksDialog
            .setItems(newDrinks) {
                dialog, which ->
                //Nothing to do
            }
    }

    private fun renewDrinksDialog(newDrinks: Array<String>, booleans: BooleanArray) {
        drinksDialog
            .setMultiChoiceItems(newDrinks, booleans) {
                    dialog, which, checked ->
                //TODO we need fill array
                booleans[which] = checked
                Log.d("Checked:", booleans[which].toString())
            }
    }

    private fun pushDeleteEventAsAdmin() {
        CoroutineScope(Dispatchers.IO).launch {
            val allMembers: MutableList<String> = calendarModel.ordinaryMembersIds.split(";").toMutableList()
            allMembers.add(calendarModel.adminId)
            //delete event
            query.ref.child(calendarModel.id).removeValue()

            //delete in users
            allMembers.forEach {memberId ->
                uRef.ref.child(memberId)
                    .child(CalendarLiveData.EVENTS)
                    .child(calendarModel.id).removeValue()
            }
            this@AdminEventProfileFragment.onSuccesDeleted()
        }
    }

    override fun onSuccesDeleted() {
        val navController =
            activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
        navController?.navigate(R.id.action_adminEventProfile_to_calendarFragment)
    }

    private fun pushStatusToDB(status: String, eventNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            query.ref.child(eventNumber).apply {
                child(CalendarLiveData.STATUS).setValue(status)
            }
        }
    }

    private fun pushDrinksToBD(drinks: String, eventNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            query.ref.child(eventNumber).apply {
                child(CalendarLiveData.DRINKS).setValue(drinks)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Start setting method
        Log.d("onActivityResult", "Im here")
        decodeAndSetAvatar(requestCode, resultCode, data)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun decodeAndSetAvatar(requestCode: Int, resultCode: Int, data: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val filePath: Uri
            if (requestCode == 71 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filePath = data.data!!
                try {
                    val result = requireContext().contentResolver?.let { ImageDecoder.createSource(it, filePath) }
                    val bitmap: Bitmap? = result?.let { ImageDecoder.decodeBitmap(it) }
                    if (bitmap != null) {
                        callback = object : AdminEventCallback {
                            override fun onCall(uri: String) {
                                Picasso.get().load(uri).into(eventAvatarView)
                            }
                        }
                        setAvatar(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setAvatar(newAvatar: Bitmap) {
        val ref = sRef.child("img_event").child(calendarModel.id)
        val baos = ByteArrayOutputStream()
        newAvatar.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray: ByteArray = baos.toByteArray()
        val uploadTask: UploadTask = ref.putBytes(byteArray)

        uploadTask.continueWithTask{task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl}.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                query.ref.child(calendarModel.id).apply {
                    child(technokek.alchotracker.data.Constants.AVATAR).setValue(downloadUri.toString())
                }
                callback?.onCall(uri = downloadUri.toString())
            }
        }
    }

    companion object {
        private val statuses = arrayOf("Default", "Preparing", "In process", "Passed")
        private val query = FirebaseDatabase
            .getInstance()
            .getReference("events")
        private val uRef = FirebaseDatabase
            .getInstance()
            .getReference("users")
        private val sRef = FirebaseStorage.getInstance().reference
    }
}