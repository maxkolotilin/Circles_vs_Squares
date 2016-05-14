package com.circlesvssquares.game.interactions;

import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 18.4.16.
 */
public class SlowMove implements Movable {
    private static SlowMove instance = new SlowMove();
    public static final float SPEED = 25f;

    private SlowMove() { }

    public static Movable getInstance() {
        return instance;
    }

    @Override
    public void move(UnitBase unit, float timeDelta) {
        unit.translate(SPEED * timeDelta, 0);
    }
}
