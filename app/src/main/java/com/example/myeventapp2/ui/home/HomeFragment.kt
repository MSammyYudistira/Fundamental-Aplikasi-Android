package com.example.myeventapp2.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myeventapp2.databinding.FragmentHomeBinding
import com.example.myeventapp2.ui.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvHomeEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvHomeEvent.addItemDecoration(itemDecoration)

        homeViewModel.listEvent.observe(viewLifecycleOwner) { listEvent ->
            Log.i("HomeFragment", listEvent.listEvents.toString())

            val adapter = EventAdapter()
            adapter.submitList(listEvent.listEvents)
            binding.rvHomeEvent.adapter = adapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { v, actionId, event ->

                    when(actionId){
                        EditorInfo.IME_ACTION_SEARCH -> {
                            searchBar.setText(searchView.text)
                            homeViewModel.findAllEvent(searchView.text.toString())
                            searchView.hide()
                        }
                    }

                    false
                }
        }

        (activity as AppCompatActivity).supportActionBar?.hide()

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}