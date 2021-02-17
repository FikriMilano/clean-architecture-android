package com.example.github.home

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.MyApplication
import com.example.github.R
import com.example.github.core.data.Resource
import com.example.github.core.ui.UserAdapter
import com.example.github.core.ui.ViewModelFactory
import com.example.github.core.utils.EspressoIdlingResource
import com.example.github.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val homeViewModel: HomeViewModel by viewModels {
        factory
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        if (activity != null) {

            val userAdapter = UserAdapter()
            userAdapter.onItemClick = { user ->
                lifecycleScope.launch {
                    delay(200)
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailUserFragment(user)
                    findNavController().navigate(action)
                }
            }

            with(binding) {
                searchView.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                    findNavController().navigate(action)
                }
                viewError.btnRetry.setOnClickListener {
                    binding.viewError.root.visibility = View.GONE
                    homeViewModel.refresh()
                }

                with(rvUsers) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = userAdapter
                }
            }

            EspressoIdlingResource.increment()
            homeViewModel.listUser.observe(viewLifecycleOwner, { users ->
                if (users != null) {
                    when (users) {
                        is Resource.Loading -> {
                            binding.progressBar.root.visibility = View.VISIBLE
                            binding.viewError.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            EspressoIdlingResource.decrement()
                            binding.progressBar.root.visibility = View.GONE
                            binding.viewError.root.visibility = View.GONE
                            userAdapter.setData(users.data)
                        }
                        is Resource.Error -> {
                            EspressoIdlingResource.decrement()
                            binding.progressBar.root.visibility = View.GONE
                            binding.viewError.progressBar.visibility = View.GONE

                            if (users.data == null) {
                                binding.viewError.tvDesc.text =
                                    users.message ?: getString(R.string.error_desc)
                                binding.viewError.root.visibility = View.VISIBLE
                                binding.viewError.root.animate().alpha(1f)
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}