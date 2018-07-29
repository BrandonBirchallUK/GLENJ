package terrains;

import java.util.ArrayList;
import java.util.List;

public class Biome {

    private static final List<Biome> biomes = new ArrayList<>();

    static {
        Biome biomeA = new Biome(10,10, 5);
        Biome biomeB = new Biome(5,5, 50);
    }

    //biome settings
    protected final float HUMIDITY;
    protected final float TEMPERATURE;

    //noise settings
    protected final float AMPLITUDE;

    //texture settings


    Biome(float HUMIDITY, float TEMPERATURE, float AMPLITUDE) {
        this.HUMIDITY = HUMIDITY;
        this.TEMPERATURE = TEMPERATURE;
        this.AMPLITUDE = AMPLITUDE;

        biomes.add(this);
    }

    public static Biome biomeBestMatch(float temperature, float humidity) {
        double shortestDistance = Double.MAX_VALUE;
        int shortestIndex = -1;
        float deltaT;
        float deltaH;
        for (int i = 0; i < biomes.size(); i++) {
            Biome b = biomes.get(i);
            deltaT = b.TEMPERATURE - temperature;
            deltaH = b.HUMIDITY - humidity;
            if(Math.abs(deltaT) + Math.abs(deltaH) < shortestDistance) {
                shortestDistance = Math.abs(deltaT) + Math.abs(deltaH);
                shortestIndex = i;
            }
        }
        return biomes.get(shortestIndex);
    }

    public float getHUMIDITY() {
        return HUMIDITY;
    }

    public float getTEMPERATURE() {
        return TEMPERATURE;
    }

    public float getAMPLITUDE() {
        return AMPLITUDE;
    }
}
