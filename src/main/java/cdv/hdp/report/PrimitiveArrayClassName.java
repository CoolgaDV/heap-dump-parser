package cdv.hdp.report;

import static cdv.hdp.protocol.BasicType.*;

/**
 * Primitive array class names to be displayed in execution report
 *
 * @author Dmitry Kulga
 *         20.10.2017 21:05
 */
public class PrimitiveArrayClassName {

    private static final String BOOLEAN_ARRAY = "boolean[]";
    private static final String CHAR_ARRAY = "char[]";
    private static final String FLOAT_ARRAY = "float[]";
    private static final String DOUBLE_ARRAY = "double[]";
    private static final String BYTE_ARRAY = "byte[]";
    private static final String SHORT_ARRAY = "short[]";
    private static final String INT_ARRAY = "int[]";
    private static final String LONG_ARRAY = "long[]";
    
    public static String find(int code) {
        switch (code) {
            case BOOLEAN: return BOOLEAN_ARRAY;
            case CHAR: return CHAR_ARRAY;
            case FLOAT: return FLOAT_ARRAY;
            case DOUBLE: return DOUBLE_ARRAY;
            case BYTE: return BYTE_ARRAY;
            case SHORT: return SHORT_ARRAY;
            case INT: return INT_ARRAY;
            case LONG: return LONG_ARRAY;
            default: throw new IllegalArgumentException(
                    "Unknown primitive basic type code: " + code);
        }
    }
    
    /** Utility class - forbid instantiation */
    private PrimitiveArrayClassName() { }
    
}
