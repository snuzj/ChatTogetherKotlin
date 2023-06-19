package com.snuzj.chattogetherkotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.snuzj.chattogetherkotlin.R
import com.snuzj.chattogetherkotlin.databinding.ReceivedItemLayoutBinding
import com.snuzj.chattogetherkotlin.databinding.SentItemLayoutBinding
import com.snuzj.chattogetherkotlin.model.MessageModel
import java.util.ArrayList

class MessageAdapter(var context: Context, var list: ArrayList<MessageModel>): Adapter<ViewHolder>() {

    var ITEM_SENT = 1
    var ITEM_RECEIVE = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType == ITEM_SENT)
            sentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false))
        else
            receivedViewHolder(LayoutInflater.from(context).inflate(R.layout.received_item_layout,parent,false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = list[position]
        if(holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as sentViewHolder
            viewHolder.binding.userMsg.text = message.message
        }
        else{
            val viewHolder = holder as receivedViewHolder
            viewHolder.binding.userMsg.text = message.message
        }
    }

    inner class sentViewHolder(view: View) : ViewHolder(view){
        var binding = SentItemLayoutBinding.bind(view)
    }

    inner class receivedViewHolder(view: View) : ViewHolder(view){
        var binding = ReceivedItemLayoutBinding.bind(view)
    }

}