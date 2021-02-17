package com.example.github.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
import com.example.github.databinding.FragmentSearchBinding
import kotlinx.coroutines.*
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private val searchViewModel: SearchViewModel by viewModels {
        factory
    }

    private var _binding: FragmentSearchBinding? = null
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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (activity != null) {

            val userAdapter = UserAdapter()
            userAdapter.onItemClick = { user ->
                lifecycleScope.launch {
                    delay(200)
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToDetailUserFragment(user)
                    findNavController().navigate(action)
                }
            }

            val searchAutoComplete =
                binding.searchView.findViewById(R.id.search_src_text) as SearchView.SearchAutoComplete
            with(searchAutoComplete) {
                setDropDownBackgroundResource(R.drawable.bg_pale_grey_rounded)
                dropDownAnchor = R.id.search_view
                threshold = 1
                onItemClickListener =
                    AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                        val selectedUsername = adapterView.getItemAtPosition(position).toString()
                        binding.searchView.setQuery(selectedUsername, true)
                    }
            }

            with(binding) {
                toolbar.setNavigationOnClickListener {
                    closeKeyboard()
                    findNavController().navigateUp()
                }

                viewError.btnRetry.setOnClickListener {
                    binding.viewError.progressBar.visibility = View.VISIBLE
                    searchViewModel.refresh()
                }

                with(searchView) {
                    val searchManager =
                        activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                    setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
                    setOnQueryTextListener(this@SearchFragment)
                }

                with(rvUsers) {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = userAdapter
                }
            }

            searchViewModel.queryResult.observe(viewLifecycleOwner, { users ->
                if (users != null) {
                    when (users) {
                        is Resource.Loading -> {
                            binding.progressBar.root.visibility = View.VISIBLE
                            binding.viewError.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.root.visibility = View.GONE

                            val listUsername = arrayListOf<String>()
                            users.data?.map {
                                listUsername.add(it.username)
                            }

                            val searchAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.select_dialog_item,
                                listUsername
                            )
                            searchAdapter.notifyDataSetChanged()

                            searchAutoComplete.setAdapter(searchAdapter)
                        }
                        is Resource.Error -> {
                            binding.progressBar.root.visibility = View.GONE
                        }
                    }
                }
            })
            searchViewModel.listUser.observe(viewLifecycleOwner, { users ->
                if (users != null) {
                    when (users) {
                        is Resource.Loading -> {
                            binding.progressBar.root.visibility = View.VISIBLE
                            binding.viewError.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.root.visibility = View.GONE
                            binding.viewError.root.visibility = View.GONE
                            userAdapter.setData(users.data)

                            if (binding.searchView.query.isNotEmpty()) {
                                binding.viewWelcome.root.animate().alpha(0f)
                                binding.viewWelcome.root.visibility = View.GONE
                                binding.viewEmpty.root.animate().alpha(0f)
                                binding.viewEmpty.root.visibility = View.GONE
                                binding.rvUsers.visibility = View.VISIBLE
                                binding.rvUsers.animate().alpha(1f)

                                if (users.data.isNullOrEmpty()) {
                                    binding.viewEmpty.root.visibility = View.VISIBLE
                                    binding.viewEmpty.root.animate().alpha(1f)
                                }
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBar.root.visibility = View.GONE
                            binding.viewError.progressBar.visibility = View.GONE
                            binding.rvUsers.visibility = View.GONE

                            if (binding.searchView.query.isNotEmpty()) {
                                binding.viewWelcome.root.visibility = View.GONE

                                if (users.data.isNullOrEmpty()) {
                                    binding.viewError.tvDesc.text =
                                        users.message ?: getString(R.string.error_desc)
                                    binding.viewError.root.visibility = View.VISIBLE
                                    binding.viewError.root.animate().alpha(1f)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        closeKeyboard()
        binding.progressBar.root.visibility = View.VISIBLE
        searchViewModel.setUsername(query)
        searchViewModel.refresh()
        return query != null
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchViewModel.setQueryNewText(newText)
        return newText != null
    }

    private fun closeKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}