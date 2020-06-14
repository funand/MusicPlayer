package com.raywenderlich.funtime.device.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.raywenderlich.funtime.R

class MediaPlayerImpl : MediaPlayer {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var context: Context

    fun initializePlayer() {
//A RenderersFactory that creates renderer instances for use by ExoPlayer; they render media from some stream.
//A TrackSelector is responsible for selecting tracks to be consumed by each of the player’s renderers.
//A LoadControl that controls the buffering of the media.
        val trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl)
    }

    override fun play(url: String) {
        //1
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        //2
        val mediaSource = ExtractorMediaSource
                .Factory(DefaultDataSourceFactory(context, userAgent))
                .setExtractorsFactory(DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url))
        //3
        exoPlayer.prepare(mediaSource)
        //4
        exoPlayer.playWhenReady = true

// A UserAgent is just a string that is generated for you based on the given application name and
// library version. You’ll use it in next step.
// In ExoPlayer, every piece of media is represented by a MediaSource. To play a piece of media, you
// must first create a corresponding MediaSource. Again, there’s a factory for media source creation
// that takes a data source factory as a parameter. Data source is a component from which streams of
// data can be read. You have to set the ExtractorsFactory, which just returns the array of extractors.
// An Extractor extracts media data from a container format. Don’t worry about the specifics of these
// classes, since using the default classes works perfectly in most use cases. What’s important here
// is the createMediaSource() method which takes a Uri of the media that you want to play. In this
// case you’ll play the media from a remote server.
// You need to call the prepare() method on the ExoPlayer instance. This method prepares the player
// to play the provided media source.
// Finally, by setting the playWhenReady variable to true or false, you actually tell the player to
// play the media when it’s ready. If the player is already in the ready state, then this method can
// be used to pause and resume playback.
    }

    override fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        return exoPlayer
    }

    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }
}