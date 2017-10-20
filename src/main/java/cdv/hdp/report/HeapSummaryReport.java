package cdv.hdp.report;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private final Map<String, Long> primitiveArrayInstances = new HashMap<>();

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

    public HeapSummaryReport withReadTimeMillis(long readTimeMillis) {
        this.readTimeMillis = readTimeMillis;
        return this;
    }

    public HeapSummaryReport withTotalTimeMillis(long overallTimeMillis) {
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

    public void addPrimitiveArrayInstances(Map<String, Long> newInstances) {
        for (Map.Entry<String, Long> arrayInstances : newInstances.entrySet()) {
            String className = arrayInstances.getKey();
            long instancesCount = arrayInstances.getValue();
            Long currentCount = primitiveArrayInstances.get(className);
            primitiveArrayInstances.put(className, currentCount == null ?
                    instancesCount :
                    instancesCount + currentCount);
        }
    }

    public void toConsole(long instancesCountThreshold) {

        List<String> instancesSummary = prepareClassInstances(instancesCountThreshold);

        System.out.println();
        System.out.println("=== Heap summary ===");
        System.out.println(" Format:    " + format);
        System.out.println(" Timestamp: " + timestamp);
        if (instancesSummary.isEmpty()) {
            System.out.println(" Instances: There are no instances above threshold");
        } else {
            System.out.println(" Instances: ");
            for (String classInstancesSummary : instancesSummary) {
                System.out.println("   " + classInstancesSummary);
            }
        }
        System.out.println();
        System.out.println("=== Execution time ===");
        System.out.println(" Read time:       " + readTimeMillis + " ms");
        System.out.println(" Processing time: " + (totalTimeMillis - readTimeMillis) + " ms");
        System.out.println(" Total time:      " + totalTimeMillis + " ms");
    }

    private List<String> prepareClassInstances(long instancesCountThreshold) {

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
        for (Map.Entry<String, Long> entry : primitiveArrayInstances.entrySet()) {
            String className = entry.getKey();
            long instancesCount = entry.getValue();
            if (instancesCount <= instancesCountThreshold) {
                continue;
            }
            int instancesCountLength = Long.toString(instancesCount).length();
            if (maxInstancesCountLength < instancesCountLength) {
                maxInstancesCountLength = instancesCountLength;
            }
            instancesSummary.add(new ClassInstancesSummary(className, instancesCount));
        }
        Collections.sort(instancesSummary);
        Collections.reverse(instancesSummary);

        int maxLength = maxInstancesCountLength;
        return instancesSummary
                .stream()
                .map(instances -> instances.format(maxLength))
                .collect(Collectors.toList());
    }

}
