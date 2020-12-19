package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.CardStackViewHolder
import technokek.alchotracker.data.models.CardModel

class CardStackAdapter(private var mData: MutableList<CardModel>) :
    RecyclerView.Adapter<CardStackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackViewHolder {
        return CardStackViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.alchoo_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardStackViewHolder, position: Int) {
        val model = mData[position]
//        holder.mAvatar.setImage
        holder.mName.text = model.name
        holder.mStatus.text = model.status
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getData(): MutableList<CardModel> {
        return mData
    }

    fun setData(data: MutableList<CardModel>) {
        this.mData = data
    }

}