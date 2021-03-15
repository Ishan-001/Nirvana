package com.music.nirvana

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.music.nirvana.fragments.GenreFragment
import com.music.nirvana.fragments.ZodiacFragment
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kaaes.spotify.webapi.android.SpotifyApi
import kaaes.spotify.webapi.android.SpotifyCallback
import kaaes.spotify.webapi.android.SpotifyError
import kaaes.spotify.webapi.android.models.Pager
import kaaes.spotify.webapi.android.models.SavedTrack
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.screen_genre.*
import retrofit.client.Response


class StartActivity : AppCompatActivity() {

    private lateinit var context: Context
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private val CLIENT_ID = "ecd5a410efe543cfb4439eda4c61be0c"
    private val REDIRECT_URI = "https://www.spotify.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        context= applicationContext
        replace(GenreFragment(context))

        var i=0;

        next_button.setOnClickListener{
            if(i==0) {
                replace(ZodiacFragment(context))
                i += 1;
            }
            else
                startActivity(Intent(context,PrefActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true)
                .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote

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


    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    private fun replace(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}