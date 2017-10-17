package cdv.hdp;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 *         16.10.2017 21:28
 */
class SectionParser extends BaseParser {

    final int identifierSize;

    SectionParser(int offset, byte[] data, int identifierSize) {
        super(offset, data);
        this.identifierSize = identifierSize;
    }

    long readIdentifier() {
        switch (identifierSize) {
            case U4_SIZE: return readU4();
            case U8_SIZE: return readU8();
            default: throw new IllegalArgumentException(
                    "Unsupported identifier size: " + identifierSize);
        }
    }

}
