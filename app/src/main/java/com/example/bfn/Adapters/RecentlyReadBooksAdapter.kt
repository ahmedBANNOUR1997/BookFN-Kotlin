package com.example.bfn.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bfn.databinding.ItemRecentBookBinding
import com.example.bfn.models.Book
import com.example.bfn.util.SimpleCallback
import com.squareup.picasso.Picasso


class RecentlyReadBooksAdapter : RecyclerView.Adapter<RecentlyReadBooksAdapter.ViewHolder>() {
    private var bookListener: ((String) -> Unit)? = null

    private var books = listOf<Book>()
    fun openBook(callback: ((String) -> Unit)) {
        this.bookListener = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return RecentlyReadBooksAdapter.ViewHolder(
            ItemRecentBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        with(holder.binding) {
            Picasso.get().load(book.coverImage?.replace("localhost","10.0.2.2")).into(imReadBook)
            imReadBook.setOnClickListener {
                bookListener?.let { callback ->
                    callback(book.id!!)
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return books.size
    }


    fun updateBooks(newBooks: List<Book>) {
        val diffResult =
            DiffUtil.calculateDiff(SimpleCallback(this.books, newBooks) { it.id!! })
        this.books = newBooks
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val binding: ItemRecentBookBinding) : RecyclerView.ViewHolder(binding.root)
}

