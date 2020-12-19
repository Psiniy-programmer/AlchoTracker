package technokek.alchotracker.callbacks

import androidx.recyclerview.widget.DiffUtil
import technokek.alchotracker.data.models.AlchooCardModel

class CardStackCallback(): DiffUtil.Callback() {
    private lateinit var old: MutableList<AlchooCardModel>
    private lateinit var baru: MutableList<AlchooCardModel>

    constructor(old: MutableList<AlchooCardModel>, baru: MutableList<AlchooCardModel>) : this() {
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