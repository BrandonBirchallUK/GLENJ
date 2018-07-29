package procedural;

import static procedural.FastNoise.NoiseType.*;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class NoiseGenerator {

    private final float TIME_PERIOD;
    private final float x1=0,x2=100;
    private final float y1=0,y2=100;

    private FastNoise heightMapGenerator = new FastNoise();
    private FastNoise temperatureMapGenerator = new FastNoise();
    private FastNoise humididtyMapGenerator = new FastNoise();

    public NoiseGenerator(int seed, float timePeriod) {
        temperatureMapGenerator.SetNoiseType(Simplex);
        temperatureMapGenerator.SetSeed(seed + 1);

        heightMapGenerator.SetNoiseType(SimplexFractal);
        heightMapGenerator.SetFractalType(FastNoise.FractalType.FBM);
        heightMapGenerator.SetSeed(seed);
        this.TIME_PERIOD = timePeriod;
    }

    public float valueAt(int x, int z, float amplitude, float frequency, int octaves, float lacunarity, float gain) {
        heightMapGenerator.SetNoiseType(SimplexFractal);
        heightMapGenerator.SetFrequency(frequency * TIME_PERIOD/100);
        heightMapGenerator.SetFractalOctaves(octaves);
        heightMapGenerator.SetFractalLacunarity(lacunarity);
        heightMapGenerator.SetFractalGain(gain);

        float s = x/TIME_PERIOD;
        float t = z/TIME_PERIOD;
        float dx = x2 - x1;
        float dy = y2 - y1;

        float nx = (float) (x1+cos(s*2*PI)*dx/(2*PI));
        float ny = (float) (y1+cos(t*2*PI)*dy/(2*PI));
        float nz = (float) (x1+sin(s*2*PI)*dx/(2*PI));
        float nw = (float) (y1+sin(t*2*PI)*dy/(2*PI));

        return amplitude * (heightMapGenerator.GetSimplex(nx,ny,nz,nw) + 1);
    }

    public float valueAtNoRepeat(int x, int z, float amplitude, float frequency, int octaves, float lacunarity, float gain) {
        heightMapGenerator.SetNoiseType(SimplexFractal);
        heightMapGenerator.SetFrequency(frequency * TIME_PERIOD/100);
        heightMapGenerator.SetFractalOctaves(octaves);
        heightMapGenerator.SetFractalLacunarity(lacunarity);
        heightMapGenerator.SetFractalGain(gain);

        return amplitude * (heightMapGenerator.GetSimplex(x,z) + 1);
    }
}
