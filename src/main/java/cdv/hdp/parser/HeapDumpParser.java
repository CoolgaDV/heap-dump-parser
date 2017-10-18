package cdv.hdp.parser;

import cdv.hdp.HeapSummaryReport;
import cdv.hdp.protocol.RecordTag;

import java.io.IOException;

/**
 * High-level heap dump parser
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:03
 */
public class HeapDumpParser extends BaseParser {

    public HeapDumpParser(byte[] bytes) {
        super(0, bytes);
    }

    public HeapSummaryReport readHeap() throws IOException {

        HeapDumpHeaderParser headerParser = new HeapDumpHeaderParser(0, data);
        headerParser.parse();
        offset = headerParser.offset;

        HeapSummaryReport report = new HeapSummaryReport()
                .withFormat(headerParser.getFormat())
                .withTimestamp(headerParser.getTimeStamp());

        while (++offset < data.length) {

            RecordTag tag = RecordTag.find(data[offset]);

            // read time
            offset += U4_SIZE;

            int length = (int) readU4();

            if (tag == RecordTag.UTF_8_STRING) {
                StringRecordParser parser = new StringRecordParser(
                        offset,
                        data,
                        length,
                        headerParser.getIdentifierSize());
                parser.parse();
                report.addString(parser.getId(), parser.getString());
            }
            if (tag == RecordTag.LOAD_CLASS) {
                LoadClassRecordParser parser = new LoadClassRecordParser(
                        offset,
                        data,
                        headerParser.getIdentifierSize());
                parser.parse();
                report.addClass(parser.getClassId(), parser.getClassNameId());
            }
            if (tag == RecordTag.HEAP_DUMP || tag == RecordTag.HEAP_DUMP_SEGMENT) {
                HeapRecordParser parser = new HeapRecordParser(
                        offset + 1,
                        data,
                        offset + length,
                        headerParser.getIdentifierSize());
                parser.parse();
                report.addInstances(parser.getInstances());
            }
            offset += length;
        }
        return report;
    }

}
