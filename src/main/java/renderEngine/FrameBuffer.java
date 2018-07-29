package renderEngine;

import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {
    private int create() {
        int ptr = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, ptr);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        return ptr;
    }
}
