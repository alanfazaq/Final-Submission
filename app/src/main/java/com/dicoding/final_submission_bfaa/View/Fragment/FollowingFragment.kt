package com.dicoding.final_submission_bfaa.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.final_submission_bfaa.Model.User
import com.dicoding.final_submission_bfaa.View.Adapter.FollowingAdapter
import com.dicoding.final_submission_bfaa.View.DetailActivity
import com.dicoding.final_submission_bfaa.View.ViewModel.FollowingViewModel
import com.dicoding.final_submission_bfaa.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingAdapter: FollowingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = followingAdapter
        binding.rvFollowing.setHasFixedSize(true)

        val intent = activity?.intent?.getParcelableExtra<User>(DetailActivity.EXTRA_USER) as User
        val getUser = intent.username

        if (getUser != null) {
            getUserFollowing(getUser)
        }
    }

    private fun getUserFollowing(getUser: String) {
        val followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        followingViewModel.setUserFollowing(getUser, requireActivity())
        followingViewModel.getUserFollowing().observe(requireActivity(), Observer {
            followingAdapter.setData(it)
            println(followingAdapter)
        })
    }
}