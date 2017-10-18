package cdv.hdp;

import java.util.Arrays;

/**
 * Basic types of values in class and instance dumps
 *
 * @author Dmitry Kulga
 *         17.10.2017 21:09
 */
public enum BasicType {

    OBJECT(2),
    BOOLEAN(4),
    CHAR(5),
    FLOAT(6),
    DOUBLE(7),
    BYTE(8),
    SHORT(9),
    INT(10),
    LONG(11);

    private int code;

    BasicType(int code) {
        this.code = code;
    }

    static BasicType find(int code) {
        return Arrays.stream(values())
                .filter(tag -> tag.code == code)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown basic type code: " + code));
    }

}
