package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    var currentPosition=0

    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setBannerViewPager()
        setHomeViewPager()
        setAlbumBtnClick()


        return binding.root
    }

    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun setBannerViewPager(){
        val bannerAdapter=BannerViewpagerAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter=bannerAdapter
        binding.homeBannerVp.orientation= ViewPager2.ORIENTATION_HORIZONTAL
    }

    private fun setHomeViewPager(){
        val homeAdapter=HomeViewpagerAdapter(this)
        binding.homeFirstVp.adapter=homeAdapter

        binding.homeCircleIndicator.setViewPager(binding.homeFirstVp)
        binding.homeCircleIndicator.createIndicators(4,0)

        binding.homeFirstVp.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                binding.homeCircleIndicator.animatePageSelected(position)
            }
        })

        val thread=Thread(PagerRunnable())
        thread.start()
    }

    private fun setPage(){
        if(currentPosition==4) currentPosition=0
        binding.homeFirstVp.setCurrentItem(currentPosition,true)
        currentPosition+=1
    }

    private fun setAlbumBtnClick(){
        binding.iuAlbumIv1.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,AlbumFragment())
                .commitAllowingStateLoss() //나중에 학습ㄱ
        }

    }


}