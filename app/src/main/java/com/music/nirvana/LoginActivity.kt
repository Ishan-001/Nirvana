package com.music.nirvana

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val CLIENT_ID = "ecd5a410efe543cfb4439eda4c61be0c"
    private val REDIRECT_URI = "https://www.spotify.com/"
    private val REQUEST_CODE = 1337
    private val SCOPES = "user-read-playback-position,user-read-private,user-read-email,playlist-read-private,user-library-read,user-library-modify,user-top-read,playlist-read-collaborative,playlist-modify-public,playlist-modify-private,ugc-image-upload,user-follow-read,user-follow-modify,user-read-playback-state,user-modify-playback-state,user-read-currently-playing,user-read-recently-played"
    private lateinit var prefs: SharedPreferences
    private var token: String = ""
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = baseContext
        prefs = this.getSharedPreferences("com.music.nirvana", Context.MODE_PRIVATE)
        login_button.setOnClickListener { loginWithSpotify() }
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
                    token = response.accessToken
                    prefs.edit().putString("Access Token", response.accessToken).apply()

                    startActivity(Intent(context,StartActivity::class.java))
                    finish()
                }
                AuthenticationResponse.Type.ERROR -> {
                }
                else -> { }
            }
        }
    }
}