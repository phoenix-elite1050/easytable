package org.vandeseer.pdfbox.easytable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;
import org.vandeseer.pdfbox.easytable.Row.RowBuilder;
import org.vandeseer.pdfbox.easytable.Table.TableBuilder;

import java.awt.*;
import java.io.IOException;

import static org.vandeseer.pdfbox.easytable.Cell.HorizontalAlignment.CENTER;
import static org.vandeseer.pdfbox.easytable.Cell.HorizontalAlignment.RIGHT;

public class TableDrawerIntegrationTest {

    private static final Color BLUE_DARK = new Color(76, 129, 190);
    private static final Color BLUE_LIGHT_1 = new Color(186, 206, 230);
    private static final Color BLUE_LIGHT_2 = new Color(218, 230, 242);

    @Test
    public void createAlternateRowsDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        page.setRotation(90);
        document.addPage(page);
        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        // TODO replace deprecated method call
        contentStream.concatenate2CTM(0, 1, -1, 0, page.getMediaBox().getWidth(), 0);
        final float startY = page.getMediaBox().getWidth() - 30;

        (new TableDrawer(contentStream, createAndGetTableWithAlternatingColors(), 30, startY)).draw();
        contentStream.close();

        document.save("target/alternateRows.pdf");
        document.close();
    }

    @Test
    public void createSampleDocument() throws Exception {
        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(PDType1Font.HELVETICA);

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("And this is another cell"))
                .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
                .setBackgroundColor(Color.BLUE)
                .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i).withAllBorders())
                    .add(Cell.withText(i * i).withAllBorders())
                    .add(Cell.withText(i + (i * i)).withAllBorders())
                    .setBackgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
                    .build());
        }

        createDocumentWithTable(tableBuilder, "target/sampleWithColorsAndBorders.pdf");
    }

    @Test
    public void createSampleDocumentWithCellSpanning() throws Exception {
        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(PDType1Font.HELVETICA);

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("And this is another cell"))
                .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
                .setBackgroundColor(Color.BLUE)
                .build());

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("And this is another cell").span(2).setBackgroundColor(Color.CYAN).setHorizontalAlignment(CENTER).withAllBorders())
                .setBackgroundColor(Color.BLUE)
                .build());

        createDocumentWithTable(tableBuilder, "target/sampleWithCellSpanning.pdf");
    }

    @Test
    public void createExcelLikeSampleDocument() throws Exception {
        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(240)
                .addColumnOfWidth(70)
                .addColumnOfWidth(70)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(PDType1Font.HELVETICA)
                .addRow(RowBuilder.newBuilder()
                    .add(Cell.withText("Product").withAllBorders())
                    .add(Cell.withText("2018").withAllBorders())
                    .add(Cell.withText("2019").withAllBorders())
                    .add(Cell.withText("Total").withAllBorders())
                    .setBackgroundColor(BLUE_DARK)
                    .withTextColor(Color.WHITE)
                    .setFont(PDType1Font.HELVETICA_BOLD)
                    .setFontSize(9)
                    .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i).withAllBorders())
                    .add(Cell.withText(i * i).withAllBorders())
                    .add(Cell.withText(i + (i * i)).withAllBorders())
                    .add(Cell.withText(i + (i * i)).withAllBorders())
                    .setBackgroundColor(i % 2 == 0 ? BLUE_LIGHT_1 : BLUE_LIGHT_2)
                    .build());
        }

        createDocumentWithTable(tableBuilder, "target/sampleExcelLike.pdf");
    }

    private void createDocumentWithTable(TableBuilder tableBuilder, String fileToSaveTo) throws IOException {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define the starting point
        final float startY = page.getMediaBox().getHeight() - 50;
        final int startX = 50;

        // Draw!
        (new TableDrawer(contentStream, tableBuilder.build(), startX, startY)).draw();
        contentStream.close();

        document.save(fileToSaveTo);
        document.close();
    }

    @Test
    public void createTableWithDifferentFontsInCells() throws IOException {
        // Define the table structure first
        TableBuilder tableBuilder = TableBuilder.newBuilder()
                .addColumnOfWidth(300)
                .addColumnOfWidth(120)
                .addColumnOfWidth(70)
                .setFontSize(8)
                .setFont(PDType1Font.HELVETICA);

        // Header ...
        tableBuilder.addRow(RowBuilder.newBuilder()
                .add(Cell.withText("This is right aligned without a border").setHorizontalAlignment(RIGHT))
                .add(Cell.withText("And this is another cell"))
                .add(Cell.withText("Sum").setBackgroundColor(Color.ORANGE))
                .setBackgroundColor(Color.BLUE)
                .build());

        // ... and some cells
        for (int i = 0; i < 10; i++) {
            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i).withAllBorders().withFont(PDType1Font.COURIER_BOLD))
                    .add(Cell.withText(i * i).withAllBorders().withFontSize(22))
                    .add(Cell.withText(i + (i * i)).withAllBorders().withFont(PDType1Font.TIMES_ITALIC))
                    .setBackgroundColor(i % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE)
                    .build());
        }

        createDocumentWithTable(tableBuilder, "target/sampleDifferentFontsInCells.pdf");
    }

    @Test
    public void createRingManagerDocument() throws Exception {
        final PDDocument document = new PDDocument();
        final PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        final float startY = page.getMediaBox().getHeight() - 150;
        final int startX = 56;

        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
        Table table = getRingManagerTable();

        (new TableDrawer(contentStream, table, startX, startY)).draw();

        contentStream.setFont(PDType1Font.HELVETICA, 8.0f);
        contentStream.beginText();

        contentStream.newLineAtOffset(startX, startY - (table.getHeight() + 22));
        contentStream.showText("Dieser Kampf muss der WB nicht entsprechen, da als Sparringskampf angesetzt.");
        contentStream.endText();

        contentStream.close();

        document.save("target/ringmanager.pdf");
        document.close();
    }

    private Table createAndGetTableWithAlternatingColors() {
        TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(20))
                .addColumn(new Column(400))
                .setFontSize(12)
                .setFont(PDType1Font.TIMES_ROMAN);

        Color lightGray = new Color(224, 224, 224);
        Color lightBlue = new Color(194, 232, 233);

        for (int i = 0; i < 10; i++) {
            Color backgroundColor = (i % 2 == 0) ? lightBlue : lightGray;

            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i)
                            .setBorderWidthBottom(2f)
                            .setHorizontalAlignment(RIGHT))
                    .add(Cell.withText(String.valueOf(i * 2)))
                    .setBackgroundColor(backgroundColor)
                    .setBorderColor(i == 6 ? Color.YELLOW : Color.RED)
                    .build());
        }

        for (int i = 0; i < 10; i++) {
            Color backgroundColor = (i % 2 == 0) ? Color.RED : Color.WHITE;

            tableBuilder.addRow(RowBuilder.newBuilder()
                    .add(Cell.withText(i)
                            .setBorderWidthRight(2f)
                            .setHorizontalAlignment(RIGHT))
                    .add(Cell.withText(i * 2))
                    .setBackgroundColor(backgroundColor)
                    .build());
        }

        return tableBuilder.build();
    }

    private Table getRingManagerTable() {
        final TableBuilder tableBuilder = new TableBuilder()
                .addColumn(new Column(26))
                .addColumn(new Column(70))
                .addColumn(new Column(390))
                .setFontSize(9)
                .setBorderColor(Color.GRAY)
                .setFont(PDType1Font.HELVETICA);

        float borderWidthOuter = 1.5f;
        float borderWidthInner = 1.0f;

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("1.")
                        .setBorderWidthTop(borderWidthOuter)
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthRight(borderWidthInner))
                .add(
                        Cell.withText("WK DBV(s)")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner))
                .add(
                        Cell.withText("Rote Ecke:")
                                .setBorderWidthTop(borderWidthOuter)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("").setBorderWidthLeft(borderWidthOuter))
                .add(
                        Cell.withText("Jugend")
                                .setBorderWidthRight(borderWidthInner)
                                .setBorderWidthLeft(borderWidthInner)).add(
                        Cell.withText("Thomas Test, m, FC St. Pauli, 01.01.1998, Jugend, 67,5 kg, 12K (8S, 4N, 0U)")
                                .setBorderWidthBottom(borderWidthInner)
                                .setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("").setBorderWidthLeft(borderWidthOuter)).add(
                Cell.withText("3x3")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)).add(
                Cell.withText("Blaue Ecke:").setBorderWidthRight(borderWidthOuter)).build());

        tableBuilder.addRow(RowBuilder.newBuilder().add(
                Cell.withText("")
                        .setBorderWidthLeft(borderWidthOuter)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                Cell.withText("10 Uz, KS")
                        .setBorderWidthRight(borderWidthInner)
                        .setBorderWidthLeft(borderWidthInner)
                        .setBorderWidthBottom(borderWidthOuter)).add(
                Cell.withText("Bernd Beispiel, m, Wedeler TSV, 02.01.1999, Jugend, 68,2 kg, 9K (7S, 2N, 0U)")
                        .setBorderWidthBottom(borderWidthOuter)
                        .setBorderWidthRight(borderWidthOuter)).build());

        return tableBuilder.build();
    }



}
