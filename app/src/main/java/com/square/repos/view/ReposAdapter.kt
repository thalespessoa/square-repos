package com.square.repos.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.square.repos.R
import com.square.repos.model.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter(private val onSelectRepo: OnSelectRepo) : RecyclerView.Adapter<ReposAdapter.Holder>(), BindableAdapter<Repo> {

    private var items = emptyList<Repo>()

    override fun setData(items: List<Repo>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.item_repo, parent, false), onSelectRepo)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    interface OnSelectRepo {
        fun onSelectRepo(repo: Repo)
    }

    //----------------------------------------------------------------------------------------------
    // View Holder
    //----------------------------------------------------------------------------------------------

    class Holder(itemView: View, private val onSelectRepo: OnSelectRepo) : RecyclerView.ViewHolder(itemView) {

        fun bind(repo: Repo) {
            itemView.title.text = repo.name
            itemView.fav.setImageDrawable(
                    itemView.context.resources.getDrawable(if (repo.isSaved)
                        R.drawable.baseline_favorite_24px
                    else
                        R.drawable.baseline_favorite_border_24px))
            itemView.setOnClickListener {
                onSelectRepo.onSelectRepo(repo)
            }
        }
    }
}