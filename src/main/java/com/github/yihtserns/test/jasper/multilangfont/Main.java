package com.github.yihtserns.test.jasper.multilangfont;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String JR_XML = "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\"" +
            " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
            " xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\"" +
            " name=\"test-jasper-multi-lang-font\">\n" +
            "    <parameter name=\"ENGLISH_TEXT\" class=\"java.lang.String\" isForPrompting=\"false\"/>\n" +
            "    <parameter name=\"CHINESE_TEXT\" class=\"java.lang.String\" isForPrompting=\"false\"/>\n" +
            "    <parameter name=\"THAI_TEXT\" class=\"java.lang.String\" isForPrompting=\"false\"/>\n" +
            "    <parameter name=\"MULTI_LANG_TEXT\" class=\"java.lang.String\" isForPrompting=\"false\"/>\n" +
            "    <detail>\n" +
            "        <band height=\"100\" splitType=\"Stretch\">\n" +
            "            <textField>\n" +
            "                <reportElement x=\"0\" y=\"0\" width=\"100\" height=\"20\"/>\n" +
            "                <textElement/>\n" +
            "                <textFieldExpression class=\"java.lang.String\">\n" +
            "                  <![CDATA[$P{ENGLISH_TEXT}]]>\n" +
            "                </textFieldExpression>\n" +
            "            </textField>\n" +
            "            <textField>\n" +
            "                <reportElement x=\"100\" y=\"0\" width=\"100\" height=\"20\"/>\n" +
            "                <textElement/>\n" +
            "                <textFieldExpression class=\"java.lang.String\">\n" +
            "                  <![CDATA[$P{CHINESE_TEXT}]]>\n" +
            "                </textFieldExpression>\n" +
            "            </textField>\n" +
            "            <textField>\n" +
            "                <reportElement x=\"200\" y=\"0\" width=\"100\" height=\"20\"/>\n" +
            "                <textElement/>\n" +
            "                <textFieldExpression class=\"java.lang.String\">\n" +
            "                  <![CDATA[$P{THAI_TEXT}]]>\n" +
            "                </textFieldExpression>\n" +
            "            </textField>\n" +
            "            <textField>\n" +
            "                <reportElement x=\"300\" y=\"0\" width=\"100\" height=\"20\"/>\n" +
            "                <textElement/>\n" +
            "                <textFieldExpression class=\"java.lang.String\">\n" +
            "                  <![CDATA[$P{MULTI_LANG_TEXT}]]>\n" +
            "                </textFieldExpression>\n" +
            "            </textField>\n" +
            "        </band>\n" +
            "    </detail>\n" +
            "</jasperReport>";

    public static void main(String[] args) throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ENGLISH_TEXT", "Me");
        parameters.put("CHINESE_TEXT", "我");
        parameters.put("THAI_TEXT", "ฉัน");
        parameters.put("MULTI_LANG_TEXT", "Me我ฉัน - Me 我 ฉัน");

        JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(JR_XML.getBytes(StandardCharsets.UTF_8)));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        {
            StringBuilder sb = new StringBuilder();

            JRCsvExporter csvExporter = new JRCsvExporter();
            csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(sb));
            csvExporter.exportReport();

            System.out.println("CSV: " + sb);
        }

        {
            File pdfFile = File.createTempFile("test-jasper-multi-lang-font", ".pdf");

            JRPdfExporter pdfExporter = new JRPdfExporter();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));
            pdfExporter.exportReport();

            System.out.println("PDF file created at: " + pdfFile.getCanonicalPath());
            Desktop.getDesktop().open(pdfFile);
        }
    }
}
