package com.kubsu.reports.controller;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.loaders.impl.JsonDataLoader;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.Report;
import com.haulmont.yarg.structure.ReportBand;
import com.haulmont.yarg.structure.ReportOutputType;
import com.haulmont.yarg.structure.impl.BandBuilder;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class ReportController {
    @GetMapping("/generate/doc")
    public void generateDocument(HttpServletResponse response) throws IOException {
        ReportBuilder reportBuilder = new ReportBuilder();
        ReportTemplateBuilder reportTemplateBuilder = new ReportTemplateBuilder().documentPath("C:\\Repositories\\kubsuAppBE\\report-service\\src\\main\\resources\\template\\letter.docx").documentName("letter.docx").outputType(ReportOutputType.docx).readFileFromPath();
        reportBuilder.template(reportTemplateBuilder.build());
        BandBuilder bandBuilder = new BandBuilder();
        String json = FileUtils.readFileToString(new File("C:\\Repositories\\kubsuAppBE\\report-service\\src\\main\\resources\\template\\Data.json"));
        ReportBand main = bandBuilder.name("Main").query("Main", "parameter=param1 $.main", "json").build();
        reportBuilder.band(main);
        Report report = reportBuilder.build();

        Reporting reporting = new Reporting();
        reporting.setFormatterFactory(new DefaultFormatterFactory());
        reporting.setLoaderFactory(new DefaultLoaderFactory().setJsonDataLoader(new JsonDataLoader()));
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        reporting.runReport(new RunParams(report).param("param1", json), response.getOutputStream());
    }
}
