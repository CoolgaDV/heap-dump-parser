heap-dump-parser
================

This project is a console heap dump analyzer that can create class diagrams 
(ordered list of class name - instance count pairs).

## Command line arguments

<table>
    <tr>
        <th>Name</th>
        <th>Mandatory</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>heap.location</td>
        <td>true</td>
        <td>
            Heap dump file location ( absolute or relative )
        </td>
    </tr>
    <tr>
        <td>instances.count.threshold</td>
        <td>false</td>
        <td>
            Classes which instances count is below or equal
            this value will not be displayed in application output
        </td>
    </tr>
    <tr>
        <td>chunk.size</td>
        <td>true</td>
        <td>
            Heap dumps can be too large to load the whole file
            in memory for processing. So processing is performed
            by chunks and this parameter defines the size
            for each of these chunks in megabytes
        </td>
    </tr>
</table>

## Example

```
java -jar \
     -Dheap.location=heap.hprof \
     -Dinstances.count.threshold=1000 \
     -Dchunk.size=64 \
     heap-dump-parser.jar
```
