package com.example.bfn.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bfn.databinding.ItemBookBinding
import com.example.bfn.models.Book
import com.example.bfn.util.SimpleCallback
import com.squareup.picasso.Picasso


class BooksAdapter : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    private var books = listOf<Book>()
    private var bookListener: ((String) -> Unit)? = null

    fun openBook(callback: ((String) -> Unit)) {
        this.bookListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return BooksAdapter.ViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        with(holder.binding) {
            tvAuthor.text = book.author
            tvTitle.text = book.title
            Picasso.get().load(book.coverImage?.replace("localhost", "10.0.2.2")).into(imCover)
            tvCategory.text = book.category

            imCover.setOnClickListener {
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

    class ViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)
}

