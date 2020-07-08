package omgbuzz.devobl.com.testprojectone.Spinkit.sprite;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;

import omgbuzz.devobl.com.testprojectone.Spinkit.animation.AnimationUtils;


/**
 * Created by ybq.
 */
@SuppressWarnings("WeakerAccess")
public abstract class SpriteContainer extends omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite {

    private omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] sprites;

    private int color;

    public SpriteContainer() {
        sprites = onCreateChild();
        initCallBack();
        onChildCreated(sprites);
    }

    private void initCallBack() {
        if (sprites != null) {
            for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
                sprite.setCallback(this);
            }
        }
    }

    public void onChildCreated(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {

    }

    public int getChildCount() {
        return sprites == null ? 0 : sprites.length;
    }

    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite getChildAt(int index) {
        return sprites == null ? null : sprites[index];
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setColor(color);
        }
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawChild(canvas);
    }

    public void drawChild(Canvas canvas) {
        if (sprites != null) {
            for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
                int count = canvas.save();
                sprite.draw(canvas);
                canvas.restoreToCount(count);
            }
        }
    }

    @Override
    protected void drawSelf(Canvas canvas) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        for (omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite : sprites) {
            sprite.setBounds(bounds);
        }
    }

    @Override
    public void start() {
        super.start();
        AnimationUtils.start(sprites);
    }

    @Override
    public void stop() {
        super.stop();
        AnimationUtils.stop(sprites);
    }

    @Override
    public boolean isRunning() {
        return AnimationUtils.isRunning(sprites) || super.isRunning();
    }

    public abstract omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] onCreateChild();

    @Override
    public ValueAnimator onCreateAnimation() {
        return null;
    }
}
