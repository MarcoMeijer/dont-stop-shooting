package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.dontstopshooting.dontstopshooting.entity.Bat;
import com.dontstopshooting.dontstopshooting.entity.Bomb;
import com.dontstopshooting.dontstopshooting.entity.BulletPowerUp;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    private final List<LevelMap> levels;
    private GameScreen screen;

    public MapGenerator(GameScreen screen) {
        levels = new ArrayList<>();
        this.screen = screen;

        addLevel("level1.tmx");
    }

    public boolean collisionCheck(HitBox hitBox) {
        for (LevelMap level : levels)
            if (level.collisionCheck(hitBox))
                return true;
        return false;
    }

    public void onHit(GridPoint2 point) {
        for (LevelMap level : levels)
            level.onHit(point);
    }

    public void addLevel(String levelName) {
        float offset = levels.size()*512.0f;
        LevelMap levelMap = new LevelMap(screen, levelName, offset);
        levels.add(levelMap);
        for (MapLayer layer : levelMap.map.getLayers()) {
            if (!(layer instanceof TiledMapTileLayer))
                continue;

            TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

            for (int x=0; x<tileLayer.getWidth(); x++) {
                for (int y=0; y<tileLayer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
                    if (cell == null)
                        continue;
                    TiledMapTile tile = cell.getTile();
                    if (tile == null)
                        continue;
                    MapProperties tileProperties = tile.getProperties();
                    if (tileProperties == null)
                        continue;
                    if (!tileProperties.containsKey("entity"))
                        continue;
                    String entityName = tileProperties.get("entity", String.class);
                    if (entityName.equals("bomb")) {
                        new Bomb(screen, new Vector2(offset + x*16.0f, y*16.0f));
                        cell.setTile(null);
                    }
                    if (entityName.equals("batv")) {
                        new Bat(screen, new Vector2(offset + x*16.0f-4, y*16.0f - 2), true);
                        cell.setTile(null);
                    }
                    if (entityName.equals("bath")) {
                        new Bat(screen, new Vector2(offset + x*16.0f-4, y*16.0f - 2), false);
                        cell.setTile(null);
                    }
                    if (entityName.equals("bullet")) {
                        new BulletPowerUp(screen, new Vector2(offset + x*16.0f+4, y*16.0f));
                        cell.setTile(null);
                    }
                }
            }
        }
    }

    public void render(OrthographicCamera camera) {
        if (screen.player.location.x + GameScreen.gameWidth >= levels.size()*512.0f) {
            Random random = new Random();
            int rng = random.nextInt(4);
            if (rng == 0)
                addLevel("level2.tmx");
            if (rng == 1)
                addLevel("level3.tmx");
            if (rng == 2)
                addLevel("level4.tmx");
            if (rng == 3)
                addLevel("level5.tmx");
        }
        for (LevelMap level : levels) {
            level.render(camera);
        }
    }
}
