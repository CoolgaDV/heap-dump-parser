package cdv.hdp;

import java.nio.charset.StandardCharsets;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:04
 */
class HeapDumpHeaderParser extends BaseParser {

    private String format;
    private int identifierSize;
    private long timeStamp;

    HeapDumpHeaderParser(int offset, byte[] data) {
        super(offset, data);
    }

    void parse() {
        while (data[offset] != 0) {
            offset++;
        }
        format = new String(data, 0, offset, StandardCharsets.UTF_8);
        identifierSize = (int) readU4();
        timeStamp = readU8();
    }

    String getFormat() {
        return format;
    }

    int getIdentifierSize() {
        return identifierSize;
    }

    long getTimeStamp() {
        return timeStamp;
    }

}
