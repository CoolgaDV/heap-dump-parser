package cdv.hdp;

import java.nio.charset.StandardCharsets;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 * 16.10.2017 19:17
 */
public class StringSectionParser extends SectionParser {

    private final int length;

    private long id;
    private String string;

    public StringSectionParser(int offset, byte[] data, int length, int identifierSize) {
        super(offset, data, identifierSize);
        this.length = length;
    }

    public long getId() {
        return id;
    }

    public String getString() {
        return string;
    }

    public void parse() {
        string = new String(
                data,
                offset + identifierSize + 1,
                length - identifierSize,
                StandardCharsets.UTF_8);
        id = readIdentifier();
    }

}
