package com.example.github.detail

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.github.MyApplication
import com.example.github.R
import com.example.github.core.data.Resource
import com.example.github.core.ui.ViewModelFactory
import com.example.github.core.utils.EspressoIdlingResource
import com.example.github.databinding.FragmentDetailUserBinding
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailUserFragment : Fragment(), MaterialButtonToggleGroup.OnButtonCheckedListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private val detailUserViewModel: DetailUserViewModel by viewModels {
        factory
    }

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!

    private val args: DetailUserFragmentArgs by navArgs()
    private var isFavorited: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (activity != null) {

            with(binding) {
                btnToggleGroupFavorite.addOnButtonCheckedListener(this@DetailUserFragment)
                toolbar.setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
                viewError.btnRetry.setOnClickListener {
                    progressBar.root.visibility = View.VISIBLE
                    viewError.progressBar.visibility = View.VISIBLE
                    detailUserViewModel.refreshDetail()
                }
                btnFavorite.setOnClickListener {
                    if (isFavorited == true) {
                        detailUserViewModel.deleteFavoriteUser()
                        detailUserViewModel.refreshIsFavorited()
                    } else {
                        detailUserViewModel.insertFavoriteUser()
                        detailUserViewModel.refreshIsFavorited()
                    }
                }
            }

            EspressoIdlingResource.increment()
            detailUserViewModel.setUser(args.user)
            detailUserViewModel.detailUser.observe(viewLifecycleOwner, { user ->
                if (user != null) {
                    when (user) {
                        is Resource.Loading -> {
                            binding.progressBar.root.visibility = View.VISIBLE
                            binding.viewError.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            EspressoIdlingResource.decrement()

                            val data = user.data
                            with(binding) {
                                viewError.root.visibility = View.GONE

                                progressBar.root.animate().alpha(0f)
                                progressBar.root.visibility = View.GONE

                                Glide.with(this@DetailUserFragment)
                                    .load(data?.avatarUrl)
                                    .placeholder(R.color.cool_grey)
                                    .override(300, 300)
                                    .into(ivAvatar)

                                tvName.text = data?.name
                                tvUsername.text = data?.username
                                tvRepo.text = data?.publicRepos
                                tvFollower.text = data?.followers
                                tvFollowing.text = data?.following
                                tvCompany.text = data?.company
                                tvLocation.text = data?.location
                                tvBlog.text = data?.blog
                                tvEmail.text = data?.email

                                containerDetail.visibility = View.VISIBLE
                                containerDetail.animate().alpha(1f)
                            }
                        }
                        is Resource.Error -> {
                            EspressoIdlingResource.decrement()
                            binding.progressBar.root.visibility = View.GONE
                            binding.viewError.progressBar.visibility = View.GONE

                            if (user.data == null) {
                                binding.viewError.tvDesc.text =
                                    user.message ?: getString(R.string.error_desc)

                                lifecycleScope.launch(Dispatchers.Main) {
                                    delay(200)
                                    binding.viewError.root.visibility = View.VISIBLE
                                    binding.viewError.root.animate().alpha(1f)
                                }
                            }
                        }
                    }
                }
            })
            detailUserViewModel.isFavorited.observe(viewLifecycleOwner, { favorited ->
                isFavorited = favorited
                if (favorited) {
                    binding.btnToggleGroupFavorite.check(R.id.btn_favorite)
                } else {
                    binding.btnToggleGroupFavorite.uncheck(R.id.btn_favorite)
                }
            })
        }
    }

    override fun onButtonChecked(
        group: MaterialButtonToggleGroup?,
        checkedId: Int,
        isChecked: Boolean
    ) {
        when (checkedId) {
            R.id.btn_favorite -> {
                val white = ContextCompat.getColor(requireContext(), R.color.white)
                val blue = ContextCompat.getColor(requireContext(), R.color.blue)

                when (isChecked) {
                    true -> {
                        binding.btnFavorite.setTextColor(white)
                        binding.btnFavorite.setBackgroundColor(blue)
                        binding.btnFavorite.strokeWidth = 0
                        binding.btnFavorite.text = getString(R.string.favorited)
                    }
                    false -> {
                        binding.btnFavorite.setTextColor(blue)
                        binding.btnFavorite.setBackgroundColor(white)
                        binding.btnFavorite.strokeWidth = 1
                        binding.btnFavorite.text = getString(R.string.favorite)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}