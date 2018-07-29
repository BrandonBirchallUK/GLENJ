package config;

import static org.lwjgl.glfw.GLFW.*;

public interface KeyMap {
    int MOVE_FORWARDS_KEY = GLFW_KEY_W;
    int MOVE_BACKWARDS_KEY = GLFW_KEY_S;
    int MOVE_LEFT_KEY = GLFW_KEY_A;
    int MOVE_RIGHT_KEY = GLFW_KEY_D;
    int MOVE_JUMP_KEY = GLFW_KEY_SPACE;
}
