package com.circlesvssquares.game.players;

import com.badlogic.gdx.math.Vector3;
import com.circlesvssquares.game.GameScreen;
import com.circlesvssquares.game.Level;
import com.circlesvssquares.game.SoundKeeper;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.game_objects.buildings.BuildingBase;
import com.circlesvssquares.game.game_objects.units.BigUnitParams;
import com.circlesvssquares.game.game_objects.units.SmallUnitParams;

import java.io.Serializable;

/**
 * Created by maximka on 1.5.16.
 */
public abstract class Player implements Serializable {
    private static final int START_MONEY = 100;
    private static final int SMALL_UNIT_PRICE = 100;
    private static final int BIG_UNIT_PRICE = 500;
    private static final int BUILDING_EXTEND_PRICE = 150;
    private static final int MAX_BUILDING_LEVEL = 3;
    transient protected Level level;
    protected double money = 0;
    protected Party party;


    public Player(Party party, Level level) {
        this.party = party;
        this.level = level;
    }

    public Party getParty() {
        return party;
    }

    public void resetPlayer() {
        level = null;
        money = START_MONEY;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getMoney() {
        return (int)money;
    }

    public void addMoney(double amount) {
        money += amount;
    }

    public boolean spendMoney(double amount) {
        if (amount <= money) {
            money -= amount;
            return true;
        }
        return false;
    }

    protected void processInput(Vector3 touchPos, float startX) {
        BuildingBase touchedBuilding = level.getTouchedBuilding(touchPos.x,
            touchPos.y);
        if (touchedBuilding != null) {
            if (touchedBuilding.getParty() == party) {
                int buildingLevel = touchedBuilding.getBuildingLevel();
                if (buildingLevel < MAX_BUILDING_LEVEL) {
                    if (spendMoney(BUILDING_EXTEND_PRICE * buildingLevel)) {
                        touchedBuilding.setBuildingLevel(buildingLevel + 1);
                    } else if (this instanceof RealPlayer) {
                        SoundKeeper.instance.playMoreGold();
                    }
                }
            }
            return;
        }

        if (touchPos.y >= GameScreen.BOTTOM_BOARD &&
                touchPos.y <= GameScreen.TOP_BOARD) {
            int currentMoney = getMoney();
            if (currentMoney >= BIG_UNIT_PRICE) {
                if (level.addUnit(party, touchPos.x,
                        touchPos.y - BigUnitParams.TEXTURE_SIZE / 2,
                        startX, true)) {
                    money -= BIG_UNIT_PRICE;
                }
            } else if (currentMoney >= SMALL_UNIT_PRICE) {
                if (level.addUnit(party, touchPos.x,
                    touchPos.y - SmallUnitParams.TEXTURE_SIZE / 2,
                    startX, false)) {
                    money -= SMALL_UNIT_PRICE;
                }
            } else if (this instanceof RealPlayer) {
                SoundKeeper.instance.playMoreGold();
            }
        }
    }

    public abstract void processInput(Vector3 touchPos);
}
