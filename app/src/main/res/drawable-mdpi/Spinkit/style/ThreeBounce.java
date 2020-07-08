package omgbuzz.devobl.com.testprojectone.Spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;


/**
 * Created by ybq.
 */
public class ThreeBounce extends omgbuzz.devobl.com.testprojectone.Spinkit.sprite.SpriteContainer {

    @Override
    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] onCreateChild() {
        return new omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[]{
                new Bounce(),
                new Bounce(),
                new Bounce()
        };
    }

    @Override
    public void onChildCreated(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        super.onChildCreated(sprites);
        sprites[1].setAnimationDelay(160);
        sprites[2].setAnimationDelay(320);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int radius = bounds.width() / 8;
        int top = bounds.centerY() - radius;
        int bottom = bounds.centerY() + radius;

        for (int i = 0; i < getChildCount(); i++) {
            int left = bounds.width() * i / 3
                    + bounds.left;
            getChildAt(i).setDrawBounds(
                    left, top, left + radius * 2, bottom
            );
        }
    }

    private class Bounce extends omgbuzz.devobl.com.testprojectone.Spinkit.sprite.CircleSprite {

        Bounce() {
            setScale(0f);
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.4f, 0.8f, 1f};
            return new omgbuzz.devobl.com.testprojectone.Spinkit.animation.SpriteAnimatorBuilder(this).scale(fractions, 0f, 1f, 0f, 0f).
                    duration(1400).
                    easeInOut(fractions)
                    .build();
        }
    }
}
