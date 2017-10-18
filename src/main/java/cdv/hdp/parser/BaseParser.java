package cdv.hdp.parser;

/**
 * Basic parser with common methods for reading data blocks
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:10
 */
abstract class BaseParser {

    static final int U1_SIZE = 1;
    static final int U2_SIZE = 2;
    static final int U4_SIZE = 4;
    static final int U8_SIZE = 8;

    int offset;

    byte[] data;

    BaseParser(int offset, byte[] data) {
        this.offset = offset;
        this.data = data;
    }

    long readU4() {
        offset += 4;
        return (data[offset] & 0xFF) |
                ((data[offset - 1] & 0xFF) << 8) |
                ((data[offset - 2] & 0xFF) << 16) |
                ((data[offset - 3] & 0xFF) << 24);
    }

    int readU2() {
        offset += 2;
        return (data[offset] & 0xFF) | ((data[offset - 1] & 0xFF) << 8);
    }

    int readU1() {
        offset++;
        return (data[offset] & 0xFF);
    }

    long readU8() {
        long upper = readU4();
        long lower = readU4();
        return upper << 32 | (lower & 0xFFFFFFFFL);
    }

}
