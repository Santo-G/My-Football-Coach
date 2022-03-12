package com.androidapp.myfootballcoach.session


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androidapp.myfootballcoach.R
import com.androidapp.myfootballcoach.databinding.FragmentSessionBinding
import com.androidapp.myfootballcoach.domain.Player
import com.androidapp.myfootballcoach.utils.bindings.viewBinding

class SessionFragment : Fragment(R.layout.fragment_session) {
    private val args: SessionFragmentArgs by navArgs()
    private val binding by viewBinding (FragmentSessionBinding::bind)
    lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = args.player
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO
    }

}
