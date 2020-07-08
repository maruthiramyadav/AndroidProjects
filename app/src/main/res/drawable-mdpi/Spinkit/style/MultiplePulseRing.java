package omgbuzz.devobl.com.testprojectone.Spinkit.style;

import omgbuzz.devobl.com.testprojectone.Spinkit.sprite.SpriteContainer;

/**
 * Created by ybq.
 */
public class MultiplePulseRing extends SpriteContainer {

    @Override
    public omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[] onCreateChild() {
        return new omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite[]{
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.PulseRing(),
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.PulseRing(),
                new omgbuzz.devobl.com.testprojectone.Spinkit.style.PulseRing(),
        };
    }

    @Override
    public void onChildCreated(omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite... sprites) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setAnimationDelay(200 * (i + 1));
        }
    }
}
