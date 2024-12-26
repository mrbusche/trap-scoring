package trap.common; // Or an appropriate package for your project

public final class EventTypes {

    private EventTypes() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SINGLES = "singles";
    public static final String DOUBLES = "doubles";
    public static final String HANDICAP = "handicap";
    public static final String SKEET = "skeet";
    public static final String CLAYS = "clays";
    public static final String FIVESTAND = "fivestand";
    public static final String DOUBLESKEET = "doublesskeet";
}
