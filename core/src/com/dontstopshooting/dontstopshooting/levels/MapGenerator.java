package com.dontstopshooting.dontstopshooting.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dontstopshooting.dontstopshooting.GameScreen;
import com.dontstopshooting.dontstopshooting.entity.*;
import com.dontstopshooting.dontstopshooting.utils.HitBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    private final List<LevelMap> levels;
    private final List<Difficulty> difficulties;
    private int difficulty = 0;
    private GameScreen screen;

    public MapGenerator(GameScreen screen) {
        levels = new ArrayList<>();
        this.screen = screen;

        difficulties = new ArrayList<>();
        JsonValue root = new JsonReader().parse(Gdx.files.internal("levels/levels.json").readString());
        for (int i=0; i<root.size; i++)
            difficulties.add(new Difficulty(root.get(i)));
    }

    public boolean collisionCheck(HitBox hitBox) {
        for (LevelMap level : levels)
            if (level.collisionCheck(hitBox))
                return true;
        return false;
    }

    public boolean isDeadly(HitBox hitBox) {
        for (LevelMap level : levels)
            if (level.isDeadly(hitBox))
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
                    if (entityName.equals("missile")) {
                        new Missile(screen, new Vector2(offset + x*16.0f+4, y*16.0f), new Vector2(0, 1), screen.player);
                        cell.setTile(null);
                    }
                    if (entityName.equals("launcher")) {
                        new MissileLauncher(screen, new Vector2(offset + x*16.0f, y*16.0f));
                        cell.setTile(null);
                    }
                }
            }
        }
    }

    public void render(OrthographicCamera camera) {
        if (screen.player.location.x + GameScreen.gameWidth >= levels.size()*512.0f) {
            Difficulty difficultyObject = difficulties.get(difficulty);
            addLevel("levels/"+difficultyObject.getNext()+".tmx");
            if (difficultyObject.isDone() && difficulty + 1 < difficulties.size()) {
                difficulty++;
            }
        }
        for (LevelMap level : levels) {
            level.render(camera);
        }
    }
}
