package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.PBREntity;
import guis.GuiRenderer;
import guis.GuiShader;
import guis.GuiTexture;
import models.PBRModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import shaders.PBRShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static config.DisplayValues.*;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static terrains.Terrain.SIZE;
import static terrains.TerrainController.MAX_RENDER_RADIUS;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = (SIZE) * (MAX_RENDER_RADIUS-1);

	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;

	private Matrix4f projectionMatrix;

	private GuiRenderer guiRenderer;
	private GuiShader guiShader = new GuiShader();

	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;

	private PBRShader pbrShader = new PBRShader();
	private PBREntityRenderer pbrEntityRenderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();

	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private Map<PBRModel, List<PBREntity>> pbrEntities = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
    private Map<Integer, List<GuiTexture>> guis = new HashMap<>();

	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
        pbrEntityRenderer = new PBREntityRenderer(pbrShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        guiRenderer = new GuiRenderer(loader);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera camera) {
	    //glDisable(GL_FRAMEBUFFER_SRGB);
		prepare();

		pbrShader.start();
		pbrShader.loadSkyColour(RED, GREEN, BLUE);
		pbrShader.loadLights(lights);
		pbrShader.loadViewMatrix(camera);
		pbrEntityRenderer.render(pbrEntities);
		pbrShader.stop();

		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();

		guiShader.start();
		guiRenderer.render(guis);
		guiShader.stop();

		pbrEntities.clear();
		terrains.clear();
		entities.clear();
		guis.clear();
        //GL11.glEnable(GL_FRAMEBUFFER_SRGB);
    }

	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

    public void processEntity(PBREntity entity) {
        PBRModel entityModel = entity.getModel();
        List<PBREntity> batch = pbrEntities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<PBREntity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            pbrEntities.put(entityModel, newBatch);
        }
    }
	public void processGui(GuiTexture toProcess) {
	    if(!guis.containsKey(toProcess.getTexture())) {
            guis.put(toProcess.getTexture(), new ArrayList<>());
        }
        guis.get(toProcess.getTexture()).add(toProcess);
    }

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
		guiRenderer.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) WIDTH / (float) HEIGHT;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix.m33(0);
	}

}
