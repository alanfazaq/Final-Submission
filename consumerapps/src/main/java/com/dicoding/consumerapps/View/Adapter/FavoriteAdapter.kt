package com.dicoding.consumerapps.View.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.consumerapps.Model.User
import com.dicoding.consumerapps.R
import com.dicoding.consumerapps.View.DetailActivity
import com.dicoding.consumerapps.databinding.ItemUserBinding

class FavoriteAdapter(private val context: Context, private val activity: Activity): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite: ArrayList<User> = ArrayList()
        set(listFavorite) {
            if (listFavorite.size > 0 ) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = listFavorite.size

    inner class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemUserBinding.bind(view)
        fun bind(user: User) {
            binding.tvUsername.text = user.username
            binding.tvType.text = user.company
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(binding.imgPhotoUser)
            itemView.setOnClickListener {
                Toast.makeText(context, "${user.username}", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                activity.startActivity(intent)
                activity.finish()
            }
        }
    }
}