package com.square.repos

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.square.repos.model.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

class ReposAdapter(private val onSelectRepo:OnSelectRepo) : RecyclerView.Adapter<ReposAdapter.Holder>(), BindableAdapter<Repo> {

    private var repos = emptyList<Repo>()

    override fun setData(items: List<Repo>) {
        repos = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.item_repo, parent, false), onSelectRepo)
    }

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(repos[position])
    }

    interface OnSelectRepo {
        fun onSelectRepo(repo: Repo)
    }

    class Holder(itemView: View, private val onSelectRepo:OnSelectRepo) : RecyclerView.ViewHolder(itemView) {

        fun bind(repo: Repo) {
            itemView.title.text = "Repo: ${repo.name}"
            itemView.setOnClickListener {
                onSelectRepo.onSelectRepo(repo)
            }
        }
    }
}