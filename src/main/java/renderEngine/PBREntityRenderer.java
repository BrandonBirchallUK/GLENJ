package renderEngine;

import entities.PBREntity;
import models.PBRModel;
import models.RawModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.PBRShader;
import toolbox.Material;

import java.util.List;
import java.util.Map;

public class PBREntityRenderer {

    private PBRShader shader;

    public PBREntityRenderer(PBRShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<PBRModel, List<PBREntity>> entities) {
        for (PBRModel model : entities.keySet()) {
            preparePBRdModel(model);
            List<PBREntity> batch = entities.get(model);
            for (PBREntity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void preparePBRdModel(PBRModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Material material = model.getMaterial();
        shader.connectTextureUnits();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        material.bindAlbedo();

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        material.bindNormal();

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        material.bindMetallic();

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        material.bindRoughness();

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        material.bindAO();

    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(PBREntity entity) {
        //Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        Matrix4f transformationMatrix = new Matrix4f()
                .translate(entity.getPosition())
                .rotateAffineXYZ((float) Math.toRadians(entity.getRotX()), (float) Math.toRadians(entity.getRotY()), (float) Math.toRadians(entity.getRotZ()))
                .scale(entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }
}
