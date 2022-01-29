package aj8gh.gameoflife.api.domain;

public enum SeederType {
    RANDOM("RANDOM"),
    FILE("FILE");

    private final String label;

    SeederType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
