package physics;

import org.joml.Vector3f;

public abstract class PhysicalBody {

    protected final Vector3f position;
    protected final Vector3f rotation;
    protected final Vector3f velocity;
    protected final Vector3f appliedForce;

    protected float volume;
    protected final PhysicalMaterial material;

    public PhysicalBody(PhysicalMaterial material) {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.velocity = new Vector3f();
        this.appliedForce = new Vector3f();
        this.volume = 0;
        this.material = material;
    }

    public PhysicalBody(Vector3f position, Vector3f rotation, Vector3f velocity, Vector3f appliedForce, float volume, PhysicalMaterial material) {
        this.position = position;
        this.rotation = rotation;
        this.velocity = velocity;
        this.appliedForce = appliedForce;
        this.volume = volume;
        this.material = material;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public Vector3f getAppliedForce() {
        return appliedForce;
    }

    public float getVolume() {
        return volume;
    }

    public PhysicalMaterial getMaterial() {
        return material;
    }

    public void update(long delta) {
        this.velocity.add(this.appliedForce.div(volume * material.density).mul(delta/1000));
        this.position.add(this.velocity);
    }
}
