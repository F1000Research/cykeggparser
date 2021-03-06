package org.cytoscape.keggparser.tuning.string;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class RequestKegg {
    private static final String requestTemplate = "http://rest.kegg.jp/get/[path_id]/kgml";
//    private String path_id = "mmu03320";

    public String stringRequest(String path_id){
        String query = requestTemplate.replace("[path_id]", path_id);
//        System.out.println("downloading from " + query);
        try {
            URL url = new URL(query);
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String kgml = "kgml/mmu/" + path_id + ".xml";
            PrintWriter writer = new PrintWriter(new File(kgml));
            String inputLine, result = "";

            while ((inputLine = in.readLine()) != null) {
//                result += inputLine;
                if(!inputLine.contains("DOCTYPE"))
                    writer.append(inputLine + "\n");
            }
            in.close();
            writer.close();
//            System.out.println(kgml + " saved");
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        RequestKegg request = new RequestKegg();
        File ids_list = new File("kgml/mm_ids.txt");
        try {
            java.util.Scanner scanner = new java.util.Scanner(ids_list);
            while (scanner.hasNext())
                request.stringRequest(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }
}
