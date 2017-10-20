package cdv.hdp.report;

/**
 * Class name and corresponding instances count for execution report
 *
 * @author Dmitry Kulga
 *         20.10.2017 21:34
 */
class ClassInstancesSummary implements Comparable<ClassInstancesSummary> {

    private final String className;
    private final long instancesCount;

    ClassInstancesSummary(String className, long instancesCount) {
        this.className = className;
        this.instancesCount = instancesCount;
    }

    @Override
    public int compareTo(ClassInstancesSummary other) {
        long difference = instancesCount - other.instancesCount;
        return difference > 0 ? 1 : (difference < 0 ? -1 : 0);
    }

    String format(int instancesCountLength) {
        String instancesCountString = Long.toString(instancesCount);
        int padding = instancesCountLength - instancesCountString.length();
        if (padding > 0) {
            StringBuilder pad = new StringBuilder();
            for (int index = 0; index < padding; index++) {
                pad.append(' ');
            }
            instancesCountString = pad + instancesCountString;
        }
        return instancesCountString + " : " + getPrettifiedClassName();
    }

    private String getPrettifiedClassName() {
        String prettified = className.replace('/', '.');
        return prettified.startsWith("[L") ?
                prettified.substring(2, prettified.length() - 1) + "[]" :
                prettified;
    }

}
