package com.example.fyp2.Search

import android.content.Intent
import android.content.SearchRecentSuggestionsProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fyp2.MAP.DisplayMapsActivity
import com.example.fyp2.R
import kotlinx.android.synthetic.main.activity_search_main_page.*

class SearchMainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_main_page)


        Search_searchbtn.setOnClickListener {

            val intent = Intent(this, DisplayMapsActivity::class.java)
            startActivity(intent)

        }


        Search_requestbtn.setOnClickListener {

            val intent = Intent(this, SeachRequestInformation::class.java)
            startActivity(intent)

        }

    }
}
