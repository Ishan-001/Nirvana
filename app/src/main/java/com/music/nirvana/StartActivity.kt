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
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity() {

    private val CLIENT_ID = "ecd5a410efe543cfb4439eda4c61be0c"
    private val REDIRECT_URI = "https://www.spotify.com/"
    private lateinit var context: Context
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private val REQUEST_CODE = 1337
    private val SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private"
    private lateinit var prefs: SharedPreferences

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
                    Toast.makeText(context, "Authenticated", Toast.LENGTH_LONG).show()
                    prefs.edit().putString("Access Token", response.accessToken).apply()
                }
                AuthenticationResponse.Type.ERROR -> { }
                else -> { }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}