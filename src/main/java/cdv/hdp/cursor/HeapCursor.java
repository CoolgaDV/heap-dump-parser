package cdv.hdp.cursor;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cursor that processes heap dump file by dividing it on chunks.
 * Each chunk is processed separately by {@linkplain ChunkCursor}.
 *
 * @author Dmitry Kulga
 * 19.10.2017 09:17
 */
public class HeapCursor implements AutoCloseable {

    private static final int READ_BUFFER_SIZE = 32 * 1024;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final int chunkSizeLimit;
    private final Path heapLocation;
    private FileInputStream stream;
    private boolean noMoreChunks = false;

    private long readTimeMillis = 0;
    private long totalBytes;
    private long currentBytes = 0;

    public HeapCursor(int chunkSizeLimit, Path heapLocation) {
        this.chunkSizeLimit = chunkSizeLimit;
        this.heapLocation = heapLocation;
    }

    public ChunkCursor init() throws IOException {
        stream = new FileInputStream(heapLocation.toFile());
        totalBytes = Files.size(heapLocation);

        System.out.println();
        System.out.println("=== Execution process ===");

        return new ChunkCursor(this, readNextChunk());
    }

    public long getReadTimeMillis() {
        return readTimeMillis;
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }

    byte[] readNextChunk() throws IOException {

        long startTimeMillis = System.currentTimeMillis();

        byte[] buffer = new byte[READ_BUFFER_SIZE];
        ByteArrayOutputStream chunkBuffer = new ByteArrayOutputStream(chunkSizeLimit);
        int totalRead = 0;
        int currentRead;
        while (totalRead < chunkSizeLimit - READ_BUFFER_SIZE) {
            currentRead = stream.read(buffer);
            if (currentRead == -1) {
                noMoreChunks = true;
                break;
            }
            totalRead += currentRead;
            chunkBuffer.write(buffer, 0, currentRead);
        }
        byte[] chunk = chunkBuffer.toByteArray();

        currentBytes += chunk.length;
        readTimeMillis += System.currentTimeMillis() - startTimeMillis;
        System.out.println(" [" + dateFormat.format(new Date()) + "] Processed " +
                toMegabytes(currentBytes) + " of " + toMegabytes(totalBytes) + " Mb");

        return chunk;
    }

    boolean isNoMoreChunks() {
        return noMoreChunks;
    }

    private long toMegabytes(long bytes) {
        return bytes / (1024 * 1024);
    }

}
