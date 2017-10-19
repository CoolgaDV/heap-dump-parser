package cdv.hdp;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Report to be displayed as application execution result
 *
 * @author Dmitry Kulga
 *         18.10.2017 09:13
 */
public class HeapSummaryReport {

    private final Map<Long, String> strings = new HashMap<>();
    private final Map<Long, Long> classes = new HashMap<>();
    private final Map<Long, Long> instances = new HashMap<>();

    private String format;
    private String timestamp;
    private long readTimeMillis;
    private long totalTimeMillis;

    public HeapSummaryReport withFormat(String format) {
        this.format = format;
        return this;
    }

    public HeapSummaryReport withTimestamp(long timestamp) {
        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(timestamp));
        return this;
    }

    HeapSummaryReport withReadTimeMillis(long readTimeMillis) {
        this.readTimeMillis = readTimeMillis;
        return this;
    }

    HeapSummaryReport withTotalTimeMillis(long overallTimeMillis) {
        this.totalTimeMillis = overallTimeMillis;
        return this;
    }

    public void addString(long id, String value) {
        strings.put(id, value);
    }

    public void addClass(long classId, long classNameId) {
        classes.put(classId, classNameId);
    }

    public void addInstances(Map<Long, Long> newInstances) {
        for (Map.Entry<Long, Long> classInstances : newInstances.entrySet()) {
            long classId = classInstances.getKey();
            long instancesCount = classInstances.getValue();
            Long currentCount = instances.get(classId);
            instances.put(classId, currentCount == null ?
                    instancesCount :
                    instancesCount + currentCount);
        }
    }

    void toConsole(long instancesCountThreshold) {

        List<ClassInstancesSummary> instancesSummary = new ArrayList<>();
        int maxInstancesCountLength = 0;
        for (Map.Entry<Long, Long> entry : instances.entrySet()) {
            long instancesCount = entry.getValue();
            if (instancesCount <= instancesCountThreshold) {
                continue;
            }
            int instancesCountLength = Long.toString(instancesCount).length();
            if (maxInstancesCountLength < instancesCountLength) {
                maxInstancesCountLength = instancesCountLength;
            }
            String className = strings.get(classes.get(entry.getKey()));
            instancesSummary.add(new ClassInstancesSummary(className, instancesCount));
        }
        Collections.sort(instancesSummary);
        Collections.reverse(instancesSummary);

        System.out.println();
        System.out.println("=== Heap summary ===");
        System.out.println(" Format:    " + format);
        System.out.println(" Timestamp: " + timestamp);
        if (instancesSummary.isEmpty()) {
            System.out.println(" Instances: There are no instances above threshold");
        } else {
            System.out.println(" Instances: ");
            for (ClassInstancesSummary classInstancesSummary : instancesSummary) {
                System.out.println("   " + classInstancesSummary.format(maxInstancesCountLength));
            }
        }
        System.out.println();
        System.out.println("=== Execution time ===");
        System.out.println(" Read time:       " + readTimeMillis + " ms");
        System.out.println(" Processing time: " + (totalTimeMillis - readTimeMillis) + " ms");
        System.out.println(" Total time:      " + totalTimeMillis + " ms");
    }

    private static class ClassInstancesSummary implements Comparable<ClassInstancesSummary> {

        private final String className;
        private final long instancesCount;

        private ClassInstancesSummary(String className, long instancesCount) {
            this.className = className;
            this.instancesCount = instancesCount;
        }

        @Override
        public int compareTo(ClassInstancesSummary other) {
            long difference = instancesCount - other.instancesCount;
            return difference > 0 ? 1 : (difference < 0 ? -1 : 0);
        }

        private String format(int instancesCountLength) {
            String instancesCountString = Long.toString(instancesCount);
            int padding = instancesCountLength - instancesCountString.length();
            if (padding > 0) {
                StringBuilder pad = new StringBuilder();
                for (int index = 0; index < padding; index++) {
                    pad.append(' ');
                }
                instancesCountString = pad + instancesCountString;
            }
            return instancesCountString + " : " + className;
        }

    }

}
