package technokek.alchotracker.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import technokek.alchotracker.R
import technokek.alchotracker.data.models.TimerModel
import technokek.alchotracker.receivers.AlertReceiver
import technokek.alchotracker.viewmodels.TimerViewModel
import java.util.*

class NewTimerFragment : Fragment() {
    private lateinit var tvTimeLeft: TextView
    private lateinit var interruptButton: Button
    private lateinit var startButton: Button
    private lateinit var res : Resources
    private lateinit var mTimerViewModel: TimerViewModel
    private var notificationCounter1 = 0
    private var notificationCounter2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        res = requireActivity().resources
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTimerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]
        mTimerViewModel.mMediatorLiveData.observe(
            viewLifecycleOwner,
            {
                //Но где сохранять список имеющихся алармов? Они будут храниться в LiveData
                Log.d("Change Timer Data", "Data changed")
            }
        )
        interruptButton = view.findViewById(R.id.button_interrupt)
        interruptButton.setOnClickListener {
            //TODO по интеррапту будем посылать оповещение об окончании события
        }
        startButton = view.findViewById(R.id.button_start)
        startButton.setOnClickListener {
            //TODO
        }
        tvTimeLeft = view.findViewById(R.id.time_textView)
    }
}