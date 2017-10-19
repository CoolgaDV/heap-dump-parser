package cdv.hdp.parser;

import cdv.hdp.cursor.ChunkCursor;

/**
 * Basic parser with common methods for reading data blocks
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:10
 */
abstract class BaseParser {

    final ChunkCursor cursor;

    BaseParser(ChunkCursor cursor) {
        this.cursor = cursor;
    }
    
}
