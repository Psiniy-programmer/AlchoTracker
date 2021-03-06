package technokek.alchotracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yuyakaido.android.cardstackview.*
import technokek.alchotracker.R
import technokek.alchotracker.adapters.AlchooAdapter
import technokek.alchotracker.api.AlchooTouchListener
import technokek.alchotracker.viewmodels.AlchooViewModel


class AlchooFragment : Fragment(), AlchooTouchListener {
    lateinit var manager: CardStackLayoutManager
    lateinit var adapter: AlchooAdapter
    private lateinit var mAlchooViewModel: AlchooViewModel
    private lateinit var mAlchooStatusOnLayout: RelativeLayout
    private lateinit var mAlchooStatusOffLayout: LinearLayout
    private lateinit var cardStackView: CardStackView
    private lateinit var touchedUid: String
    private lateinit var refresherView: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var statusButton: Button
    private lateinit var mAlchooLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.alchoo, container, false)

        mAlchooStatusOnLayout = view.findViewById(R.id.alchoo_status_on)
        mAlchooStatusOffLayout = view.findViewById(R.id.alchoo_status_off)
        cardStackView = view.findViewById(R.id.alchoo_card)
        refresherView = view.findViewById(R.id.alchoo_refresh)
        refreshButton = view.findViewById(R.id.alchoo_refresh__button)
        mAlchooLayout = view.findViewById(R.id.alchoo_fragment)
        statusButton = view.findViewById(R.id.alchoo_status__button)
        mAlchooViewModel = ViewModelProvider(this)[AlchooViewModel()::class.java]
        activity?.title = title

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listener = this as AlchooTouchListener

        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                if (direction != null) {
                    val kek: Float = ratio * ratio * 255
                    val res: Int = kek.toInt()
                    when (direction) {
                        Direction.Left -> {
                            mAlchooLayout.setBackgroundColor(Color.rgb(255, 255 - res, 255 - res))
                        }
                        Direction.Right -> {
                            mAlchooLayout.setBackgroundColor(Color.rgb(255 - res, 255, 255 - res))
                        }
                    }
                }
            }

            override fun onCardSwiped(direction: Direction?) {
                mAlchooLayout.setBackgroundColor(Color.rgb(255, 255, 255))
                when (direction) {
                    Direction.Right -> mAlchooViewModel.acceptBody(touchedUid)
                    Direction.Left -> mAlchooViewModel.declineBody(touchedUid)
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
                mAlchooLayout.setBackgroundColor(Color.rgb(255, 255, 255))
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
                mAlchooLayout.setBackgroundColor(Color.rgb(255, 255, 255))

            }

            override fun onCardAppeared(view: View?, position: Int) {
                Log.d(TAG, "accepted")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.d(TAG, "decline")
            }
        })

        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = if (mAlchooViewModel.alchoo.value != null) {
            AlchooAdapter(mAlchooViewModel.alchoo.value!!, listener)
        } else {
            AlchooAdapter(mutableListOf(), listener)
        }

        mAlchooViewModel.status.observe(viewLifecycleOwner, {
            if (it.status) {
                mAlchooStatusOnLayout.visibility = View.VISIBLE
                mAlchooStatusOffLayout.visibility = View.GONE
            } else {
                mAlchooStatusOffLayout.visibility = View.VISIBLE
                mAlchooStatusOnLayout.visibility = View.GONE
            }
        })

        mAlchooViewModel.alchoo.observe(viewLifecycleOwner, {
            if (cardStackView.adapter == null) {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
                cardStackView.adapter = adapter
            } else {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }

            if (adapter.itemCount == 0) {
                cardStackView.visibility = View.GONE
                refresherView.visibility = View.VISIBLE
            } else {
                cardStackView.visibility = View.VISIBLE
                refresherView.visibility = View.GONE
            }
        })

        refreshButton.setOnClickListener {
            mAlchooViewModel.refreshList()
        }

        statusButton.setOnClickListener {
            mAlchooViewModel.changeStatus()
        }
        cardStackView.layoutManager = manager
        cardStackView.itemAnimator = DefaultItemAnimator()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "AlchooFragment"
        const val title = "Alchoo"
    }

    override fun touchBody(uid: String) {
        touchedUid = uid
    }
}