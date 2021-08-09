# SimpleReturnExpectationCalculator

How to use: 

- A program that estimates the expected return of a given index based on historical data of two selected market indices by CAPM function.
- The program assumes that the index follows market trend. Indices that do not follow the market data will lead to very large margin of error. Thus, it is important to carefully pre screen the input data. 
- You need an txt file as input, file name is [indicesE.txt].
- Format for input is [rank index predictor1 predictor2].
- You need to rank the indices from the newest date to the oldest date with the newest data on the first line.
- Lines with one or both indices lack data need to be either deleted or populated with previous data point. 
- Blank lines after data need to be removed.
- The [indicesE.txt] needs to be at the same folder as [SimpleReturnExpectationCalculator.java] file.
- Treasury bill rate is not entered in percent.
- OBS! Computer Resgion Format Setting is preferably to be [English (United States)] since under certain region setting computer would read "," instead of "." causing an InputMismatchException.
- To run the Java program through Command Prompt under windows in gernal: https://www.baeldung.com/java-lang-unsupportedclassversion. Compile code under JDK 8 to ensure compatibility
- An [IndicesE.txt] is provided for testing purposes.

Rationale:

- The project is created with simplicity and ease of use in mind. So that it can have a wide audience. Thus, minimum input is required from the user, and the program only outputs key information.

Introduction:

- Documentation is created to explain the codes and outputs. To reach the goal of making this tool freely available to as many people as possible, it is important to provide an interpretation of some of the results. Hence, not only finance professionals can have key insides to the data, traders with a variety of backgrounds can make informed decisions based on results. Underlying theory behind the program and interpretation of the result will be discussed. 

Underlying theory:

- The underlying theory of SimpleExpectedReturnCalculator is the Capital Asset Pricing Model. The model takes three inputs, asset’s sensitivity to systematic risk, expected return of the market, and expected return of a risk-free asset, to calculate the expected return of an asset. It is chosen over modern models such as arbitrage pricing theory or zero-beta CAPM due to its simplicity and flexibility.

Interpretation of the results

- Asset’s sensitivity is denoted as β in the program.  β value by itself does not have any meaning, since there could be a drastic price difference between asset price and market indices. Hence, r2 and r2adj (since only two indices are allowed, the two values should be close) are calculated. It has a typical value between 0 to 1. The closer the r2 value is to one, the closer the index matches the given market trend. It can be read as % of asset value variation is explained by market indices. Thus, if there is a surge in market return, the program would predict a similar change to the assets return. Conversely, a r2 close to 0 suggests the asset price cannot be explained by market data. It could suggest the systemic risk inherent to the market does not carry over to the asset. But it also makes the output return result unreliable. In the case, the r2 value is low, a new model should be considered by using a selection of markets that is more related to the asset. Fstat is a statistical variable for F test, which gives the significance of the selected model. It can be interpreted as repeating the test 100 times 90/95/99 times the result would be similar. It needs to be calculated separately, but the function is to be added to the program.

- The expected return of the market is another input required for CAPM. Usually, it took the historical performance of the given indices. However, it gives rise to a common criticism of the model that the past does not necessarily predict the future.  

- Unlike the index model, CAPM ignores α values in its calculation, as the empirical evidence suggests the α value should be zero for a fairly priced asset. However, it is not usually the case. Given that the model is significant, large α values should be a cause of concern, it suggests the asset is overvalued given the market condition, and a negative α value can be a sign of arbitrage opportunity. As a result, even though CAPM does not need α to calculate the program still prints out α.

