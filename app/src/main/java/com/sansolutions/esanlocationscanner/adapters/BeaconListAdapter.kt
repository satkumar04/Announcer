package  com.sansolutions.esanlocationscanner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sansolutions.esanlocationscanner.R
import com.sansolutions.esanlocationscanner.db.LocationEntity
import kotlinx.android.synthetic.main.recycler_item.view.*

class BeaconListAdapter(val items : List<LocationEntity>?, val context: Context, val clickListener : (LocationEntity) -> Unit, val deleteClickListener : (LocationEntity) -> Unit) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false))
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.locationName.text = "Name :"+items?.get(position)?.locationname
        holder.meters.text = "Range  :"+items?.get(position)?.locationmeters
        holder.beaconName.text = "Latitude :"+items?.get(position)?.latitude
        holder.rssiValue.text = "Longitude : "+items?.get(position)?.longitude
        holder.arrivingmeters.text = "Arriving range : "+items?.get(position)?.arrivedmeters
        holder.editImageView.setOnClickListener({clickListener(items!!.get(position))})
        holder.deleteImageView. setOnClickListener{ deleteClickListener(items!!.get(position)) }
    }

}

class ViewHolder(view :View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
{
    val beaconName = view.beaconItemName
    val locationName = view.locationname
    val meters = view.meters
    val arrivingmeters = view.arrivingmeters
    val rssiValue = view.beaconRssi
    val editImageView= view.edit
    val deleteImageView = view.delete

}