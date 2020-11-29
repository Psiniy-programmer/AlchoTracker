package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.PreferencesViewHolder
import technokek.alchotracker.data.models.PreferencesModel

class PreferencesAdapter(private var mData: MutableList<PreferencesModel>) :
    RecyclerView.Adapter<PreferencesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferencesViewHolder {
        return PreferencesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.preferences_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PreferencesViewHolder, position: Int) {
        val model = mData[position]
        holder.mNameView.text = model.name
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<PreferencesModel>) {
        this.mData = mData
    }
}