package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.yuyakaido.android.cardstackview.*
import technokek.alchotracker.R
import technokek.alchotracker.adapters.CardStackAdapter
import technokek.alchotracker.callbacks.CardStackCallback
import technokek.alchotracker.data.models.CardModel


class AlchooFragment : Fragment() {
    lateinit var manager: CardStackLayoutManager
    lateinit var adapter: CardStackAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.alchoo, container, false)
        val cardStackView = view.findViewById<CardStackView>(R.id.alchoo_card)
        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
                if (direction != null) {
                    Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
                };
            }

            override fun onCardSwiped(direction: Direction?) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right) {
                    Toast.makeText(context, "Direction Right", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Top) {
                    Toast.makeText(context, "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Left) {
                    Toast.makeText(context, "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(context, "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                if (manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }

            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition);
            }

            override fun onCardAppeared(view: View?, position: Int) {
                Log.d(TAG, "onCardAppeared: " + position.toString() + ", nama: ")
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                Log.d(TAG, "onCardAppeared: " + position.toString() + ", nama: ")
            }

        })
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addData())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()

        return view
    }

    private fun paginate() {
        val old: MutableList<CardModel> = adapter.getData()
        val baru: MutableList<CardModel> = ArrayList(addData())
        val callback = CardStackCallback(old, baru)
        val hasil = DiffUtil.calculateDiff(callback)
        adapter.setData(baru)
        hasil.dispatchUpdatesTo(adapter)
    }

    private fun addData(): MutableList<CardModel> {
        val data: MutableList<CardModel> = ArrayList()
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        data.add(CardModel("kek", "lol"))
        return data
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = title
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "AlchooFragment"
        const val title = "Alchoo"
    }
}