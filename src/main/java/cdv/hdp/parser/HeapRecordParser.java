package cdv.hdp.parser;

import cdv.hdp.protocol.BasicType;
import cdv.hdp.protocol.HeapSectionTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for heap dump or heap dump segment record
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:16
 */
class HeapRecordParser extends RecordParser {

    private final int border;
    private final Map<Long, Long> instances = new HashMap<>();
    
    HeapRecordParser(int offset, byte[] data, int border, int identifierSize) {
        super(offset, data, identifierSize);
        this.border = border;
    }

    Map<Long, Long> getInstances() {
        return instances;
    }

    void parse() {
        instances.clear();
        while (offset < border) {
            HeapSectionTag tag = HeapSectionTag.find(data[offset] & 0xFF);
            switch (tag) {
                case ROOT_UNKNOWN: offset += identifierSize; break;
                case ROOT_JNI_GLOBAL: offset += 2 * identifierSize; break;
                case ROOT_JNI_LOCAL: offset += identifierSize + 2 * U4_SIZE; break;
                case ROOT_JAVA_FRAME: offset += identifierSize + 2 * U4_SIZE; break;
                case ROOT_NATIVE_STACK: offset += identifierSize + U4_SIZE; break;
                case ROOT_STICKY_CLASS: offset += identifierSize; break;
                case ROOT_THREAD_BLOCK: offset += identifierSize + U4_SIZE; break;
                case ROOT_MONITOR_USED: offset += identifierSize; break;
                case ROOT_THREAD_OBJECT: offset += identifierSize + 2 * U4_SIZE; break;
                case CLASS_DUMP: parseClassDump(); break;
                case INSTANCE_DUMP: parseInstanceDump(); break;
                case OBJECT_ARRAY_DUMP: parseObjectArrayDump(); break;
                case PRIMITIVE_ARRAY_DUMP: parsePrimitiveArrayDump(); break;
            }
            offset++;
        }
    }

    private void parseClassDump() {

        offset += identifierSize + U4_SIZE + 6 * identifierSize + U4_SIZE;

        // constant pool size
        int size = readU2();
        for (int i = 0; i < size; i++) {
            offset += U2_SIZE;
            int type = readU1();
            offset += getSize(type);
        }

        // number of static fields
        size = readU2();
        for (int i = 0; i < size; i++) {
            offset += identifierSize;
            int type = readU1();
            offset += getSize(type);
        }

        // number of instance fields
        size = readU2();
        for (int i = 0; i < size; i++) {
            offset += identifierSize + U1_SIZE;
        }
    }

    private void parseInstanceDump() {
        offset += identifierSize + U4_SIZE;
        long classId = readU8();
        Long count = instances.get(classId);
        instances.put(classId, count == null ? 1 : count + 1);
        long size = readU4();
        offset += size;
    }

    private void parseObjectArrayDump() {
        offset += identifierSize + U4_SIZE;
        long size = readU4();
        long classId = readU8();
        Long count = instances.get(classId);
        instances.put(classId, count == null ? 1 : count + 1);
        offset += identifierSize * size;
    }

    private void parsePrimitiveArrayDump() {
        offset += identifierSize + U4_SIZE;
        long size = readU4();
        int valueSize = getSize(readU1());
        offset += size * valueSize;
    }

    private int getSize(int code) {
        BasicType type = BasicType.find(code);
        switch(type) {
            case OBJECT: return identifierSize;
            case BOOLEAN: return 1;
            case CHAR: return 2;
            case FLOAT: return 4;
            case DOUBLE: return 8;
            case BYTE: return 1;
            case SHORT: return 2;
            case INT: return 4;
            case LONG: return 8;
            default: throw new IllegalArgumentException("Unknown basic type: " + type);
        }
    }
    
}
