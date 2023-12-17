package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.encoders.JPEGEncoder;
import org.krysalis.jcharts.properties.*;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.ITextPDFExample.*;

public class Functions {
    public static AxisChart getChart(String xAxisTitle, ResultSet rs, String heading){
        String[] xAxisLabels = {"1", "2", "3"};
//        String xAxisTitle = "Panel Names";
        String yAxisTitle = "Interview Count"; /*Y-Axis label */
        String title = " "; /* Chart title */
        DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle, yAxisTitle, title);

        double[][] data = new double[][]{{0, 0, 0}};
        int i = 0;
        while(true){
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                xAxisLabels[i] = rs.getString(heading);
                data[0][i] = rs.getInt("no_of_interviews");
                i += 1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        String[] legendLabels = {" "};
        Paint[] paints = TestDataGenerator.getRandomPaints(1);
        BarChartProperties barChartProperties = new BarChartProperties();
        AxisChartDataSet axisChartDataSet = null;
        try {
            axisChartDataSet = new AxisChartDataSet(data, legendLabels, paints, ChartType.BAR, barChartProperties);
        } catch (ChartDataException e) {
            throw new RuntimeException(e);
        }
        dataSeries.addIAxisPlotDataSet(axisChartDataSet);

        /* Step -3 - Create the chart */
        ChartProperties chartProperties = new ChartProperties(); /* Special chart properties, if any */
        AxisProperties axis_Properties = new AxisProperties();
        LegendProperties legend_Properties = new LegendProperties(); /* Dummy Axis and legend properties class */
        return new AxisChart(dataSeries, chartProperties, axis_Properties, legend_Properties, 450, 280); /* Create Chart object */
    }
    public static Image chartToImage(AxisChart chart){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            JPEGEncoder.encode(chart, 1, outputStream);
            return new Jpeg(outputStream.toByteArray());
        } catch (ChartDataException | PropertyException | IOException | BadElementException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMetaData(Document document) {
        document.addTitle("iText PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText, jCharts");
        document.addAuthor(System.getProperty("user.name"));
        document.addCreator(System.getProperty("user.name"));
    }

    static void addTitlePage(Document document, String str)
            throws DocumentException {
        Paragraph preface = new Paragraph();

        addEmptyLine(preface, 1);
        // a big header
        preface.add(new Paragraph(str, catFont));

        addEmptyLine(preface, 1);
        document.add(preface);
    }
    public static void addHeading(Document document, String str)
            throws DocumentException {
        Paragraph preface = new Paragraph();

        addEmptyLine(preface, 1);
        // Lbig header
        preface.add(new Paragraph(str, subFont));

        addEmptyLine(preface, 1);
        document.add(preface);
    }

    public static void createTable(Document document, String[] headings, String[][] rows)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();
        int cols = rows[0].length;
        PdfPTable table = new PdfPTable(cols);
        table.setPaddingTop(2);

        for(int i = 0; i < headings.length; i++){
            PdfPCell c1 = new PdfPCell(new Phrase(headings[i]));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }
        table.setHeaderRows(1);
        for(int i = 0; i < rows.length; i++){
            for(int j = 0; j < cols; j++){
                PdfPCell cell = new PdfPCell(new Phrase(rows[i][j]));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);
            }
        }
        paragraph.add(table);
        document.add(paragraph);
    }


    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    public static void funcToCreateTable(String s) throws SQLException, DocumentException {
        PreparedStatement p = null;
        ResultSet rs = null;
        Connection con = DBCPDataSource.getConnection();

        p = con.prepareStatement(s);
        rs = p.executeQuery();
        System.out.println();
        while (rs.next()) {
            String team = rs.getString("team");
            String total_interviews = rs.getString("total_interviews");
//                System.out.println(team+*-*-" "+total_interviews);
            createTable(document, new String[]{"Team", "Total Interviews"}, new String[][]{new String[]{team, total_interviews}});
        }
    }
}
