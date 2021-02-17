package com.example.github.favorite

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github.MyApplication
import com.example.github.R
import com.example.github.core.ui.UserAdapter
import com.example.github.core.ui.ViewModelFactory
import com.example.github.favorite.databinding.FragmentFavoriteBinding
import com.example.github.favorite.di.DaggerFavoriteComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        factory
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val coreComponent = (requireActivity().application as MyApplication).coreComponent
        DaggerFavoriteComponent.factory().create(coreComponent).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (activity != null) {

            val userAdapter = UserAdapter()
            userAdapter.onItemClick = { user ->
                lifecycleScope.launch {
                    delay(200)
                    val action =
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailUserFragment(user)
                    findNavController().navigate(action)
                }
            }

            with(binding) {
                toolbar.setNavigationOnClickListener {
                    findNavController().navigateUp()
                }
                viewEmpty.btnExploreUser.setOnClickListener {
                    findNavController().navigateUp()
                }

                with(rvUsers) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = userAdapter
                }
            }

            favoriteViewModel.listUser.observe(viewLifecycleOwner, { users ->
                if (users != null) {
                    userAdapter.setData(users)
                }
                if (users.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        delay(100)
                        binding.viewEmpty.root.visibility = View.VISIBLE
                        binding.viewEmpty.root.animate().alpha(1f)
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}