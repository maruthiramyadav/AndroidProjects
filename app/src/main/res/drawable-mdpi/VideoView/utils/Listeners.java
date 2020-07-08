package omgbuzz.devobl.com.testprojectone.VideoView.utils;

import android.widget.LinearLayout;

import omgbuzz.devobl.com.testprojectone.VideoView.VidstaPlayer;

public class Listeners {
    final private LinearLayout messagesContainer;

    public Listeners(LinearLayout container) {
        this.messagesContainer = container;
    }

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoBufferingListener bufferingListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoBufferingListener() {
        @Override
        public void OnVideoBuffering(VidstaPlayer evp, int buffPercent) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video buffering " + buffPercent + "%!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoErrorListener errorListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoErrorListener() {
        @Override
        public void OnVideoError(int what, int extra) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video errored!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoFinishedListener finishedListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoFinishedListener() {
        @Override
        public void OnVideoFinished(VidstaPlayer evp) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video finished!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoPausedListener pausedListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoPausedListener() {
        @Override
        public void OnVideoPaused(VidstaPlayer evp) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video paused!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoRestartListener restartListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoRestartListener() {
        @Override
        public void OnVideoRestart(VidstaPlayer evp) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video restarted!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoStartedListener startedListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoStartedListener() {
        @Override
        public void OnVideoStarted(VidstaPlayer evp) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video started!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoStoppedListener stoppedListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.VideoStateListeners.OnVideoStoppedListener() {
        @Override
        public void OnVideoStopped(VidstaPlayer evp) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video stopped!");
        }
    };

    final public omgbuzz.devobl.com.testprojectone.VideoView.listeners.FullScreenClickListener fullScreenListener = new omgbuzz.devobl.com.testprojectone.VideoView.listeners.FullScreenClickListener() {
        @Override
        public void onToggleClick(boolean isFullscreen) {
            omgbuzz.devobl.com.testprojectone.VideoView.utils.ListenerHelper.createListenerLog(messagesContainer, "Video set to " + (isFullscreen ? "not " : "") + " fullscreen!");
        }
    };
}
