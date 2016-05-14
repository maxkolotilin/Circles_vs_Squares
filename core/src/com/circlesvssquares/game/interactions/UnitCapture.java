package com.circlesvssquares.game.interactions;

import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.game_objects.buildings.BuildingBase;
import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 18.4.16.
 */
public class UnitCapture implements Interactable {
    private static UnitCapture instance = new UnitCapture();

    private UnitCapture() { }

    public static Interactable getInstance() {
        return instance;
    }

    @Override
    public void interact(UnitBase unit, GameObject other, float timeDelta) {
        BuildingBase building = (BuildingBase) other;
        if (building.getParty() == Party.NONE) {
            if (!building.isAlive()) {
                building.setCaptureParty(unit.getParty());
            }
            if (building.getCaptureParty() == unit.getParty()) {
                building.capture(unit.getCaptureSpeed() * timeDelta);
            }
            else {
                // remove enemy's capture
                unit.setInteraction(UnitAttack.getInstance());
            }
        } else {
            unit.setInteractionTarget(null);
            unit.setInteraction(null);
        }
    }
}
