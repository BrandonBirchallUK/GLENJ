package physics;

public class PhysicalMaterial {
    protected final float density;
    protected final float youngModulus;

    public PhysicalMaterial(float density, float youngModulus) {
        this.density = density;
        this.youngModulus = youngModulus;
    }

    public float getDensity() {
        return density;
    }

    public float getYoungModulus() {
        return youngModulus;
    }

}
