package omgbuzz.devobl.com.testprojectone.Spinkit;


import omgbuzz.devobl.com.testprojectone.Spinkit.style.Circle;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.CubeGrid;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.FoldingCube;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.RotatingCircle;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.RotatingPlane;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.ThreeBounce;
import omgbuzz.devobl.com.testprojectone.Spinkit.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite create(omgbuzz.devobl.com.testprojectone.Spinkit.Style style) {
        omgbuzz.devobl.com.testprojectone.Spinkit.sprite.Sprite sprite = null;
        switch (style) {
            case WAVE:
                sprite = new Wave();
                break;
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.DoubleBounce();
                break;
            case WANDERING_CUBES:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.WanderingCubes();
                break;
            case PULSE:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.Pulse();
                break;
            case CHASING_DOTS:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new omgbuzz.devobl.com.testprojectone.Spinkit.style.MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
