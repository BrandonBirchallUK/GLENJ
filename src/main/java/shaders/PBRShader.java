package shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import toolbox.Material;
import toolbox.Maths;

import java.util.List;

import static config.GraphicsValues.MAX_NUMBER_OF_LIGHTS;

public class PBRShader extends ShaderProgram {


    private static final String VERTEX_FILE = "src/main/java/shaders/pbrVertexShader.glsl";
    private static final String FRAGMENT_FILE = "src/main/java/shaders/pbrFragmentShader.glsl";

    private int location_transformationMatrix;
    private int location_projectiontionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_skyColour;

    private int location_albedo;
    private int location_normalMap;
    private int location_metallicMap;
    private int location_roughnessMap;
    private int location_ambientOcclusionMap;

    public PBRShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectiontionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_skyColour = super.getUniformLocation("skyColour");

        location_lightPosition = new int[MAX_NUMBER_OF_LIGHTS];
        location_lightColour = new int[MAX_NUMBER_OF_LIGHTS];
        location_attenuation = new int[MAX_NUMBER_OF_LIGHTS];
        for (int i = 0; i < MAX_NUMBER_OF_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }

        location_albedo = super.getUniformLocation("albedo");
        location_normalMap = super.getUniformLocation("normalMap");
        location_metallicMap = super.getUniformLocation("metallicMap");
        location_roughnessMap = super.getUniformLocation("roughnessMap");
        location_ambientOcclusionMap = super.getUniformLocation("aoMap");
    }

    public void connectTextureUnits() {
        super.loadInt(location_albedo, 0);
        super.loadInt(location_normalMap, 1);
        super.loadInt(location_metallicMap, 2);
        super.loadInt(location_roughnessMap, 3);
        super.loadInt(location_ambientOcclusionMap, 4);
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(location_skyColour, new Vector3f(r, g, b));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_NUMBER_OF_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
                super.loadVector(location_lightColour[i], lights.get(i).getColour());
                super.loadFloat(location_attenuation[i], lights.get(i).getAttenuation().z);
            } else {
                super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.loadFloat(location_attenuation[i], 0);
            }
        }
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectiontionMatrix, projection);
    }
}
