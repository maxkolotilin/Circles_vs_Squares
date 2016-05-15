package com.circlesvssquares.game.game_objects.units;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.circlesvssquares.game.game_objects.Party;

/**
 * Created by maximka on 1.5.16.
 */
public abstract class UnitCircle extends UnitBase {
    private Circle unitShape = new Circle();

    public UnitCircle(float defaultHP, float defaultDamage,
                      float defaultCaptureSpeed, float viewRadius) {
        super(defaultHP, defaultDamage, defaultCaptureSpeed, viewRadius);
        party = Party.CIRCLES;
    }

    @Override
    public Shape2D getShape() {
        unitShape.radius = sprite.getWidth() / 2;
        unitShape.setPosition(getCenter());
        return unitShape;
    }
}
