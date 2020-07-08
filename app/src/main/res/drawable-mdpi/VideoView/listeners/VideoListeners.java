package omgbuzz.devobl.com.testprojectone.VideoView.listeners;


import omgbuzz.devobl.com.testprojectone.VideoView.VidstaPlayer;

public interface VideoListeners {
    void OnVideoStarted(VidstaPlayer evp);

    void OnVideoPaused(VidstaPlayer evp);

    void OnVideoStopped(VidstaPlayer evp);

    void OnVideoFinished(VidstaPlayer evp);

    void OnVideoBuffering(VidstaPlayer evp, int buffPercent);

    void OnVideoError(Exception err);//Todo: Add Error listener, not urgent tho.

    void OnVideoRestart(VidstaPlayer VidstaPlayer);
}
