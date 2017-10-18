package cdv.hdp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Application entry point
 *
 * @author Dmitry Kulga
 *         18.10.2017 08:48
 */
public class Main {

    private static final String HEAP_LOCATION_PROPERTY = "heap.location";
    private static final String INSTANCES_COUNT_THRESHOLD_PROPERTY = "instances.count.threshold";

    public static void main(String[] args) throws Exception {

        Path heapLocation = getHeapLocation();
        long instancesCountThreshold = getInstanceCountThreshold();

        byte[] bytes = Files.readAllBytes(heapLocation);
        HeapSummaryReport report = new HeapDumpParser(bytes).readHeap();

        report.toConsole(instancesCountThreshold);
    }

    private static Path getHeapLocation() {
        String heapLocationProperty = System.getProperty(HEAP_LOCATION_PROPERTY);
        if (heapLocationProperty == null) {
            throw new IllegalArgumentException("Heap location is not specified. " +
                    "To do that specify '" + HEAP_LOCATION_PROPERTY + "' " +
                    "property using '-D' Java command line option.");
        }
        Path heapLocation = Paths.get(heapLocationProperty);
        if ( ! Files.exists(heapLocation)) {
            throw new IllegalArgumentException(
                    "Heap location " + heapLocationProperty + " does not exists.");
        }
        System.out.println("Using heap location: " + heapLocation.toAbsolutePath()+ ".");
        return heapLocation;
    }

    private static long getInstanceCountThreshold() {
        String thresholdProperty = System.getProperty(INSTANCES_COUNT_THRESHOLD_PROPERTY);
        if (thresholdProperty == null) {
            System.out.println("Instances count threshold is not specified.");
            return 0;
        }
        long threshold;
        try {
            threshold = Long.parseLong(thresholdProperty);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Instances count threshold parsing failure.");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException(
                    "Instances count threshold should not be a negative number.");
        }
        System.out.println("Using instances count threshold: " + threshold + ".");
        return threshold;
    }

}
