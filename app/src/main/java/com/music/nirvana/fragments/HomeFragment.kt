package com.music.nirvana.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.music.nirvana.R
import com.music.nirvana.adapters.TrackAdapter
import com.music.nirvana.models.Track
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyCallback
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.SavedTrack
import retrofit.client.Response


class HomeFragment() : Fragment() {

    private lateinit var token : String
    private lateinit var recyclerView : RecyclerView
    private val CLIENT_ID = "ecd5a410efe543cfb4439eda4c61be0c"
    private val REDIRECT_URI = "https://www.spotify.com/"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null

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

                val adapter = TrackAdapter(context!!,tracks,mSpotifyAppRemote!!)
                recyclerView.adapter=adapter
                recyclerView.layoutManager= LinearLayoutManager(context!!)
            }

            override fun failure(error: SpotifyError) {
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    getResults()
                    Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun connected() {
        //mSpotifyAppRemote?.playerApi?.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
    }
}