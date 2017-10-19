package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;
import cdv.hdp.protocol.BasicType;
import cdv.hdp.protocol.HeapSectionTag;

import java.util.HashMap;
import java.util.Map;

import static cdv.hdp.cursor.ChunkCursor.U1_SIZE;
import static cdv.hdp.cursor.ChunkCursor.U2_SIZE;
import static cdv.hdp.cursor.ChunkCursor.U4_SIZE;

/**
 * Parser for heap dump or heap dump segment record
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:16
 */
class HeapRecordParser extends RecordParser {

    private final int border;
    private final Map<Long, Long> instances = new HashMap<>();

    private int bytesRead;

    HeapRecordParser(ChunkCursor cursor, int border, int identifierSize) {
        super(cursor, identifierSize);
        this.border = border;
    }

    Map<Long, Long> getInstances() {
        return instances;
    }

    void parse() {
        instances.clear();
        while (bytesRead < border) {
            HeapSectionTag tag = HeapSectionTag.find(readU1() & 0xFF);
            switch (tag) {
                case ROOT_UNKNOWN: skipBytes(identifierSize); break;
                case ROOT_JNI_GLOBAL: skipBytes(2 * identifierSize); break;
                case ROOT_JNI_LOCAL: skipBytes(identifierSize + 2 * U4_SIZE); break;
                case ROOT_JAVA_FRAME: skipBytes(identifierSize + 2 * U4_SIZE); break;
                case ROOT_NATIVE_STACK: skipBytes(identifierSize + U4_SIZE); break;
                case ROOT_STICKY_CLASS: skipBytes(identifierSize); break;
                case ROOT_THREAD_BLOCK: skipBytes(identifierSize + U4_SIZE); break;
                case ROOT_MONITOR_USED: skipBytes(identifierSize); break;
                case ROOT_THREAD_OBJECT: skipBytes(identifierSize + 2 * U4_SIZE); break;
                case CLASS_DUMP: parseClassDump(); break;
                case INSTANCE_DUMP: parseInstanceDump(); break;
                case OBJECT_ARRAY_DUMP: parseObjectArrayDump(); break;
                case PRIMITIVE_ARRAY_DUMP: parsePrimitiveArrayDump(); break;
            }
        }
    }

    private void parseClassDump() {

        skipBytes(identifierSize + U4_SIZE + 6 * identifierSize + U4_SIZE);

        // constant pool size
        int size = readU2();
        for (int i = 0; i < size; i++) {
            skipBytes(U2_SIZE);
            int type = readU1();
            skipBytes(getSize(type));
        }

        // number of static fields
        size = readU2();
        for (int i = 0; i < size; i++) {
            skipBytes(identifierSize);
            int type = readU1();
            skipBytes(getSize(type));
        }

        // number of instance fields
        size = readU2();
        for (int i = 0; i < size; i++) {
            skipBytes(identifierSize + U1_SIZE);
        }
    }

    private void parseInstanceDump() {
        skipBytes(identifierSize + U4_SIZE);
        long classId = readIdentifier();
        Long count = instances.get(classId);
        instances.put(classId, count == null ? 1 : count + 1);
        int size = (int) readU4();
        skipBytes(size);
    }

    private void parseObjectArrayDump() {
        skipBytes(identifierSize + U4_SIZE);
        int size = (int) readU4();
        long classId = readIdentifier();
        Long count = instances.get(classId);
        instances.put(classId, count == null ? 1 : count + 1);
        skipBytes(identifierSize * size);
    }

    private void parsePrimitiveArrayDump() {
        skipBytes(identifierSize + U4_SIZE);
        int size = (int) readU4();
        int valueSize = getSize(readU1());
        skipBytes(size * valueSize);
    }

    private void skipBytes(int bytes) {
        cursor.skipBytes(bytes);
        bytesRead += bytes;
    }

    private long readU4() {
        bytesRead += U4_SIZE;
        return cursor.readU4();
    }

    private int readU2() {
        bytesRead += U2_SIZE;
        return cursor.readU2();
    }

    private int readU1() {
        bytesRead += U1_SIZE;
        return cursor.readU1();
    }

    long readIdentifier() {
        bytesRead += identifierSize;
        return super.readIdentifier();
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