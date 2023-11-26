# CafeBase64

[![CodeQL](https://github.com/jh3nd3rs0n/cafebase64/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/cafebase64/actions/workflows/codeql-analysis.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/022a9623cb904e33bce041079b879329)](https://app.codacy.com/gh/jh3nd3rs0n/cafebase64/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

## Contents

-   [Introduction](#introduction)
-   [License](#license)
-   [Requirements](#requirements)
-   [Automated Testing](#automated-testing)
-   [Building](#building)
-   [Running CafeBase64](#running-cafebase64)
-   [Usage](#usage)

## Introduction

CafeBase64 is a Java implementation of GNU's command line utility base64 and an 
example of using ArgMatey.

## License

CafeBase64 is licensed under the 
[MIT license](https://github.com/jh3nd3rs0n/cafebase64/blob/master/LICENSE)

## Requirements

For automated testing and building:

-   Apache Maven 3.3.9 or higher
-   Java 8 or higher

For running CafeBase64

-   Java 8 or higher

## Automated Testing

To run automated testing, run the following commands:

```bash
cd cafebase64
mvn clean test
```

## Building

To build and package CafeBase64 as an executable JAR file, run the following 
command:

```bash
mvn clean package
```

After running the aforementioned command, the executable JAR file can be found 
in the following path:

```text
target/cafebase64-VERSION.jar
```

`VERSION` is further specified by the name of the actual executable JAR file.

## Running CafeBase64

To run CafeBase64, you can run the following command:

```bash
java -jar cafebase64-VERSION.jar [OPTION]... [FILE]
```

Be sure to remove or replace the following:

-   Replace `VERSION` with the actual version shown within the name of the 
executable JAR file.
-   Remove `[OPTION]...` or replace `[OPTION]...` with one or more of the 
command line options described in the usage below.
-   Remove `[FILE]` or replace `[FILE]` with the file you would like to be 
transformed to standard output. (Removing `[FILE]` will cause CafeBase64 to 
use standard input as input instead of a file.)

## Usage

```text
Usage: cafebase64 [OPTION]... [FILE]
Base64 encode or decode FILE, or standard input, to standard output.

With no FILE, or when FILE is -, read standard input.
	
OPTIONS:
  -d, --decode
	  decode data
  -i, --ignore-garbage
	  when decoding, ignore non-alphabet characters
  -w COLS, --wrap=COLS
	  wrap encoded lines after COLS character (default 76).
	  Use 0 to disable line wrapping
  --help
	  display this help and exit
  --version
	  display version information and exit

```
