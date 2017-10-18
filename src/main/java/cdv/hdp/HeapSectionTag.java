package cdv.hdp;

import java.util.Arrays;

/**
 * Tags in heap dump or heap dump segment record
 *
 * @author Dmitry Kulga
 *         17.10.2017 21:36
 */
public enum HeapSectionTag {

    ROOT_UNKNOWN(0xFF),
    ROOT_JNI_GLOBAL(0x01),
    ROOT_JNI_LOCAL(0x02),
    ROOT_JAVA_FRAME(0x03),
    ROOT_NATIVE_STACK(0x04),
    ROOT_STICKY_CLASS(0x05),
    ROOT_THREAD_BLOCK(0x06),
    ROOT_MONITOR_USED(0x07),
    ROOT_THREAD_OBJECT(0x08),
    CLASS_DUMP(0x20),
    INSTANCE_DUMP(0x21),
    OBJECT_ARRAY_DUMP(0x22),
    PRIMITIVE_ARRAY_DUMP(0x23);

    private int code;

    HeapSectionTag(int code) {
        this.code = code;
    }

    static HeapSectionTag find(int code) {
        return Arrays.stream(values())
                .filter(tag -> tag.code == code)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown heap section tag code: " + code));
    }

}
