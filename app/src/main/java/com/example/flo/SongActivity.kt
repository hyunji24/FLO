package com.example.flo

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(intent.hasExtra("title")&& intent.hasExtra("singer")){
            binding.songTitleTv.text=intent.getStringExtra("title")
            binding.songSingerTv.text=intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener{
            finish() //SongActivity 끄기기
       }

        binding.songMiniplayerIv.setOnClickListener{
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener{
           setPlayerStatus(true)
        }


    }

    fun setPlayerStatus(isPlaying:Boolean){
        if(isPlaying){
            binding.songPauseIv.visibility= View.GONE
            binding.songMiniplayerIv.visibility=View.VISIBLE
        }
        else{
            binding.songMiniplayerIv.visibility=View.GONE
            binding.songPauseIv.visibility=View.VISIBLE
        }
    }

}