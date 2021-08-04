# SimpleReturnExpectationCalculator
A program that estimates the return of a given index based on historical data of one selected market index by CAPM function.

The program assumes that the index follows market trend. Indices that do not follow the market data will lead to very large margin of error. Thus, it is important to carefully pre screen the input data. 

Error estimation will be added in a later version. 

You need an txt file as input, file name is [indicesE.txt].

Format for input is [rank index market].

You need to rank the indices from the newest date to the oldest date with the newest data on the first line.

Lines with one or both indices lack data need to be either deleted or populated with previous data point. 

Blank lines after data need to be removed.

The [indicesE.txt] needs to be at the same folder as [SimpleReturnExpectationCalculator.java] file.

OBS! Computer Resgion Format Setting is preferably to be [English (United States)] since under certain region setting computer would read "," instead of "." causing an InputMismatchException.

To run the Java program through Command Prompt under windows in gernal: https://www.baeldung.com/java-lang-unsupportedclassversion. Compile code under JDK 8 to ensure compatibility

An [Indices.txt] is provided for testing and debuging purpose.
