package com.dicoding.consumerapps.View.Adapter

import android.view.View

class CustomOnClickListener(  private val position: Int,
                              private val onItemClickCallback: OnItemClickCallback
) : View.OnClickListener {
    override fun onClick(v: View?) {
        onItemClickCallback.onItemClicked(v!!, position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}
