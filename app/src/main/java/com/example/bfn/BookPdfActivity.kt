package com.example.bfn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bfn.databinding.ActivityBookPdfBinding
import com.example.bfn.util.ApiClient
import okhttp3.ResponseBody
import java.io.*

class BookPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookPdfBinding
    private var fileUri: Uri? = null
    private var pdfUrl: String? = null
    private val apiService = ApiClient.apiService

    private val geturiFromFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == Activity.RESULT_OK) {
                fileUri = result.data?.data
                binding.pdfView.fromUri(fileUri)
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .load()

            } else Log.d("Faddit wlh w t3ebet", "Uri from file is null")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookPdfBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupUi()
    }


    private fun setupUi() {
        pdfUrl = intent.getStringExtra(BOOK_PDF) ?: return finish()

        binding.imBack.setOnClickListener {
            finish()
        }
        pdfUrl?.let {


        }


        /*val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        geturiFromFile.launch(intent)*/


    }


    companion object {
        private const val BOOK_PDF = "BOOK_PDF"


        fun start(context: Context, pdf_url: String) {
            val intent = Intent(context, BookPdfActivity::class.java)
            intent.putExtra(BOOK_PDF, pdf_url)

            context.startActivity(intent)
        }
    }
}