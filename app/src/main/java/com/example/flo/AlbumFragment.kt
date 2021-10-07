package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var binding:FragmentAlbumBinding
    var toggleIsOff:Boolean=true
    val information=arrayListOf("수록곡","상세정보","영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAlbumBinding.inflate(inflater,container,false)


        binding.albumArrowIb.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss() //나중에 학습ㄱ
        }

//        binding.albumToggleoffIv.setOnClickListener {
//            setToggleStatus(toggleIsOff)
//        }
//
//
//
//        binding.songLilacLayout.setOnClickListener{
//            Toast.makeText(activity,"라일락",Toast.LENGTH_SHORT).show()
//        }
        val albumAdapter=AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.tabLayout,binding.albumContentVp){
            tab,position ->
            tab.text=information[position]
        }.attach() //viewpager랑 tablayout붙이기


        return binding.root
    }

//    fun setToggleStatus(toggleOff:Boolean){
//        if(toggleOff) {
//            binding.albumToggleoffIv.setImageResource(R.drawable.btn_toggle_on)
//            toggleIsOff=false
//
//        }
//        else {
//            binding.albumToggleoffIv.setImageResource(R.drawable.btn_toggle_off)
//            toggleIsOff=true
//        }
//
//    }
}