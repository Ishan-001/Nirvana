package com.music.nirvana

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import retrofit.client.Response


class StartActivity : AppCompatActivity() {

    private val CLIENT_ID = "ecd5a410efe543cfb4439eda4c61be0c"
    private val REDIRECT_URI = "https://www.spotify.com/"
    private lateinit var context: Context
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private val REQUEST_CODE = 1337
    private val SCOPES = "user-read-playback-position,user-read-private,user-read-email,playlist-read-private,user-library-read,user-library-modify,user-top-read,playlist-read-collaborative,playlist-modify-public,playlist-modify-private,ugc-image-upload,user-follow-read,user-follow-modify,user-read-playback-state,user-modify-playback-state,user-read-currently-playing,user-read-recently-played"
    private lateinit var prefs: SharedPreferences
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        context= applicationContext
        prefs = this.getSharedPreferences("com.music.nirvana", Context.MODE_PRIVATE)

        auth_button.setOnClickListener {
            loginWithSpotify()
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

    private fun loginWithSpotify() {
        val builder = AuthenticationRequest.Builder(
            CLIENT_ID,
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )

        builder.setScopes(arrayOf(SCOPES))
        val request = builder.build()

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> {
                    //Toast.makeText(context, "Authenticated", Toast.LENGTH_LONG).show()
                    token = response.accessToken
                    prefs.edit().putString("Access Token", response.accessToken).apply()
                    play()
                }
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> { }
            }
        }
    }

    private fun play() {
        val api = SpotifyApi().setAccessToken(token)
        val service = api.service

        service.getMySavedTracks(object : SpotifyCallback<Pager<SavedTrack?>?>() {
            override fun success(savedTrackPager: Pager<SavedTrack?>?, response: Response?) {
                val info = savedTrackPager?.items?.get(0)?.track?.name
                Toast.makeText(context, info, Toast.LENGTH_LONG).show()
            }

            override fun failure(error: SpotifyError) {
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}