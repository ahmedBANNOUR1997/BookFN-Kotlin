package com.example.bfn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bfn.databinding.ActivityBookDetailsBinding
import com.example.bfn.models.Book
import com.example.bfn.models.BookId
import com.example.bfn.models.BookResponse
import com.example.bfn.util.ApiClient
import com.example.bfn.util.BlurAppBar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookDetails : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailsBinding
    private val apiservice = ApiClient.apiService

    private var bookId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra(BOOK_ID) ?: return finish()
        Toast.makeText(this,bookId,Toast.LENGTH_SHORT).show()

        setupUi()
        showBook()


    }





    private  fun setupUi(){

        val blurAppBar = BlurAppBar(this)
        blurAppBar.blurAppBar(binding.appBar,binding.imEvent)

        binding.imBack.setOnClickListener {
            finish()
        }
    }


    private fun showBook(){

        bookId?.let {
            apiservice.showBook(BookId(it)).enqueue(object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>?,
                    response: Response<BookResponse>
                ) {

                    if (response.isSuccessful){
                        val book = response.body().response

                    Picasso.get().load(book.coverImage.replace("localhost","10.0.2.2")).into(binding.imBook)
                        Picasso.get().load(book.coverImage.replace("localhost","10.0.2.2")).into(binding.imBlurBook)
                        binding.pages.text = book.nbPages.toString()
                        binding.duration.text = "10:45 min "
                        binding.lang.text = "Fr | Eng"
                        binding.tvPage.text = book.description
                        binding.tvTitile.text = book.title
                        binding.tvAuthor.text = book.author.toString()
                        binding.btnRead.setOnClickListener {
                            BookPdfActivity.start(this@BookDetails,book.filePDF)
                        }





                    }
                }

                override fun onFailure(call: Call<BookResponse>?, t: Throwable?) {
                    Toast.makeText(this@BookDetails,"Network failure", Toast.LENGTH_SHORT).show()


                }

            })
        }
    }

    companion object {
        private const val BOOK_ID = "BOOK_ID"
        private const val BOOK_URL = "BOOK_URL"

        fun start(context: Context, bookId: String) {
            val intent = Intent(context, BookDetails::class.java)
            intent.putExtra(BOOK_ID, bookId)

            context.startActivity(intent)
        }
    }
}