package com.circlesvssquares.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by maximka on 17.4.16.
 */
public class MyInputProcessor implements InputProcessor {
    final OrthographicCamera camera;
    final GameScreen screen;
    final float CAMERA_OFFSET;
    final float FIELD_WIDTH;
    final float SCROLL_SPEED = 100f;

    private Vector3 touchPos = new Vector3();

    public MyInputProcessor(GameScreen scr, OrthographicCamera cam,
                            float camWidth, float fieldWidth) {
        screen = scr;
        camera = cam;
        CAMERA_OFFSET = camWidth / 2;
        FIELD_WIDTH = fieldWidth;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.x = screenX;
        touchPos.y = screenY;
        camera.unproject(touchPos);

        if (button == Input.Buttons.LEFT) {
            //screen.leftClick(touchPos);
            GameScreen.getCirclesPlayer().processInput(touchPos);
            return true;
        } else if (button == Input.Buttons.RIGHT) {
            // screen.rightClick(touchPos);
            GameScreen.getSquaresPlayer().processInput(touchPos);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount == -1) {
            if (camera.position.x <= FIELD_WIDTH - CAMERA_OFFSET) {
                float delta = FIELD_WIDTH - CAMERA_OFFSET - camera.position.x;
                if (delta >= 100f) {
                    camera.translate(SCROLL_SPEED, 0);
                } else {
                    camera.translate(delta, 0);
                }
            }
        } else {
            if (camera.position.x >= CAMERA_OFFSET) {
                float delta = CAMERA_OFFSET - camera.position.x;
                if (delta <= -100f) {
                    camera.translate(-SCROLL_SPEED, 0);
                } else {
                    camera.translate(delta, 0);
                }
            }
        }

        return false;
    }
}
