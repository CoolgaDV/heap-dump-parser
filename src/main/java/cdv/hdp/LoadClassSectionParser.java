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
        offset += 4;
        classId = readIdentifier();
        offset += 4;
        classNameId = readIdentifier();
    }

}
