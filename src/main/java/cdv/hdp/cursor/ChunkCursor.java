package cdv.hdp.cursor;

import java.io.IOException;

/**
 * Cursor that work with one heap chunk at once and
 * acquires next chunk when current chunk is processed
 *
 * @author Dmitry Kulga
 *         19.10.2017 09:46
 */
public class ChunkCursor {

    public static final int U1_SIZE = 1;
    public static final int U2_SIZE = 2;
    public static final int U4_SIZE = 4;
    public static final int U8_SIZE = 8;
    
    private final HeapCursor parent;
    
    private byte[] currentChunk;
    private int offset;

    ChunkCursor(HeapCursor parent, byte[] initialChunk) {
        this.parent = parent;
        currentChunk = initialChunk;
        offset = 0;
    }

    public long readU4() {
        return ((readNextByte() & 0xFF) << 24) |
                ((readNextByte() & 0xFF) << 16) |
                ((readNextByte() & 0xFF) << 8) |
                (readNextByte() & 0xFF);

    }

    public int readU2() {
        return ((readNextByte() & 0xFF) << 8) | (readNextByte() & 0xFF);
    }

    public int readU1() {
        return (readNextByte() & 0xFF);
    }

    public long readU8() {
        long upper = readU4();
        long lower = readU4();
        return upper << 32 | (lower & 0xFFFFFFFFL);
    }

    public void skipBytes(int bytes) {
        for (int index = 0; index < bytes; index++) {
            readNextChunkIfNecessary();
            offset++;
        }
    }

    public boolean hasMoreBytes() {
        return ! (parent.isNoMoreChunks() && offset == currentChunk.length);
    }

    private byte readNextByte() {
        readNextChunkIfNecessary();
        return currentChunk[offset++];
    }

    private void readNextChunkIfNecessary() {
        if (offset >= currentChunk.length) {
            try {
                currentChunk = parent.readNextChunk();
            } catch (IOException ex) {
                throw new RuntimeException("Error while reading heap dump data", ex);
            }
            offset = 0;
        }
    }

}
