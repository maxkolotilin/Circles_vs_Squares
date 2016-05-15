package com.circlesvssquares.game.game_objects.units;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.interactions.*;

/**
 * Created by maximka on 18.4.16.
 */

public abstract class UnitBase extends GameObject {
    private final float DEFAULT_DAMAGE;
    private final float DEFAULT_CAPTURE_SPEED;
    private final Circle viewArea;

    private int currentTextureIndex;
    private float damagePerSecond;
    private float capturePerSecond;

    protected Interactable interaction = null;
    protected Movable movement = null;
    protected GameObject interactionTarget = null;
    protected UnitBase myKiller = null;

    public UnitBase(float defaultHP, float defaultDamage,
                    float defaultCaptureSpeed, float viewRadius)
    {
        super(defaultHP);
        DEFAULT_DAMAGE = defaultDamage;
        DEFAULT_CAPTURE_SPEED = defaultCaptureSpeed;
        viewArea = new Circle(0, 0, viewRadius);
    }

    public UnitBase getMyKiller() {
        return myKiller;
    }

    public void setMyKiller(UnitBase myKiller) {
        this.myKiller = myKiller;
    }

    public GameObject getInteractionTarget() {
        return interactionTarget;
    }

    public void setInteractionTarget(GameObject interactionTarget) {
        this.interactionTarget = interactionTarget;
    }

    public void setInteraction(Interactable interaction) {
        this.interaction = interaction;
        if (interaction == null) {
            movement = NormalMove.getInstance();
        } else {
            movement = SlowMove.getInstance();
        }
    }

    public Circle getViewArea() {
        viewArea.setPosition(getCenter());
        return viewArea;
    }

    public void setDamageRatio(float damageRatio) {
        damagePerSecond = DEFAULT_DAMAGE * damageRatio;
    }

    public float getDamagePerSecond() {
        return damagePerSecond;
    }

    public float getCapturePerSecond() {
        return capturePerSecond;
    }

    public void resetUnit(float x, float y) {
        healthPoints = DEFAULT_HP;
        damagePerSecond = DEFAULT_DAMAGE;
        capturePerSecond = DEFAULT_CAPTURE_SPEED;
        currentTextureIndex = 0;
        setPosition(x, y);
        setTexture(0);
        setInteraction(null);
        setInteractionTarget(null);
        setMyKiller(null);
    }

    public void interact(float timeDelta) {
        if (interaction != null) {
            if (isInViewArea(interactionTarget)) {
                interaction.interact(this, interactionTarget, timeDelta);
            } else {
                setInteraction(null);
                setInteractionTarget(null);
            }
        }
    }

    public void move(float timeDelta) {
        if (movement != null) {
            movement.move(this, timeDelta);
        }
    }

    @Override
    public void receiveDamage(float value) {
        super.receiveDamage(value);

        float healthRatio = healthPoints / DEFAULT_HP;
        if (healthRatio > 0) {
            int textureIndex = MathUtils.floor(4 - healthRatio * 4);
            if (textureIndex != currentTextureIndex) {
                setTexture(textureIndex);
                currentTextureIndex = textureIndex;
            }
        }
    }

    public boolean isInViewArea(GameObject other) {
        Shape2D shape = other.getShape();

        if (shape instanceof Rectangle) {
            return Intersector.overlaps(getViewArea(), (Rectangle) shape);
        } else if (shape instanceof Circle) {
            return getViewArea().overlaps((Circle) shape);
        } else {
            throw new NotImplementedException("Unknown shape");
        }
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        final int STEP = 12;
        final int RADIUS = 3;
        final float OFFSET = 2f;

        if (interactionTarget != null) {
            shapeRenderer.setColor(partyColor[party.ordinal()]);
            Vector2 vect1 = getCenter();
            Vector2 vect2 = interactionTarget.getCenter();

            if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
                if (interaction instanceof UnitCapture) {
                    // render capture dotted line
                    Vector2 line = vect1.sub(vect2);
                    float length = line.len();
                    for (int i = 0; i < length; i += STEP) {
                        line.clamp(length - i, length - i);
                        shapeRenderer.circle(vect2.x + line.x, vect2.y + line.y,
                            RADIUS);
                    }
                }
                if (interaction instanceof UnitAttack) {
                    // render attack line
                    float delta = OFFSET;
                    if (party == Party.CIRCLES) {
                        delta = -delta;
                    }
                    vect1.add(delta, delta);
                    shapeRenderer.rectLine(vect1, vect2, damagePerSecond);
                }
            } else {
                throw new IllegalArgumentException("Wrong ShapeType in UnitBase: " +
                     shapeRenderer.getCurrentType());
            }
        }
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);

        currentTextureIndex = -1;
        // set texture according health
        receiveDamage(0);
        setInteraction(null);
        setInteractionTarget(null);
        damagePerSecond = DEFAULT_DAMAGE;
        capturePerSecond = DEFAULT_CAPTURE_SPEED;
    }

    public abstract void setTexture(int index);  // TODO: move to GameObject?
}
