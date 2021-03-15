package com.music.nirvana.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.music.nirvana.LoginActivity
import com.music.nirvana.R
import com.music.nirvana.adapters.ImageAdapter
import kotlinx.android.synthetic.main.fragment_vibe.view.*


class VibeoFragment() : Fragment() {
    constructor(context: Context) : this()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_vibe, container, false)

        root.find_vibe.setOnClickListener{
            root.progress_bar.visibility=View.VISIBLE
            Handler().postDelayed(Runnable {
                root.progress_bar.visibility = View.INVISIBLE
                Toast.makeText(context,"Coming soon",Toast.LENGTH_LONG).show()
            }, 2000)
        }

        return root
    }
}