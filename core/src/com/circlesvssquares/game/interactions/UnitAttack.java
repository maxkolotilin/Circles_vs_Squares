package com.circlesvssquares.game.interactions;

import com.circlesvssquares.game.SoundKeeper;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.buildings.BuildingBase;
import com.circlesvssquares.game.game_objects.units.UnitBase;

import java.util.Random;

/**
 * Created by maximka on 18.4.16.
 */

public class UnitAttack implements Interactable {
    private static final double DAMAGE_CHANCE = 0.9;
    private static UnitAttack instance = new UnitAttack();
    private Random generator = new Random();

    private UnitAttack() { }

    public static Interactable getInstance() {
        return instance;
    }

    @Override
    public void interact(UnitBase unit, GameObject other, float timeDelta) {
        if (other.isAlive()) {
            if (generator.nextDouble() < DAMAGE_CHANCE) {
                other.receiveDamage(unit.getDamagePerSecond() * timeDelta);
                SoundKeeper.instance.playShooting(unit);
                if (other instanceof UnitBase && !other.isAlive()) {
                    ((UnitBase) other).setMyKiller(unit);
                }
            }
        } else {
            if (other instanceof UnitBase) {
                unit.setInteraction(null);
                unit.setInteractionTarget(null);
            } else {
                // buildings
                ((BuildingBase) other).setCaptureParty(unit.getParty());
                unit.setInteraction(UnitCapture.getInstance());
            }
        }
    }
}
