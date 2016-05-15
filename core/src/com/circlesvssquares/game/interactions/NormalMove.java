package com.circlesvssquares.game.interactions;


import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 18.4.16.
 */

public class NormalMove implements Movable {
    private static NormalMove instance = new NormalMove();
    public static final float SPEED = 75f;

    private NormalMove() { }

    public static Movable getInstance() {
        return instance;
    }

    @Override
    public void move(UnitBase unit, float timeDelta) {
        unit.translate(SPEED * timeDelta, 0);
    }
}
