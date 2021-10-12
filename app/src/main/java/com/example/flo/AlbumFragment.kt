package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator


//viewpager scrollview https://stackoverflow.com/questions/57819500/android-nestedscrollview-doesnt-work-in-layout-with-viewpager?noredirect=1&lq=1
//https://stackoverflow.com/questions/30580954/viewpager-in-a-nestedscrollview]
class AlbumFragment : Fragment() {

    lateinit var binding:FragmentAlbumBinding

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


        val albumAdapter=AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.albumTabLayout,binding.albumContentVp){
            tab,position ->
            tab.text=information[position]
        }.attach() //viewpager랑 tablayout붙이기


        return binding.root
    }


}