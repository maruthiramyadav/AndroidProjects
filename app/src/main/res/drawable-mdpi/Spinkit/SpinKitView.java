package omgbuzz.devobl.com.testprojectone.Spinkit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import omgbuzz.devobl.com.testprojectone.R;


/**
 * Created by ybq.
 */
public class SpinKitView extends ProgressBar {

    private omgbuzz.devobl.com.testprojectone.Spinkit.Style mStyle;
    private int mColor;
    private omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite mSprite;

    public SpinKitView(Context context) {
        this(context, null);
    }

    public SpinKitView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SpinKitViewStyle);
    }

    public SpinKitView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.SpinKitView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpinKitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinKitView, defStyleAttr,
                defStyleRes);
        mStyle = omgbuzz.devobl.com.testprojectone.Spinkit.Style.values()[a.getInt(R.styleable.SpinKitView_SpinKit_Style, 0)];
        mColor = a.getColor(R.styleable.SpinKitView_SpinKit_Color, Color.WHITE);
        a.recycle();
        init();
        setIndeterminate(true);
    }

    private void init() {
        omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite = omgbuzz.devobl.com.testprojectone.Spinkit.SpriteFactory.create(mStyle);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setIndeterminateDrawable(sprite);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (!(d instanceof omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite)) {
            throw new IllegalArgumentException("this d must be instanceof Sprite");
        }
        setIndeterminateDrawable((omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite) d);
    }

    public void setIndeterminateDrawable(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite d) {
        super.setIndeterminateDrawable(d);
        mSprite = d;
        if (mSprite.getColor() == 0) {
            mSprite.setColor(mColor);
        }
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        if (getVisibility() == VISIBLE) {
            mSprite.start();
        }
    }

    @Override
    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite getIndeterminateDrawable() {
        return mSprite;
    }

    public void setColor(int color) {
        this.mColor = color;
        if (mSprite != null) {
            mSprite.setColor(color);
        }
        invalidate();
    }

    @Override
    public void unscheduleDrawable(Drawable who) {
        super.unscheduleDrawable(who);
        if (who instanceof omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite) {
            ((omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite) who).stop();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (mSprite != null && getVisibility() == VISIBLE) {
                mSprite.start();
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (screenState == View.SCREEN_STATE_OFF) {
            if (mSprite != null) {
                mSprite.stop();
            }
        }
    }

}
