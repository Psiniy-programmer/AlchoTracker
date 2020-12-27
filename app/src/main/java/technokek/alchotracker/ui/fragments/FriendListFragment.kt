package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import technokek.alchotracker.R

class FriendListFragment : Fragment() {

    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_friend, container, false)

        activity?.title = resources.getString(R.string.friend_title)
        uid = arguments?.get("uid") as String

        return view
    }


}
