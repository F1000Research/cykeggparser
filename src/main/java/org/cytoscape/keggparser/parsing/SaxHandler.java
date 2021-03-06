package org.cytoscape.keggparser.parsing;

import org.cytoscape.keggparser.com.*;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;

public class SaxHandler extends DefaultHandler {
    private Graph graph;
    private KeggNode node;
    private KeggRelation relation;

    public SaxHandler(Graph graph) {
        this.graph = graph;
    }

    public void startDocument() throws SAXException {

    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals("entry")){
            node = new KeggNode(Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_ID.getAttrName())),
                    attributes.getValue(EKGMLNodeAttrs.KGML_NAME.getAttrName()),
                    attributes.getValue(EKGMLNodeAttrs.KGML_TYPE.getAttrName()));
            node.setLink(attributes.getValue(EKGMLNodeAttrs.KGML_LINK.getAttrName()));
        }
        else if (qName.equals("graphics")){
            node.setGraphicsName(attributes.getValue(EKGMLNodeAttrs.KGML_NAME.getAttrName()));
            node.setFgColorAttr(attributes.getValue(EKGMLNodeAttrs.KGML_FGCOLOR.getAttrName()));
            node.setFgColor(Color.decode(attributes.getValue(EKGMLNodeAttrs.KGML_FGCOLOR.getAttrName())));
            node.setBgColorAttr(attributes.getValue(EKGMLNodeAttrs.KGML_BGCOLOR.getAttrName()));
            node.setBgColor(Color.decode(attributes.getValue(EKGMLNodeAttrs.KGML_BGCOLOR.getAttrName())));
            node.setShape(attributes.getValue(EKGMLNodeAttrs.KGML_TYPE.getAttrName()));
            int x;
            try {
                x = Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_X.getAttrName()));
            } catch (IllegalArgumentException e1 ){

                try {
                    x = (int) Double.parseDouble(attributes.getValue(EKGMLNodeAttrs.KGML_X.getAttrName()));
                } catch (IllegalArgumentException e2 ){
                    LoggerFactory.getLogger(SaxHandler.class).warn(e2.getMessage());
                    x = 0;
                }
            }
            node.setX(x);
            int y;
            try {
                y = Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_Y.getAttrName()));
            } catch (IllegalArgumentException e1 ){

                try {
                    y = (int) Double.parseDouble(attributes.getValue(EKGMLNodeAttrs.KGML_Y.getAttrName()));
                } catch (IllegalArgumentException e2 ){
                    LoggerFactory.getLogger(SaxHandler.class).warn(e2.getMessage());
                    y = 0;
                }
            }
            node.setY(y);

            node.setWidth(Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_WIDTH.getAttrName())));
            node.setHeight(Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_HEIGHT.getAttrName())));
        }
        else if (qName.equals("component")){
            node.addComponentId(Integer.parseInt(attributes.getValue(EKGMLNodeAttrs.KGML_ID.getAttrName())));
        }

        else if(qName.equals("relation")){
            relation = new KeggRelation(graph.getNode(Integer.parseInt(attributes.getValue(EKGMLEdgeAttrs.KGML_ENTRY1.getAttrName()))),
                    graph.getNode(Integer.parseInt(attributes.getValue(EKGMLEdgeAttrs.KGML_ENTRY2.getAttrName()))),
                    attributes.getValue(EKGMLEdgeAttrs.KGML_TYPE.getAttrName()));
        }

        else if (qName.equals("subtype")){
            relation.setSubtype(attributes.getValue(EKGMLEdgeAttrs.KGML_SUBTYPE_NAME.getAttrName()));
            relation.setRelationValue(attributes.getValue(EKGMLEdgeAttrs.KGML_SUBTYPE_VALUE.getAttrName()));
        }

        else if (qName.equals("pathway")){
            graph.setPathwayName(attributes.getValue(EKGMLNetworkAttrs.NAME.getAttrName()));
            graph.setOrganism(attributes.getValue(EKGMLNetworkAttrs.ORGANISM.getAttrName()));
            graph.setNumber(attributes.getValue(EKGMLNetworkAttrs.NUMBER.getAttrName()));
            graph.setTitle(attributes.getValue(EKGMLNetworkAttrs.TITLE.getAttrName()));
            graph.setImage(attributes.getValue(EKGMLNetworkAttrs.IMAGE.getAttrName()));
            graph.setLink(attributes.getValue(EKGMLNetworkAttrs.LINK.getAttrName()));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("entry")){
            graph.addNode(node);
        }
        else if (qName.equals("relation")){
            graph.addRelation(relation);
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
    }

    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
    }

}