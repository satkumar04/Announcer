package  com.sansolutions.esanlocationscanner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sansolutions.esanlocationscanner.db.LocationEntity
import com.sansolutions.esanlocationscanner.R
import kotlinx.android.synthetic.main.route_list_item.view.*


class RouteListAdapter(private val items: List<LocationEntity>?, val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RouteViewHolder>() {

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.route_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {

        holder.routeName.text = items?.get(position)?.locationname
    }


}

class RouteViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    val routeName: TextView = view.routeName

}