package com.bbd.gifsrepository.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bbd.gifsrepository.data.consts.getRatingType
import com.bbd.gifsrepository.data.source.local.model.GifModel
import com.bbd.gifsrepository.databinding.FragmentGifDetailsBinding
import com.bbd.gifsrepository.presentation.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.gif_detail_layout.view.*
import javax.inject.Inject

@AndroidEntryPoint
class GifDetailsFragment : BaseFragment() {

    lateinit var glide: RequestManager
    private var _binding: FragmentGifDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: GifDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGifDetailsBinding.inflate(inflater, container, false)
        glide = Glide.with(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        showGifDetails(args.gifDetails)
    }

    private fun setupListeners() {
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showGifDetails(gifModel: GifModel) {
        binding.gifDetailsView.setGifDetails(gifModel)
        binding.gifHeaderTv.text = gifModel.title
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}