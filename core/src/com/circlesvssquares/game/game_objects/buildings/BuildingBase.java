package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;

/**
 * Created by maximka on 18.4.16.
 */

public abstract class BuildingBase extends GameObject {
    private static final long TIME_SINCE_DAMAGE = 2500000000l;    // 2.5 sec
    private static final float RESTORE_SPEED = 1f;

    protected final Rectangle healthBar = new Rectangle();
    protected final Rectangle partyLabel = new Rectangle();
    protected final Rectangle levelLabel = new Rectangle();

    private Party captureParty = Party.NONE;
    private long lastDamageTime;
    protected int buildingLevel;

    public BuildingBase(float defaultHP) {
        super(defaultHP);
        buildingLevel = 1;
        party = Party.NEUTRAL;
        healthPoints = DEFAULT_HP;
        lastDamageTime = TimeUtils.nanoTime();
    }

    public void setHP(float hp) {
        if (hp < DEFAULT_HP * buildingLevel) {
            healthPoints = hp;
        } else {
            healthPoints = DEFAULT_HP * buildingLevel;
        }
    }

    public Party getCaptureParty() {
        return captureParty;
    }

    public void setCaptureParty(Party captureParty) {
        this.captureParty = captureParty;
    }

    public void capture(float hp) {
        healthPoints += hp;
        if (healthPoints >= DEFAULT_HP) {
            healthPoints = DEFAULT_HP;
            setParty(captureParty);
        }
    }

    public float getHealthRatio() {
        return healthPoints / DEFAULT_HP / buildingLevel;
    }

    @Override
    public void receiveDamage(float value) {
        super.receiveDamage(value);
        lastDamageTime = TimeUtils.nanoTime();
        if (!isAlive()) {
            setParty(Party.NONE);
            setCaptureParty(Party.NONE);
            setBuildingLevel(1);
            healthPoints = 0;
        }
    }

    public void interact(float timeDelta) {
        restoreHealth(timeDelta);
    }

    private void restoreHealth(float timeDelta) {
        if ((party != Party.NEUTRAL && party != Party.NONE) &&
                healthPoints < DEFAULT_HP * buildingLevel &&
                TimeUtils.timeSinceNanos(lastDamageTime) >= TIME_SINCE_DAMAGE) {
            healthPoints += RESTORE_SPEED * timeDelta;
            if (healthPoints > DEFAULT_HP * buildingLevel) {
                healthPoints = DEFAULT_HP * buildingLevel;
            }
        }
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(partyColor[party.ordinal()]);

        if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Line) {
            Gdx.gl.glLineWidth(2f);
            // health bar frame
            shapeRenderer.rect(healthBar.x, healthBar.y,
                healthBar.width, healthBar.height);

            // level of building
            float xDelta = 2f;
            shapeRenderer.setColor(0, 0, 0, 1);
            for (int i = 0; i < buildingLevel; ++i) {
                float x = levelLabel.x + xDelta * i * 2;
                shapeRenderer.line(x, levelLabel.y,
                    x, levelLabel.y + levelLabel.height);
            }
            shapeRenderer.line(levelLabel.x - xDelta, levelLabel.y,
                levelLabel.x + (2 * buildingLevel - 1) * xDelta,
                levelLabel.y);
            float y = levelLabel.y + levelLabel.height;
            shapeRenderer.line(levelLabel.x - xDelta, y,
                levelLabel.x + (2 * buildingLevel - 1) * xDelta,
                y);

        } else if (shapeRenderer.getCurrentType() ==
                ShapeRenderer.ShapeType.Filled) {
            // fill health bar
            if (party == Party.NONE) {
                shapeRenderer.setColor(partyColor[captureParty.ordinal()]);
            }
            shapeRenderer.rect(healthBar.x + 1, healthBar.y + 1,
                healthBar.width - 2, healthBar.height - 2);
            shapeRenderer.setColor(partyColor[party.ordinal()]);

            // draw party shape
            if (party == Party.CIRCLES) {
                float radius = partyLabel.width / 2;
                shapeRenderer.circle(partyLabel.x + radius,
                    partyLabel.y + radius, radius);
            } else if (party == Party.SQUARES) {
                shapeRenderer.rect(partyLabel.x, partyLabel.y,
                    partyLabel.width, partyLabel.height);
            } else if (party == Party.NEUTRAL) {
                shapeRenderer.triangle(partyLabel.x, partyLabel.y,
                    partyLabel.x + partyLabel.width, partyLabel.y,
                    partyLabel.x + partyLabel.width / 2,
                    partyLabel.y + partyLabel.height);
            }

        } else {
            throw new IllegalArgumentException("Wrong ShapeType in BuildingBase: " +
                shapeRenderer.getCurrentType());
        }
    }

    public int getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(int buildingLevel) {
        this.buildingLevel = buildingLevel;
        healthPoints = DEFAULT_HP * buildingLevel;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("bl", buildingLevel);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        setBuildingLevel(jsonData.getInt("bl"));
        super.read(json, jsonData);
    }
}
