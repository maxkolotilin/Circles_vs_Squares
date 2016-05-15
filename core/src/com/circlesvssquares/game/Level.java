package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.game_objects.buildings.*;
import com.circlesvssquares.game.game_objects.units.*;
import com.circlesvssquares.game.interactions.NormalMove;
import com.circlesvssquares.game.interactions.SlowMove;
import com.circlesvssquares.game.interactions.UnitAttack;
import com.circlesvssquares.game.interactions.UnitCapture;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by maximka on 19.4.16.
 */

public class Level {
    static private class DeadUnitsInfo {
        public float x;
        public float y;
        public boolean isBig;
        public boolean isBlood;
        public float rotation;
    }

    private static final int MAX_UNITS = 12;

    private Stack<SmallCircleUnit> smallCirclesPool =
        new Stack<SmallCircleUnit>(MAX_UNITS);
    private Stack<BigCircleUnit> bigCirclesPool =
        new Stack<BigCircleUnit>(MAX_UNITS);
    private Stack<SmallSquareUnit> smallSquaresPool =
        new Stack<SmallSquareUnit>(MAX_UNITS);
    private Stack<BigSquareUnit> bigSquaresPool =
        new Stack<BigSquareUnit>(MAX_UNITS);

    private float fieldWidth;

    private LinkedList<UnitBase> unitsOnField;
    private ArrayList<BuildingBase> buildings;
    private Array<DeadUnitsInfo> deadInfo = new Array<DeadUnitsInfo>();
    transient private Array<Sprite> blood = new Array<Sprite>();
    transient private Random generator = new Random();


    public Level(float fieldWidth) {
        this.fieldWidth = fieldWidth;

        for (int i = 0; i < MAX_UNITS; ++i) {
            smallCirclesPool.Push(new SmallCircleUnit());
            smallSquaresPool.Push(new SmallSquareUnit());
            bigCirclesPool.Push(new BigCircleUnit());
            bigSquaresPool.Push(new BigSquareUnit());
        }


        unitsOnField = new LinkedList<UnitBase>();
        buildings = new ArrayList<BuildingBase>();

        // TODO
        // init buildings

        MainBase b = new MainBase();
        b.setPosition(20, 200);
        b.setNativeParty(Party.CIRCLES);
        b.setBuildingLevel(1);
        buildings.add(b);
        b = new MainBase();
        b.setPosition(20, 500);
        b.setNativeParty(Party.CIRCLES);
        b.setBuildingLevel(1);
        buildings.add(b);
        b = new MainBase();
        b.setPosition(fieldWidth + 140, 200);
        b.setNativeParty(Party.SQUARES);
        b.setBuildingLevel(1);
        buildings.add(b);
        b = new MainBase();
        b.setPosition(fieldWidth + 140, 500);
        b.setNativeParty(Party.SQUARES);
        b.setBuildingLevel(1);
        buildings.add(b);

        Mine m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(120 + 550, 540);
        buildings.add(m);
        m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(120 + 330, 180);
        buildings.add(m);
        m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(fieldWidth + 120 - 550 - 80, 180);
        buildings.add(m);
        m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(fieldWidth + 120 - 330 - 80, 540);
        buildings.add(m);
        m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(120 + 930, 360);
        buildings.add(m);
        m = new Mine();
        m.setBuildingLevel(1);
        m.setHP(5);
        m.setPosition(fieldWidth + 120 - 930 - 80, 360);
        buildings.add(m);

        Factory f = new Factory();
        f.setBuildingLevel(1);
        f.setParty(Party.CIRCLES);
        f.setPosition(120 + fieldWidth / 2 - 480, 360);
        buildings.add(f);
        f = new Factory();
        f.setBuildingLevel(1);
        f.setParty(Party.SQUARES
        );
        f.setPosition(120 + fieldWidth / 2 + 400, 360);
        buildings.add(f);

        DefenseTower dt = new DefenseTower();
//        dt.setPosition(120 + fieldWidth / 2 - 24, 376);
//        dt.setBuildingLevel(3);
//        buildings.add(dt);
//        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 24, 180);
        dt.setBuildingLevel(3);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 24, 572);
        dt.setBuildingLevel(3);
        buildings.add(dt);

        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 1250 - 48, 276);
        dt.setBuildingLevel(1);
        dt.setHP(5);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 1250 - 48, 476);
        dt.setBuildingLevel(1);
        dt.setHP(5);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 800 - 48, 600);
        dt.setBuildingLevel(2);
        dt.setHP(10);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 - 800 - 48, 152);
        dt.setBuildingLevel(2);
        dt.setHP(10);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 + 1250, 276);
        dt.setBuildingLevel(1);
        dt.setHP(5);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 + 1250, 476);
        dt.setBuildingLevel(1);
        dt.setHP(5);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 + 800, 600);
        dt.setBuildingLevel(2);
        dt.setHP(10);
        buildings.add(dt);
        dt = new DefenseTower();
        dt.setPosition(120 + fieldWidth / 2 + 800, 152);
        dt.setBuildingLevel(2);
        dt.setHP(10);
        buildings.add(dt);


        SupportTower st = new SupportTower();
        st.setPosition(560, 376);
        st.setBuildingLevel(1);
        buildings.add(st);
        st = new SupportTower();
        st.setPosition(fieldWidth + 120 - 440 - 48, 376);
        st.setBuildingLevel(1);
        buildings.add(st);
        st = new SupportTower();
        st.setPosition(fieldWidth / 2 + 120 - 788, 376);
        st.setBuildingLevel(1);
        buildings.add(st);
        st = new SupportTower();
        st.setPosition(fieldWidth / 2 + 120 + 740, 376);
        st.setBuildingLevel(1);
        buildings.add(st);
        st = new SupportTower();
        st.setPosition(120 + fieldWidth / 2 - 24, 376);
        st.setBuildingLevel(3);
        buildings.add(st);
    }

    public Level() {
    }

    public ArrayList<BuildingBase> getBuildings() {
        return buildings;
    }

    public LinkedList<UnitBase> getUnitsOnField() {
        return unitsOnField;
    }

    public void moveUnits(float timeDelta) {
        for (UnitBase unit: unitsOnField) {
            unit.move(timeDelta);
        }
    }

    public void performInteractions(float timeDelta) {
        for (UnitBase unit: unitsOnField) {
            unit.interact(timeDelta);
        }
        for (BuildingBase building: buildings) {
            building.interact(timeDelta);
        }

        for (UnitBase unit: unitsOnField) {
            GameObject target = unit.getInteractionTarget();
            if (target != null) {
                if (!target.isAlive()) {
                    if (target instanceof UnitBase) {
                        unit.setInteraction(null);
                        unit.setInteractionTarget(null);
                    } else {
                        // instanceof BuildingBase
                        unit.setInteraction(UnitCapture.getInstance());
                    }
                }
            }

            unit.setDamageRatio(1);
        }
        for (BuildingBase building: buildings) {
            if (building instanceof DefenseTower) {
                GameObject target = ((DefenseTower) building).getInteractionTarget();
                if (target != null && !target.isAlive()) {
                    ((DefenseTower) building).setInteractionTarget(null);
                    ((DefenseTower) building).setDamageRatio(1);
                }
            }
        }
    }

    public void viewWorld() {
        for (UnitBase unit : unitsOnField) {
            if (unit.getInteractionTarget() instanceof UnitBase) {
                continue;
            }

            for (UnitBase enemy : unitsOnField) {
                if (unit.getParty() != enemy.getParty() &&
                        unit.isInViewArea(enemy)) {
                    unit.setInteractionTarget(enemy);
                    unit.setInteraction(UnitAttack.getInstance());
                    break;
                }
            }

            if (unit.getInteractionTarget() == null) {      // TODO
                // search for buildings
                for (BuildingBase building : buildings) {
                    if (unit.getParty() != building.getParty() &&
                        unit.isInViewArea(building)) {
                        unit.setInteractionTarget(building);

                        if (!building.isAlive()) {
                            building.setCaptureParty(unit.getParty());
                        }

                        if (building.getCaptureParty() == unit.getParty()) {
                            unit.setInteraction(UnitCapture.getInstance());
                        } else {
                            unit.setInteraction(UnitAttack.getInstance());
                        }

                        if (building instanceof DefenseTower &&
                            building.getParty() != Party.NONE) {
                            break;
                        }
                    }
                }
            }
        }

        for (BuildingBase building : buildings) {
            if (building instanceof SupportTower &&
                    building.getParty() != Party.NONE) {
                SupportTower tower = (SupportTower) building;
                for (UnitBase unit : unitsOnField) {
                    if (tower.isInSupportArea(unit) &&
                            unit.getParty() == tower.getParty()) {
                        unit.setDamageRatio(tower.getSupportRatio());
                    } else {
                        // unit.setDamageRatio(1);
                    }
                }
                for (BuildingBase _building : buildings) {
                    if (tower.getParty() == _building.getParty() &&
                            _building instanceof DefenseTower) {
                        if (tower.isInSupportArea(_building) &&
                                _building.getParty() == tower.getParty()) {
                            ((DefenseTower) _building).setDamageRatio(tower.getSupportRatio());
                        } else {
                            // ((DefenseTower) _building).setDamageRatio(1);
                        }
                    }
                }
            }

            if (building instanceof DefenseTower &&
                    building.getParty() != Party.NONE) {
                DefenseTower tower = (DefenseTower) building;

                if (tower.getInteractionTarget() instanceof UnitBase) {
                    continue;
                }

                for (UnitBase enemy : unitsOnField) {
                    if (tower.getParty() != enemy.getParty() &&
                        tower.isInViewArea(enemy)) {
                        tower.setInteractionTarget(enemy);
                        break;
                    }
                }


                if (tower.getInteractionTarget() == null) {
                    // search for buildings
                    for (BuildingBase _building : buildings) {
                        if (tower.getParty() != _building.getParty() &&
                                _building.getParty() != Party.NONE &&
                                tower.isInViewArea(_building)) {
                            tower.setInteractionTarget(_building);

//                            if (_building.isAlive() && (building.getParty() != Party.NONE ||
//                                building.getCaptureParty() != unit.getParty())) {
//                                unit.setInteraction(UnitAttack.getInstance());
//                            break;
                        }
                    }
                }
            }
        }
    }

    public BuildingBase getTouchedBuilding(float x, float y) {
        for (BuildingBase building: buildings) {
            if (building.getShape().contains(x, y)) {
                return building;
            }
        }

        return null;
    }

    public boolean addUnit(Party party, float x, float y, float defaultX,
                           boolean isBig) {
        Stack<? extends UnitBase> smallUnits = smallCirclesPool;
        Stack<? extends UnitBase> bigUnits = bigCirclesPool;
        if (party == Party.SQUARES) {
            smallUnits = smallSquaresPool;
            bigUnits = bigSquaresPool;
        }

        if (smallUnits.Size() + bigUnits.Size() > MAX_UNITS) {
            UnitBase unit;
            if (isBig) {
                unit = bigUnits.Pop();
            } else {
                unit = smallUnits.Pop();
            }

            for (BuildingBase building: buildings) {
                if (building instanceof Factory &&
                        building.getParty() == party) {
                    Factory factory = (Factory) building;
                    if (party == Party.CIRCLES &&
                            factory.getStartX() > defaultX &&
                            factory.getStartX() <= x + 40) {
                        defaultX = factory.getStartX();
                    }
                    if (party == Party.SQUARES &&
                            factory.getStartX() < defaultX &&
                            factory.getStartX() >= x - 40) {
                        defaultX = factory.getStartX();
                    }
                }
            }

            if (party == Party.SQUARES) {
                if (isBig) {
                    defaultX -= BigSquareUnit.TEXTURE_SIZE;
                } else {
                    defaultX -= SmallSquareUnit.TEXTURE_SIZE;
                }
            }

            unit.resetUnit(defaultX, y);
            unitsOnField.add(unit);

            return true;
        } else {
            return false;
        }
    }

    public void drawBlood(SpriteBatch batch) {
        batch.begin();
        for (Sprite bloodSprite: blood) {
            bloodSprite.draw(batch);
        }
        batch.end();
    }

    private void addDeadInfo(Sprite sprite, UnitBase unit, boolean isBlood) {
        DeadUnitsInfo info = new DeadUnitsInfo();
        info.x = sprite.getX();
        info.y = sprite.getY();
        info.rotation = sprite.getRotation();
        info.isBig = unit instanceof BigUnitParams;
        info.isBlood = isBlood;

        deadInfo.add(info);
    }

    public void removeDeadUnits() {
        Iterator<UnitBase> iter = unitsOnField.iterator();
        while (iter.hasNext()) {
            UnitBase unit = iter.next();
            if (!unit.isAlive()) {
                iter.remove();

                Texture bloodTexture = TextureKeeper.getInstance().getBloodTexture(unit);
                Sprite bloodSprite = new Sprite(bloodTexture,
                    bloodTexture.getWidth(), bloodTexture.getHeight());
                bloodSprite.setCenter(unit.getCenter().x,
                    unit.getCenter().y);
                bloodSprite.rotate(generator.nextInt(360));
                blood.add(bloodSprite);
                addDeadInfo(bloodSprite, unit, true);

                if (unit.getMyKiller() instanceof BigUnitParams &&
                        unit instanceof SmallUnitParams) {
                    SoundKeeper.getInstance().playChainsaw();
                    Texture parts = TextureKeeper.getInstance().getDeadParts();
                    for (int i = 0; i < 2; ++i) {
                        Sprite partsSprite = new Sprite(parts,
                            parts.getWidth(), parts.getHeight());
                        partsSprite.setCenter(unit.getCenter().x,
                            unit.getCenter().y);
                        partsSprite.rotate(generator.nextInt(360));
                        blood.add(partsSprite);
                        addDeadInfo(bloodSprite, unit, false);
                    }
                }

                if (unit.getParty() == Party.CIRCLES) {
                    if (unit instanceof SmallUnitParams) {
                        smallCirclesPool.Push((SmallCircleUnit) unit);
                    } else {
                        bigCirclesPool.Push((BigCircleUnit) unit);
                    }
                } else {
                    if (unit instanceof SmallUnitParams) {
                        smallSquaresPool.Push((SmallSquareUnit) unit);
                    } else {
                        bigSquaresPool.Push((BigSquareUnit) unit);
                    }
                }
            }
        }
    }

    void drawGameObjects(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        drawObjects(unitsOnField, batch);
        drawObjects(buildings, batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // drawShapes(unitsOnField, shapeRenderer);
        drawShapes(buildings, shapeRenderer);
        shapeRenderer.flush();
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        drawShapes(unitsOnField, shapeRenderer);
        drawShapes(buildings, shapeRenderer);
        shapeRenderer.end();
    }

    private void drawObjects(Iterable<? extends GameObject> objects,
                             SpriteBatch batch) {
        for (GameObject object: objects) {
            object.getSprite().draw(batch);
        }
    }

    private void drawShapes(Iterable<? extends GameObject> objects,
                            ShapeRenderer shapeRenderer) {
        for (GameObject object: objects) {
            object.drawShapes(shapeRenderer);
        }
    }

    public void processCollisions(float top, float bottom, float left,
                                  float right, float timeDelta) {
        // TODO
        // collisions between units, buildings

        // between circle and square
        for (int i = 0; i < MAX_UNITS; ++i) {
            for (UnitBase unit : unitsOnField) {
                if (unit.getParty() == Party.CIRCLES) {
                    for (UnitBase enemy : unitsOnField) {
                        if (enemy.getParty() == Party.SQUARES &&
                            unit.overlaps(enemy)) {
                            float yDelta = SlowMove.SPEED * timeDelta / MAX_UNITS;
                            float xDelta = -yDelta;

                            if (unit.getSprite().getX() > enemy.getSprite().getX()) {
                                xDelta = -xDelta;
                            }
                            if (unit.getSprite().getY() > enemy.getSprite().getY()) {
                                yDelta = -yDelta;
                            }

                            unit.translate(xDelta, -yDelta);
                            enemy.translate(xDelta, yDelta);
                        }
                    }
                }
            }

            // between circle and circle, square and square
            for (UnitBase unit : unitsOnField) {
                float yDelta = NormalMove.SPEED / MAX_UNITS * timeDelta * 2;
                if (unit.getInteractionTarget() != null)
                    yDelta = SlowMove.SPEED * timeDelta / MAX_UNITS * 2;

                for (UnitBase other : unitsOnField) {
                    if (unit.getParty() == other.getParty() && unit != other) {
                        if (unit.overlaps(other)) {
                            float xDelta = -yDelta * 3;
                            if (unit.getSprite().getY() >
                                other.getSprite().getY()) {
                                yDelta = -yDelta;
                            }

                            if (unit.getParty() == Party.CIRCLES) {
                                if (unit.getSprite().getX() <
                                    other.getSprite().getX()) {
                                    unit.translate(xDelta, -yDelta);
                                    other.translate(0, yDelta);
                                } else {
                                    unit.translate(0, -yDelta);

                                    other.translate(xDelta, yDelta);
                                }
                            } else {
                                // for squares
                                if (unit.getSprite().getX() <
                                    other.getSprite().getX()) {
                                    unit.translate(0, -yDelta);
                                    other.translate(xDelta, yDelta);
                                } else {
                                    unit.translate(xDelta, -yDelta);

                                    other.translate(0, yDelta);
                                }
                            }
                        }
                    }
                }
            }

            // between building and unit
            for (UnitBase unit : unitsOnField) {
                for (BuildingBase building : buildings) {
                    if (unit.overlaps(building)) {
                        float yDelta = NormalMove.SPEED / MAX_UNITS * timeDelta;
                        if (unit.getInteractionTarget() != null)
                            yDelta = SlowMove.SPEED * timeDelta / MAX_UNITS;
                        float xDelta = -yDelta * 2;

                        Sprite buildingSprite = building.getSprite();
                        float unitHeight = unit.getSprite().getHeight();
                        float spaceOver = top -
                            (buildingSprite.getY() + buildingSprite.getHeight());
                        float spaceUnder = buildingSprite.getY() - bottom;

                        if (spaceOver < unitHeight || (spaceUnder >= unitHeight &&
                            unit.getSprite().getY() < building.getSprite().getY())) {
                            yDelta = -yDelta;
                        }

                        unit.translate(xDelta, yDelta);
                    }
                }
            }
        }

        // field boards
        for (UnitBase unit : unitsOnField) {
            Sprite sprite = unit.getSprite();
            float x = sprite.getX();
            float y = sprite.getY();

            if (x < left) {
                sprite.setX(left);
            }
            if (x > right - sprite.getWidth()) {
                sprite.setX(right - sprite.getWidth());
            }
            if (y < bottom) {
                sprite.setY(bottom);
            }
            if (y > top - sprite.getHeight()) {
                sprite.setY(top - sprite.getHeight());
            }
        }
    }

    public void saveLevel() {
        Json serializer = new Json();

        FileHandle file = Gdx.files.local("saved.txt");
        file.writeString(serializer.toJson(this), false);
    }

    public Level loadLevel() throws IOException {
        Json serializer = new Json();

        BufferedReader reader = Gdx.files.local("saved.txt").reader(8192);

        String savedLevel = reader.readLine();

        Level level = serializer.fromJson(Level.class, savedLevel);
        for (DeadUnitsInfo info: level.deadInfo) {
            Texture t;
            if (info.isBlood) {
                if (info.isBig) {
                    t = TextureKeeper.getInstance().getBloodTexture(bigCirclesPool.Peek());
                } else {
                    t = TextureKeeper.getInstance().getBloodTexture(smallCirclesPool.Peek());
                }
            } else {
                t = TextureKeeper.getInstance().getDeadParts();
            }

            Sprite bloodSprite = new Sprite(t, t.getWidth(), t.getHeight());
            bloodSprite.setCenter(info.x, info.y);
            bloodSprite.rotate(info.rotation);
            level.blood.add(bloodSprite);
        }

        return level;
    }

    public float getFieldWidth() {
        return fieldWidth;
    }
}
