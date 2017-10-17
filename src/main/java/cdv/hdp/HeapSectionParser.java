package cdv.hdp;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:16
 */
public class HeapSectionParser extends SectionParser {

    private final int border;
    private final Map<Long, Long> instances = new HashMap<>();
    
    public HeapSectionParser(int offset, byte[] data, int border, int identifierSize) {
        super(offset, data, identifierSize);
        this.border = border;
    }

    public Map<Long, Long> getInstances() {
        return instances;
    }

    public void parse() {
        instances.clear();
        while (offset < border) {
            int tag = data[offset] & 0xFF;
            switch (tag) {
                case 0xFF: offset += identifierSize; break;
                case 0x01: offset += 2 * identifierSize; break;
                case 0x02: offset += identifierSize + 2 * U4_SIZE; break;
                case 0x03: offset += identifierSize + 2 * U4_SIZE; break;
                case 0x04: offset += identifierSize + U4_SIZE; break;
                case 0x05: offset += identifierSize; break;
                case 0x06: offset += identifierSize + U4_SIZE; break;
                case 0x07: offset += identifierSize; break;
                case 0x08: offset += identifierSize + 2 * U4_SIZE; break;
                case 0x20: parseClassDump(); break;
                case 0x21: parseInstanceDump(); break;
                case 0x22: parseObjectArrayDump(); break;
                case 0x23: parsePrimitiveArrayDump(); break;
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
        switch(code) {
            case 2: return identifierSize;
            case 4: return 1;
            case 5: return 2;
            case 6: return 4;
            case 7: return 8;
            case 8: return 1;
            case 9: return 2;
            case 10: return 4;
            case 11: return 8;
            default: throw new IllegalArgumentException("Unknown code: " + code);
        }
    }
    
}
