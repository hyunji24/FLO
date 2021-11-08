package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var player: Player
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var song: Song = Song()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
        setBottomNavigation()

        val song = Song("라일락", "아이유(IU)", 0, 215, false, "music_lilac")
        setMiniPlayer(song)

        if (intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("second") && intent.hasExtra(
                "playTime") && intent.hasExtra("isPlaying") && intent.hasExtra("music")
        ) {
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            Log.d("second", song.isPlaying.toString())
            song.second = intent.getIntExtra("second", 0)
            Log.d("second", song.second.toString())
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime", 215)
            song.music = intent.getStringExtra("music")!!
            val music = resources.getIdentifier(song.music, "raw", this.packageName)
            mediaPlayer = MediaPlayer.create(this, music) //MediaPlayer랑 연결됨


            if(song.isPlaying){
                binding.mainMiniplayerPlayIv.visibility=View.GONE
                binding.mainMiniplayerPauseIv.visibility=View.VISIBLE
            }
            else{
                binding.mainMiniplayerPlayIv.visibility=View.VISIBLE
                binding.mainMiniplayerPauseIv.visibility=View.GONE

            }
            //setMiniPlayer(song) //제목,가수,seekbar,플레이버튼아이콘 설정

            // val music = resources.getIdentifier(song.music, "raw", this.packageName)
            //mediaPlayer = MediaPlayer.create(this, music) //MediaPlayer랑 연결됨


        }

        player = Player(
            song.playTime,
            song.second,
            song.isPlaying
        )  //player 쓰레드: isPlaying true이면 시간 가면서 seekbar 갱신
        player.start()





        binding.mainMiniplayerPlayIv.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
            song.isPlaying = true
            //mediaPlayer?.seekTo(song.second)
            mediaPlayer?.start()
        }

        binding.mainMiniplayerPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
            song.isPlaying = false
            mediaPlayer?.pause()
        }


        //아래 플레이어 누르면 songActivity로 이동
        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", binding.mainProgressSb.progress)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("music", song.music)
            startActivity(intent)
        }


    }

    private fun setPlayerStatus(isPlaying: Boolean) { //false 로 처음에 넘어옴
        if (isPlaying) {
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
        } else {
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
            binding.mainMiniplayerPauseIv.visibility = View.GONE
        }
    }

    private fun setBottomNavigation() {
        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    private fun setMiniPlayer(song: Song) { //제목,가수,seekbar,플레이버튼아이콘 설정
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 1000 / song.playTime)

        if (song.isPlaying) {
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE

        } else {
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song", null)
        song = if (jsonSong == null) {
            Song("라일락", "아이유(IU)", 0, 215, false, "music_lilac")

        } else {
            gson.fromJson(jsonSong, Song::class.java) //json포맷을 song이라는 자바 객체로 변환
        }

        setMiniPlayer(song)


    }

    inner class Player(
        private val playTime: Int,
        private val currentPlayTime: Int,
        var isPlaying: Boolean
    ) : Thread() { //binding쓰기 위해 inner 붙여줌
        private var second = currentPlayTime
        override fun run() { //thread.start하면 실행되는 코드
            try {
                while (true) {
                    if (second > playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(2000)
                        second++
                        runOnUiThread {
                            //뷰 렌더링
                            binding.mainProgressSb.progress = second * 1000 / playTime
                        }
                    }

                }
            } catch (e: InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }

        }

    }

//    override fun onPause() {
//        super.onPause()
//        mediaPlayer?.pause() //미디어플레이어 중지
//        player.isPlaying=false //스레드 중지
//        song.isPlaying=false
//        song.second=(binding.mainProgressSb.progress*song.playTime)/1000
//        setPlayerStatus(false) //정지 상태 이미지로 전환
//
//        val sharedPreferences=getSharedPreferences("song", MODE_PRIVATE)
//        //데이터를 내부 저장소에 저장.
//        val editor=sharedPreferences.edit() //sharedPreference 조작할때 사용
//        val json=gson.toJson(song) //song데이터 객체를 바로 json으로 변환
//        editor.putString("song",json)
//
//        editor.apply()
//    }


    override fun onDestroy() { //이 화면이 꺼지면 불림
        player.interrupt() //스레드 해제 - 오류를 내서 쓰레드 완전히 종료시킴(쓰레드는 앱 꺼져도 백그라운드에서 계속 돌아가므로 인터럽트 필요)
        super.onDestroy()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스해제
        mediaPlayer = null //미디어 플레이어 해제

    }

}

