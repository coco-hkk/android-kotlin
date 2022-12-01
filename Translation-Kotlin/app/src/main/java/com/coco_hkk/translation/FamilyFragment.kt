package com.coco_hkk.translation

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.coco_hkk.translation.databinding.WordListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FamilyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FamilyFragment : Fragment() {
    // 视图绑定
    private var mBinding: WordListBinding? = null
    private val binding get() = mBinding!!

    private var mMedia: MediaPlayer? = null

    private lateinit var mFocusRequest: AudioFocusRequest

    // media 服务回调
    private val mCompletionListener: MediaPlayer.OnCompletionListener =
        MediaPlayer.OnCompletionListener {
            releaseMediaPlayer()
        }

    private lateinit var mAudioManager: AudioManager

    // 3. AudioFocus 回调
    private val afChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS -> {
                    releaseMediaPlayer()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    // 对于单词，重新播放
                    mMedia?.pause()
                    mMedia?.seekTo(0)
                }
                AudioManager.AUDIOFOCUS_GAIN -> {
                    mMedia?.start()
                }
                else -> {
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = WordListBinding.inflate(inflater, container, false)

        // 0. 获取音频服务
        mAudioManager = activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // 1. 定义 AudioFocus 属性
        val mPlaybackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        // 2. 设置 AudioFocusRequest，指定音频焦点变化时的回调函数
        mFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
            .setAudioAttributes(mPlaybackAttributes)
            .setOnAudioFocusChangeListener(afChangeListener)
            .build()

        val family = listOf(
            Word("father", "爹", R.drawable.family_father, R.raw.family_father),
            Word("mother", "娘", R.drawable.family_mother, R.raw.family_mother),
            Word("son", "儿子", R.drawable.family_son, R.raw.family_son),
            Word("daughter", "女儿", R.drawable.family_daughter, R.raw.family_daughter),
            Word(
                "older brother",
                "兄",
                R.drawable.family_older_brother,
                R.raw.family_older_brother
            ),
            Word(
                "younger brother",
                "弟",
                R.drawable.family_younger_brother,
                R.raw.family_younger_brother
            ),
            Word(
                "older sister",
                "姐",
                R.drawable.family_older_sister,
                R.raw.family_older_sister
            ),
            Word(
                "younger sister",
                "妹",
                R.drawable.family_younger_sister,
                R.raw.family_younger_sister
            ),
            Word("grandmother", "祖父", R.drawable.family_grandfather, R.raw.family_grandfather),
            Word("grandfather", "祖母", R.drawable.family_grandmother, R.raw.family_grandmother),
        )

        val adapter = WordAdapter(activity!!, family, R.color.category_family)

        val listView: ListView = binding.list

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val word: Word = family[position]

            releaseMediaPlayer()

            // 4. AudioManager 处理音频焦点
            val result = mAudioManager.requestAudioFocus(mFocusRequest)

            // 5. 若请求成功则播放音频
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mMedia = MediaPlayer.create(activity, word.soundId)
                mMedia?.setAudioAttributes(mPlaybackAttributes)
                mMedia?.start()

                // media 一结束就调用 mCompletionListener, 它是全局变量，不需要每次都重新创建
                mMedia?.setOnCompletionListener(mCompletionListener)
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()

        releaseMediaPlayer()
    }

    // 释放 media 资源
    private fun releaseMediaPlayer() {
        mMedia?.release()
        mMedia = null

        // 6. 当失去音频焦点时调用
        mAudioManager.abandonAudioFocusRequest(mFocusRequest)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FamilyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FamilyFragment().apply {}
    }
}