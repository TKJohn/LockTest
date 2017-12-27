mvn clean package
java -jar benchmark.jar

Result:

|Benchmark                                        |Mode  |Cnt  |   Score |    Error  |Units|
|-------------------------------------------------|------|-----|---------|-----------|-----|
|AddDecLockTestMain.doubleSynchronizedLockerTest  |avgt  | 10  |1851.551 |±  19.539  |ms/op|
|AddDecLockTestMain.reentrantLockLocker           |avgt  | 10  |4534.705 |± 173.604  |ms/op|
|AddDecLockTestMain.singleSynchronizedLockerTest  |avgt  | 10  |6149.102 |± 342.512  |ms/op|



Environment:

* Intel Core i7-3630QM
* 8GB DDR3

* Windows 10
* JMH version: 1.19
* VM version: JDK 1.8.0_92, VM 25.92-b14