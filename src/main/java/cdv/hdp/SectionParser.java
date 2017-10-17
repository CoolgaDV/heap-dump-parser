package cdv.hdp;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 21:28
 */
public class SectionParser extends BaseParser {

    final int identifierSize;

    public SectionParser(int offset, byte[] data, int identifierSize) {
        super(offset, data);
        this.identifierSize = identifierSize;
    }

    long readIdentifier() {
        switch (identifierSize) {
            case 4: return readInt();
            case 8: return readLong();
            default: throw new IllegalArgumentException(
                    "Unsupported identifier size: " + identifierSize);
        }
    }

}
