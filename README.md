# Bioliner

Bioliner is a command line application used to facilitate a 'pipeline' of bioinformatics jobs. Each pipeline has a list of modules to execute sequentially.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

```
Java 8+
Maven 3.5+
```

### Installing

Ensure Java 8 is installed:

```
java -version
```

Ensure Maven is installed:

```
mvn -version
```

```
git clone https://github.com/omicsdatalab/bioliner.git
cd bioliner
mvn clean install
```

## Running the tests

You can run tests for the system by:
```
mvn test
```

## Built With

* [Java8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details
