package audio;

import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public final class Source {
    private Vector3f position = new Vector3f(0,0,0);
    private Vector3f velocity = new Vector3f(0,0,0);

    private boolean looping = false;

    private float pitch = 1.0f;
    private float gain = 1.0f;

    private int sourceID;


    public Source(String name) {
        sourceID = alGenSources();

        updateSourceValues();

        AudioController.sourceMap.put(name,this);
    }


    public void pause() {
        alSourcePause(sourceID);
    }

    public void resume() {
        alSourcePlay(sourceID);
    }

    public void stop() {
        alSourceStop(sourceID);
    }


    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateSourceValues();
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
        updateSourceValues();
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
        updateSourceValues();
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        updateSourceValues();
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
        updateSourceValues();
    }

    public int getSourceID() {
        return sourceID;
    }

    public void setSourceID(int sourceID) {
        this.sourceID = sourceID;
        updateSourceValues();
    }

    public boolean isPlaying() {
        return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
    }

    void delete() {
        stop();
        alDeleteSources(sourceID);
    }

    public void play(String soundName) {
        stop();
        alSourcei(sourceID, AL_BUFFER, AudioController.soundMap.get(soundName));
        resume();
    }

    private void updateSourceValues() {
        alSourcef(sourceID, AL_MAX_GAIN, 1.0f);
        alSourcef(sourceID, AL_GAIN, gain);
        alSourcef(sourceID, AL_PITCH, pitch);
        alSource3f(sourceID, AL_POSITION, position.x, position.y, position.z);
        alSource3f(sourceID, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
        alSourcei(sourceID, AL_LOOPING, looping? AL_TRUE:AL_FALSE);
    }
}
