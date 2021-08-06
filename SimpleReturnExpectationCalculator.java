/*
 * Date: 2021-8-5.
 * File Name: SimpleReturnExpectationCalculator.java
 * Version: 0.4
 * Author: Weikang Ke
 */

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * A program that estimates the return of a given index based on historical data of one selected market index by CAPM function
 */

public class SimpleReturnExpectationCalculator {
    public static void main(String[] args) {
        //input indices, and selected market performance
        String filename0 = "IndicesE.txt";
        Scanner inputStream0 = null;
        try {
            inputStream0 = new Scanner(new FileInputStream(filename0));
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename0 + " was not found, or could not be opened");
            System.exit(0);
        }

        //read total number of dates for indices
        int dateNum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename0))) {
            while (reader.readLine() != null) dateNum++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total number of date is: [" + dateNum + "]");

        // write into array
        double[][] indexPredict = new double[3][dateNum];
        for (int i = 0; i < dateNum; i++) {
            double date = inputStream0.nextDouble();
            indexPredict[0][i] = inputStream0.nextDouble();
            indexPredict[1][i] = inputStream0.nextDouble();
            indexPredict[2][i] = inputStream0.nextDouble();
            String line = inputStream0.nextLine();
        }
        inputStream0.close();
        System.out.println("_________________________________________________________________________");
        System.out.println("[Data Table]");
        for (int i = 0; i < dateNum; i++) {
            System.out.println("Line: " + i + " index: " + indexPredict[0][i] + " predictor1: " + indexPredict[1][i] + " predictor2: " + indexPredict[2][i]);
        }
        System.out.println("_________________________________________________________________________");


        //enter the amount of predictor and run the respective method
        System.out.println("how many predictor is provided? 1 or 2");
        int predictorNum = 1;
        int count0 = 0;
        do {
            try {
                Scanner in0 = new Scanner(System.in);
                predictorNum = in0.nextInt();
                break;
            } catch (InputMismatchException e0) {
                System.out.println("Non-numeric values are not allowed, please re enter");
            } finally {
                count0++;
                if (count0 == 10) {
                    System.out.println("Too many misinputs");
                    System.exit(0);
                }
            }
        } while (count0 > 0);

        if (predictorNum == 1) {
            methodSLR(dateNum, indexPredict);
        }
        if (predictorNum == 2) {
            methodMLR(dateNum, indexPredict);
        }
    }

    //create risk free rate object
    public static class RiskFreeRates {
        //data members
        private static double riskFreeRate60;
        private static double riskFreeRate120;
        private static double riskFreeRate240;

        // constructors
        public RiskFreeRates(double rf60, double rf120, double rf240) {
            riskFreeRate60 = rf60;
            riskFreeRate120 = rf120;
            riskFreeRate240 = rf240;
        }

        public RiskFreeRates() {
            riskFreeRate60 = 0;
            riskFreeRate120 = 0;
            riskFreeRate240 = 0;
        }

        //getter
        public static double getRiskFreeRate60() {
            return riskFreeRate60;
        }

        public static double getRiskFreeRate120() {
            return riskFreeRate120;
        }

        public static double getRiskFreeRate240() {
            return riskFreeRate240;
        }

        //setter
        public static void setRiskFreeRate60(double rf60) {
            riskFreeRate60 = rf60;
        }

        public static void setRiskFreeRate120(double rf120) {
            riskFreeRate120 = rf120;
        }

        public static void setRiskFreeRate240(double rf240) {
            riskFreeRate240 = rf240;
        }

        //method
        public static void setRiskFreeRates(int n) {
            int dateNum = n;
            //check data size
            if (dateNum < 60) {
                System.out.println("Data samples are too small to reach a conclusive estimation");
            }

            //take inputs
            System.out.println("The return of a three-month government treasury bill is ___ ");
            double riskFreeRate60 = 0; //Rf60
            int count0 = 0;
            do {
                try {
                    Scanner in0 = new Scanner(System.in);
                    riskFreeRate60 = in0.nextDouble();
                    break;
                } catch (InputMismatchException e0) {
                    System.out.println("Non-numeric values are not allowed, please re enter");
                } finally {
                    count0++;
                    setRiskFreeRate60(riskFreeRate60);
                    if (count0 == 10) {
                        System.out.println("Too many misinputs");
                        System.exit(0);
                    }
                }
            } while (count0 > 0);
            double riskFreeRate120 = 0; //Rf120
            int count1 = 0;
            if (dateNum > 120) {
                System.out.println("The return of a six-month government treasury bill is ___ ");
                do {
                    try {
                        Scanner in0 = new Scanner(System.in);
                        riskFreeRate120 = in0.nextDouble();
                        break;
                    } catch (InputMismatchException e0) {
                        System.out.println("Non-numeric values are not allowed, please re enter");
                    } finally {
                        count1++;
                        setRiskFreeRate120(riskFreeRate120);
                        if (count1 == 10) {
                            System.out.println("Too many misinputs");
                            System.exit(0);
                        }
                    }
                } while (count1 > 0);
            }
            double riskFreeRate240 = 0; //Rf240
            int count2 = 0;
            if (dateNum > 120) {
                System.out.println("The return of a one year government treasury bill is ___% ");
                do {
                    try {
                        Scanner in0 = new Scanner(System.in);
                        riskFreeRate240 = in0.nextDouble();
                        break;
                    } catch (InputMismatchException e0) {
                        System.out.println("Non-numeric values are not allowed, please re enter");
                    } finally {
                        count2++;
                        setRiskFreeRate240(riskFreeRate240);
                        if (count2 == 10) {
                            System.out.println("Too many misinputs");
                            System.exit(0);
                        }
                    }
                } while (count2 > 0);
            }
        }

    }

    // Simple Linear method
    public static void methodSLR(int n, double[][] indexPredict) {
        int dateNum = n;
        double[][] indexMarket = new double[2][dateNum];
        for (int i = 0; i < dateNum; i++) {
            indexMarket[0][i] = indexPredict[0][i];
            indexMarket[1][i] = indexPredict[1][i];
        }

        //calculate index mean and market mean
        double sumIndex = 0;
        double sumMarket = 0;
        for (int i = 0; i < dateNum; i++) {
            sumIndex += indexMarket[0][i];
            sumMarket += indexMarket[1][i];
        }
        double indexMean = sumIndex / dateNum;  //index as ȳ since it is dependent on market
        double marketMean = sumMarket / dateNum; //market as x̄ since it is independent
        //System.out.println("Index Mean (ȳ) =" + indexMean + " Market Mean (x̄) =" + marketMean);

        //calculate covariance between index and market, assume population data
        double pod; //product of difference
        double sopod = 0; //sum of product of difference
        for (int i = 0; i < dateNum; i++) {
            double diffI = indexMarket[0][i] - indexMean;
            double diffM = indexMarket[1][i] - marketMean;
            pod = diffI * diffM;
            sopod += pod;
        }
        double covIM = sopod / dateNum; //n, not n-1, due to population data
        //System.out.println("Covariance between Index and market =" + covIM);

        //calculate Std.Dev() and Var() of index and market
        double stdDevI;
        double stdDevM;
        double varI;
        double varM;
        double sodI; //Square of difference
        double sodM;
        double sosodI = 0; //sum of square of difference
        double sosodM = 0;
        for (int i = 0; i < dateNum; i++) {
            double diffI = indexMarket[0][i] - indexMean;
            double diffM = indexMarket[1][i] - marketMean;
            sodI = Math.pow(diffI, 2);
            sosodI += sodI;
            sodM = Math.pow(diffM, 2);
            sosodM += sodM;
        }
        varI = sosodI / dateNum;
        varM = sosodM / dateNum;
        stdDevI = Math.sqrt(varI);
        stdDevM = Math.sqrt(varM);
        //System.out.println("Std.Dev. of index (y) =" + stdDevI + " Std.Dev. of market (x)=" + stdDevM);

        //calculate persons correlation coefficient between index and market
        double corIM = 0;
        corIM = covIM / (stdDevI * stdDevM);
        //System.out.println("Persons correlation coefficient between index and market =" + corIM);

        //β and α calculation, βi = cor(i,m)(σi/σm), α = ȳ-βx̄
        //double beta = corIM * (stdDevI / stdDevM);
        double beta = covIM / varM;
        double alpha = indexMean - beta * marketMean;

        //r square calculation, market explains r% of the data variation
        double[] sodfm = new double[dateNum]; //index, Square of Difference from Mean or (y- ȳ)^2
        for (int i = 0; i < dateNum; i++) {
            sodfm[i] = Math.pow((indexMarket[0][i] - indexMean), 2);
        }
        double[] regressEst = new double[dateNum]; //y hat, estimated value of y, index, from the regression function
        for (int i = 0; i < dateNum; i++) {
            regressEst[i] = alpha + beta * indexMarket[1][i]; //est index value = α + β * historical market index
        }
        double[] soefm = new double[dateNum]; //Square of estimate from mean, (y hat - ȳ)^2
        for (int i = 0; i < dateNum; i++) {
            soefm[i] = Math.pow((regressEst[i] - indexMean), 2);
        }
        double sosodfm = 0; //sum of square of difference from mean
        for (int i = 0; i < dateNum; i++) {
            sosodfm += sodfm[i];
        }
        double sosoefm = 0; //sum of square of estimate from mean
        for (int i = 0; i < dateNum; i++) {
            sosoefm += soefm[i];
        }
        double rSquare = sosoefm / sosodfm; // sosoefm / sosodfm
        System.out.println("______________________________________________________________________");
        System.out.println("  α= [" + String.format("%.3f", alpha) + "], β= [" + String.format("%.3f", beta) + "], r^2 (market index explains r^2 % of the data variation) = [" + String.format("%.3f", rSquare * 100) + "%].");

        //Std.Error of Est
        double[] soErr = new double[dateNum]; //square of abs.error
        for (int i = 1; i < dateNum; i++) {
            soErr[i] = Math.pow(Math.abs(regressEst[i] - indexMarket[0][i]), 2);
        }
        double sosoErr = 0; // sum of Square of error
        for (int i = 0; i < dateNum; i++) {
            sosoErr += soErr[i];
        }
        double stdErrEst = 0;
        stdErrEst = Math.sqrt(sosoErr / (dateNum - 2));
        System.out.println("  Std. Err. of Est. (average expected error for each index) = [" + String.format("%.3f", stdErrEst) + "].");

        //find market return for 60,120 and 240 days
        double marketD60; //market price 60 days ago
        double marketD0;
        double returnD60;
        marketD60 = indexMarket[1][59];
        marketD0 = indexMarket[1][0];
        returnD60 = (marketD0 - marketD60) / marketD60;
        System.out.println("______________________________________________________________________");
        System.out.println("  Market 3 month return = [" + String.format("%.3f", returnD60 * 100) + "%].");
        double marketD120; //market price 120 days ago
        double returnD120 = 0;
        if (dateNum >= 120) {
            marketD120 = indexMarket[1][119];
            returnD120 = (marketD0 - marketD120) / marketD120;
            System.out.println("  Market 6 month return = [" + String.format("%.3f", returnD120 * 100) + "%].");
        } else if (dateNum < 120) {
            System.out.println("Data samples are too small for half a year return");
        }
        double marketD240; //market price 240 days ago
        double returnD240 = 0;
        if (dateNum >= 240) {
            marketD240 = indexMarket[1][239];
            returnD240 = (marketD0 - marketD240) / marketD240;
            System.out.println("  Market 1 year return  = [" + String.format("%.3f", returnD240 * 100) + "%].");
        } else if (dateNum < 240) {
            System.out.println("Data samples are too small for half a year return");
        }
        System.out.println("______________________________________________________________________");

        //take data from riskfreerate objects
        RiskFreeRates.setRiskFreeRates(dateNum);
        double riskFreeRate60 = RiskFreeRates.getRiskFreeRate60();
        double riskFreeRate120 = RiskFreeRates.getRiskFreeRate120();
        double riskFreeRate240 = RiskFreeRates.getRiskFreeRate240();

        //return expectation calculation, E(ri) calculation, based on historical data
        double exp60;
        double exp120;
        double exp240;
        exp60 = riskFreeRate60 + beta * (returnD60 - riskFreeRate60);  //since rf is entered in % return should be in % as well.
        exp120 = riskFreeRate120 + beta * (returnD120 - riskFreeRate120);
        exp240 = riskFreeRate240 + beta * (returnD240 - riskFreeRate240);
        System.out.println("______________________________________________________________________");
        System.out.println("  [Result]: ");
        if (dateNum >= 60) {
            System.out.println("   In 3 month, given past market condition, the index should have a expected return = [" + String.format("%.3f", exp60 * 100) + "%].");
        }
        if (dateNum >= 120) {
            System.out.println("   In 6 month, given past market condition, the index should have a expected return = [" + String.format("%.3f", exp120 * 100) + "%].");
        }
        if (dateNum >= 240) {
            System.out.println("   In 1 year, given past market condition, the index should have a expected return = [" + String.format("%.3f", exp240 * 100) + "%].");
        }
        System.out.println("______________________________________________________________________");
    }

    //Multi-linear Method
    public static void methodMLR(int n, double[][] indexPredict) {
        int dateNum = n;
        double[][] yx1x2 = new double[3][dateNum];
        for (int i = 0; i < dateNum; i++) {
            yx1x2[0][i] = indexPredict[0][i];
            yx1x2[1][i] = indexPredict[1][i];
            yx1x2[2][i] = indexPredict[2][i];
        }

        //find y bar, x1bar, x2bar
        double sumY = 0, sumX1 = 0, sumX2 = 0;
        double meanY = 0, meanX1 = 0, meanX2 = 0;
        for (int i = 0; i < dateNum; i++) {
            sumY += yx1x2[0][i];
            sumX1 += yx1x2[1][i];
            sumX2 += yx1x2[2][i];
        }
        meanY = sumY / dateNum;
        meanX1 = sumX1 / dateNum;
        meanX2 = sumX2 / dateNum;

        //create X1Square, X2Square, X1Y, X2Y, X1X2
        double sumX1Square = 0;
        double sumX2Square = 0;
        double sumX1Y = 0;
        double sumX2Y = 0;
        double sumX1X2 = 0;
        for (int i = 0; i < dateNum; i++) {
            sumX1Square += Math.pow(yx1x2[1][i], 2);
            sumX2Square += Math.pow(yx1x2[2][i], 2);
            sumX1Y += (yx1x2[1][i] * yx1x2[0][i]);
            sumX2Y += (yx1x2[2][i] * yx1x2[0][i]);
            sumX1X2 += (yx1x2[1][i] * yx1x2[2][i]);
        }
        //System.out.println("means:" + meanY + "," + meanX1 + "," + meanX2);
        //System.out.println("sums:" + sumX1Square + "," + sumX2Square + "," + sumX1Y + "," + sumX2Y + "," + sumX1X2);

        // regSum calculation
        double regSumx1Square = 0;
        double regSumx2Square = 0;
        double regSumx1y = 0;
        double regSumx2y = 0;
        double regSumx1x2 = 0;
        regSumx1Square = sumX1Square - (Math.pow(sumX1, 2) / dateNum);
        regSumx2Square = sumX2Square - (Math.pow(sumX2, 2) / dateNum);
        regSumx1y = sumX1Y - ((sumX1 * sumY) / dateNum);
        regSumx2y = sumX2Y - ((sumX2 * sumY) / dateNum);
        regSumx1x2 = sumX1X2 - ((sumX1 * sumX2) / dateNum);
        //System.out.println("reSums:" + regSumx1Square + "," + regSumx2Square + "," + regSumx1y + "," + regSumx2y + "," + regSumx1x2);

        //β0, β1, β2
        double b0, b1, b2;
        double bBase = regSumx1Square * regSumx2Square - Math.pow(regSumx1x2, 2);
        b1 = (regSumx2Square * regSumx1y - regSumx1x2 * regSumx2y) / bBase;
        b2 = (regSumx1Square * regSumx2y - regSumx1x2 * regSumx1y) / bBase;
        b0 = meanY - b1 * meanX1 - b2 * meanX2;
        System.out.println("____________________________________________________________________");
        System.out.println("    [Coefficients]: ");
        System.out.println("    α= [" + String.format("%.3f", b0) + "], β1= [" + String.format("%.3f", b1) + "], β2= [" + String.format("%.3f", b2) + "].");
        System.out.println("_____________________________________________________________________");

        //ANOVA calculation
        double dfm, dfe; //difference from mean , difference from estimate
        double sodfm, sodfe; // square of  dfm, dfe
        double sst = 0, sse = 0, ssr; //sum of square total, error, regression
        double msr, mse; // mean square regression, mean square error
        double[] yHat = new double[dateNum]; //date array of estimated y values
        for (int i = 0; i < dateNum; i++) {
            dfm = yx1x2[0][i] - meanY;
            sodfm = Math.pow(dfm, 2);
            sst += sodfm;
        }
        for (int i = 0; i < dateNum; i++) {
            yHat[i] = b0 + b1 * yx1x2[1][i] + b2 * yx1x2[2][i];
        }
        for (int i = 0; i < dateNum; i++) {
            dfe = yx1x2[0][i] - yHat[i];
            sodfe = Math.pow(dfe, 2);
            sse += sodfe;
        }
        ssr = sst - sse;
        double rSquare = ssr / sst;
        double rSquareAdj = 1 - (sse / (dateNum - 2 - 1)) / (sst / (dateNum - 1));
        mse = sse / (dateNum - 2 - 1);
        msr = ssr / 2;
        double fstat = msr / mse;
        int dfSSE = dateNum - 2 - 1;
        int dfSST = dateNum - 1;
        System.out.println("    [ANOVA] table: ");
        System.out.println("    [Source]    [SS]      [df]       [MS]       [Fstat]  ");
        System.out.println("    [SSR]     [" + String.format("%.3f", ssr) + "]   " + "[2]   [" + String.format("%.3f", msr) + "]      [" + String.format("%.3f", fstat) + "]");
        System.out.println("    [SSE]     [" + String.format("%.3f", sse) + "]   [" + dfSSE + "]   [" + String.format("%.3f", mse) + "]");
        System.out.println("    [SST]     [" + String.format("%.3f", sst) + "]  [" + dfSST + "]");
        System.out.println("    [R^2] = [" + String.format("%.3f", rSquare) + "]");
        System.out.println("    [R^2 adjusted] = [" + String.format("%.3f", rSquareAdj) + "]");
        System.out.println("______________________________________________________________________");

        //read market data
        double x1D0 = 0, x1D60 = 0, x2D0 = 0, x2D60 = 0; //index at date D
        double rx1D60 = 0, rx2D60 = 0; // basic 60 day return R(x1) R(x2)
        x1D0 = yx1x2[1][0];
        x1D60 = yx1x2[1][59];
        rx1D60 = (x1D0 - x1D60) / x1D60;
        x2D0 = yx1x2[2][0];
        x2D60 = yx1x2[2][59];
        rx2D60 = (x2D0 - x2D60) / x2D60;
        double x1D120 = 0, x2D120 = 0;
        double rx1D120 = 0, rx2D120 = 0;
        if (dateNum >= 120) {
            x1D120 = yx1x2[1][119];
            x2D120 = yx1x2[2][119];
            rx1D120 = (x1D0 - x1D120) / x1D120;
            rx2D120 = (x2D0 - x2D120) / x2D120;
        } else {
            System.out.println("Not enough data from 6 month return calculation. Lines of data = " + dateNum);
        }
        double x1D240, x2D240;
        double rx1D240 = 0, rx2D240 = 0;
        if (dateNum >= 240) {
            x1D240 = yx1x2[1][239];
            x2D240 = yx1x2[2][239];
            rx1D240 = (x1D0 - x1D240) / x1D240;
            rx2D240 = (x2D0 - x2D240) / x2D240;
        } else {
            System.out.println("Not enough data from 1 year return calculation. Lines of data = " + dateNum);
        }
        System.out.println("   [Parameter 1]");
        System.out.println("   3 month return = [" + String.format("%.3f", rx1D60 * 100) + "%].");
        System.out.println("   6 month return = [" + String.format("%.3f", rx1D120 * 100) + "%].");
        System.out.println("   1 year return  = [" + String.format("%.3f", rx1D240 * 100) + "%].");

        System.out.println("   [Parameter 2]");
        System.out.println("   3 month return = [" + String.format("%.3f", rx2D60 * 100) + "%].");
        System.out.println("   6 month return = [" + String.format("%.3f", rx2D120 * 100) + "%].");
        System.out.println("   1 year return  = [" + String.format("%.3f", rx2D240 * 100) + "%].");
        System.out.println("______________________________________________________________________");

        //take data from riskfreerate objects
        RiskFreeRates.setRiskFreeRates(dateNum);
        double riskFreeRate60 = RiskFreeRates.getRiskFreeRate60();
        double riskFreeRate120 = RiskFreeRates.getRiskFreeRate120();
        double riskFreeRate240 = RiskFreeRates.getRiskFreeRate240();

        //return expectation calculation, Ri, on historical data
        double ri60, ri120, ri240;
        ri60 = riskFreeRate60 + b1 * (rx1D60 - riskFreeRate60) + b2 * (rx2D60 - riskFreeRate60);
        ri120 = riskFreeRate120 + b1 * (rx1D120 - riskFreeRate120) + b2 * (rx2D120 - riskFreeRate120);
        ri240 = riskFreeRate240 + b1 * (rx1D240 - riskFreeRate240) + b2 * (rx2D240 - riskFreeRate240);
        System.out.println("______________________________________________________________________");
        System.out.println("   [Result]: ");
        if (dateNum >= 60) {
            System.out.println("   In 3 month, given past market condition, the index should have a expected return = [" + String.format("%.3f", ri60 * 100) + "%].");
        }
        if (dateNum >= 120) {
            System.out.println("   In 6 month, given past market condition, the index should have a expected return = [" + String.format("%.3f", ri120 * 100) + "%].");
        }
        if (dateNum >= 240) {
            System.out.println("   In 1 year, given past market condition, the index should have a expected return = [" + String.format("%.3f", ri240 * 100) + "%].");
        }
        System.out.println("______________________________________________________________________");
    }
}
