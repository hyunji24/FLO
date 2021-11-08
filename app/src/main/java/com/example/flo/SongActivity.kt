package com.example.flo

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
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
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var isitRandom:Boolean=false
    var repeatStatus:Int=0
    private val song:Song=Song()
    private lateinit var player:Player //스레드 객체
    private var playAgain:Boolean=false
    private var mediaPlayer: MediaPlayer?=null
    private var gson:Gson = Gson()
    //private val handler= Handler(Looper.getMainLooper()) //이거 대신 runonUiThread 써도 됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong() //mainActivity에서 넘어온 음악정보 설정하기 (Song객체인 song에 저장)
        initButton()

        player=Player(song.playTime,song.second,song.isPlaying) //player 쓰레드: isPlaying true이면 시간 가면서 seekbar 갱신
        player.start() //이걸 해줘야 쓰레드 시작됨





    }

//    override fun onBackPressed() {
//        val intent= Intent(this,MainActivity::class.java)
//        intent.putExtra("isPlaying",song.isPlaying)
//        intent.putExtra("title",song.title)
//        intent.putExtra("singer",song.singer)
//        intent.putExtra("second",player.currentPlayTime)
//        intent.putExtra("music",song.music)
//
//    }



    inner class Player(private val playTime:Int, var currentPlayTime:Int, var isPlaying:Boolean):Thread(){ //binding쓰기 위해 inner 붙여줌
        private var second= currentPlayTime
        override fun run() { //thread.start하면 실행되는 코드
            try {
                while(true) {
                    if(second>playTime){
                        if(playAgain) {second=0}
                        else break
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



    private fun initSong(){  //mainActivity에서 넘어온 음악정보 설정하기

        if(intent.hasExtra("title")&& intent.hasExtra("singer")&&intent.hasExtra("second")&&intent.hasExtra("playTime")&&intent.hasExtra("isPlaying")&&intent.hasExtra("music")){

            song.title=intent.getStringExtra("title")!!
            song.singer=intent.getStringExtra("singer")!!
            song.second=intent.getIntExtra("second",0)
            song.playTime=intent.getIntExtra("playTime",0)
            song.isPlaying=intent.getBooleanExtra("isPlaying",false)
            song.music=intent.getStringExtra("music")!!
            val music=resources.getIdentifier(song.music,"raw",this.packageName)


            binding.songEndTimeTv.text=String.format("%02d:%02d",song.playTime/60,song.playTime%60)
            binding.songTitleTv.text=song.title
            binding.songSingerTv.text=song.singer
            setPlayerStatus(song.isPlaying)
            mediaPlayer=MediaPlayer.create(this,music) //MediaPlayer랑 연결됨


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
            song.second=(binding.songSeekbar.progress*song.playTime)/1000
            val intent= Intent(this,MainActivity::class.java)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("title",song.title)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("singer",song.singer)
            intent.putExtra("second",song.second) //song.second?
            intent.putExtra("music",song.music)
            startActivity(intent)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish() //SongActivity 끄기

    }

    private fun initButton(){

        binding.songMiniplayerIv.setOnClickListener{
            player.isPlaying=true
            setPlayerStatus(true)
            song.isPlaying=true
            mediaPlayer?.start()
        }
        binding.songPauseIv.setOnClickListener{
            player.isPlaying=false
            setPlayerStatus(false)
            song.isPlaying=false
            mediaPlayer?.pause()
        }
        binding.songRandomIv.setOnClickListener {
            setRandomStatus(isitRandom)
        }
        binding.songRepeatIv.setOnClickListener {
            setRepeatStatus(repeatStatus)
        }

//        binding.songBackBtn.setOnClickListener{
//            song.second=(binding.songSeekbar.progress*song.playTime)/1000
//            val intent= Intent(this,MainActivity::class.java)
//            intent.putExtra("isPlaying",song.isPlaying)
//            intent.putExtra("title",song.title)
//            intent.putExtra("playTime",song.playTime)
//            intent.putExtra("singer",song.singer)
//            intent.putExtra("second",song.second) //song.second?
//            intent.putExtra("music",song.music)
//            startActivity(intent)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            finish() //SongActivity 끄기
//        }



    }

    private fun setPlayerStatus(isPlaying:Boolean){ //false 로 처음에 넘어옴
        if(isPlaying){
            binding.songPauseIv.visibility= View.VISIBLE
            binding.songMiniplayerIv.visibility=View.GONE
        }
        else{
            binding.songMiniplayerIv.visibility=View.VISIBLE
            binding.songPauseIv.visibility=View.GONE
        }
    }


    private fun setRandomStatus(isRandom:Boolean){
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
    private fun setRepeatStatus(status:Int){
        if(status==0){
            binding.songRepeatIv.setImageResource(R.drawable.btn_player_repeat_on_light)
            repeatStatus=1

        }
        else if(status==1){
            binding.songRepeatIv.setImageResource(R.drawable.btn_player_repeat_on1_light)
            repeatStatus=2
            playAgain=true
        }
        else{
            binding.songRepeatIv.setImageResource(R.drawable.nugu_btn_repeat_inactive)
            repeatStatus=0
            playAgain=false
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause() //미디어플레이어 중지
        player.isPlaying=false //스레드 중지
        song.isPlaying=false
        song.second=(binding.songSeekbar.progress*song.playTime)/1000
        setPlayerStatus(false) //정지 상태 이미지로 전환

        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
        //데이터를 내부 저장소에 저장.
        val editor=sharedPreferences.edit() //sharedPreference 조작할때 사용
        val json=gson.toJson(song) //song데이터 객체를 바로 json으로 변환
        editor.putString("song",json)

        editor.apply()
    }



    override fun onDestroy() { //이 화면이 꺼지면 불림
        player.interrupt() //스레드 해제 - 오류를 내서 쓰레드 완전히 종료시킴(쓰레드는 앱 꺼져도 백그라운드에서 계속 돌아가므로 인터럽트 필요)
        super.onDestroy()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스해제
        mediaPlayer=null //미디어 플레이어 해제

    }

}