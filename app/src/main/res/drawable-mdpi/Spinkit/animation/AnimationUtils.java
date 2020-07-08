package omgbuzz.devobl.com.testprojectone.Spinkit.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * Created by ybq.
 */
public class AnimationUtils {

    public static void start(Animator animator) {
        if (animator != null && !animator.isStarted()) {
            animator.start();
        }
    }

    public static void stop(Animator animator) {
        if (animator != null && !animator.isRunning()) {
            animator.end();
        }
    }

    public static void start(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
            sprite.start();
        }
    }

    public static void stop(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
            sprite.stop();
        }
    }

    public static boolean isRunning(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
            if (sprite.isRunning()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRunning(ValueAnimator animator) {
        return animator != null && animator.isRunning();
    }

    public static boolean isStarted(ValueAnimator animator) {
        return animator != null && animator.isStarted();
    }
}
