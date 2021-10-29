package com.example.flo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var isitRandom:Boolean=false
    var repeatStatus:Int=0
    private val song:Song=Song()
    private lateinit var player:Player
    private val handler= Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //MAINLAYOUT.setPadding(0, statusBarHeight(this), 0, 0)

        initSong()

        player=Player(song.playTime,song.isPlaying)
        player.start() //이걸 해줘야 쓰레드 시작됨





        binding.songDownIb.setOnClickListener{
            finish() //SongActivity 끄기기
       }
        binding.songMiniplayerIv.setOnClickListener{
            player.isPlaying=true
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener{
            player.isPlaying=false
           setPlayerStatus(true)
        }
        binding.songRandomIv.setOnClickListener {
            setRandomStatus(isitRandom)
        }
       binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(repeatStatus)
        }




    }


    fun setRandomStatus(isRandom:Boolean){
        if(!isRandom) {
            binding.songRandomIv.setImageResource(R.drawable.btn_player_random_on_light)
            isitRandom=true

        }
        else {
            binding.songRandomIv.setImageResource(R.drawable.nugu_btn_random_inactive)
            isitRandom=false
        }

        }

    @JvmName("setRepeatStatus1")
    fun setRepeatStatus(status:Int){
        if(status==0){
            binding.songRepeatIv.setImageResource(R.drawable.btn_player_repeat_on_light)
            repeatStatus=1
        }
        else if(status==1){
            binding.songRepeatIv.setImageResource(R.drawable.btn_player_repeat_on1_light)
            repeatStatus=2
        }
        else{
            binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            repeatStatus=0
        }
    }


    private fun initSong(){
        if(intent.hasExtra("title")&& intent.hasExtra("singer")&&intent.hasExtra("playTime")&&intent.hasExtra("isPlaying")){

            song.title=intent.getStringExtra("title")!!
            song.singer=intent.getStringExtra("singer")!!
            song.playTime=intent.getIntExtra("playTime",0)
            song.isPlaying=intent.getBooleanExtra("isPlaying",false)

            binding.songTitleTv.text=song.title
            binding.songSingerTv.text=song.singer
            setPlayerStatus(song.isPlaying)

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



    inner class Player(private val playTime:Int,var isPlaying:Boolean):Thread(){
        private var second= 0
        override fun run() { //thread.start하면 실행되는 코드
            try {
                while(true) {
                    if(second>=playTime){
                        break
                    }

                    if(isPlaying){
                        sleep(2000)
                        second++
                        runOnUiThread {
                            //뷰 렌더링
                            binding.songSeekbar.progress=second*1000/playTime
                            binding.songStartTimeTv.text =String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }

                }
            }catch(e:InterruptedException){
                Log.d("interrupt","쓰레드가 종료되었습니다.")
            }

        }

    }

    override fun onDestroy() { //이 화면이 꺼지면 불림
        player.interrupt()
        super.onDestroy()

    }

}