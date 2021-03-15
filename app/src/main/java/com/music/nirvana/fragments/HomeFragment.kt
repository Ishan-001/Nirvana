package com.music.nirvana.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.music.nirvana.R
import com.music.nirvana.adapters.ImageAdapter
import com.music.nirvana.adapters.TrackAdapter
import com.music.nirvana.models.Track
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyCallback
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.SavedTrack
import retrofit.client.Response


class HomeFragment() : Fragment() {

    private lateinit var token : String
    private lateinit var recyclerView : RecyclerView

    constructor(context: Context) : this()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_home, container, false)
        val prefs = requireContext().getSharedPreferences("com.music.nirvana", Context.MODE_PRIVATE)

        recyclerView = root.findViewById(R.id.recycler_view)
        token = prefs.getString("Access Token","null").toString()
        //Toast.makeText(requireContext(),token,Toast.LENGTH_LONG).show()
        getResults()
        return root
    }

    private fun getResults() {
        val api = SpotifyApi().setAccessToken(token)
        val service = api.service
        val tracks = ArrayList<Track>()

        service.getMySavedTracks(object : SpotifyCallback<Pager<SavedTrack?>?>() {
            override fun success(savedTrackPager: Pager<SavedTrack?>?, response: Response?) {
                val info = savedTrackPager?.items?.get(0)?.track?.name
                //val ifo = savedTrackPager?.items?
                for(track in savedTrackPager!!.items){
                    tracks.add(Track(
                            track!!.track.name,
                            track.track.artists[0].name,
                            track.track.popularity.toString(),
                            track.track.preview_url,
                            track.track.type,
                            track.track.uri))
                }

                val adapter = TrackAdapter(context!!,tracks)
                recyclerView.adapter=adapter
                recyclerView.layoutManager= LinearLayoutManager(context!!)
                Toast.makeText(context, tracks[0].name, Toast.LENGTH_LONG).show()
            }

            override fun failure(error: SpotifyError) {
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}