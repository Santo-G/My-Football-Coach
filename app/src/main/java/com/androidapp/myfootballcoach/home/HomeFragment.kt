package com.androidapp.myfootballcoach.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidapp.myfootballcoach.R
import com.androidapp.myfootballcoach.databinding.FragmentHomeBinding
import com.androidapp.myfootballcoach.detail.DetailFragment
import com.androidapp.myfootballcoach.domain.Player
import com.androidapp.myfootballcoach.utils.bindings.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel : HomeViewModel by viewModel()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private var playersList: ArrayList<Player> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.send(HomeScreenEvents.OnReady)
        requireActivity().setTitle(R.string.app_name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playersAdapter = PlayersAdapter(context = requireContext()) {
            player -> viewModel.send(HomeScreenEvents.OnPlayerClick(player))
        }

/*        binding.swiperefresh.setOnClickListener {
            viewModel.send(HomeScreenEvents.OnRefreshClicked)
        }*/

        binding.playersList.adapter = playersAdapter

        playersAdapter.setPlayersList(playersList)

        observeStates(playersAdapter, binding)
        observeActions()
    }


    private fun observeStates(playersAdapter: PlayersAdapter, fragmentHomeBinding: FragmentHomeBinding) {
        viewModel.states.observe(viewLifecycleOwner) {
            state -> Timber.d(state.toString())
            when(state){
                is HomeScreenStates.Content -> setupContent(fragmentHomeBinding, playersAdapter, state)
                is Error -> setupError(state as HomeScreenStates.Error, fragmentHomeBinding)
            }
        }
    }

    private fun setupContent(
        fragmentHomeBinding: FragmentHomeBinding,
        playersAdapter: PlayersAdapter,
        state: HomeScreenStates.Content
    ) {
        if(state.playersList.isEmpty()) {
            fragmentHomeBinding.innerLayoutNoPlayerFound.root.visibility = View.VISIBLE
        } else {
            fragmentHomeBinding.innerLayoutNoPlayerFound.root.visibility = View.GONE
            state.playersList.map { player ->
                playersList.add(player)
            }
            errorVisibilityGone(fragmentHomeBinding)
            fragmentHomeBinding.playersList.visibility = View.VISIBLE
        }
    }

    private fun errorVisibilityGone(errorBinding: FragmentHomeBinding) {
        errorBinding.innerLayoutServerError.root.visibility = View.GONE
        errorBinding.innerLayoutNoPlayerFound.root.visibility = View.GONE
    }

    private fun setupError(state: HomeScreenStates.Error, fragmentHomeBinding: FragmentHomeBinding) {
        when(state.error) {
            ErrorStates.ShowNoPlayerFound -> {
                fragmentHomeBinding.innerLayoutNoPlayerFound.root.visibility = View.VISIBLE
            }
            ErrorStates.ShowServerError -> {
                fragmentHomeBinding.innerLayoutServerError.root.visibility = View.VISIBLE
            }
        }
    }


    private fun observeActions() {
        viewModel.actions.observe(viewLifecycleOwner) { action ->
            Timber.d(action.toString())
            when(action) {
                is HomeScreenActions.NavigateToDetail -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.player))
                }
            }
        }
    }


}
