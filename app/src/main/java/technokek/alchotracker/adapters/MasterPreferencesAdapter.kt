package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.MasterPreferencesViewHolder
import technokek.alchotracker.api.PreferencesClickListener
import technokek.alchotracker.data.models.MasterPreferencesModel

class MasterPreferencesAdapter(
    private var mData: MutableList<MasterPreferencesModel>,
    private val listener: PreferencesClickListener
) :
    RecyclerView.Adapter<MasterPreferencesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterPreferencesViewHolder {
        return MasterPreferencesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.preferences_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MasterPreferencesViewHolder, position: Int) {
        val model = mData[position]
        holder.mNameView.text = model.name
        holder.mDeleteView.setOnClickListener {
            listener.pressEventAddPreference(model.name)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<MasterPreferencesModel>) {
        this.mData = mData
    }
}