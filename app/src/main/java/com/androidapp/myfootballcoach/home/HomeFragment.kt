package com.androidapp.myfootballcoach.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.androidapp.myfootballcoach.R
import com.androidapp.myfootballcoach.databinding.FragmentHomeBinding
import com.androidapp.myfootballcoach.domain.Player
import com.androidapp.myfootballcoach.utils.bindings.viewBinding
import com.androidapp.myfootballcoach.utils.exhaustive
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private var listOfPlayers: ArrayList<Player>? = ArrayList()
    lateinit var playersAdapter: PlayersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.send(HomeScreenEvents.OnReady)
        requireActivity().setTitle(R.string.app_name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playersAdapter = PlayersAdapter(context = requireContext()) { player ->
            viewModel.send(HomeScreenEvents.OnPlayerClick(player))
        }

        binding.swiperefresh.setOnClickListener {
            viewModel.send(HomeScreenEvents.OnRefreshClicked)
        }


        playersAdapter.setPlayersList(listOfPlayers ?: ArrayList())
        binding.playersList.adapter = playersAdapter

        observeStates(binding)
        observeActions()
    }


    private fun observeStates(fragmentHomeBinding: FragmentHomeBinding) {
        viewModel.states.observe(viewLifecycleOwner) { state ->
            Timber.d(state.toString())
            when (state) {
                is HomeScreenStates.Content -> setupContent(fragmentHomeBinding, state)
                is HomeScreenStates.Error -> setupError(fragmentHomeBinding, state)
                HomeScreenStates.Loading -> fragmentHomeBinding.swiperefresh.isRefreshing = true
            }.exhaustive
        }
    }

    private fun setupContent(
        fragmentHomeBinding: FragmentHomeBinding,
        state: HomeScreenStates.Content
    ) {
        fragmentHomeBinding.swiperefresh.isRefreshing = false
        if (state.playersList.isEmpty()) {
            fragmentHomeBinding.innerLayoutNoPlayerFound.root.visibility = View.VISIBLE
        } else {
            fragmentHomeBinding.innerLayoutNoPlayerFound.root.visibility = View.GONE
            state.playersList.map { player ->
                listOfPlayers?.add(player)
            }
            playersAdapter.notifyDataSetChanged()
            errorVisibilityGone(fragmentHomeBinding)
            fragmentHomeBinding.playersList.visibility = View.VISIBLE
        }
    }

    private fun errorVisibilityGone(errorBinding: FragmentHomeBinding) {
        errorBinding.innerLayoutServerError.root.visibility = View.GONE
        errorBinding.innerLayoutNoPlayerFound.root.visibility = View.GONE
    }

    private fun setupError(fragmentHomeBinding: FragmentHomeBinding, state: HomeScreenStates.Error) {
        when (state.error) {
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
            when (action) {
                is HomeScreenActions.NavigateToDetail -> {
                    // manage dialog for setting up the distance in meters

                    var dialog: AlertDialog? = null
                    val builder = AlertDialog.Builder(requireContext())
                    // set custom layout
                    val view = layoutInflater.inflate(R.layout.dialog_layout, null)
                    // gets views references from your layout
                    val textView: TextView = view.findViewById(R.id.dialog_textview)
                    val editText: EditText = view.findViewById(R.id.dialog_edit_text)
                    val buttonPositive: Button = view.findViewById(R.id.dialog_positive_button)
                    val buttonNegative: Button = view.findViewById(R.id.dialog_negative_button)

                    textView.setText("Dialog Title")
                    editText.setHint("Insert distance in meteres")

                    buttonPositive.setOnClickListener(View.OnClickListener {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.player))
                        dialog?.dismiss()
                    })
                    buttonNegative.setOnClickListener(View.OnClickListener {
                        dialog?.dismiss()
                    })

                    builder.setView(view).create().show()


                    // ************************************ //
                    // var mDialogView: View? = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
/*
                    val editText = EditText(requireContext())
                    val alertDialog = AlertDialog.Builder(requireContext())

                    alertDialog.apply {
                        setTitle("Hello")
                        setMessage("I just wanted to greet you. I hope you are doing great!")
                        setView(R.id.dialog_constraint_layout)
                        setPositiveButton("Positive") { _, _ ->
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.player))
                        }
                        setNegativeButton("Negative") { _, _ ->

                        }
                        setNeutralButton("Neutral") { _, _ ->

                        }
                    }.create().show()

                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.dialog_title)
                        .setMessage(R.string.dialog_message)
                        .setContentView(R.layout.dialog_layout)
                        .setView(R.id.dialog_input_text)
                        .setPositiveButton(R.string.dialog_positive_button, DialogInterface.OnClickListener {

                        String editTextInput = editText.getText().toString()
                    })*/

                    // findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.player))

                }
            }
        }
    }


}
