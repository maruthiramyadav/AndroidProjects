package com.devobal.ogabuzz;

/**
 * Created by Devobal on 2/26/2019 on 7:48 PM.
 */
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.devobal.ogabuzz.SpecialClasses.CallBacks;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;


public class Exoplayer {

    private static Exoplayer mInstance = null;
    private static Exoplayer mAdsInstance = null;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private String TAG = "Exoplayer";
    private SimpleExoPlayer player;
    SimpleExoPlayerView simpleExoPlayerView;
    DefaultDataSourceFactory dataSourceFactory;
    String uriString = "";
    ArrayList<String> playList = null;
    Integer playlistIndex = 0;
    CallBacks.playerCallBack listner;

    public static Exoplayer getSharedInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new Exoplayer(mContext);
        }
        return mInstance;
    }

    public static Exoplayer getSharedInstanceAds(Context mContext) {
        if (mAdsInstance == null) {
            mAdsInstance = new Exoplayer(mContext);
        }
        return mAdsInstance;
    }

    public Exoplayer(Context mContext) {
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        simpleExoPlayerView = new SimpleExoPlayerView(mContext);

        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        Uri mp4VideoUri = Uri.parse(uriString);

        dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "exoplayer2example"), BANDWIDTH_METER);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        final MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, new DefaultExtractorsFactory(),
                null, null);//new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
//        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

        player.prepare(videoSource);
        player.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_READY){
                    player.setPlayWhenReady(true);
                }
                if (playbackState == 4 && playList != null && playlistIndex + 1 < playList.size()) {
                    Log.e(TAG, "Song Changed...");
                    playlistIndex++;
                    listner.onItemClickOnItem(playlistIndex);
                    playStream(playList.get(playlistIndex));
                } else if (playbackState == 4 && playList != null && playlistIndex + 1 == playList.size()) {
                    player.setPlayWhenReady(false);
                }
                if (playbackState == 4 && listner != null) {
                    listner.onPlayingEnd();
                }
                if (playbackState == ExoPlayer.STATE_ENDED){
                    //player back ended
                   // listner.onPlayingEnd();
                    player.stop();
                }

                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {}

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity(int reason) { }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }

            @Override
            public void onSeekProcessed() { }
        });
    }

    public void setPlayerListener(CallBacks.playerCallBack ominaCallBack) {
        listner = ominaCallBack;
    }

    public SimpleExoPlayerView getSimpleExoPlayerView() {
        return simpleExoPlayerView;
    }

    public void playStream(String urlToPlay) {
        uriString = urlToPlay;
        Uri mp4VideoUri = Uri.parse(uriString);
        MediaSource videoSource;
        String filenameArray[] = urlToPlay.split("\\.");
        if (uriString.toUpperCase().contains("M3U8")) {
            videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, null, null);
        } else {
            mp4VideoUri = Uri.parse(urlToPlay);
            videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, new DefaultExtractorsFactory(),
                    null, null);
        }
// Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
    }

    public void setplayerVolume(float vol) {
        player.setVolume(vol);
    }

    public void setUriString(String uri) {
        uriString = uri;
    }

    public void setPlaylist(ArrayList<String> uriArrayList, Integer index, CallBacks.playerCallBack callBack) {
        playList = uriArrayList;
        playlistIndex = index;
        listner = callBack;
        playStream(playList.get(playlistIndex));
    }

    public void playerPlaySwitch() {
        if (uriString != "") {
            player.setPlayWhenReady(!player.getPlayWhenReady());
        }
    }

    public void stopPlayer(boolean state) {
        player.setPlayWhenReady(!state);
    }

    public void destroyPlayer() {
        player.stop();
    }

    public Boolean isPlayerPlaying() {
        return player.getPlayWhenReady();
    }

    public ArrayList<String> readURLs(String url) {
        if (url == null) return null;
        ArrayList<String> allURls = new ArrayList<String>();
        try {

            URL urls = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urls
                    .openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                allURls.add(str);
            }
            in.close();
            return allURls;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

