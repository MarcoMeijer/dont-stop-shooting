package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class LevelMap {

    private final TiledMap map;
    private final OrthoCachedTiledMapRenderer renderer;

    public LevelMap(String levelName) {
        map = new TmxMapLoader().load(levelName);
        renderer = new OrthoCachedTiledMapRenderer(map, 1.0f);
        renderer.setBlending(true);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public boolean collisionCheck(HitBox hitBox) {
        for (MapLayer layer : map.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer))
                continue;

            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

            Vector2 fPos = hitBox.getRealLocation();
            Vector2 ePos = fPos.cpy().add(hitBox.width, hitBox.height);
            int tileWidth = tileLayer.getTileWidth();
            int tileHeight = tileLayer.getTileHeight();

            int begX = (int) (fPos.x+tileWidth )/tileWidth  - 1;
            int begY = (int) (fPos.y+tileHeight)/tileHeight - 1;
            int endX = (int) (ePos.x+tileWidth )/tileWidth  - 1;
            int endY = (int) (ePos.y+tileHeight)/tileHeight - 1;

            for (int x=begX; x<=endX; x++) {
                for (int y=begY; y<=endY; y++) {
                    TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                    if (cell == null)
                        continue;
                    TiledMapTile tile = cell.getTile();
                    if (tile == null)
                        continue;
                    MapProperties tileProperties = tile.getProperties();
                    if (tileProperties == null)
                        continue;
                    if (!tileProperties.containsKey("collision"))
                        continue;
                    boolean collision = tileProperties.get("collision", Boolean.class);
                    if (collision)
                        return true;
                }
            }
        }
        return false;
    }
}
