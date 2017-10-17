package cdv.hdp;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 * 16.10.2017 19:17
 */
public class LoadClassSectionParser extends SectionParser {

    private long classId;
    private long classNameId;

    public LoadClassSectionParser(int offset, byte[] data, int identifierSize) {
        super(offset, data, identifierSize);
    }

    public long getClassId() {
        return classId;
    }

    public long getClassNameId() {
        return classNameId;
    }

    public void parse() {
        offset += U4_SIZE;
        classId = readIdentifier();
        offset += U4_SIZE;
        classNameId = readIdentifier();
    }

}
