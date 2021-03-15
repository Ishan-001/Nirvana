package com.music.nirvana.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.music.nirvana.R
import com.music.nirvana.models.Track
import com.spotify.android.appremote.api.SpotifyAppRemote


class TrackAdapter internal constructor(
    private val context: Context,
    private val trackList: ArrayList<Track>,
    private val spotifyAppRemote: SpotifyAppRemote

): RecyclerView.Adapter<TrackAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var trackName: TextView = itemView.findViewById(R.id.track_name)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        var trackVibe: TextView = itemView.findViewById(R.id.track_vibe)
        var trackImage: ImageView = itemView.findViewById(R.id.track_image)
        var trackRecommendation: TextView = itemView.findViewById(R.id.track_recommendation)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.track_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackAdapter.ViewHolder, position: Int) {
        holder.trackName.text = trackList[position].name
        holder.trackArtist.text = trackList[position].artist
        holder.trackVibe.text = trackList[position].vibe
        holder.trackRecommendation.text = trackList[position].recommendation+"%"

        holder.itemView.setOnClickListener{ spotifyAppRemote.playerApi.play(trackList[position].trackURI) }

    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}

