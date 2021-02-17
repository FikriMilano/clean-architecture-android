package com.example.github.core.ui

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.example.github.core.ui.model.UserPresenter

class UserDiffCallback(
    private val oldList: List<UserPresenter>,
    private val newList: List<UserPresenter>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, oldUsername, oldAvatarUrl) = oldList[oldItemPosition]
        val (_, newUsername, newAvatarUrl) = newList[newItemPosition]

        return oldUsername == newUsername && oldAvatarUrl == newAvatarUrl
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}