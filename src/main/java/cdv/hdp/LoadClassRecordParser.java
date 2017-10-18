package cdv.hdp;

/**
 * Parser for load class record
 *
 * @author Dmitry Kulga
 *         16.10.2017 19:17
 */
class LoadClassRecordParser extends RecordParser {

    private long classId;
    private long classNameId;

    LoadClassRecordParser(int offset, byte[] data, int identifierSize) {
        super(offset, data, identifierSize);
    }

    long getClassId() {
        return classId;
    }

    long getClassNameId() {
        return classNameId;
    }

    void parse() {
        offset += U4_SIZE;
        classId = readIdentifier();
        offset += U4_SIZE;
        classNameId = readIdentifier();
    }

}
