package com.square.repos.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.square.repos.R
import com.square.repos.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.Holder>(), BindableAdapter<User> {

    private var items = emptyList<User>()

    override fun setData(items: List<User>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    //----------------------------------------------------------------------------------------------
    // View Holder
    //----------------------------------------------------------------------------------------------

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            itemView.title.text = user.login
        }
    }
}