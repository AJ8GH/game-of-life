package aj8gh.gameoflife.api.domain;

import java.util.Set;

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

    public static Set<String> getLabels() {
        return Set.of(RANDOM.label, FILE.label);
    }
}
