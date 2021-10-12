package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->HomeVp1Fragment()
            1->HomeVp2Fragment()
            2->HomeVp3Fragment()
            3->HomeVp4Fragment()
            else->HomeVp1Fragment()
        }
    }
}