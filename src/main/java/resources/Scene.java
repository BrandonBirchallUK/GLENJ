package resources;

import entities.Entity;
import entities.PBREntity;
import guis.GuiTexture;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private final List<Entity> entityList = new ArrayList<>();
    private final List<PBREntity> pbrEntityList = new ArrayList<>();
    private final List<Terrain> terrainList = new ArrayList<>();
    private final List<GuiTexture> guiList = new ArrayList<>();

    public List<Entity> getEntityList() {
        return entityList;
    }

    public List<PBREntity> getPbrEntityList() {
        return pbrEntityList;
    }

    public List<Terrain> getTerrainList() {
        return terrainList;
    }

    public List<GuiTexture> getGuiList() {
        return guiList;
    }
}
