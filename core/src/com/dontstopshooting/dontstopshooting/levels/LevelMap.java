package com.dontstopshooting.dontstopshooting.levels;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.Sounds;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

public class LevelMap {

    public final GameScreen screen;
    public final TiledMap map;
    private OrthoCachedTiledMapRenderer renderer;
    private final float offset;
    public boolean hasChanged = false;

    public LevelMap(GameScreen screen, String levelName, float offset) {
        this.screen = screen;
        this.offset = offset;
        map = new TmxMapLoader().load(levelName);
        renderer = new OrthoCachedTiledMapRenderer(map, 1.0f);
    }

    public void render(OrthographicCamera camera) {
        camera.translate(-offset, 0.0f);
        camera.update();
        if (hasChanged) {
            renderer = new OrthoCachedTiledMapRenderer(map, 1.0f);
            hasChanged = false;
        }
        renderer.setBlending(true);
        renderer.setView(camera);
        renderer.render();
        camera.translate( offset, 0.0f);
        camera.update();
    }

    public boolean collisionCheck(HitBox hitBox) {
        for (MapLayer layer : map.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer))
                continue;

            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

            Vector2 fPos = hitBox.getRealLocation();
            fPos.x -= offset;
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

    public void onHit(GridPoint2 point) {
        point = point.cpy();
        point.x -= offset/16;

        for (MapLayer layer : map.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer))
                continue;

            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

            TiledMapTileLayer.Cell cell = tileLayer.getCell(point.x, point.y);
            if (cell == null)
                continue;
            TiledMapTile tile = cell.getTile();
            if (tile == null)
                continue;
            MapProperties tileProperties = tile.getProperties();
            if (tileProperties == null)
                continue;
            if (!tileProperties.containsKey("destructible"))
                continue;
            boolean destructible = tileProperties.get("destructible", Boolean.class);
            if (destructible) {
                Sounds.wood.play();
                tileLayer.setCell(point.x, point.y, new TiledMapTileLayer.Cell());
                hasChanged = true;
                GameScreen.particles.createGunExplosion(offset + point.x*16.0f + 8.0f, point.y*16.0f + 8.0f);
                GameScreen.particles.createWood(offset + point.x*16.0f, point.y*16.0f);
                screen.createPoints(new Vector2(offset + point.x*16.0f + 8.0f, point.y*16.0f + 8.0f), 50);
                screen.playerCamera.shake(1.0f);
            }
        }
    }
}
