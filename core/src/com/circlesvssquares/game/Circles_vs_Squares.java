package com.circlesvssquares.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.async.ThreadUtils;


public class Circles_vs_Squares extends Game {
    private GameState state;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void create() {
//        state = GameState.START_MENU;
//        menuScreen = new MenuScreen(this);
//        gameScreen = new GameScreen(this);
        setScreen(new StartScreen(this));
    }

    public void createScreens() {
        state = GameState.START_MENU;
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        getScreen().dispose();
        setScreen(menuScreen);
    }

    public void render() {
        super.render();
    }

    public void switchGameState() {
        if (state == GameState.PLAY) {
            state = GameState.PAUSE_MENU;
            setScreen(menuScreen);
        } else if (state == GameState.END_LEVEL) {
            state = GameState.START_MENU;
            gameScreen = new GameScreen(this);
            Screen scr = getScreen();
            setScreen(menuScreen);
            scr.dispose();
        } else {
            state = GameState.PLAY;
            setScreen(gameScreen);
        }
    }

    public void saveGame() {
        gameScreen.saveLevel();
    }

    public void loadGame() {
        gameScreen.loadLevel();
        state = GameState.PAUSE_MENU;
    }

    public void dispose() {
        super.dispose();
        TextureKeeper.instance.dispose();
    }

}

enum GameState { START_MENU, PLAY, PAUSE_MENU, END_LEVEL };
