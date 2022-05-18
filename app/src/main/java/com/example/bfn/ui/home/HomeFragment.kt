package com.example.bfn.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bfn.Adapters.BooksAdapter
import com.example.bfn.Adapters.RecentlyReadBooksAdapter
import com.example.bfn.BookDetails
import com.example.bfn.BookPdfActivity
import com.example.bfn.databinding.FragmentHomeBinding
import com.example.bfn.models.*
import com.example.bfn.prefs.PrefsManager
import com.example.bfn.util.ApiClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val apiservice = ApiClient.apiService
    private val readBooksAdapter = BooksAdapter()
    private val recentlyReadBooksAdapter = RecentlyReadBooksAdapter()
    private var bookPATH : String? = null
    private var BOOK_PDF = "BOOK_PDF"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupUi()

        binding.btnContinueReading.setOnClickListener {
            val intent = Intent(activity, BookPdfActivity::class.java)
            var bookURLL = bookPATH?.replace("localhost","10.0.2.2")
            intent.putExtra(BOOK_PDF,bookURLL)
            startActivity(intent)
        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllBooks()
        showLastRecentlyReadBookInUser()
        showAllRecentlyReadBooks()
        getUser()
    }


    private fun setupUi() {


        binding.rvRecent.apply {
            adapter = recentlyReadBooksAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvBooks.apply {
            adapter = readBooksAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }


    }

    private fun getAllBooks() {

        apiservice.getAllBooks().enqueue(object : Callback<BooksResponse> {
            override fun onResponse(
                call: Call<BooksResponse>,
                response: Response<BooksResponse>
            ) {
                if (response.isSuccessful) {
                    readBooksAdapter.updateBooks(response.body().response)

                    readBooksAdapter.openBook {
                        // Toast.makeText(requireActivity(),it,Toast.LENGTH_SHORT).show()
                        BookDetails.start(requireActivity(),it)

                    }

                }
            }

            override fun onFailure(call: Call<BooksResponse>?, t: Throwable?) {
                Toast.makeText(requireActivity(), "Network failure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUser() {
        val token = PrefsManager.geToken(requireActivity())
         token?.let {
            apiservice.getUserById(UserX(token)).enqueue(object : Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>?,
                    response: Response<GetUserResponse>
                ) {
                    if (response.isSuccessful){
                        binding.tvUsername.text = "Hi " + response.body().dataid.firstName +" !"
                    }
                }

                override fun onFailure(call: Call<GetUserResponse>?, t: Throwable?) {
                    Toast.makeText(requireActivity(), "Network Failure", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    private fun showLastRecentlyReadBookInUser() {
        val token = PrefsManager.geToken(requireActivity())
        token?.let {
            apiservice.showLastRecentlyReadBook(UserY(token)).enqueue(object : Callback<lastBook?> {
                override fun onResponse(
                    call: Call<lastBook?>?,
                    response: Response<lastBook?>

                ) {
                    if (response.isSuccessful){

                        val LastReadBook = response.body()?.lastBook
                        if (LastReadBook != null) {
                            bookPATH = LastReadBook.filePDF.toString()
                        }
                            Log.d("AHMED", response.body().toString())
                        LastReadBook?.let {
                            // this condition is allowing the LastReadBook to be null miselich ama mannajamich
                            // nconditioni ala est ce que LastReadBook est null ou pas !!!
                            binding.tvBookTitle.text = it.title
                            binding.tvAuteur.text = "By " + it.author
                            Picasso.get().load(it?.coverImage?.replace("localhost","10.0.2.2")).into(binding.imHomePicture)

                        }
                        if(LastReadBook == null ) {
                            binding.btnContinueReading.visibility = View.INVISIBLE
                            binding.btnAudioBook.visibility = View.INVISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<lastBook?>?, t: Throwable?) {
                    Toast.makeText(requireActivity(), "Network Failure", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

    private fun showAllRecentlyReadBooks() {
        val token = PrefsManager.geToken(requireActivity())
        token?.let {
            apiservice.showAllRecentlyReadBooks(UserY(token)).enqueue(object : Callback<RecentlyReadBooks?> {
                override fun onResponse(
                    call: Call<RecentlyReadBooks?>?,
                    response: Response<RecentlyReadBooks?>

                ) {
                    if (response.isSuccessful){

                        val LISTBOOKS = response.body()?.listBooks
                        LISTBOOKS?.let {

                            recentlyReadBooksAdapter.updateBooks(it)

                        }

                        recentlyReadBooksAdapter.openBook {
                            // Toast.makeText(requireActivity(),it,Toast.LENGTH_SHORT).show()
                            BookDetails.start(requireActivity(),it)

                        }

                    }
                }

                override fun onFailure(call: Call<RecentlyReadBooks?>?, t: Throwable?) {
                    Toast.makeText(requireActivity(), "Network Failure", Toast.LENGTH_SHORT).show()
                }

            })

        }
    }

}