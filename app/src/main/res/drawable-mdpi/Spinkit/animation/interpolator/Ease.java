package omgbuzz.devobl.com.testprojectone.Spinkit.animation.interpolator;

import android.view.animation.Interpolator;

/**
 * Created by ybq.
 */
public class Ease {
    public static Interpolator inOut() {
        return omgbuzz.devobl.com.testprojectone.Spinkit.animation.interpolator.PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f);
    }
}
