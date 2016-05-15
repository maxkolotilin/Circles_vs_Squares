package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.players.ComputerPlayer;
import com.circlesvssquares.game.players.RealPlayer;

import java.io.*;

/**
 * Created by maximka on 17.4.16.
 */

public class GameScreen implements Screen {
    public static float WIDTH;
    public static float LEFT_BOARD = 120f;
    public static float RIGHT_BOARD;
    public static float TOP_BOARD = 700f;
    public static float BOTTOM_BOARD = 100f;
    private static RealPlayer circlesPlayer = new RealPlayer(Party.CIRCLES, null);
    private static ComputerPlayer squaresPlayer = new ComputerPlayer(Party.SQUARES, null);
    private static Party winner;

    private SpriteBatch fieldBatch;
    private SpriteBatch guiBatch;
    private ShapeRenderer fieldRenderer;
    private ShapeRenderer guiRenderer;
    private OrthographicCamera fieldCamera;
    private OrthographicCamera guiCamera;
    private BitmapFont font;
    private Music music;
    private Texture coins;
    private Texture clock;
    private final Circles_vs_Squares game;
    private Level currentLevel;
    private boolean isPaused = true;
    private float game_speed = 1;
    private InputProcessor myInputProcessor;

    public GameScreen(final Circles_vs_Squares game) {
        this.game = game;
        currentLevel = new Level(5000f);
        RIGHT_BOARD = currentLevel.getFieldWidth() + LEFT_BOARD;
        WIDTH = RIGHT_BOARD + LEFT_BOARD;

        fieldBatch = new SpriteBatch(100);
        guiBatch = new SpriteBatch();

        fieldRenderer = new ShapeRenderer();
        fieldRenderer.setAutoShapeType(true);
        guiRenderer = new ShapeRenderer();

        fieldCamera = new OrthographicCamera(1280, 720);
        guiCamera = new OrthographicCamera(1280, 720);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/bg.mp3"));
        music.setVolume(0.5f);
        music.setLooping(true);
        coins = new Texture("coins.png");
        clock = new Texture("sand-clock.png");
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.getData().setScale(0.75f, 0.75f);
        font.setColor(0, 0, 0, 1);

        myInputProcessor = new MyInputProcessor(this, fieldCamera, 1280, WIDTH);

        winner = Party.NONE;
        circlesPlayer.resetPlayer();
        squaresPlayer.resetPlayer();
        circlesPlayer.setLevel(currentLevel);
        squaresPlayer.setLevel(currentLevel);
    }

    public static ComputerPlayer getSquaresPlayer() {
        return squaresPlayer;
    }

    public static RealPlayer getCirclesPlayer() {
        return circlesPlayer;
    }

    public static void setWinner(Party party) {
        winner = party;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.85f, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (winner != Party.NONE) {
                game.setState(GameState.END_LEVEL);
            }
            game.switchGameState();
            isPaused = true;
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            isPaused = !isPaused;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
            if (game_speed < 8) {
                game_speed *= 2;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            if (game_speed > 0.5) {
                game_speed /= 2;
            }
        }

        fieldCamera.update();
        guiCamera.update();

        guiRenderer.setProjectionMatrix(guiCamera.combined);
        guiBatch.setProjectionMatrix(guiCamera.combined);
        fieldBatch.setProjectionMatrix(fieldCamera.combined);
        fieldRenderer.setProjectionMatrix(fieldCamera.combined);

        currentLevel.drawBlood(fieldBatch);

        if (isPaused || winner != Party.NONE) {
            currentLevel.drawGameObjects(fieldBatch, fieldRenderer);
            drawGUI();
            if (winner == Party.NONE) {
                guiBatch.begin();
                font.draw(guiBatch, "Paused. Press right CTRL to resume.", 320, 50);
                guiBatch.end();
            } else {
                guiBatch.begin();
                font.draw(guiBatch, "Epic win of " + winner +
                    ". Press ESC to exit.", 300, 50);
                guiBatch.end();
            }
            return;
        }

        squaresPlayer.makeInput();

        float timeDelta = Gdx.graphics.getDeltaTime() * game_speed;
        currentLevel.removeDeadUnits();
        currentLevel.viewWorld();
        currentLevel.moveUnits(timeDelta);
        currentLevel.processCollisions(TOP_BOARD, BOTTOM_BOARD, LEFT_BOARD,
            RIGHT_BOARD, timeDelta);

        currentLevel.drawGameObjects(fieldBatch, fieldRenderer);

        currentLevel.performInteractions(timeDelta);

        drawGUI();
    }

    private void drawGUI() {
        fieldRenderer.begin(ShapeRenderer.ShapeType.Line);
        fieldRenderer.setColor(0, 0, 0, 1);
        Gdx.gl.glLineWidth(4);
        fieldRenderer.rect(LEFT_BOARD, BOTTOM_BOARD, currentLevel.getFieldWidth(),
            TOP_BOARD - BOTTOM_BOARD);
        fieldRenderer.end();
        guiRenderer.begin(ShapeRenderer.ShapeType.Filled);
        guiRenderer.setColor(.85f, 1, 1, 1);
        guiRenderer.rect(0, 0, 1280, BOTTOM_BOARD - 2);
        guiRenderer.rect(0, TOP_BOARD + 2, 1280, 720 - TOP_BOARD);
        guiRenderer.end();
        guiBatch.begin();
        font.draw(guiBatch, circlesPlayer.getMoney() + "", 125, 50);
        // font.draw(guiBatch, squaresPlayer.getMoney() + "", 1125, 50);
        guiBatch.draw(coins, 25, 10);
        guiBatch.draw(clock, 1120, 10);
        font.draw(guiBatch, "x" + game_speed, 1180, 50);
        guiBatch.end();
    }

    public void saveLevel() {
        currentLevel.saveLevel();

        try {
            FileOutputStream fos = new FileOutputStream("players.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(circlesPlayer);
            oos.writeObject(squaresPlayer);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadLevel() {
        try {
            FileInputStream fis = new FileInputStream("players.bin");
            ObjectInputStream oin = new ObjectInputStream(fis);
            circlesPlayer = (RealPlayer) oin.readObject();
            squaresPlayer = (ComputerPlayer) oin.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            currentLevel = currentLevel.loadLevel();

            RIGHT_BOARD = currentLevel.getFieldWidth() + LEFT_BOARD;
            WIDTH = RIGHT_BOARD + LEFT_BOARD;

            Gdx.input.setInputProcessor(new MyInputProcessor(this, fieldCamera, 1280, WIDTH));
        }
        catch (IOException e) {
            Gdx.app.log("IO error", e.getMessage());
        }

        circlesPlayer.setLevel(currentLevel);
        squaresPlayer.setLevel(currentLevel);
    }

    @Override
    public void resize(int width, int height) {
        float x = fieldCamera.position.x;
        if (x == 0) {
            x = fieldCamera.viewportWidth / 2;
        }

        fieldCamera.setToOrtho(false, fieldCamera.viewportWidth, fieldCamera.viewportHeight);
        fieldCamera.position.x = x;

        guiCamera.setToOrtho(false, fieldCamera.viewportWidth, fieldCamera.viewportHeight);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        music.play();
        Gdx.input.setInputProcessor(myInputProcessor);
    }

    @Override
    public void hide() {
        music.pause();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        guiBatch.dispose();
        fieldBatch.dispose();
        fieldRenderer.dispose();
        guiRenderer.dispose();
        font.dispose();
        music.dispose();
        coins.dispose();
        clock.dispose();
    }
}
