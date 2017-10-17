package cdv.hdp;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 * 16.10.2017 19:17
 */
class LoadClassSectionParser extends SectionParser {

    private long classId;
    private long classNameId;

    LoadClassSectionParser(int offset, byte[] data, int identifierSize) {
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
