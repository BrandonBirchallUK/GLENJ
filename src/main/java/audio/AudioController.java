package audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class AudioController {

    private static boolean isInit = false;

    private static long deviceHandle;
    private static long context;

    static Map<String, Integer> soundMap = new HashMap<>();
    static Map<String, Source> sourceMap = new HashMap<>();

    public static void init() {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        System.out.println("Starting Audio Engine");
        deviceHandle = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        context = alcCreateContext(deviceHandle, attributes);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(deviceHandle);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if(alCapabilities.OpenAL10) {
            System.out.println("OpenAL 1.0 Supported");
            //OpenAL 1.0 support
        } if(alCapabilities.OpenAL11) {
            System.out.println("OpenAL 1.1 Supported");
        }

        alListener3f(AL_POSITION, 0, 0, 0);
        alListener3f(AL_VELOCITY, 0,0,0);

        isInit = true;
        System.out.println("Audio Engine Started");
    }

    public static void updateListenerData(Vector3f position, Vector3f velocity) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }
    public static void updateListenerData(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }
    public static void createSound(String soundname, String filename) {
        if(!isInit) init();
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename("res/soundeffects/".concat(filename), channelsBuffer, sampleRateBuffer);

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        //free allocated buffers
        stackPop();
        stackPop();

        //Find the correct OpenAL format
        int format = -1;
        if(channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if(channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        //Request space for the buffer
        int bufferPointer = alGenBuffers();

        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);

        //Free the memory allocated by STB
        free(rawAudioBuffer);

        soundMap.put(soundname, bufferPointer);
    }

    public static void cleanUp() {
        if(!isInit) return;
        sourceMap.forEach((K,V) -> V.delete());
        alcDestroyContext(context);
        alcCloseDevice(deviceHandle);
    }
}
