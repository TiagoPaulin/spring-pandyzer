package com.pandyzer.backend.controllers;

import com.pandyzer.backend.services.ReportService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluations")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<byte[]> downloadConsolidatedReport(@PathVariable("id") Long evaluationId) {
        try {
            byte[] pdf = reportService.generateConsolidatedPdf(evaluationId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("relatorio-avaliacao-" + evaluationId + ".pdf")
                    .build());
            headers.setCacheControl(CacheControl.noCache());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Falha ao gerar relatório: " + ex.getMessage()).getBytes());
        }
    }
}
