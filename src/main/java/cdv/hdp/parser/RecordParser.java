package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;

import static cdv.hdp.cursor.ChunkCursor.U4_SIZE;
import static cdv.hdp.cursor.ChunkCursor.U8_SIZE;

/**
 * Basic class for record parsers
 *
 * @author Dmitry Kulga
 *         16.10.2017 21:28
 */
abstract class RecordParser extends BaseParser {

    final int identifierSize;

    RecordParser(ChunkCursor cursor, int identifierSize) {
        super(cursor);
        this.identifierSize = identifierSize;
    }

    long readIdentifier() {
        switch (identifierSize) {
            case U4_SIZE: return cursor.readU4();
            case U8_SIZE: return cursor.readU8();
            default: throw new IllegalArgumentException(
                    "Unsupported identifier size: " + identifierSize);
        }
    }

}
