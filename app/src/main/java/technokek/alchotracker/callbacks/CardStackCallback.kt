package technokek.alchotracker.callbacks

import androidx.recyclerview.widget.DiffUtil
import technokek.alchotracker.data.models.CardModel

class CardStackCallback(): DiffUtil.Callback() {
    private lateinit var old: MutableList<CardModel>
    private lateinit var baru: MutableList<CardModel>

    constructor(old: MutableList<CardModel>, baru: MutableList<CardModel>) : this() {
        this.old = old
        this.baru = baru
    }

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return baru.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition].name == baru[newItemPosition].name;
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == baru[newItemPosition];
    }

}