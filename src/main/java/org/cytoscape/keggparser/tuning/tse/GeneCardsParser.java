package org.cytoscape.keggparser.tuning.tse;

import org.cytoscape.keggparser.com.TuningReportGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * A class to parse the xml formatted gene expression data from GeneCards.
 */

public class GeneCardsParser {

    private Document document;
    private File geneCardsxml = new File("GeneCards/bioGps.xml");
    private Element rootElement;
    private static GeneCardsParser parser;

    private GeneCardsParser(){}

    public static GeneCardsParser getParser(){
        if (parser == null)
            parser = new GeneCardsParser();
        return parser;
    }

    public void loadDocument() {
        if (document == null)
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(geneCardsxml);
                this.rootElement = document.getDocumentElement();
            } catch (Exception e) {
                TuningReportGenerator.getInstance().appendLine(
                        e.getMessage());
            }
    }

    public Element getRootElement() {
        if (rootElement == null)
            loadDocument();
        rootElement = document.getDocumentElement();
        return rootElement;
    }

    public double getExpValue(String geneId, String tissue) {
        if (rootElement == null)
            loadDocument();
        rootElement = document.getDocumentElement();
        NodeList elements = rootElement.getElementsByTagName("geneId_" + geneId);
        if (elements.getLength() != 0) {
            Element gene = (Element) elements.item(0);
            return Double.parseDouble(gene.getAttribute(tissue));
        }
        else
            TuningReportGenerator.getInstance().appendLine("Gene " + geneId + " not found in the xml file");

        return Double.NaN;
    }

    public static void main(String[] args) {
        GeneCardsParser parser = new GeneCardsParser();
        System.out.println(parser.getExpValue("2", "Adipocyte"));
    }
}
