package com.circlesvssquares.game;

/**
 * Created by maximka on 17.4.16.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {
    final float LEFT_MARGIN = 340f;
    final float TOP_MARGIN = 500f;
    final float ITEMS_MARGIN = 80f;
    final Circles_vs_Squares game;
    final int MENU_LENGTH;

    SpriteBatch batch;
    OrthographicCamera camera;
    BitmapFont font;

    String[] startMenuItems = { "Start Game", "Load", "Save",
            "Full screen / Window", "Exit" };
    String[] pauseMenuItems = { "Continue game", "Load", "Save",
            "Full screen / Window", "Exit" };
    int selectedItem = 0;


    public MenuScreen(final Circles_vs_Squares game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1280, 720);
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(0, 0, 0, 1);
        MENU_LENGTH = startMenuItems.length;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.85f, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        processInput();
        drawMenu();
    }

    private void processInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            ++selectedItem;
            selectedItem %= MENU_LENGTH;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            --selectedItem;
            if (selectedItem < 0) {
                selectedItem = MENU_LENGTH - 1;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (game.getState() != GameState.START_MENU) {
                game.switchGameState();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedItem) {
                case 0:
                    // start, resume
                    game.switchGameState();
                    break;
                case 1:
                    game.loadGame();
                    break;
                case 2:
                    game.saveGame();
                    break;
                case 3:
                    if(Gdx.graphics.supportsDisplayModeChange()) {
                        if (Gdx.graphics.isFullscreen()) {
                            Gdx.graphics.setWindowedMode(1280, 720);
                        } else {
                            Gdx.graphics.setFullscreenMode(
                                    Gdx.graphics.getDisplayMode(
                                            Gdx.graphics.getMonitor()
                                    )
                            );
                        }
                    }
                    break;
                case 4:
                    Gdx.app.exit();
                    break;
                default:
                    break;
            }
        }
    }

    private void drawMenu() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        font.draw(batch, "Welcome to Circles vs Squares!!!", 280, 620);

        String[] menuItems = pauseMenuItems;
        if (game.getState() != GameState.PAUSE_MENU) {
            menuItems = startMenuItems;
        }
        float topMargin = TOP_MARGIN;
        for (int i = 0; i < MENU_LENGTH; ++i) {
            if (selectedItem == i) {
                font.setColor(1, 0, 0, 1);
            }
            font.draw(batch, menuItems[i], LEFT_MARGIN, topMargin);
            font.setColor(0, 0, 0, 1);
            topMargin -= ITEMS_MARGIN;
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);

    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
