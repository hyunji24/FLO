package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    val information=arrayListOf("저장한 곡","음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)


        val albumAdapter=LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter=albumAdapter

        TabLayoutMediator(binding.lockerTabLayout,binding.lockerContentVp){
                tab,position ->
            tab.text=information[position]
        }.attach() //viewpager랑 tablayout붙이기
        return binding.root
    }


}