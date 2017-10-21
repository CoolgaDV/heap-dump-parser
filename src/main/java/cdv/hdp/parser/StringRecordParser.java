package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Parser for UTF-8 string record
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:17
 */
class StringRecordParser extends RecordParser {

    private final int length;

    private long id;
    private String string;

    StringRecordParser(ChunkCursor cursor, int length, int identifierSize) {
        super(cursor, identifierSize);
        this.length = length;
    }

    long getId() {
        return id;
    }

    String getString() {
        return string;
    }

    void parse() {

        id = readIdentifier();

        int stringLength = length - identifierSize;
        ByteArrayOutputStream stringBuffer = new ByteArrayOutputStream(stringLength);
        for (int index = 0; index < stringLength; index++) {
            stringBuffer.write(cursor.readU1());
        }
        byte[] stringBytes = stringBuffer.toByteArray();
        string = new String(stringBytes, 0, stringBytes.length, StandardCharsets.UTF_8);
    }

}
