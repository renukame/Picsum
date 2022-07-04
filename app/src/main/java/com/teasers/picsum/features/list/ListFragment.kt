package com.teasers.picsum.features.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teasers.picsum.data.model.PicSum
import com.teasers.picsum.databinding.FragmentListBinding
import com.teasers.picsum.features.PicSumViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var mPicSumRecyclerView: RecyclerView
    private val mPicSumViewModel: PicSumViewModel by activityViewModels()
    private lateinit var mPicSumAdapter: PicSumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPicSumRecyclerView = binding.rvPicsum
        setUpRecyclerView()
        showList()
    }

    private fun setUpRecyclerView() {
        mPicSumAdapter = PicSumAdapter { onCardClicked(it) }

        mPicSumRecyclerView.apply {
            adapter = mPicSumAdapter
            adapter = mPicSumAdapter.withLoadStateHeaderAndFooter(
                header = PicSumLoadStateAdapter { mPicSumAdapter.retry() },
                footer = PicSumLoadStateAdapter { mPicSumAdapter.retry() }
            )
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun onCardClicked(picSum: PicSum) {
        mPicSumViewModel.selectedPicSum(picSum)
        findNavController().navigate(
            ListFragmentDirections.actionListToDetail()
        )

    }

    private fun showList() {
        lifecycleScope.launch {
            mPicSumViewModel.fetchAllPicSums().collectLatest {
                mPicSumAdapter.submitData(it)
            }
        }
    }
}