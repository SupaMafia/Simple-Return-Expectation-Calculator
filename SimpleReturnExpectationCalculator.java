/*
 * Date: 2021-8-4.
 * File Name: SimpleReturnExpectationCalculator.java
 * Version: 0.1
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
        System.out.println("Total number of date is: " + dateNum);

        //data into array: indexMarket, write orginal data into array for calculation
        double[][] indexMarket = new double[2][dateNum];
        for (int i = 0; i < dateNum; i++) {
            double date = inputStream0.nextDouble();
            indexMarket[0][i] = inputStream0.nextDouble();
            indexMarket[1][i] = inputStream0.nextDouble();
            String line = inputStream0.nextLine();
        }
        inputStream0.close();
        for (int i = 0; i < dateNum; i++) {
            System.out.println("Line: " + i + " index: " + indexMarket[0][i] + " market: " + indexMarket[1][i]);
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
        System.out.println("Index Mean (ȳ) =" + indexMean + " Market Mean (x̄) =" + marketMean);

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
        System.out.println("Covariance between Index and market =" + covIM);

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
        System.out.println("Std.Dev. of index (y) =" + stdDevI + " Std.Dev. of market (x)=" + stdDevM);

        //calculate persons correlation coefficient between index and market
        double corIM = 0;
        corIM = covIM / (stdDevI * stdDevM);
        System.out.println("Persons correlation coefficient between index and market =" + corIM);

        //β and α calculation, βi = cor(i,m)(σi/σm), α = ȳ-βx̄
        double beta = corIM * (stdDevI / stdDevM);
        double alpha = indexMean - beta * marketMean;
        System.out.println("α=" + alpha + " β=" + beta);

        //r square calculation to estimate accuracy of the model, to be included in version 0.2

        //Std.Error of Est, to be included in version 0.2

        //take input of the return of three month government treasury bill as the risk free discount rate. Can improve by adding t-bill of different time length
        System.out.println("The return of a three-month government tresury bill is ___% ");
        double riskFreeRate = 0; //Rf
        int count0 = 0;
        do {
            try {
                Scanner in0 = new Scanner(System.in);
                riskFreeRate = in0.nextDouble();
                break;
            } catch (InputMismatchException e0) {
                System.out.println("Non-numeric values are not allowed, please re enter");
            } finally {
                count0++;
            }
        } while (count0 < 10);

        //check data size
        if (dateNum < 60) {
            System.out.println("Data samples are too small to reach a conclusive estimation");
        }

        //find market return for 60,120 and 240 days
        double marketD60; //market price 60 days ago
        double marketD0;
        double returnD60;
        marketD60 = indexMarket[1][60];
        marketD0 = indexMarket[1][0];
        returnD60 = (marketD0 - marketD60) / marketD60;
        System.out.println("Market 3 month return =" + returnD60);

        //Basic 120, half year, return expectation, E(ri) calculation, on historical data
        double marketD120; //market price 120 days ago
        double returnD120;
        if (dateNum < 120) {
            System.out.println("Data samples are too small for half a year return");
        }
        marketD120 = indexMarket[1][120];
        returnD120 = (marketD0 - marketD120) / marketD120;
        System.out.println("Market half year return =" + returnD120);

        //Basic 240, half year, return expectation, E(ri) calculation, on historical data
        double marketD240; //market price 240 days ago
        double returnD240;
        if (dateNum < 120) {
            System.out.println("Data samples are too small for year return");
        }
        marketD240 = indexMarket[1][240];
        returnD240 = (marketD0 - marketD240) / marketD240;
        System.out.println("Market year return =" + returnD240);

        //return expectation, E(ri) calculation, on historical data
        double exp60;
        double exp120;
        double exp240;
        exp60 = riskFreeRate + beta * (returnD60 - riskFreeRate);
        exp120 = riskFreeRate + beta * (returnD120 - riskFreeRate);
        exp240 = riskFreeRate + beta * (returnD240 - riskFreeRate);
        System.out.println("In 3 month, given past market condition, the index should have a expected return of" + exp60);
        System.out.println("In 6 month, given past market condition, the index should have a expected return of" + exp120);
        System.out.println("In 1 year, given past market condition, the index should have a expected return of" + exp240);

    }
}
