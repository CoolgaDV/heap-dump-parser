package cdv.hdp.protocol;

/**
 * Basic types of values in class and instance dumps.
 * Used block of constants instead of enum because of performance reasons.
 *
 * @author Dmitry Kulga
 *         17.10.2017 21:09
 */
public class BasicType {

    public static final int OBJECT = 2;
    public static final int BOOLEAN = 4;
    public static final int CHAR = 5;
    public static final int FLOAT = 6;
    public static final int DOUBLE = 7;
    public static final int BYTE = 8;
    public static final int SHORT = 9;
    public static final int INT = 10;
    public static final int LONG = 11;

    /** Utility class - forbid instantiation */
    private BasicType() { }

}
