package com.circlesvssquares.game.players;

import com.badlogic.gdx.math.Vector3;
import com.circlesvssquares.game.GameScreen;
import com.circlesvssquares.game.Level;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.game_objects.buildings.*;
import com.circlesvssquares.game.game_objects.units.UnitBase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by maximka on 1.5.16.
 */

public class ComputerPlayer extends Player {
    private static final boolean __DEBUG__ = false;

    transient private ArrayList<BuildingBase> buildings;
    transient private LinkedList<UnitBase> units;
    transient private Vector3 touchPosition;
    transient private Random generator;

    public ComputerPlayer(Party party, Level level) {
        super(party, level);
        init();
    }

    private void readObject(ObjectInputStream in) throws IOException,
                                                    ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    private void init() {
        units = new LinkedList<UnitBase>();
        touchPosition = new Vector3(0, 0, 0);
        generator = new Random();
    }

    @Override
    public void processInput(Vector3 touchPos) {
        if (__DEBUG__) {
            processInput(touchPos, GameScreen.RIGHT_BOARD);
        }
    }

    public void makeInput(){
        if (!__DEBUG__) {
            if (money >= MIN_PRICE) {
                buildings = level.getBuildings();
                units.clear();
                units.addAll(level.getUnitsOnField());

                tryProtectBase();
                tryProtectMyBuildings();
                if (generator.nextDouble() < 0.01) {
                    if (generator.nextDouble() < 0.05) {
                        randomTouch();
                    } else {
                        tryCaptureBuildings();
                    }
                } else {
                    tryUpgradeBuildings();
                }
            }
        }
    }

    private void tryProtectBase() {
        for (UnitBase unit: units) {
            if (unit.getParty() != party) {
                if (unit.getCenter().x > GameScreen.RIGHT_BOARD - 200) {
                    touchPosition.x = unit.getCenter().x;
                    touchPosition.y = unit.getCenter().y;
                    processInput(touchPosition, GameScreen.RIGHT_BOARD);
                }
            }
        }
    }

    private void tryProtectMyBuildings() {
        for (UnitBase unit: units) {
            if (unit.getParty() != party) {
                GameObject target = unit.getInteractionTarget();
                if (target != null && target.getParty() == party) {
                    touchPosition.x = unit.getCenter().x;
                    touchPosition.y = unit.getCenter().y;
                    processInput(touchPosition, GameScreen.RIGHT_BOARD);

                    // return;
                    // TODO: 3/4 of field?
                }
            }
        }
    }

    private void tryCaptureBuildings() {
        // TODO: choose building according to distance?
        for (BuildingBase building: buildings) {
            if (building.getParty() == Party.NEUTRAL ||
                    building.getParty() == Party.NONE) {
                if (building.getCenter().x > GameScreen.RIGHT_BOARD / 2 + 200) {
                    touchPosition.x = building.getSprite().getX() - 1;
                    touchPosition.y = building.getCenter().y;
                    processInput(touchPosition, GameScreen.RIGHT_BOARD);
                }
            }
        }
    }

    private void tryUpgradeBuildings() {
        Tower tower = null;

        for (BuildingBase building: buildings) {
            if (building.getParty() == party) {
                if (building instanceof MainBase ||
                        building instanceof Mine) {
                    touchPosition.x = building.getCenter().x;
                    touchPosition.y = building.getCenter().y;
                    processInput(touchPosition, GameScreen.RIGHT_BOARD);
                }

                if (building instanceof SupportTower) {
                    if (tower == null) {
                        tower = (Tower)building;
                    } else {
                        if (building.getBuildingLevel() < tower.getBuildingLevel()) {
                            tower = (Tower)building;
                        }
                    }
                }
            }
        }

        if (tower != null && generator.nextDouble() < 0.005) {
            touchPosition.x = tower.getCenter().x;
            touchPosition.y = tower.getCenter().y;
            processInput(touchPosition, GameScreen.RIGHT_BOARD);
        }
    }

    private void randomTouch() {
        touchPosition.x = generator.nextInt((int)GameScreen.RIGHT_BOARD);
        touchPosition.y = generator.nextInt((int)(GameScreen.TOP_BOARD -
            GameScreen.BOTTOM_BOARD)) + GameScreen.BOTTOM_BOARD;
    }
}
