package terrains;

import entities.Player;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;

public class TerrainController {


    TerrainTexture backgroundTexture;
    TerrainTexture rTexture;
    TerrainTexture gTexture;
    TerrainTexture bTexture;

    TerrainTexturePack texturePack;
    TerrainTexture blendMap;

    public static final int MAX_RENDER_RADIUS = 6;

    //x,z
    private List<Terrain> currentLoadedTerrains = new ArrayList<>();

    private MasterRenderer masterRenderer;
    private Loader loader;

    public TerrainController(MasterRenderer renderer, Loader loader) {
        this.masterRenderer = renderer;
        this.loader = loader;


        backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        bTexture = new TerrainTexture(loader.loadTexture("path"));

        texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
    }

    public void update(Player player) {
        int lowerGridX = (int) ((player.getPosition().x / Terrain.SIZE) - MAX_RENDER_RADIUS);
        int lowerGridZ = (int) ((player.getPosition().z / Terrain.SIZE) - MAX_RENDER_RADIUS);
        int upperGridX = (int) ((player.getPosition().x / Terrain.SIZE) + MAX_RENDER_RADIUS);
        int upperGridZ = (int) ((player.getPosition().z / Terrain.SIZE) + MAX_RENDER_RADIUS);

        currentLoadedTerrains.clear();
        for (int i = lowerGridX; i < upperGridX; i++) {
            for (int j = lowerGridZ; j < upperGridZ; j++) {
                currentLoadedTerrains.add(Terrain.create(i,j, loader, texturePack, blendMap));
            }
        }
        Terrain.loadedTiles.removeIf(terrain -> terrain.getGridX() < lowerGridX || terrain.getGridX() > upperGridX || terrain.getGridZ() < lowerGridZ || terrain.getGridZ() > upperGridZ);
    }

    public Terrain getTile(int gridX, int gridZ) {
        for (Terrain t : Terrain.loadedTiles) {
            if(t.getGridX() == gridX && t.getGridZ() == gridZ) {
                return t;
            }
        }
        return null;
    }

    public void update() {
        currentLoadedTerrains.forEach(masterRenderer::processTerrain);
    }
}
