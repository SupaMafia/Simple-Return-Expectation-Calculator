# SimpleReturnExpectationCalculator
A program that estimates the return of a given index based on historical data of one selected market index by CAPM function

You need an txt file as input, file name is [indicesE.txt].

Format for input is [rank index market].

You need to rank the indices from the newest date to the oldest date with the newest data on the first line.

Blank lines after data need to be removed.

The [indicesE.txt] needs to be at the same folder as [SimpleReturnExpectationCalculator.java] file.

OBS! Computer Resgion Format Setting is preferably to be [English (United States)] since under certain region setting computer would read "," instead of "." causing an InputMismatchException.

To run the Java program through Command Prompt under windows in gernal: https://www.baeldung.com/java-lang-unsupportedclassversion. Compile code under JDK 8 to ensure compatibility
