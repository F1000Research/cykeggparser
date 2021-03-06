package org.cytoscape.keggparser.tuning.tse;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class GeneCardsStatistics {


    public static void getMeanExpressions() {
        Map<String, PriorityQueue<Double>> expMap = new HashMap<String, PriorityQueue<Double>>();
        for (Tissue tissue : Tissue.values()){
            expMap.put(tissue.getTissue(), new PriorityQueue<Double>());
        }

        GeneCardsParser parser = GeneCardsParser.getParser();
        parser.loadDocument();
        Element rootElement = parser.getRootElement();

        NodeList nodelist = rootElement.getChildNodes();

        String exp;
        double expValue;
        int geneIndex = -1;
        for (int index = 0; index < nodelist.getLength(); index++) {
            Node node = nodelist.item(index);
            if (node instanceof Element) {
                geneIndex++;
                Element element = (Element) node;
                for (Tissue tissue : Tissue.values()) {

                    exp = element.getAttribute(tissue.getTissue());
                    if (exp != null && !exp.equals("")) {
                        expValue = Double.parseDouble(exp);
                        expMap.get(tissue.getTissue()).add(expValue);
                    } else{
//                        System.out.println("exp value for " + geneIndex + " " + tissue);
                    }
                }
            }    else {
//                System.out.println(node.toString());
            }


        }
        Map<String, Double> meanMap = new HashMap<String, Double>();
        Map<String, Double> stdevMap = new HashMap<String, Double>();
        Map<String, Double> medianMap = new HashMap<String, Double>();
        for (Map.Entry<String, PriorityQueue<Double>> tissueExp : expMap.entrySet()){
            PriorityQueue<Double> expValues = tissueExp.getValue();

            double sum = 0;
            for (int i = 0; i< expValues.size(); i++)
                sum += expValues.peek();
            double mean = sum/expValues.size();
            double sqrSum = 0;
            for (int i = 0; i< expValues.size(); i++)
                sqrSum += (expValues.peek() - mean)*(expValues.peek() - mean);
            double stdev = Math.sqrt(sqrSum/expValues.size());
            meanMap.put(tissueExp.getKey(), mean);
            stdevMap.put(tissueExp.getKey(), stdev);
            PriorityQueue<Double> expValues2 = new PriorityQueue<Double>();
            expValues2.addAll(expValues);
            for (int j = 0; j < expValues2.size()*3/4; j++)
                expValues2.poll();
            double median = expValues2.poll();
            medianMap.put(tissueExp.getKey(), median);
        }

        for (Tissue tissue : Tissue.values()){
            double mean = meanMap.get(tissue.getTissue());
            double stdev = stdevMap.get(tissue.getTissue());
            double median = medianMap.get(tissue.getTissue());
            tissue.setMean(meanMap.get(tissue.getTissue()));
            tissue.setStdev(stdevMap.get(tissue.getTissue()));
//            System.out.println(tissue.getTissue() + "\t" + mean + "\t" + stdev + "\t" + median);
        }
    }

    public static void main(String[] args) {
        getMeanExpressions();
    }

}
