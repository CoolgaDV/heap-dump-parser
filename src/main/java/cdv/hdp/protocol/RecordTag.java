package cdv.hdp.protocol;

/**
 * Types of heap dump records.
 * Used block of constants instead of enum because of performance reasons.
 *
 * @author Dmitry Kulga
 *         16.10.2017 20:48
 */
public class RecordTag {

    public static final int UTF_8_STRING = 0x01;
    public static final int LOAD_CLASS = 0x02;
    public static final int UNLOAD_CLASS = 0x03;
    public static final int STACK_FRAME = 0x04;
    public static final int STACK_TRACE = 0x05;
    public static final int ALLOC_SITES = 0x06;
    public static final int HEAP_SUMMARY = 0x07;
    public static final int START_THREAD = 0x0A;
    public static final int END_THREAD = 0x0B;
    public static final int HEAP_DUMP = 0x0C;
    public static final int HEAP_DUMP_SEGMENT = 0x1C;
    public static final int HEAP_DUMP_END = 0x2C;
    public static final int CPU_SAMPLES = 0x0D;
    public static final int CONTROL_SETTINGS = 0x0E;

    /** Utility class - forbid instantiation */
    private RecordTag() { }

}
