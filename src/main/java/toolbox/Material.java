package toolbox;

public class Material {
    private final String folderDir;

    private Texture albedo;
    private Texture normal;
    private Texture metallic;
    private Texture roughness;
    private Texture ao;

    public Material(String folderDir) {
        this.folderDir = folderDir;
        load();
    }

    private void load() {
        albedo = Texture.loadTexture(folderDir + "/albedo.png");
        normal = Texture.loadTexture(folderDir + "/normal.png");
        metallic = Texture.loadTexture(folderDir + "/metallic.png");
        roughness = Texture.loadTexture(folderDir + "/roughness.png");
        ao = Texture.loadTexture(folderDir + "/ao.png");
    }

    public void bindAlbedo() {
        albedo.bind();
    }

    public void bindNormal() {
        normal.bind();
    }

    public void bindMetallic() {
        metallic.bind();
    }

    public void bindRoughness() {
        roughness.bind();
    }

    public void bindAO() {
        ao.bind();
    }
}
