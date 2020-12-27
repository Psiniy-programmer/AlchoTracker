package technokek.alchotracker.ui.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import technokek.alchotracker.R
import technokek.alchotracker.data.CalendarLiveData
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.data.repositories.Constants
import technokek.alchotracker.ui.fragments.calendarfragment.utils.isEmpty

class AdminEventProfileFragment() : Fragment() {
    private lateinit var alkoEventPlace: MaterialTextView
    private lateinit var alkoEventStatus: MaterialTextView
    private lateinit var priceNumber: MaterialTextView
    private lateinit var changeEventStatus: MaterialCardView
    private lateinit var changeEventDrinks: MaterialCardView
    private lateinit var eventMembers: MaterialCardView
    private lateinit var deleteEvent: MaterialCardView

    private var checkedItem = 0
    private lateinit var drinks: Array<String>
    private lateinit var statusDialog: MaterialAlertDialogBuilder
    private lateinit var drinksDialog: MaterialAlertDialogBuilder


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
        changeEventStatus = view.findViewById(R.id.event_status_change_btn)
        changeEventDrinks = view.findViewById(R.id.event_drinks_btn)
        eventMembers = view.findViewById(R.id.event_members_btn)
        deleteEvent = view.findViewById(R.id.delete_event_btn)


        if (arguments != null) {
            val args = AdminEventProfileFragmentArgs.fromBundle(requireArguments())
            val calendarModel = args.calendarModel
            drinksDialog = buildDrinksDialog(calendarModel = calendarModel)
            statusDialog = buildStatusDialog(calendarModel = calendarModel)
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
                val newDrinks = calendarModel.drinks + "," + etDrinks.text.toString()
                drinks = newDrinks.split(",").toTypedArray()
                renewDrinksDialog(drinks)
                pushDrinksToBD(newDrinks, calendarModel.id)
                bottomSheetDialog.dismiss()
            }
        }


        bottomSheetDialog.show()
    }

    private fun buildDrinksDialog(calendarModel: CalendarModel): MaterialAlertDialogBuilder {
        val thisDrinks: MutableList<String> = calendarModel.drinks.split(",") as MutableList<String>
        val booleans: BooleanArray = BooleanArray(thisDrinks.size) {
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
                /*thisDrinks.forEachIndexed { index, s ->
                    if (booleans[index] == true) {
                        thisDrinks.removeAt(index)
                        booleans.removeAt(index)
                    }
                }
                renewDrinksDialog(thisDrinks.toTypedArray())*/
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


    private fun pushStatusToDB(status: String, eventNumber: String) {
        query.ref.child(eventNumber).apply {
            child(CalendarLiveData.STATUS).setValue(status)
        }
    }

    private fun pushDrinksToBD(drinks: String, eventNumber: String) {
        query.ref.child(eventNumber).apply {
            child(CalendarLiveData.DRINKS).setValue(drinks)
        }
    }

    companion object {
        val statuses = arrayOf("Default", "Preparing", "In process", "Passed")
        private val query = FirebaseDatabase
            .getInstance()
            .getReference("events")
    }
}