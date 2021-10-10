package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    lateinit var binding : FragmentSongBinding
    var toggleIsOff:Boolean=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSongBinding.inflate(inflater,container,false)

                binding.albumToggleoffIv.setOnClickListener {
            setToggleStatus(toggleIsOff)
        }



        binding.songLilacLayout.setOnClickListener{
            Toast.makeText(activity,"라일락",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

        fun setToggleStatus(toggleOff:Boolean){
        if(toggleOff) {
            binding.albumToggleoffIv.setImageResource(R.drawable.btn_toggle_on)
            toggleIsOff=false

        }
        else {
            binding.albumToggleoffIv.setImageResource(R.drawable.btn_toggle_off)
            toggleIsOff=true
        }

    }
}