package omgbuzz.devobl.com.testprojectone.VideoView;

/**
 * @author Aidan Follestad
 * Modified by Halil Ozercan
 */
public interface BetterVideoCallback {

    /**
     * Called right after Android MediaPlayer is started
     * @param player
     */
    void onStarted(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player);

    /**
     * Called right after Android MediaPlayer is paused
     * @param player
     */
    void onPaused(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player);

    /**
     * Called just before setting the source of Android MediaPlayer
     * @param player
     */
    void onPreparing(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player);

    /**
     * Called when Android MediaPlayer is prepared
     * @param player
     */
    void onPrepared(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player);

    /**
     * Called whenever Android MediaPlayer fires a BufferUpdate.
     * @param percent
     */
    void onBuffering(int percent);

    /**
     * Exception occurred in the player.
     * @param player
     * @param e
     */
    void onError(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player, Exception e);

    /**
     * Called after video is completed and every action is taken by the player.
     * @param player
     */
    void onCompletion(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player);

    /**
     * New: Control toggling might be of importance especially when using fullscreen.
     * You might also adjust your layout or overlays according to controls' visibility.
     * This method is called whenever visibility of controls is changed.
     * @param player
     * @param isShowing : True if controls are visible.
     */
    void onToggleControls(omgbuzz.devobl.com.testprojectone.VideoView.BetterVideoPlayer player, boolean isShowing);

    void onFullScreen();

}