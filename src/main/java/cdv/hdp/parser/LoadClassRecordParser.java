package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;

import static cdv.hdp.cursor.ChunkCursor.U4_SIZE;

/**
 * Parser for load class record
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:17
 */
class LoadClassRecordParser extends RecordParser {

    private long classId;
    private long classNameId;

    LoadClassRecordParser(ChunkCursor cursor, int identifierSize) {
        super(cursor, identifierSize);
    }

    long getClassId() {
        return classId;
    }

    long getClassNameId() {
        return classNameId;
    }

    void parse() {
        cursor.skipBytes(U4_SIZE);
        classId = readIdentifier();
        cursor.skipBytes(U4_SIZE);
        classNameId = readIdentifier();
    }

}
