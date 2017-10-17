package cdv.hdp;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:10
 */
abstract class BaseParser {

    int offset;

    byte[] data;

    BaseParser(int offset, byte[] data) {
        this.offset = offset;
        this.data = data;
    }

    long readInt() {
        offset += 4;
        return (data[offset] & 0xFF) |
                ((data[offset - 1] & 0xFF) << 8) |
                ((data[offset - 2] & 0xFF) << 16) |
                ((data[offset - 3] & 0xFF) << 24);
    }

    int readShort() {
        offset += 2;
        return (data[offset] & 0xFF) | ((data[offset - 1] & 0xFF) << 8);
    }

    int readByte() {
        offset++;
        return (data[offset] & 0xFF);
    }

    long readLong() {
        long upper = readInt();
        long lower = readInt();
        return upper << 32 | (lower & 0xFFFFFFFFL);
    }

}
