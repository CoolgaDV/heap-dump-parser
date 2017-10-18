package cdv.hdp;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO: write comments here
 *
 * @author Dmitry Kulga
 * 18.10.2017 09:13
 */
public class HeapSummaryReport {

    private final Map<Long, String> strings = new HashMap<>();
    private final Map<Long, Long> classes = new HashMap<>();
    private final Map<Long, Long> instances = new HashMap<>();

    private String format;
    private String timestamp;

    HeapSummaryReport withFormat(String format) {
        this.format = format;
        return this;
    }

    HeapSummaryReport withTimestamp(long timestamp) {
        this.timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(timestamp));
        return this;
    }

    void addString(long id, String value) {
        strings.put(id, value);
    }

    void addClass(long classId, long classNameId) {
        classes.put(classId, classNameId);
    }

    void addInstances(Map<Long, Long> newInstances) {
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