package com.mio.music.helper

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.mio.base.Tag.TAG
import com.mio.music.calculateSixDigitNumber
import com.mio.music.data.Music
import com.mio.music.parseAudioFileName
import com.mio.music.requireMusicName
import java.io.File
import java.security.MessageDigest

/**
 * 获取本地音乐有两个方案
 * 从媒体库查：快速，高效，信息全；但添加新的音频后必须重启才能更新
 * 从文件扫：扫的全；但速度比较慢，且获取不到作者/专辑等信息
 */
object MusicScanHelper {
    /**
     * 整合2种扫描 ==> 扫描文件获取 然后去媒体库查
     * 结果通过函数回调 不需要进行去重
     */
    fun scanLocalMusic(
        context: Context,
        songFoundCallback: (Music) -> Unit
    ) {
        // 从媒体库获取
//        scanLocalMusicFromMedia(context, songFoundCallback)

        // 扫描文件获取
        val scanDir = listOf(
//                            Environment.getExternalStorageDirectory(),
            File("/storage/emulated/0"),
            File("/mnt/shared/Pictures"),
            File("/mnt/sdcard/"),
        )
        val files = mutableListOf<File>()
        val musics = mutableListOf<Music>()

        scanLocalMusicFromFile(context, scanDir) { file ->
            val music = getAudioInfoFromMediaStore(context, file.path)
            music?.let { music ->
                songFoundCallback(music)
                musics.add(music)
            } ?: run {
                files.add(file)
            }
        }

        // 过滤掉已经回调过的歌曲 根据歌名
        val titleMap = musics.map { it.title }
        val list = files.filter {
            it.name.requireMusicName() !in titleMap
        }.distinctBy { it.name }

        // 到这里剩下的就是扫描出来的音乐文件 但是媒体库识别不了的
        for (f in list) {
            // Log.d(TAG, "scanLocalMusic: music file3: $f ,music: ${f.name.parseAudioFileName()}")
            f.name.parseAudioFileName(f.path)?.let { songFoundCallback(it) }
        }
    }

    @SuppressLint("Range")
    fun scanLocalMusicFromMedia(
        context: Context,
        songFoundCallback: (Music) -> Unit
    ) {
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = contentResolver.query(uri, null, selection, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val duration =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                // Log.d(TAG, "scanLocalMusic: title: $title , path: $string")

                songFoundCallback(Music(id, title, artist, album, duration, path))
            } while (cursor.moveToNext())
        }

        cursor?.close()
    }


    /**
     * 调用如下：
     * MusicScanHelper.findAllAudioFilesInDirectories(
     *     listOf(
     *         File("/sdcard"),
     *     ),
     * ) {
     *     Log.d(TAG, "initData3: $it")
     * }
     */
    fun scanLocalMusicFromFile(
        context: Context,
        directories: List<File>,
        fileFoundCallback: (File) -> Unit
    ) {
        for (directory in directories) {
            if (directory.isDirectory) {
                val files = directory.listFiles() ?: continue

                for (file in files) {
                    if (file.isDirectory) {
                        // 如果是目录，递归遍历子目录
                        scanLocalMusicFromFile(context, listOf(file), fileFoundCallback)
                    } else if (isAudioFile(file)) {
                        // 如果是音频文件，将其添加到集合并触发回调
                        file?.let { fileFoundCallback(it) }
                    }
                }
            }
        }
    }

    fun isAudioFile(file: File): Boolean {
        val audioFileExtensions = listOf("mp3", "wav", "ogg", "flac", "m4a", "wma")
        val fileExtension = file.extension.toLowerCase()
        return audioFileExtensions.contains(fileExtension)
    }

    @SuppressLint("Range")
    fun getAudioInfoFromMediaStore(context: Context, filePath: String): Music? {
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = "${MediaStore.Audio.Media.DATA} = ?"
        val selectionArgs = arrayOf(filePath)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
        )

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
            val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

            // 构建 Music 对象并返回
            val music = Music(id, title, artist, album, duration, filePath)

            cursor.close()

            return music
        }

        cursor?.close()
        return null
    }
}