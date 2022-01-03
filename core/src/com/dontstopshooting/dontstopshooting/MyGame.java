package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	@Override
	public void create () {
		setScreen(new GameScreen());
	}
}
