package technokek.alchotracker.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.data.repositories.Constants

class MemberEventProfile : Fragment() {
    private lateinit var alkoEventPlace: MaterialTextView
    private lateinit var alkoEventStatus: MaterialTextView
    private lateinit var priceNumber: MaterialTextView
    private lateinit var eventPhoto: MaterialCardView
    private lateinit var eventDrinks: MaterialCardView
    private lateinit var eventMembers: MaterialCardView
    private lateinit var eventAvatarView: ImageView

    private lateinit var drinks: Array<String>
    private lateinit var calendarModel:CalendarModel
    private lateinit var thisDrinks:MutableList<String>
    private lateinit var drinksDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.alko_event_toolbar)
        return inflater.inflate(R.layout.member_event_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alkoEventPlace = view.findViewById(R.id.member_event_name)
        alkoEventStatus = view.findViewById(R.id.member_event_status_text)
        priceNumber = view.findViewById(R.id.member_price_number)
        eventPhoto = view.findViewById(R.id.member_event_photo)
        eventDrinks = view.findViewById(R.id.member_event_drinks_btn)
        eventMembers = view.findViewById(R.id.member_event_members_btn)
        eventAvatarView = view.findViewById(R.id.member_event_profile_avatar)


        if (arguments != null) {
            val args = AdminEventProfileFragmentArgs.fromBundle(requireArguments())
            calendarModel = args.calendarModel
            thisDrinks = calendarModel.drinks.split(",") as MutableList<String>
            drinksDialog = buildDrinksDialog()
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

        eventDrinks.setOnClickListener {
            drinksDialog.show()
        }
        /*
        Слава, это твоя хуня
         */
        eventMembers.setOnClickListener {
            //TODO in navgraph
            val navController =
                activity?.let { it -> Navigation.findNavController(it, R.id.content) }

            val action
                    = MemberEventProfileDirections
                .actionMemberEventProfileToMembersFragment(calendarModel.id)
            navController?.navigate(action)
        }
    }

    private fun buildDrinksDialog(): MaterialAlertDialogBuilder {

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(Constants.DRINKS)
            .setItems(thisDrinks.toTypedArray()) {
                dialog, which ->

            }
            .setPositiveButton("Close") {
                dialog, which ->
            }
            .setBackground(resources.getDrawable(R.drawable.bg_status_dialog))
    }
}