package com.example.bfn.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bfn.databinding.ItemBookBinding
import com.example.bfn.databinding.ItemRecentBookBinding
import com.example.bfn.models.Book
import com.example.bfn.util.SimpleCallback
import com.squareup.picasso.Picasso


class BookShelfAdapter : RecyclerView.Adapter<BookShelfAdapter.ViewHolder>() {

    private var books = listOf<Book>()
    private var bookListener: ((String) -> Unit)? = null

    fun openBook(callback: ((String) -> Unit)) {
        this.bookListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return BookShelfAdapter.ViewHolder(
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
            Picasso.get().load(book.coverImage?.replace("localhost:3000","bookfanatic.herokuapp.com")).into(imReadBook)
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
        this.notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemRecentBookBinding) : RecyclerView.ViewHolder(binding.root)
}

