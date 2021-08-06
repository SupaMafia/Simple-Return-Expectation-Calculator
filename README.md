# SimpleReturnExpectationCalculator
A program that estimates the expected return of a given index based on historical data of two selected market indices by CAPM function.

The program assumes that the index follows market trend. Indices that do not follow the market data will lead to very large margin of error. Thus, it is important to carefully pre screen the input data. 

You need an txt file as input, file name is [indicesE.txt].

Format for input is [rank index predictor1 predictor2].

You need to rank the indices from the newest date to the oldest date with the newest data on the first line.

Lines with one or both indices lack data need to be either deleted or populated with previous data point. 

Blank lines after data need to be removed.

The [indicesE.txt] needs to be at the same folder as [SimpleReturnExpectationCalculator.java] file.

Treasury bill rate is not entered in percent.

OBS! Computer Resgion Format Setting is preferably to be [English (United States)] since under certain region setting computer would read "," instead of "." causing an InputMismatchException.

To run the Java program through Command Prompt under windows in gernal: https://www.baeldung.com/java-lang-unsupportedclassversion. Compile code under JDK 8 to ensure compatibility

An [IndicesE.txt] is provided for testing purposes.

Progam now uses Multi-Beta Captial Asset Pricing Model which improves prediction (Dogukanli, H. and Kandir S. Y., 2002). 

Reference: https://borsaistanbul.com/datum/imkbdergi/EN/ISE_Review_23.pdf
