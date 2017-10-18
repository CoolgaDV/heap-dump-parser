package cdv.hdp;

import java.io.IOException;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:03
 */
class HeapDumpParser extends BaseParser {

    HeapDumpParser(byte[] bytes) {
        super(0, bytes);
    }

    HeapSummaryReport readHeap() throws IOException {

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
                StringSectionParser parser = new StringSectionParser(
                        offset,
                        data,
                        length,
                        headerParser.getIdentifierSize());
                parser.parse();
                report.addString(parser.getId(), parser.getString());
            }
            if (tag == RecordTag.LOAD_CLASS) {
                LoadClassSectionParser parser = new LoadClassSectionParser(
                        offset,
                        data,
                        headerParser.getIdentifierSize());
                parser.parse();
                report.addClass(parser.getClassId(), parser.getClassNameId());
            }
            if (tag == RecordTag.HEAP_DUMP || tag == RecordTag.HEAP_DUMP_SEGMENT) {
                HeapSectionParser parser = new HeapSectionParser(
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
