package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerViewpagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment){

    private val fragmentlist: ArrayList<Fragment> =ArrayList()

    override fun getItemCount(): Int= fragmentlist.size


    override fun createFragment(position: Int): Fragment =fragmentlist[position] //position의 fragment를 만들어주겠다~

    fun addFragment(fragment:Fragment){ //HomeFragment에서 접근하기 위해서
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) //viewpager한테 새로운 프래그먼트 추가했음 알려줌
    }


}
