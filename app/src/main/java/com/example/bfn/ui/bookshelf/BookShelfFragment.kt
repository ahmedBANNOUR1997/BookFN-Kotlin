package com.example.bfn.ui.bookshelf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bfn.Adapters.BooksAdapter
import com.example.bfn.Adapters.RecentlyReadBooksAdapter
import com.example.bfn.BookDetails
import com.example.bfn.R
import com.example.bfn.UploadBook
import com.example.bfn.databinding.FragmentBookshelfBinding
import com.example.bfn.databinding.FragmentHomeBinding
import com.example.bfn.models.BooksResponse
import com.example.bfn.util.ApiClient
import kotlinx.android.synthetic.main.fragment_bookshelf.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookShelfFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentBookshelfBinding? = null
    private val binding get() = _binding!!
    private val apiservice = ApiClient.apiService
    private val mAdapter = RecentlyReadBooksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        _binding = FragmentBookshelfBinding.inflate(inflater, container, false)
        binding.rvBookShelf.apply {
            layoutManager = GridLayoutManager(requireActivity(),3)
            adapter = mAdapter
        }

        binding.imAddBook.setOnClickListener {
            startActivity(Intent(requireActivity(),UploadBook::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiservice.getAllBooks().enqueue(object : Callback<BooksResponse> {
            override fun onResponse(
                call: Call<BooksResponse>,
                response: Response<BooksResponse>
            ) {
                if (response.isSuccessful) {
                    mAdapter.updateBooks(response.body().response)
                    mAdapter.openBook {
                       // Toast.makeText(requireActivity(),it,Toast.LENGTH_SHORT).show()
                        BookDetails.start(requireActivity(),it)

                    }

                }
            }

            override fun onFailure(call: Call<BooksResponse>?, t: Throwable?) {
                Log.d("krahet 7yeti",t?.message.toString())
                Toast.makeText(requireActivity(), "Network faillure", Toast.LENGTH_SHORT).show()
            }

        })



    }


}