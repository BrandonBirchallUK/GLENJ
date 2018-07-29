package entities;

import audio.AudioController;
import audio.Source;
import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;
import renderEngine.DisplayManager;
import terrains.Terrain;
import terrains.TerrainController;

import static config.KeyMap.*;
import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {

	private static final float RUN_SPEED = 200;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -9.81f;
	private static final float JUMP_POWER = 30;



	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	public void move(TerrainController tc) {
        tc.update(this);
        AudioController.updateListenerData(super.getPosition());
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta()/1000, 0);
		float distance = currentSpeed * DisplayManager.getDelta()/1000;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManager.getDelta()/100;
		super.increasePosition(0, upwardsSpeed * DisplayManager.getDelta()/1000, 0);
		Terrain tile = tc.getTile((int)Math.floor(getPosition().x / Terrain.SIZE),(int)Math.floor(getPosition().z / Terrain.SIZE));
		if(tile == null) {
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = 0;
            return;
        }
		float terrainHeight = tile.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    public void checkInputs(long windowHandle) {
        if (glfwGetKey(windowHandle, MOVE_FORWARDS_KEY) == GLFW_TRUE) {
            this.currentSpeed = RUN_SPEED;
        } else if (glfwGetKey(windowHandle, MOVE_BACKWARDS_KEY) == GLFW_TRUE) {
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (glfwGetKey(windowHandle, MOVE_RIGHT_KEY) == GLFW_TRUE) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (glfwGetKey(windowHandle, MOVE_LEFT_KEY) == GLFW_TRUE) {
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (glfwGetKey(windowHandle, MOVE_JUMP_KEY) == GLFW_TRUE) {
            jump();
        }
    }

}
