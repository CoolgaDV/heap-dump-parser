package cdv.hdp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:03
 */
public class HeapDumpParser extends BaseParser {

    public static void main(String[] args) throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
        new HeapDumpParser(bytes).readHeap();
    }

    private HeapDumpParser(byte[] bytes) {
        super(0, bytes);
    }

    private void readHeap() throws IOException {

        HeapDumpHeaderParser headerParser = new HeapDumpHeaderParser(0, data);
        headerParser.parse();
        offset = headerParser.offset;

        Map<Long, String> strings = new HashMap<>();
        Map<Long, Long> classes = new HashMap<>();
        Map<Long, Long> instances = new HashMap<>();

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
                strings.put(parser.getId(), parser.getString());
            }
            if (tag == RecordTag.LOAD_CLASS) {
                LoadClassSectionParser parser = new LoadClassSectionParser(
                        offset,
                        data,
                        headerParser.getIdentifierSize());
                parser.parse();
                classes.put(parser.getClassId(), parser.getClassNameId());
            }
            if (tag == RecordTag.HEAP_DUMP || tag == RecordTag.HEAP_DUMP_SEGMENT) {
                HeapSectionParser parser = new HeapSectionParser(
                        offset + 1,
                        data,
                        offset + length,
                        headerParser.getIdentifierSize());
                parser.parse();
                instances.putAll(parser.getInstances());
            }
            offset += length;
        }

        for (Map.Entry<Long, Long> entry : instances.entrySet()) {
            long count = entry.getValue();
            String key = strings.get(classes.get(entry.getKey()));
            System.out.println(key + " :: " + count);
        }

        System.out.println("Format:    " + headerParser.getFormat());
        System.out.println("ID size:   " + headerParser.getIdentifierSize());
        System.out.println("Timestamp: " + new Date(headerParser.getTimeStamp()));
    }

}
