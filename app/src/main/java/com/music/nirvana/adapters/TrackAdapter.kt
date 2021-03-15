package com.music.nirvana.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.music.nirvana.R
import kaaes.spotify.webapi.android.models.Track

class TrackAdapter internal constructor(
    private val context: Context,
    private val requestList: ArrayList<Track>

): RecyclerView.Adapter<TrackAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var trackName: TextView = itemView.findViewById(R.id.track_name)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        var trackVibe: TextView = itemView.findViewById(R.id.track_vibe)
        var trackImage: TextView = itemView.findViewById(R.id.track_image)
        var trackRecommendation: TextView = itemView.findViewById(R.id.track_recommendation)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        val request= requestList[position]


    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}