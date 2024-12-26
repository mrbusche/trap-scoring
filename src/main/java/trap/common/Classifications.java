package trap.common; // Or an appropriate package for your project

public final class Classifications {

    private Classifications() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ROOKIE = "Rookie";
    public static final String VARSITY = "Varsity";
    public static final String INTERMEDIATE_ENTRY = "Intermediate Entry";
    public static final String JUNIOR_VARSITY = "Junior Varsity";
    public static final String INTERMEDIATE_ADVANCED = "Intermediate Advanced";
}
