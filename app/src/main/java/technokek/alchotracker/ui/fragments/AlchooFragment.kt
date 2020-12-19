package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
    private lateinit var cardStackView:CardStackView
    private lateinit var touchedUid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.alchoo, container, false)

        cardStackView = view.findViewById(R.id.alchoo_card)
        mAlchooViewModel = ViewModelProvider(this)[AlchooViewModel()::class.java]
        activity?.title = title

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val listener = this as AlchooTouchListener

        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                if (direction != null) {
                    Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
                }
            }
            override fun onCardSwiped(direction: Direction?) {
                if (direction == Direction.Right) {
                    mAlchooViewModel.acceptBody(touchedUid)
                }
                if (direction == Direction.Left) {
                    mAlchooViewModel.declineBody(touchedUid)
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
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

        mAlchooViewModel.alchoo.observe(viewLifecycleOwner, {
            if (cardStackView.adapter == null) {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
                cardStackView.adapter = adapter
            } else {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })
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
//        save
    }
}