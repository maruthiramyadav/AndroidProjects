package omgbuzz.devobl.com.testprojectone.Spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Build;

import omgbuzz.devobl.com.testprojectone.Spinkit.sprite.RectSprite;
import omgbuzz.devobl.com.testprojectone.Spinkit.sprite.SpriteContainer;


/**
 * Created by ybq.
 */
public class WanderingCubes extends SpriteContainer {

    @Override
    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] onCreateChild() {
        return new omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[]{
                new Cube(0),
                new Cube(3)
        };
    }

    @Override
    public void onChildCreated(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        super.onChildCreated(sprites);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            sprites[1].setAnimationDelay(-900);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        bounds = clipSquare(bounds);
        super.onBoundsChange(bounds);
        for (int i = 0; i < getChildCount(); i++) {
            omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(
                    bounds.left,
                    bounds.top,
                    bounds.left + bounds.width() / 4,
                    bounds.top + bounds.height() / 4
            );
        }
    }

    private class Cube extends RectSprite {
        int startFrame;

        public Cube(int startFrame) {
            this.startFrame = startFrame;
        }

        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.25f, 0.5f, 0.51f, 0.75f, 1f};
            omgbuzz.devobl.com.testprojectone.Spinkit.animation.SpriteAnimatorBuilder builder = new omgbuzz.devobl.com.testprojectone.Spinkit.animation.SpriteAnimatorBuilder(this).
                    rotate(fractions, 0, -90, -179, -180, -270, -360).
                    translateXPercentage(fractions, 0f, 0.75f, 0.75f, 0.75f, 0f, 0f).
                    translateYPercentage(fractions, 0f, 0f, 0.75f, 0.75f, 0.75f, 0f).
                    scale(fractions, 1f, 0.5f, 1f, 1f, 0.5f, 1f).
                    duration(1800).
                    easeInOut(fractions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.
                        startFrame(startFrame);
            }
            return builder.build();
        }
    }
}
