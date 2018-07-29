package api;

public class EngineAPI {
    private static EngineAPI instance;
    private EngineAPI() {}

    public static final EngineAPI getInstance() {
        if(instance ==  null) {
            instance = new EngineAPI();
        }
        return instance;
    }
}
