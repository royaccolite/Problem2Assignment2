package org.example;

import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.encoders.PNGEncoder;
import org.krysalis.jcharts.properties.*;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class JChartsExample {
    public static void main(String[] args) {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream(new File("C:\Assignment2Task2\Problem2\src\main\resources\\photo.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String[] xAxisLabels = {"Q1", "Q2", "Q3", "Q4"};
        String xAxisTitle = "Quarter"; /* X - Axis label */
        String yAxisTitle = "Sales Count"; /*Y-Axis label */
        String title = "CountryVsSales - Bar Chart"; /* Chart title */
        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);


        /* Step - 2: Define the data for the clustered bar chart */
        double[][] data = new double[][]{{30, 33, 56, 2}, {45, 12, 98, 15}};
        String[] legendLabels = {"Rome", "Cairo"}; /* Define legend for bar chart */
        Paint[] paints = TestDataGenerator.getRandomPaints(2);
        ClusteredBarChartProperties clusteredBarChartProperties = new ClusteredBarChartProperties();
        AxisChartDataSet axisChartDataSet = null;
        try {
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR_CLUSTERED, clusteredBarChartProperties);
        } catch (ChartDataException e) {
            throw new RuntimeException(e);
        }
        dataSeries.addIAxisPlotDataSet(axisChartDataSet);

        /* Step -3 - Create the chart */
        ChartProperties chartProperties = new ChartProperties(); /* Special chart properties, if any */
        AxisProperties axis_Properties = new AxisProperties();
        LegendProperties legend_Properties = new LegendProperties(); /* Dummy Axis and legend properties class */
        AxisChart my_output_chart = new AxisChart(dataSeries, chartProperties, axis_Properties, legend_Properties, 640, 480); /* Create Chart object */


        /* Step -3: Set the response type to PNG */
//        response.setContentType("image/png"); /* Set the HTTP Response Type */

        /* Step -4: Write the chart output as PNG image file */
        try {
            PNGEncoder.encode(my_output_chart, fout); /* This class creates the output PNG file for the chart */
        } catch (ChartDataException e) {
            throw new RuntimeException(e);
        } catch (PropertyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
