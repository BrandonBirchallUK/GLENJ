package config;

import static org.lwjgl.glfw.GLFW.GLFW_TRUE;

public interface DisplayValues {
    int WIDTH = 1280;
    int HEIGHT = WIDTH * 9 /16;

    boolean VSYNC = true;
    boolean FULLSCREEN = true;
}
