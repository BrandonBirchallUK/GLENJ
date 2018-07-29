package entities;

import models.PBRModel;
import org.joml.Vector3f;
import physics.PhysicalBody;
import physics.PhysicalMaterial;

public class PBREntity extends PhysicalBody {

    private static final PhysicalMaterial defaultEntityMaterial = new PhysicalMaterial(1,1);

    private PBRModel model;
    private float scale;

    public PBREntity(PBRModel model, Vector3f position, Vector3f rotation, float scale) {
        super(defaultEntityMaterial);
        this.model = model;

        this.position.zero();
        this.position.add(position);
        this.rotation.zero();
        this.rotation.add(rotation);

        this.scale = scale;
    }

    public PBREntity(PBRModel model, int index, Vector3f position, Vector3f rotation , float scale) {
        super(defaultEntityMaterial);
        this.model = model;

        this.position.zero();
        this.position.add(position);
        this.rotation.zero();
        this.rotation.add(rotation);

        this.scale = scale;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotation.add(dx,dy,dz);
    }

    public PBRModel getModel() {
        return model;
    }

    public void setModel(PBRModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.zero();
        this.position.add(position);
    }

    public float getRotX() {
        return rotation.x;
    }

    public float getRotY() {
        return rotation.y;
    }

    public float getRotZ() {
        return rotation.z;
    }

    public float getScale() {
        return scale;
    }
}
