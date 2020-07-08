package omgbuzz.devobl.com.testprojectone.Spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Build;


/**
 * Created by ybq.
 */
public class Wave extends omgbuzz.devobl.com.testprojectone.Spinkit.sprite.SpriteContainer {

    @Override
    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] onCreateChild() {
        WaveItem[] waveItems = new WaveItem[5];
        for (int i = 0; i < waveItems.length; i++) {
            waveItems[i] = new WaveItem();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                waveItems[i].setAnimationDelay(600 + i * 100);
            } else {
                waveItems[i].setAnimationDelay(-1200 + i * 100);
            }

        }
        return waveItems;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int rw = bounds.width() / getChildCount();
        int width = bounds.width() / 5 * 3 / 5;
        for (int i = 0; i < getChildCount(); i++) {
            omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite = getChildAt(i);
            int l = bounds.left + i * rw + rw / 5;
            int r = l + width;
            sprite.setDrawBounds(l, bounds.top, r, bounds.bottom);
        }
    }

    public class WaveItem extends omgbuzz.devobl.com.testprojectone.Spinkit.sprite.RectSprite {

        WaveItem() {
            setScaleY(0.4f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.2f, 0.4f, 1f};
            return new omgbuzz.devobl.com.testprojectone.Spinkit.animation.SpriteAnimatorBuilder(this).scaleY(fractions, 0.4f, 1f, 0.4f, 0.4f).
                    duration(1200).
                    easeInOut(fractions)
                    .build();
        }
    }
}
