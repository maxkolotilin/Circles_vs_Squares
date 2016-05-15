package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;

/**
 * Created by maximka on 24.4.16.
 */

public class DefenseTower extends Tower {
    private static final float DEFAULT_HP = 10;
    private static final float DEFAULT_DAMAGE = 2f;
    private static final float VIEW_RADIUS = 128f;

    private Circle viewArea = new Circle();
    private float damagePerSecond = DEFAULT_DAMAGE;
    private GameObject interactionTarget = null;

    public DefenseTower() {
        super(DEFAULT_HP);
        sprite = new Sprite(TextureKeeper.getInstance().getBuilding(3),
            TEXTURE_SIZE, TEXTURE_SIZE);
        viewArea.radius = VIEW_RADIUS;
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        viewArea.setPosition(getCenter());
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);
        damagePerSecond = DEFAULT_DAMAGE * buildingLevel;
    }

    public void setInteractionTarget(GameObject target) {
        interactionTarget = target;
    }

    public GameObject getInteractionTarget() {
        return interactionTarget;
    }

    public void setDamageRatio(float damageRatio) {
        damagePerSecond = DEFAULT_DAMAGE * buildingLevel * damageRatio;
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        final float OFFSET = 2f;
        super.drawShapes(shapeRenderer);

        if (interactionTarget != null) {
            if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
                shapeRenderer.setColor(partyColor[party.ordinal()]);
                Vector2 vect1 = getCenter();
                Vector2 vect2 = interactionTarget.getCenter();

                // render attack line
                float delta = OFFSET;
                if (party == Party.CIRCLES) {
                    delta = -delta;
                }
                vect1.add(delta, delta);
                shapeRenderer.rectLine(vect1, vect2, damagePerSecond);
            }
        }
    }

    public boolean isInViewArea(GameObject other) {
        Shape2D shape = other.getShape();

        if (shape instanceof Rectangle) {
            return Intersector.overlaps(viewArea, (Rectangle) shape);
        } else if (shape instanceof Circle) {
            return viewArea.overlaps((Circle) shape);
        } else {
            throw new NotImplementedException("Unknown shape");
        }
    }

    @Override
    public void interact(float timeDelta) {
        super.interact(timeDelta);

        if (party == Party.NONE) {
            interactionTarget = null;
        }

        if (interactionTarget != null) {
            if (interactionTarget.getParty() != Party.NONE &&
                    interactionTarget.isAlive()) {
                if (isInViewArea(interactionTarget)) {
                    interactionTarget.receiveDamage(timeDelta * damagePerSecond);
                } else {
                    interactionTarget = null;
                }
            } else {
                interactionTarget = null;
            }
        }
    }
}
