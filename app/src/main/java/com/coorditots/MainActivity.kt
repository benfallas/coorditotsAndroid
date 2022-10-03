package com.coorditots

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), KeyboardAdapter.Listener {

    private lateinit var database: DatabaseReference
    var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity Test", "MainActivity")

        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance().reference

        val alphonics: MutableList<Alphonic> = mutableListOf()

        val gridView = findViewById<GridView>(R.id.gridview)
        val adapter = KeyboardAdapter(this, alphonics, this)

        gridView.adapter = adapter

        database
            .child("1erVDFdI8r89ZsEaLmJ5hE_MY9ho7d6v6trH3HoD9jEE/Default Phonics")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    for (value in it.children) {
                        val alphonic = value.getValue(Alphonic::class.java)
                        if (alphonic != null) {
                            alphonics.add(alphonic)

                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                Log.d("FirebaseTest", "Got value ${it.value}")
            }.addOnFailureListener {
                Log.d("FirebaseTest", "error getting data ", it)
            }
    }

    override fun onKeyTapped(key: Alphonic) {
        if (mMediaPlayer == null) {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            val subtitle = view.findViewById<TextView>(R.id.dialog_subtitle)
            val image = view.findViewById<ImageView>(R.id.dialog_image)


            val spannable = SpannableString(key.subtitle)
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.light_pink)),
                0, 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(
                RelativeSizeSpan(2.0f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            subtitle.setText(spannable)
            if (!TextUtils.isEmpty(key.imageUrl)) {
                Picasso.get().load(key.imageUrl).into(image);
            }

            mMediaPlayer = MediaPlayer.create(this, Uri.parse(key.voiceUrl))
            mMediaPlayer!!.start()
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

            mMediaPlayer!!.setOnCompletionListener {
                dialog.hide()
                mMediaPlayer = null

            }
        }
    }
}