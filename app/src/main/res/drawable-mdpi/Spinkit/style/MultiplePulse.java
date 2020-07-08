package omgbuzz.devobl.com.testprojectone.Spinkit.style;

import omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite;
import omgbuzz.devobl.com.testprojectone.Spinkit.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class MultiplePulse extends SpriteContainer {
    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.Pulse(),
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.Pulse(),
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.Pulse(),
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setAnimationDelay(200 * (i + 1));
        }
    }
}
