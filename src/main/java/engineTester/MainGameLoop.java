package engineTester;

import audio.AudioController;
import de.matthiasmann.twl.utils.PNGDecoder;
import entities.*;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.PBRModel;
import models.RawModel;
import models.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.TerrainController;
import textures.ModelTexture;
import toolbox.Material;
import toolbox.Texture;

import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class MainGameLoop {

    private static long windowHandle;

	public static void main(String[] args) {
        windowHandle = DisplayManager.createDisplay();

        AudioController.init();

        //load sounds


		Loader loader = new Loader();

		// *********TERRAIN TEXTURE STUFF***********
/*
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(0, 1, loader, texturePack, blendMap);
		Terrain terrain3 = new Terrain(1, 0, loader, texturePack, blendMap);
		Terrain terrain4 = new Terrain(1, 1, loader, texturePack, blendMap);
*/

		// *****************************************

		RawModel model = OBJLoader.loadObjModel("tree", loader);

		TexturedModel tree = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("flower")));

		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		fernTexture.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), fernTexture);

		TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader), new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader), new ModelTexture(loader.loadTexture("lamp")));

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		List<Entity> entities = new ArrayList<>();

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

		entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293), new Vector3f(0,0,0), 1));
		entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), new Vector3f(0,0,0), 1));
		entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305), new Vector3f(0,0,0), 1));

		MasterRenderer renderer = new MasterRenderer(loader);

        TerrainController tc = new TerrainController(renderer, loader);

		RawModel playerModel = OBJLoader.loadObjModel("player", loader);
		RawModel box = OBJLoader.loadObjModel("box", loader);
		Material m = new Material("res/textures/pbr");
        PBRModel pbrModel = new PBRModel(box, m);
        PBREntity pbrEntity = new PBREntity(pbrModel, new Vector3f(370, 8f , -300), new Vector3f(0,0,0), 4f);

		TexturedModel playerTexturedModel = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture")));

		Player player = new Player(playerTexturedModel, new Vector3f(370, 4.2f, -300), new Vector3f(0,180,0), 0.6f);
		Camera camera = new Camera(player);

		GuiTexture health = new GuiTexture(loader.loadTexture("gui/health"), new Vector2f(-0.74f, 0.925f), new Vector2f(0.25f, 0.25f));
        tc.update(player);
		while (!glfwWindowShouldClose(windowHandle)) {
		    player.checkInputs(windowHandle);
		    DisplayManager.clearFramebuffer();
			player.move(tc);
			camera.move();
			renderer.processEntity(pbrEntity);
            pbrEntity.setPosition(player.getPosition());
            renderer.processGui(health);
			renderer.processEntity(player);
			tc.update();
			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		AudioController.cleanUp();
	}
}
