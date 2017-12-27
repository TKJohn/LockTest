mvn clean package
java -jar benchmark.jar

Result:

|Benchmark                                        |Mode  |Cnt  |   Score |    Error  |Units|
|-------------------------------------------------|------|-----|---------|-----------|-----|
|AddDecLockTestMain.doubleSynchronizedLockerTest  |avgt  | 10  |1897.903 |±  34.252  |ms/op|
|AddDecLockTestMain.reentrantLockLocker           |avgt  | 10  |5409.243 |± 310.933  |ms/op|
|AddDecLockTestMain.singleSynchronizedLockerTest  |avgt  | 10  |6818.001 |± 649.110  |ms/op|


Environment:

* Intel Core i7-3630QM
* 8GB DDR3

* Windows 10
* JMH version: 1.19
* VM version: JDK 1.8.0_92, VM 25.92-b14