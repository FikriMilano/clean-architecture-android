package com.example.github.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.github.core.R
import com.example.github.core.databinding.ItemListUserBinding
import com.example.github.core.ui.model.UserPresenter

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var listUser = listOf<UserPresenter>()
    var onItemClick: ((UserPresenter) -> Unit)? = null

    fun setData(newListUser: List<UserPresenter>?) {
        if (newListUser == null) return
        val diffCallback = UserDiffCallback(listUser, newListUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listUser = newListUser
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_user, parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(listUser[position])

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListUserBinding.bind(itemView)

        fun bind(data: UserPresenter) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.avatarUrl)
                    .placeholder(R.color.cool_grey)
                    .override(100, 100)
                    .into(ivAvatar)
                tvUsername.text = data.username
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listUser[adapterPosition])
            }
        }
    }
}