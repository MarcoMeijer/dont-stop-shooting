package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

public class LevelMap {

    private final TiledMap map;
    private final OrthoCachedTiledMapRenderer renderer;

    public LevelMap(String levelName) {
        map = new TmxMapLoader().load(levelName);
        renderer = new OrthoCachedTiledMapRenderer(map, 1.0f);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }
}
