package cdv.hdp;

import java.nio.charset.StandardCharsets;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:04
 */
public class HeapDumpHeaderParser extends BaseParser {

    private String format;
    private int identifierSize;
    private long timeStamp;

    public HeapDumpHeaderParser(int offset, byte[] data) {
        super(offset, data);
    }

    public void parse() {
        while (data[offset] != 0) {
            offset++;
        }
        format = new String(data, 0, offset, StandardCharsets.UTF_8);
        identifierSize = (int) readU4();
        timeStamp = readU8();
    }

    public String getFormat() {
        return format;
    }

    public int getIdentifierSize() {
        return identifierSize;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
