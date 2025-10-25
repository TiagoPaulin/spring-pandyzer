package com.pandyzer.backend.services;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.Evaluator;
import com.pandyzer.backend.models.Objective;
import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.EvaluatorRepository;
import com.pandyzer.backend.repositories.ObjectiveRepository;
import com.pandyzer.backend.repositories.ProblemRepository;

@Service
public class ReportService {

    private final EvaluationRepository evaluationRepository;
    private final ObjectiveRepository objectiveRepository;
    private final EvaluatorRepository evaluatorRepository;
    private final ProblemRepository problemRepository;

    public ReportService(EvaluationRepository evaluationRepository,
                         ObjectiveRepository objectiveRepository,
                         EvaluatorRepository evaluatorRepository,
                         ProblemRepository problemRepository) {
        this.evaluationRepository = evaluationRepository;
        this.objectiveRepository = objectiveRepository;
        this.evaluatorRepository = evaluatorRepository;
        this.problemRepository = problemRepository;
    }

    // ========= Paleta =========
    private static final Color C_BLACK   = new Color(18, 18, 18);
    private static final Color C_WHITE   = Color.WHITE;
    private static final Color C_GRAY50  = new Color(248, 248, 248);
    private static final Color C_GRAY100 = new Color(240, 240, 240);
    private static final Color C_GRAY300 = new Color(214, 214, 214);
    private static final Color C_GRAY700 = new Color(97, 97, 97);
    private static final Color C_AMBER   = new Color(251, 191, 36);

    // ========= Tipografia =========
    private static final Font F_TITLE_W = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, C_WHITE);
    private static final Font F_WHITE_S = FontFactory.getFont(FontFactory.HELVETICA, 9, C_WHITE);
    private static final Font F_WHITE_B = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, C_WHITE);

    private static final Font F_H1    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, C_BLACK);
    private static final Font F_H2    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, C_BLACK);
    private static final Font F_BODY  = FontFactory.getFont(FontFactory.HELVETICA, 10, C_BLACK);
    private static final Font F_MUTED = FontFactory.getFont(FontFactory.HELVETICA, 10, C_GRAY700);

    // ========= Logo =========
    // Você colocou em src/main/resources/logo.png (perfeito).
    // Ainda assim, deixo candidatos extras para robustez.
    private static final String[] LOGO_CANDIDATES = new String[] {
            "/logo.png",
            "logo.png",
            "/static/images/logo.png",
            "static/images/logo.png",
            "/assets/images/logo_app_bar.png",
            "assets/images/logo_app_bar.png",
    };

    // ========= Público =========
    public byte[] generateConsolidatedPdf(Long evaluationId) throws Exception {
        Evaluation eval = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada: " + evaluationId));

        List<Objective> objectives = objectiveRepository.findByEvaluationId(evaluationId);
        List<Evaluator> evaluators = evaluatorRepository.findByEvaluationId(evaluationId);
        List<Problem> problems     = problemRepository.findAllByEvaluation(evaluationId);

        Map<String, Long> bySeverity  = aggregateBySeverity(problems);
        Map<String, Long> byHeuristic = aggregateByHeuristic(problems);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 36, 36, 46, 54);
        PdfWriter writer = PdfWriter.getInstance(doc, out);
        writer.setPageEvent(new FooterEvent());

        doc.open();

        addHeader(doc, eval);                                // Cabeçalho com logo
        addInfoCard(doc, eval);                              // Card arredondado
//        addObjectivesCard(doc, objectives);                  // Card arredondado
//        addChartsCard(doc, bySeverity, byHeuristic);         // Card arredondado
//        addEvaluatorsCards(doc, evaluators, objectives, problems); // Cards arredondados
        addObjectivesCard(doc, objectives);
        addChartsCard(doc, bySeverity, byHeuristic);
        addObjectivesThenEvaluationsCards(doc, objectives, evaluators, problems);

        doc.close();
        return out.toByteArray();
    }

    // ========= Cabeçalho (com logo) =========
    private void addHeader(Document doc, Evaluation eval) throws Exception {
        PdfPTable t = new PdfPTable(new float[]{1f, 5f});
        t.setWidthPercentage(100);

        // LOGO
        Image logo = loadLogo();
        PdfPCell logoCell;
        if (logo != null) {
            logo.scaleToFit(44, 44);
            logoCell = new PdfPCell(logo, true);
        } else {
            logoCell = new PdfPCell(new Phrase(""));
        }
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setBackgroundColor(C_BLACK);
        logoCell.setPadding(12);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // TÍTULO
        Phrase ph = new Phrase();
        ph.add(new Phrase(
                eval.getDescription() != null ? eval.getDescription() : "Relatório da Avaliação",
                F_TITLE_W
        ));
        ph.add(Chunk.NEWLINE);
        ph.add(new Phrase("Consolidado • " + nowStr(), F_WHITE_S));

        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setBackgroundColor(C_BLACK);
        textCell.setPadding(12);
        textCell.addElement(ph);

        t.addCell(logoCell);
        t.addCell(textCell);

        // pequena faixa para “fechar” o bloco
        PdfPCell strip = new PdfPCell(new Phrase(""));
        strip.setColspan(2);
        strip.setFixedHeight(2);
        strip.setBorder(Rectangle.NO_BORDER);
        strip.setBackgroundColor(C_BLACK);
        t.addCell(strip);

        doc.add(t);
        doc.add(vSpace(10));
    }

    // ========= Card: Informações =========
    private void addInfoCard(Document doc, Evaluation eval) throws Exception {
        PdfPTable content = new PdfPTable(1);
        content.setWidthPercentage(100);

        PdfPCell head = new PdfPCell(new Phrase("Informações", F_H1));
        head.setBorder(Rectangle.NO_BORDER);
        head.setBackgroundColor(C_GRAY100);
        head.setPadding(10);
        content.addCell(head);

        PdfPTable grid = new PdfPTable(new float[]{1.4f, 1f});
        grid.setWidthPercentage(100);

        String left =
                row("Avaliador:",   safe(eval.getUser()!=null ? eval.getUser().getName() : "-")) +
                        row("Link:",        safe(eval.getLink())) +
                        row("Solicitante:", safe(eval.getUser()!=null ? eval.getUser().getName() : "-"));

        String right =
                row("Data de Início:", date(eval.getStartDate())) +
                        row("Data Final:",     date(eval.getFinalDate()));

        PdfPCell l = box(left); l.setPaddingTop(6); l.setPaddingBottom(6);
        PdfPCell r = box(right); r.setPaddingTop(6); r.setPaddingBottom(6);
        grid.addCell(l);
        grid.addCell(r);

        content.addCell(noBorder(grid));

        doc.add(wrapRounded(content));
        doc.add(vSpace(8));
    }

    // ========= Card: Objetivos =========
    private void addObjectivesCard(Document doc, List<Objective> objectives) throws Exception {
        PdfPTable content = new PdfPTable(1);
        content.setWidthPercentage(100);

        PdfPCell head = new PdfPCell(new Phrase("Objetivos", F_H1));
        head.setBorder(Rectangle.NO_BORDER);
        head.setBackgroundColor(C_GRAY100);
        head.setPadding(10);
        content.addCell(head);

        if (objectives == null || objectives.isEmpty()) {
            content.addCell(boxMuted("Nenhum objetivo cadastrado."));
        } else {
            for (Objective o : objectives) {
                PdfPCell li = new PdfPCell(new Phrase("• " + safe(objectiveLabel(o)), F_BODY));
                li.setBorderColor(C_GRAY300);
                li.setPadding(6);
                content.addCell(li);
            }
        }

        doc.add(wrapRounded(content));
        doc.add(vSpace(8));
    }


    // ========= Card: Gráficos =========
    private void addChartsCard(Document doc, Map<String, Long> bySeverity, Map<String, Long> byHeuristic) throws Exception {
        PdfPTable content = new PdfPTable(1);
        content.setWidthPercentage(100);

        PdfPCell head = new PdfPCell(new Phrase("Gráficos", F_H1));
        head.setBorder(Rectangle.NO_BORDER);
        head.setBackgroundColor(C_GRAY100);
        head.setPadding(10);
        content.addCell(head);

        PdfPTable charts = new PdfPTable(new float[]{1f, 1f});
        charts.setWidthPercentage(100);

        Image left = (bySeverity == null || bySeverity.isEmpty())
                ? emptyChart("Sem problemas identificados")
                : toPdfImage(pie(bySeverity, "Problemas Identificados"), 420, 280);

        Image right = (byHeuristic == null || byHeuristic.isEmpty())
                ? emptyChart("Sem violações de heurística")
                : toPdfImage(bars(byHeuristic, "Heurísticas Violadas", "ID da Heurística", "Qtd"), 420, 280);

        charts.addCell(imgCell(left));
        charts.addCell(imgCell(right));

        content.addCell(noBorder(charts));

        doc.add(wrapRounded(content));
        doc.add(vSpace(8));
    }

    // ========= Cards por objetivo =========
    // NOVO: chame este método no lugar do addEvaluatorsCards(...)
    void addObjectivesThenEvaluationsCards(
            Document doc,
            List<Objective> objectives,
            List<Evaluator> evaluators,
            List<Problem> allProblems) throws Exception {

        if (objectives == null || objectives.isEmpty()) return;

        // 1) Agrupa problemas por (objectiveId -> (userId -> lista de problemas))
        Map<Long, Map<Long, List<Problem>>> byObjThenEval = new LinkedHashMap<>();
        for (Problem p : (allProblems != null ? allProblems : List.<Problem>of())) {
            Long oid = (p.getObjective() != null ? p.getObjective().getId() : null);
            Long uid = (p.getUser() != null ? p.getUser().getId() : null);
            if (oid == null || uid == null) continue;
            byObjThenEval
                    .computeIfAbsent(oid, k -> new LinkedHashMap<>())
                    .computeIfAbsent(uid, k -> new ArrayList<>())
                    .add(p);
        }

        // 2) Mapa de userId -> nome do avaliador (mais direto e robusto)
        Map<Long, String> evaluatorNameByUserId = new LinkedHashMap<>();
        for (Evaluator ev : (evaluators != null ? evaluators : List.<Evaluator>of())) {
            if (ev.getUser() != null && ev.getUser().getId() != null) {
                evaluatorNameByUserId.put(
                        ev.getUser().getId(),
                        ev.getUser().getName() != null ? ev.getUser().getName() : "Avaliador"
                );
            }
        }

        // 3) Percorre os objetivos na ordem fornecida
        for (Objective obj : objectives) {
            PdfPTable content = new PdfPTable(1);
            content.setWidthPercentage(100);

            // Cabeçalho do OBJETIVO
            String objTitle = objectiveLabel(obj);
            PdfPCell head = new PdfPCell(new Phrase("Objetivo – " + safe(objTitle), F_H1));
            head.setBorder(Rectangle.NO_BORDER);
            head.setBackgroundColor(C_GRAY100);
            head.setPadding(10);
            content.addCell(head);

            Map<Long, List<Problem>> byEval = byObjThenEval.getOrDefault(obj.getId(), Map.of());
            if (byEval.isEmpty()) {
                content.addCell(boxMuted("Nenhuma avaliação/nenhum problema registrado para este objetivo."));
                doc.add(wrapRounded(content));
                doc.add(vSpace(8));
                continue;
            }

            // 4) Dentro do objetivo: um “subcard” por avaliador (userId)
            for (Map.Entry<Long, List<Problem>> e : byEval.entrySet()) {
                Long userId = e.getKey();

                // Nome do avaliador: tenta via mapa; se faltar, usa o nome do próprio problema
                String nomeAvaliador = evaluatorNameByUserId.get(userId);
                if (nomeAvaliador == null || nomeAvaliador.isBlank()) {
                    // fallback pelo primeiro problema
                    Problem first = e.getValue().isEmpty() ? null : e.getValue().get(0);
                    if (first != null && first.getUser() != null && first.getUser().getName() != null) {
                        nomeAvaliador = first.getUser().getName();
                    } else {
                        nomeAvaliador = "Avaliador";
                    }
                }

                PdfPCell sub = new PdfPCell(new Phrase("Avaliação – " + nomeAvaliador, F_H2));
                sub.setBackgroundColor(C_GRAY100);
                sub.setBorderColor(C_GRAY300);
                sub.setPadding(8);
                content.addCell(sub);

                int i = 1;
                for (Problem p : e.getValue()) {
                    PdfPTable row = new PdfPTable(new float[]{1.4f, 1f});
                    row.setWidthPercentage(100);

                    // Detalhes do problema
                    String detalhes =
                            "Problema " + i + "\n" +
                                    "Heurística: " + heur(p) + "\n\n" + // se quiser apenas ID: use p.getHeuristic()!=null ? p.getHeuristic().getId() : "-"
                                    "Descrição do Problema: " + safe(p.getDescription()) + "\n\n" +
                                    "Recomendação de Melhoria: " + safe(p.getRecomendation()) + "\n\n" +
                                    "Severidade do Problema: " +
                                    (p.getSeverity()!=null && p.getSeverity().getDescription()!=null
                                            ? p.getSeverity().getDescription() : "-");

                    PdfPCell left = new PdfPCell(new Phrase(detalhes, F_BODY));
                    left.setBorderColor(C_GRAY300);
                    left.setPadding(8);
                    row.addCell(left);

                    // Evidência (foto) à direita
                    if (p.getImageBase64()!=null && !p.getImageBase64().isBlank()) {
                        try {
                            byte[] b = Base64.getDecoder().decode(p.getImageBase64());
                            Image img = Image.getInstance(b);
                            img.scaleToFit(260, 160);
                            PdfPCell right = new PdfPCell(img, true);
                            right.setBorderColor(C_GRAY300);
                            right.setPadding(4);
                            row.addCell(right);
                        } catch (Exception ex) {
                            row.addCell(boxMuted("Evidência indisponível"));
                        }
                    } else {
                        row.addCell(boxMuted("Sem evidência"));
                    }

                    content.addCell(row);
                    i++;
                }
            }

            doc.add(wrapRounded(content));
            doc.add(vSpace(8));
        }
    }



    // ========= Cards por avaliador =========
    private void addEvaluatorsCards(Document doc,
                                    List<Evaluator> evaluators,
                                    List<Objective> objectives,
                                    List<Problem> allProblems) throws Exception {
        if (evaluators == null || evaluators.isEmpty()) return;

        for (Evaluator ev : evaluators) {
            String nome = ev.getUser()!=null ? ev.getUser().getName() : "Avaliador";

            PdfPTable content = new PdfPTable(1);
            content.setWidthPercentage(100);

            PdfPCell head = new PdfPCell(new Phrase("Avaliação – " + nome, F_H1));
            head.setBorder(Rectangle.NO_BORDER);
            head.setBackgroundColor(C_GRAY100);
            head.setPadding(10);
            content.addCell(head);

            // agrupa problemas do avaliador por objetivo (no seu modelo, Problem tem 'user')
            Map<Long, List<Problem>> byObj = new LinkedHashMap<>();
            for (Problem p : allProblems) {
                Long uid = (p.getUser()!=null) ? p.getUser().getId() : null;
                if (uid != null && ev.getUser()!=null && Objects.equals(uid, ev.getUser().getId())) {
                    Long oid = (p.getObjective()!=null ? p.getObjective().getId() : -1L);
                    byObj.computeIfAbsent(oid, k -> new ArrayList<>()).add(p);
                }
            }

            if (byObj.isEmpty()) {
                content.addCell(boxMuted("Nenhum problema registrado por este avaliador."));
                doc.add(wrapRounded(content));
                doc.add(vSpace(8));
                continue;
            }

            for (Map.Entry<Long, List<Problem>> e : byObj.entrySet()) {
                String objTitle = objectives.stream()
                        .filter(o -> Objects.equals(o.getId(), e.getKey()))
                        .map(Objective::getDescription)
                        .findFirst().orElse("Objetivo");

                PdfPCell sub = new PdfPCell(new Phrase(objTitle, F_H2));
                sub.setBackgroundColor(C_GRAY100);
                sub.setBorderColor(C_GRAY300);
                sub.setPadding(8);
                content.addCell(sub);

                int i = 1;
                for (Problem p : e.getValue()) {
                    PdfPTable row = new PdfPTable(new float[]{1.4f, 1f});
                    row.setWidthPercentage(100);

                    String detalhes =
                            "Problema " + i + "\n" +
                                    "Heurística: " + heur(p) + "\n\n" +
                                    "Descrição do Problema: " + safe(p.getDescription()) + "\n\n" +
                                    "Recomendação de Melhoria: " + safe(p.getRecomendation()) + "\n\n" +
                                    "Severidade do Problema: " + sev(p);

                    PdfPCell left = box(detalhes);
                    left.setPadding(10);
                    row.addCell(left);

                    if (p.getImageBase64()!=null && !p.getImageBase64().isBlank()) {
                        try {
                            byte[] b = Base64.getDecoder().decode(p.getImageBase64());
                            Image img = Image.getInstance(b);
                            img.scaleToFit(260, 160);
                            PdfPCell right = new PdfPCell(img, true);
                            right.setBorderColor(C_GRAY300);
                            right.setPadding(4);
                            row.addCell(right);
                        } catch (Exception ex) {
                            row.addCell(boxMuted("Evidência indisponível"));
                        }
                    } else {
                        row.addCell(boxMuted("Sem evidência"));
                    }

                    content.addCell(noBorder(row));
                    i++;
                }
            }

            doc.add(wrapRounded(content));
            doc.add(vSpace(10));
        }
    }

    // ========= XChart =========
    private PieChart pie(Map<String, Long> data, String title) {
        PieChart ch = new PieChartBuilder().width(400).height(300).title(title).build();
        ch.getStyler().setChartBackgroundColor(Color.WHITE);
        ch.getStyler().setPlotBackgroundColor(Color.WHITE);
        ch.getStyler().setLegendVisible(true);
        ch.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        ch.getStyler().setChartTitleBoxVisible(false);
        ch.getStyler().setPlotBorderVisible(false);

        Color[] colors = new Color[]{C_BLACK, C_AMBER, new Color(52,211,153), new Color(248,113,113), C_GRAY700};
        ch.getStyler().setSeriesColors(colors);

        for (Map.Entry<String, Long> e : data.entrySet()) {
            ch.addSeries(e.getKey(), e.getValue().doubleValue());
        }
        return ch;
    }

    private CategoryChart bars(Map<String, Long> data, String title, String x, String y) {
        CategoryChart ch = new CategoryChartBuilder().width(400).height(300)
                .title(title).xAxisTitle(x).yAxisTitle(y).build();

        ch.getStyler().setChartBackgroundColor(Color.WHITE);
        ch.getStyler().setPlotBackgroundColor(Color.WHITE);
        ch.getStyler().setLegendVisible(false);
        ch.getStyler().setChartTitleBoxVisible(false);
        ch.getStyler().setPlotBorderVisible(false);
        ch.getStyler().setSeriesColors(new Color[]{C_BLACK});

        List<String> xs = new ArrayList<>(data.keySet());
        List<Double> ys = new ArrayList<>();
        for (Long v : data.values()) ys.add(v.doubleValue());

        ch.addSeries("Qtd", xs, ys);
        return ch;
    }

    private Image toPdfImage(org.knowm.xchart.internal.chartpart.Chart<?, ?> chart, int w, int h) throws Exception {
        // XChart (3.8.x) não aceita width/height aqui. Gero o PNG e redimensiono.
        byte[] png = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

        BufferedImage original = ImageIO.read(new java.io.ByteArrayInputStream(png));
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.drawImage(original, 0, 0, w, h, null);
        g.dispose();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(scaled, "png", out);
        return Image.getInstance(out.toByteArray());
    }

    private Image emptyChart(String msg) throws Exception {
        int w = 420, h = 280;
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = bi.createGraphics();
        g.setColor(Color.WHITE); g.fillRect(0,0,w,h);
        g.setColor(new Color(230,230,230)); g.drawRect(1,1,w-3,h-3);
        g.setColor(new Color(90,90,90));
        g.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 14));
        int tw = g.getFontMetrics().stringWidth(msg);
        g.drawString(msg, (w - tw)/2, h/2); g.dispose();

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", buf);
        return Image.getInstance(buf.toByteArray());
    }

    // ========= Footer =========
    private static class FooterEvent extends PdfPageEventHelper {
        @Override public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Rectangle page = document.getPageSize();

            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    new Phrase("Gerado em " + nowStr(), F_WHITE_S),
                    page.getLeft() + 36, page.getBottom() + 22, 0);

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("@panda", F_WHITE_B),
                    (page.getLeft()+page.getRight())/2, page.getBottom() + 22, 0);

            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase("Página " + writer.getPageNumber(), F_WHITE_S),
                    page.getRight() - 36, page.getBottom() + 22, 0);
        }
    }

    // ========= Helpers de UI =========
    /** Envolve uma tabela de conteúdo com um "cartão" de cantos arredondados. */
    private PdfPTable wrapRounded(PdfPTable inner) {
        PdfPTable wrapper = new PdfPTable(1);
        wrapper.setWidthPercentage(100);

        PdfPCell c = new PdfPCell();
        c.setBorder(Rectangle.NO_BORDER);
        c.setPadding(0);
        c.setCellEvent(new RoundedCardEvent(C_WHITE, C_GRAY300, 10f)); // raio

        PdfPTable padded = new PdfPTable(1);
        padded.setWidthPercentage(100);
        PdfPCell pad = new PdfPCell();
        pad.setBorder(Rectangle.NO_BORDER);
        pad.setPadding(10);
        pad.addElement(inner);
        padded.addCell(pad);

        c.addElement(padded);
        wrapper.addCell(c);
        return wrapper;
    }

    /** Desenha um retângulo arredondado atrás da célula (bg + borda). */
    private static class RoundedCardEvent implements PdfPCellEvent {
        private final Color bg;
        private final Color border;
        private final float radius;
        RoundedCardEvent(Color bg, Color border, float radius) {
            this.bg = bg; this.border = border; this.radius = radius;
        }
        @Override public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            // fundo
            PdfContentByte back = canvas[PdfPTable.BACKGROUNDCANVAS];
            back.saveState();
            back.setColorFill(bg);
            back.roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), radius);
            back.fill();
            back.restoreState();

            // borda
            PdfContentByte line = canvas[PdfPTable.LINECANVAS];
            line.saveState();
            line.setColorStroke(border);
            line.setLineWidth(1f);
            line.roundRectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight(), radius);
            line.stroke();
            line.restoreState();
        }
    }

    private static PdfPCell box(String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt, F_BODY));
        c.setBorderColor(C_GRAY300);
        c.setPadding(8);
        return c;
    }

    private static PdfPCell boxMuted(String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt, F_MUTED));
        c.setBackgroundColor(C_GRAY50);
        c.setBorderColor(C_GRAY300);
        c.setPadding(8);
        return c;
    }

    private static PdfPCell imgCell(Image img) {
        PdfPCell c = new PdfPCell(img, true);
        c.setBorderColor(C_GRAY300);
        c.setPadding(6);
        return c;
    }

    private static PdfPCell noBorder(PdfPTable t) {
        PdfPCell c = new PdfPCell(t);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private static PdfPTable vSpace(int h) {
        PdfPTable t = new PdfPTable(1);
        PdfPCell s = new PdfPCell(new Phrase(""));
        s.setBorder(Rectangle.NO_BORDER);
        s.setFixedHeight(h);
        t.addCell(s);
        return t;
    }

    // ========= Agregações =========
    private Map<String, Long> aggregateBySeverity(List<Problem> problems) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (problems == null) return map;
        for (Problem p : problems) {
            String k = (p.getSeverity() != null && p.getSeverity().getDescription()!=null)
                    ? p.getSeverity().getDescription() : "Sem classificação";
            map.put(k, map.getOrDefault(k, 0L) + 1);
        }
        return map;
    }

    private Map<String, Long> aggregateByHeuristic(List<Problem> problems) {
        Map<String, Long> map = new LinkedHashMap<>();
        if (problems == null) return map;
        for (Problem p : problems) {
            String k;
            if (p.getHeuristic() != null) {
                k = String.valueOf(p.getHeuristic().getId()); // <<< SOMENTE ID
            } else {
                k = "—";
            }
            map.put(k, map.getOrDefault(k, 0L) + 1);
        }
        return map;
    }

    // ========= Utils =========
    private static String row(String label, String value) { return label + " " + value + "\n"; }
    private static String safe(String v) { return (v == null || v.isBlank()) ? "-" : v; }
    private static String date(Date d) { return d == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(d); }
    private static String nowStr() { return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); }
    private static String sev(Problem p) { return p.getSeverity()!=null ? safe(p.getSeverity().getDescription()) : "-"; }

    // Coloque perto dos outros helpers (ex.: heur(), safe(), etc.)
    private String objectiveLabel(Objective o) {
        if (o == null) return "-";
        // tente vários campos em ordem de prioridade
        String[] cands = new String[] {
                // ajuste estes nomes para os campos reais da sua entidade Objective
                o.getDescription(),
        };
        for (String s : cands) {
            if (s != null && !s.isBlank()) return s.trim();
        }
        // fallback: ID
        return "Objetivo #" + o.getId();
    }


    private static String heur(Problem p) {
        if (p.getHeuristic()==null) return "-";
        String id = String.valueOf(p.getHeuristic().getId());
        String ds = p.getHeuristic().getDescription()!=null ? p.getHeuristic().getDescription() : "";
        return id + (ds.isBlank() ? "" : " - " + ds);
    }

    private Image loadLogo() {
        for (String path : LOGO_CANDIDATES) {
            try (InputStream in = getClass().getResourceAsStream(path)) {
                if (in == null) continue;
                byte[] bytes = in.readAllBytes();
                return Image.getInstance(bytes);
            } catch (Exception ignore) {}
        }
        return null; // sem logo
    }
}
