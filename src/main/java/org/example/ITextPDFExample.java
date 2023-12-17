package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jdk.swing.interop.SwingInterOpUtils;
import org.krysalis.jcharts.axisChart.AxisChart;
import org.krysalis.jcharts.chartData.AxisChartDataSet;
import org.krysalis.jcharts.chartData.ChartDataException;
import org.krysalis.jcharts.chartData.DataSeries;
import org.krysalis.jcharts.encoders.JPEGEncoder;
import org.krysalis.jcharts.encoders.PNGEncoder;
import org.krysalis.jcharts.properties.*;
import org.krysalis.jcharts.test.TestDataGenerator;
import org.krysalis.jcharts.types.ChartType;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.example.Functions.*;

//import static jdk.internal.org.jline.utils.Colors.s;

public class ITextPDFExample {
    public static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    public static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    public static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    public static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    public static Document document = new Document();


    public static void main(String args[]) {

        //Question 1
        try{
            PdfWriter.getInstance(document, new FileOutputStream("./src/main/resources/InterviewAnalysis.pdf"));
            document.open();
            addMetaData(document);
            addTitlePage(document, "Interview Report");

            Connection con = DBCPDataSource.getConnection();
            PreparedStatement p = null;
            ResultSet rs = null;
            addHeading(document, "Team with Maximum Interviews");
            ArrayList<String> Queries=new ArrayList<>(Arrays.asList(
                    "select team, count(*) as total_interviews from interviews where interviewdate between '2023-10-01' and '2023-11-30' group by team order by count(*) desc limit 1;",
                    "select team, count(*) as total_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                            "group by team order by count(*) limit 1;",
                    "select panelname, count(*) as no_of_interviews from interviews\n" +
                            "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                            "group by panelname order by no_of_interviews desc limit 3;",
                    "select skill, count(*) as no_of_interviews from interviews\n" +
                    "where interviewdate between '2023-10-01' and '2023-11-30'\n" +
                            "group by skill order by no_of_interviews desc limit 3;",
                    "select skill, count(skill) as no_of_interviews from interviews where month(interviewdate) =\n" +
                            "(\n" +
                            "with month_with_interview_count as\n" +
                            "(select month(interviewdate) as peak, count(*) as interviewcount from interviews group by month(interviewdate))\n" +
                            "select peak from month_with_interview_count where interviewcount = (select max(interviewcount) from month_with_interview_count)\n" +
                            ")\n" +
                            "group by skill order by no_of_interviews desc limit 3;"));

            //Question 1
            funcToCreateTable(Queries.get(0));

            document.add(new Paragraph("\n"));
            //Question 2
            addHeading(document, "Team with Minimum Interviews");
            funcToCreateTable(Queries.get(1));

//            document.add(new Paragraph("\n"));
            //Question 3
            addHeading(document, "Top 3 Panels for the month of October and November 2023");
            p = con.prepareStatement(Queries.get(2));
            rs = p.executeQuery();
            AxisChart my_output_chart = getChart("Panel Names", rs, "panelname");
            Image newimage = chartToImage(my_output_chart);
            document.add(newimage);
            document.newPage();

            //Question 4
            addHeading(document, "Top 3 Skills for the month of October and November 2023");

            p = con.prepareStatement(Queries.get(3));
            rs = p.executeQuery();

            my_output_chart = getChart("Panel Names", rs, "skill");

            newimage = chartToImage(my_output_chart);
            document.add(newimage);
//            document.add(new Paragraph("\n"));

            //Question 5
            addHeading(document, "Top 3 Skills for which the interviews were conducted in the\n" +
                    "Peak Time");
            p = con.prepareStatement(Queries.get(4));
            rs = p.executeQuery();

            my_output_chart = getChart("Skill", rs, "skill");

            newimage = chartToImage(my_output_chart);
            document.add(newimage);
            document.add(new Paragraph("\n"));

        }catch (SQLException | DocumentException | FileNotFoundException e){
            e.printStackTrace();
        }finally {
            document.close();
        }

    }

}