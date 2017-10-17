package cdv.hdp;

import java.util.Arrays;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 * 16.10.2017 20:48
 */
enum RecordTag {

    UTF_8_STRING(0x01),
    LOAD_CLASS(0x02),
    UNLOAD_CLASS(0x03),
    STACK_FRAME(0x04),
    STACK_TRACE(0x05),
    ALLOC_SITES(0x06),
    HEAP_SUMMARY(0x07),
    START_THREAD(0x0A),
    END_THREAD(0x0B),
    HEAP_DUMP(0x0C),
    HEAP_DUMP_SEGMENT(0x1C),
    HEAP_DUMP_END(0x2C),
    CPU_SAMPLES(0x0D),
    CONTROL_SETTINGS(0x0E);

    private final int code;

    RecordTag(int code) {
        this.code = code;
    }

    static RecordTag find(int code) {
        return Arrays.stream(values())
                .filter(tag -> tag.code == code)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown record tag code: " + code));
    }

}
