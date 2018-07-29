package entities;

import models.TexturedModel;
import org.joml.Vector3f;
import physics.PhysicalBody;
import physics.PhysicalMaterial;

public class Entity extends PhysicalBody {

    private static final PhysicalMaterial defaultEntityMaterial = new PhysicalMaterial(1,1);

	private TexturedModel model;
	private float scale;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(defaultEntityMaterial);
		this.model = model;

		this.position.zero();
		this.position.add(position);
		this.rotation.zero();
		this.rotation.add(rotation);

		this.scale = scale;
	}

	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation , float scale) {
		super(defaultEntityMaterial);
		this.textureIndex = index;
		this.model = model;

        this.position.zero();
        this.position.add(position);
        this.rotation.zero();
        this.rotation.add(rotation);

		this.scale = scale;
	}

	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.add(dx,dy,dz);
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
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
