package cdv.hdp.protocol;

/**
 * Tags in heap dump or heap dump segment record.
 * Used block of constants instead of enum because of performance reasons.
 *
 * @author Dmitry Kulga
 *         17.10.2017 21:36
 */
public class HeapSectionTag {

    public static final int ROOT_UNKNOWN = 0xFF;
    public static final int ROOT_JNI_GLOBAL = 0x01;
    public static final int ROOT_JNI_LOCAL = 0x02;
    public static final int ROOT_JAVA_FRAME = 0x03;
    public static final int ROOT_NATIVE_STACK = 0x04;
    public static final int ROOT_STICKY_CLASS = 0x05;
    public static final int ROOT_THREAD_BLOCK = 0x06;
    public static final int ROOT_MONITOR_USED = 0x07;
    public static final int ROOT_THREAD_OBJECT = 0x08;
    public static final int CLASS_DUMP = 0x20;
    public static final int INSTANCE_DUMP = 0x21;
    public static final int OBJECT_ARRAY_DUMP = 0x22;
    public static final int PRIMITIVE_ARRAY_DUMP = 0x23;

    /** Utility class - forbid instantiation */
    private HeapSectionTag() { }

}
