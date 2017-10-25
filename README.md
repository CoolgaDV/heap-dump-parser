heap-dump-parser
================

This project is a console heap dump analyzer that can create class diagrams 
(ordered list of class name - instance count pairs).

## Command line arguments

| Name                      | Mandatory | Description                                            |
| :---:                     | :---:     | :---:                                                  |
| heap.location             | true      | Heap dump file location ( absolute or relative )       |
| instances.count.threshold | false     | Classes which instances count is below or equal 
                                          this value will not be displayed in application output |
| chunk.size                | true      | Heap dumps can be too large to load the whole file 
                                          in memory for processing. So processing is performed 
                                          by chunks and this parameter defines the size 
                                          for each of these chunks in megabytes                  |

## Example

```
java -jar \
     -Dheap.location=heap.hprof \
     -Dinstances.count.threshold=1000 \
     -Dchunk.size=64 \
     heap-dump-parser.jar
```