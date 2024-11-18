package com.example.myeventapp2.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myeventapp2.data.response.Event
import com.example.myeventapp2.data.response.ListEventResponse
import com.example.myeventapp2.data.retrofit.ApiConfig
import com.example.myeventapp2.databinding.ItemEventBinding
import com.example.myeventapp2.ui.detail.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventAdapter : ListAdapter<Event, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, holder.itemView.context)
    }
    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, context: Context){
            binding.tvEventName.text = "${event.name}"
            binding.tvEventCityName.text = "📍 ${event.cityName}"
            binding.tvEventOwnerName.text = " ${event.ownerName}"
            Glide.with(context)
                .load(event.mediaCover)
                .into(binding.ivEventMediaCover)

            binding.cardView.setOnClickListener {

                val moveIntent = Intent(context, DetailActivity::class.java)
                moveIntent.putExtra(EXTRA_EVENT, event)
                context.startActivity(moveIntent)
            }
        }


    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
        private const val TAG = "EventAdapter"

        const val EXTRA_EVENT = "extra_event"
    }
}