package com.bbd.gifsrepository.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bbd.gifsrepository.R
import com.bbd.gifsrepository.data.ResponseState
import com.bbd.gifsrepository.data.consts.SEARCH_DEBOUNCE_IN_MILL
import com.bbd.gifsrepository.data.enums.PageAction
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.data.source.local.model.SearchResults
import com.bbd.gifsrepository.databinding.FragmentHomeGifBinding
import com.bbd.gifsrepository.presentation.adapter.GifsAdapter
import com.bbd.gifsrepository.presentation.base.BaseFragment
import com.bbd.gifsrepository.presentation.viewmodel.HomeGifViewModel
import com.bbd.gifsrepository.util.getQueryTextChangeStateFlow
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@AndroidEntryPoint
class HomeGifFragment : BaseFragment() {


    @Inject
    lateinit var gifsAdapter: GifsAdapter

    lateinit var glide: RequestManager
    private lateinit var binding: FragmentHomeGifBinding
    private val viewModel: HomeGifViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (this::binding.isInitialized) {
            binding
        } else {
            binding = FragmentHomeGifBinding.inflate(inflater, container, false)
            initViews()
            setupListeners()
            setupGifsList()
        }
        initObservers()
        return binding.root
    }

    private fun initViews() {
        glide = Glide.with(this)
    }

    private fun setupListeners() {
        binding.backIv.setOnClickListener {
            viewModel.setAction(PageAction.RANDOM)
        }

        binding.clearSearchIv.setOnClickListener {
            binding.searchInputEt.setText("")
            clearList()
        }

        lifecycleScope.launchWhenCreated {
            binding.searchInputEt.getQueryTextChangeStateFlow()
                .debounce(SEARCH_DEBOUNCE_IN_MILL)
                .distinctUntilChanged()
                .map {
                    if (it.isEmpty()) {
                        gifsAdapter.searchResults = arrayListOf()
                        binding.clearSearchIv.visibility = View.GONE
                    }
                    if (it.trim().isNotEmpty()) {
                        binding.clearSearchIv.visibility = View.VISIBLE
                    }
                    it
                }
                .filter { it.trim().length > 1 }
                .mapLatest {
                    viewModel.setAction(PageAction.SEARCH)
                    viewModel.search(it.trim())
                }
                .collect {}
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (viewModel.getAction() == PageAction.SEARCH) {
                        viewModel.setAction(PageAction.RANDOM)
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setupGifsList() {
        binding.gifResultRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = gifsAdapter
        }

        gifsAdapter.setCallBack {
            findNavController().navigate(
                HomeGifFragmentDirections.actionHomeToGifDetail(
                    it
                )
            )
        }
    }

    private fun initObservers() {
        //observe random gif
        viewModel.gif.observe(viewLifecycleOwner, {
            when (it) {
                is ResponseState.Loading -> {
                    showOrHideLoading(true)
                }
                is ResponseState.Success -> {
                    showOrHideLoading(false)
                    it.data?.let { gifModel ->
                        showGifDetails(gifModel)
                    } ?: kotlin.run {
                        //show error on getting gif
                    }
                }
                is ResponseState.Error -> {
                    showOrHideLoading(false)
                    showErrorMessage(binding.actionTv, localException = it.localException)
                    //show error on getting gif
                }
            }
        })

        //observe search results
        viewModel.searchResult.observe(viewLifecycleOwner, {
            when (it) {
                is ResponseState.Loading -> {
                    showOrHideLoading(true)
                    binding.searchStatusTv.visibility = View.GONE
                }
                is ResponseState.Success -> {
                    showOrHideLoading(false)
                    showResults(it.data)

                }
                is ResponseState.Error -> {
                    showOrHideLoading(false)
                    showErrorMessage(binding.gifResultRv, localException = it.localException)
                }
            }
        })

        //observe page action
        viewModel.pageAction.observe(viewLifecycleOwner, { action ->
            action?.let {
                when (action) {
                    PageAction.RANDOM -> {
                        setupRandomGif()
                    }
                    PageAction.SEARCH -> {
                        setupSearchGif()
                    }
                }
            }
        })
    }

    private fun setupRandomGif() {
        viewModel.startToFetchRandomGif()
        gifsAdapter.searchResults = arrayListOf()
        binding.gifDetailsView.visibility = View.VISIBLE
        binding.searchStatusTv.visibility = View.GONE
        binding.backIv.visibility = View.GONE
        binding.clearSearchIv.visibility = View.GONE
        binding.actionTv.text = getText(R.string.random_gif)
        binding.searchInputEt.setText("")
        hideKeyboard(binding.backIv)
    }

    private fun setupSearchGif() {
        viewModel.cancelFetchingGifJob()
        binding.gifDetailsView.visibility = View.GONE
        binding.searchStatusTv.visibility = View.VISIBLE
        binding.backIv.visibility = View.VISIBLE
        binding.clearSearchIv.visibility = View.VISIBLE
        binding.actionTv.text = getText(R.string.search_results)

    }

    private fun showOrHideLoading(show: Boolean) {
        if (show) binding.loadingPb.visibility = View.VISIBLE else binding.loadingPb.visibility =
            View.INVISIBLE
    }

    private fun showResults(result: SearchResults?) {
        result?.let { results ->
            if (results.searchItems.isEmpty()) noResultsFound()
            else {
                gifsAdapter.searchResults = results.searchItems
            }
        } ?: kotlin.run {
            noResultsFound()
        }
    }

    private fun noResultsFound() {
        binding.searchStatusTv.visibility = View.VISIBLE
        binding.searchStatusTv.text = getText(R.string.no_results_found)
        gifsAdapter.searchResults = arrayListOf()
    }

    private fun clearList() {
        binding.searchStatusTv.visibility = View.VISIBLE
        binding.searchStatusTv.text = getText(R.string.type_something)
        gifsAdapter.searchResults = arrayListOf()
    }

    private fun showGifDetails(gifModel: GifModel) {
        binding.gifDetailsView.setGifDetails(gifModel)
    }

    override fun onDestroyView() {
        viewModel.cancelFetchingGifJob()
        super.onDestroyView()
    }
}