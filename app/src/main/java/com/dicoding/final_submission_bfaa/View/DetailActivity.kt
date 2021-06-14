package com.dicoding.final_submission_bfaa.View

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_AVATAR
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_COMPANY
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_LOCATION
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_REPOSITORY
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_TYPE
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.COLUMNS_USERNAME
import com.dicoding.final_submission_bfaa.Database.DBContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.final_submission_bfaa.Helper.UserHelper
import com.dicoding.final_submission_bfaa.Model.User
import com.dicoding.final_submission_bfaa.R
import com.dicoding.final_submission_bfaa.View.Adapter.SectionPageAdapter
import com.dicoding.final_submission_bfaa.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_POSITION = "extra_position"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var userHelper: UserHelper
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        viewPagerConfig()
        val textClock: TextClock = findViewById<TextClock>(R.id.textClock)
        textClock.setOnClickListener{
            val time = textClock.text
            Toast.makeText(this, time,Toast.LENGTH_LONG).show()
        }
        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val values = ContentValues()
        userHelper = UserHelper.getInstance(this)
        userHelper.open()
        binding.fabFavorite.setOnClickListener {
            if (statusFavorite) {
                userHelper.deleteDB(user.username.toString())
                setStatusFavorite(false)
                Toast.makeText(this, "Delete from Favorite", Toast.LENGTH_SHORT).show()
            } else {
                values.put(COLUMNS_USERNAME, user.username)
                values.put(COLUMNS_TYPE, user.type)
                values.put(COLUMNS_LOCATION, user.location)
                values.put(COLUMNS_REPOSITORY, user.repository)
                values.put(COLUMNS_COMPANY, user.company)
                values.put(COLUMNS_AVATAR, user.avatar)

                contentResolver.insert(CONTENT_URI, values)
                setStatusFavorite(true)
                Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
            }
        }

        val select = userHelper.queryById(user.username.toString())
        if (select.moveToNext()) {
            setStatusFavorite(true)
        }else {
            setStatusFavorite(false)
        }
    }

    private fun getData() {
        val extras = intent
        val user = extras.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = user.username
        binding.tvUsername.text = user.username
        binding.tvType.text = user.type
        binding.tvCompany.text = user.company
        binding.tvLocation.text = user.location
        binding.tvRepository.text = user.repository

        Glide.with(this)
            .load(user.avatar)
            .into(binding.imgAvatar)
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun viewPagerConfig() {
        val sectionPagerAdapter = SectionPageAdapter(this, this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setStatusFavorite(favorite: Boolean) {
        if (favorite) {
            statusFavorite = true
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            Toast.makeText(this, "Add to Favorite", Toast.LENGTH_SHORT).show()
        }else{
            statusFavorite = false
            binding.fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}