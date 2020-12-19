package technokek.alchotracker.adapters

import android.annotation.SuppressLint
import android.text.method.Touch.onTouchEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.AlchooViewHolder
import technokek.alchotracker.data.models.AlchooCardModel
import android.util.Log
import technokek.alchotracker.api.AlchooInterface
import technokek.alchotracker.api.AlchooTouchListener

class AlchooAdapter(
    private var mData: MutableList<AlchooCardModel>,
    private val listener: AlchooTouchListener
) :
    RecyclerView.Adapter<AlchooViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlchooViewHolder {
        return AlchooViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.alchoo_card, parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AlchooViewHolder, position: Int) {
        val model = mData[position]
        Picasso.get()
            .load(model.avatar)
            .fit()
            .centerCrop()
            .into(holder.mAvatar)
        holder.mName.text = model.name
        holder.mStatus.text = model.status
        holder.itemView.setOnTouchListener { v, event ->
            listener.touchBody(model.id)
            true
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getData(): MutableList<AlchooCardModel> {
        return mData
    }

    fun setData(data: MutableList<AlchooCardModel>) {
        this.mData = data
    }

}