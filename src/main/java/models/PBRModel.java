package models;

import toolbox.Material;

public class PBRModel {

    private RawModel rawModel;
    private Material material;

    public PBRModel(RawModel model, Material material) {
        this.rawModel = model;
        this.material = material;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Material getMaterial() {
        return material;
    }
}
