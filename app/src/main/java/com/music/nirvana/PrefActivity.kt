package com.music.nirvana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.music.nirvana.fragments.GenreFragment
import kotlinx.android.synthetic.main.activity_pref.*


class PrefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref)

        home.setOnClickListener { repl }

        replace(GenreFragment())
    }

    private fun replace(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}