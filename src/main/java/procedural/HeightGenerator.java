package procedural;

import terrains.Biome;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static procedural.FastNoise.NoiseType.Simplex;
import static procedural.FastNoise.NoiseType.SimplexFractal;

public class HeightGenerator {

    private static final int BIOME_SCALE_FACTOR = 64;

    private final float TIME_PERIOD;
    private final float x1=0,x2=100;
    private final float y1=0,y2=100;

    private float amplitude;

    private FastNoise heightMapGenerator = new FastNoise();
    private FastNoise temperatureMapGenerator = new FastNoise();
    private FastNoise humididtyMapGenerator = new FastNoise();

    public HeightGenerator(int seed, float timePeriod) {
        temperatureMapGenerator.SetNoiseType(Simplex);
        temperatureMapGenerator.SetSeed(seed + 1);

        humididtyMapGenerator.SetNoiseType(Simplex);
        humididtyMapGenerator.SetSeed(seed + 2);

        heightMapGenerator.SetNoiseType(SimplexFractal);
        heightMapGenerator.SetFractalType(FastNoise.FractalType.FBM);
        heightMapGenerator.SetSeed(seed);
        this.TIME_PERIOD = timePeriod;
    }

    public void setNoiseValues(float amplitude, float frequency, int octaves, float lacunarity, float gain) {
        heightMapGenerator.SetNoiseType(SimplexFractal);
        heightMapGenerator.SetFrequency(frequency * TIME_PERIOD/100);
        heightMapGenerator.SetFractalOctaves(octaves);
        heightMapGenerator.SetFractalLacunarity(lacunarity);
        heightMapGenerator.SetFractalGain(gain);
        this.amplitude = amplitude;
    }

    public float valueAtRepeat(int x, int z) {
        float s  = x / TIME_PERIOD;
        float t  = z / TIME_PERIOD;
        float dx = x2 - x1;
        float dy = y2 - y1;

        float nx = (float) (x1+cos(s*2*PI)*dx/(2*PI));
        float ny = (float) (y1+cos(t*2*PI)*dy/(2*PI));
        float nz = (float) (x1+sin(s*2*PI)*dx/(2*PI));
        float nw = (float) (y1+sin(t*2*PI)*dy/(2*PI));

        return amplitude * (heightMapGenerator.GetSimplex(nx,ny,nz,nw) + 1);
    }

    public float valueAtNoRepeat(int x, int z) {

        Biome b  = biomeAt(x,z);
        Biome b1 = biomeAt(x + 1,z);
        Biome b2 = biomeAt(x - 1,z);
        Biome b3 = biomeAt(x,z + 1);
        Biome b4 = biomeAt(x,z - 1);

        float averageAmplitude = (b.getAMPLITUDE() + b1.getAMPLITUDE() + b2.getAMPLITUDE() + b3.getAMPLITUDE() + b4.getAMPLITUDE()) / 5;

        return averageAmplitude * (heightMapGenerator.GetSimplex(x,z) + 1);
    }

    public Biome biomeAt(int x, int z) {
        float temperature = temperatureMapGenerator.GetSimplex(x / BIOME_SCALE_FACTOR, z / BIOME_SCALE_FACTOR);
        float humidity = temperatureMapGenerator.GetSimplex(x / BIOME_SCALE_FACTOR, z / BIOME_SCALE_FACTOR);

        return Biome.biomeBestMatch(temperature, humidity);
    }
}
