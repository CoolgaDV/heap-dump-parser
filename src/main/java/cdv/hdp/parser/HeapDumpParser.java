package cdv.hdp.parser;

import cdv.hdp.report.HeapSummaryReport;
import cdv.hdp.cursor.ChunkCursor;

import java.io.IOException;

import static cdv.hdp.protocol.RecordTag.*;

/**
 * High-level heap dump parser
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:03
 */
public class HeapDumpParser extends BaseParser {

    public HeapDumpParser(ChunkCursor cursor) {
        super(cursor);
    }

    public HeapSummaryReport readHeap() throws IOException {

        HeapDumpHeaderParser headerParser = new HeapDumpHeaderParser(cursor);
        headerParser.parse();

        HeapSummaryReport report = new HeapSummaryReport()
                .withFormat(headerParser.getFormat())
                .withTimestamp(headerParser.getTimeStamp());

        while (cursor.hasMoreBytes()) {

            int tag = cursor.readU1();

            // read time
            cursor.skipBytes(ChunkCursor.U4_SIZE);

            int length = (int) cursor.readU4();

            switch (tag) {
                case UTF_8_STRING: {
                    StringRecordParser parser = new StringRecordParser(
                            cursor,
                            length,
                            headerParser.getIdentifierSize());
                    parser.parse();
                    report.addString(parser.getId(), parser.getString());
                    break;
                }
                case LOAD_CLASS: {
                    LoadClassRecordParser parser = new LoadClassRecordParser(
                            cursor,
                            headerParser.getIdentifierSize());
                    parser.parse();
                    report.addClass(parser.getClassId(), parser.getClassNameId());
                    break;
                }
                case HEAP_DUMP:
                case HEAP_DUMP_SEGMENT: {
                    HeapRecordParser parser = new HeapRecordParser(
                            cursor,
                            length,
                            headerParser.getIdentifierSize());
                    parser.parse();
                    report.addInstances(parser.getInstances());
                    report.addPrimitiveArrayInstances(parser.getPrimitiveArrayInstances());
                    break;
                }
                default: {
                    cursor.skipBytes(length);
                    break;
                }
            }
        }

        return report;
    }

}
