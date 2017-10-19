package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Heap dump header parser
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:04
 */
class HeapDumpHeaderParser extends BaseParser {

    private String format;
    private int identifierSize;
    private long timeStamp;

    HeapDumpHeaderParser(ChunkCursor cursor) {
        super(cursor);
    }

    void parse() {
        ByteArrayOutputStream stringBuffer = new ByteArrayOutputStream();
        int read;
        while ((read = cursor.readU1()) != 0) {
            stringBuffer.write(read);
        }
        byte[] stringBytes = stringBuffer.toByteArray();
        format = new String(stringBytes, 0, stringBytes.length - 1, StandardCharsets.UTF_8);
        identifierSize = (int) cursor.readU4();
        timeStamp = cursor.readU8();
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
