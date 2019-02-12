package com.juhnowski;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import static com.juhnowski.MapUtil.sortByValue;

public class Gephi {

    public static void main(String[] args) {
        try {
            convert();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void inc(Integer key){
        if (nodes.containsKey(key)){
            Integer val = nodes.get(key)+1;
            nodes.put(key,val);
        } else {
            nodes.put(key, new Integer(1));
        }
    }

    public static HashMap<Integer,Integer> nodes = new HashMap<>();


    public static void convert() throws Exception{
        FileInputStream inputStream = null;
        FileInputStream inputStream1 = null;
        Scanner sc = null;



        inputStream = new FileInputStream("p2p-Gnutella04.txt");


        FileOutputStream fos=new FileOutputStream("Edges1.csv");
        sc = new Scanner(inputStream, "UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("Source;Target;Type;Weight\n");
        byte[] buffer = sb.toString().getBytes();
        fos.write(buffer, 0, buffer.length);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#")){
                System.out.println(line);
            } else {
                String[] edges = line.split("\t");
                inc(Integer.valueOf(edges[0]));
                inc(Integer.valueOf(edges[1]));

                sb = new StringBuilder();
                sb.append(edges[0]).append(";").append(edges[1]).append(";Directed;1\n");
                buffer = sb.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
            }
        }
        fos.flush();
        fos.close();

        //write nodes
        fos=new FileOutputStream("Nodes1.csv");
        String text_hdr = "Id;Label;Interval\n";
        buffer = text_hdr.toString().getBytes();
        fos.write(buffer, 0, buffer.length);


        Iterator<Integer> it = nodes.keySet().iterator();
        while(it.hasNext()){
            sb = new StringBuilder();
            Integer i = it.next();
            sb.append(i).append(";").append(i).append(";").append(nodes.get(i)).append("\n");
            buffer = sb.toString().getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        fos.flush();
        fos.close();


        int TOP = 100;
        //write new nodes
        fos=new FileOutputStream("Nodes_top"+TOP+".csv");
        text_hdr = "Id;Label;Interval\n";
        buffer = text_hdr.toString().getBytes();
        fos.write(buffer, 0, buffer.length);

        Map<Integer,Integer> map = sortByValue(nodes);

        it = map.keySet().iterator();
        int cnt = 0;
        HashMap<Integer,Integer> nodesTop = new HashMap<>();

        while(it.hasNext()){
            sb = new StringBuilder();
            Integer i = it.next();
            if (cnt++>10876-TOP) {
                sb.append(i).append(";").append(i).append(";").append(nodes.get(i)).append("\n");
                buffer = sb.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
                nodesTop.put(i, nodes.get(i));
            }
        }
        fos.flush();
        fos.close();

        //write new edges
        fos=new FileOutputStream("Edges_top"+TOP+".csv");
        inputStream1 = new FileInputStream("p2p-Gnutella04.txt");
        sc = new Scanner(inputStream1, "UTF-8");
        sb = new StringBuilder();
        sb.append("Source;Target;Type;Weight\n");
        buffer = sb.toString().getBytes();
        fos.write(buffer, 0, buffer.length);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#")){
                System.out.println(line);
            } else {
                String[] edges = line.split("\t");

                Integer n0 = Integer.valueOf(edges[0]);
                Integer n1 = Integer.valueOf(edges[1]);


                sb = new StringBuilder();

                if (nodesTop.containsKey(n0) && nodesTop.containsKey(n1)) {
                    sb.append(edges[0]).append(";").append(edges[1]).append(";Directed;1\n");
                    buffer = sb.toString().getBytes();
                    fos.write(buffer, 0, buffer.length);
                }
            }
        }
        fos.flush();
        fos.close();


        //calculate edges counts per node
        fos=new FileOutputStream("Edges_distr.csv");

        HashMap<Integer,Integer> counts = new HashMap<>();

        nodes.forEach((k,v)->{
            if (counts.containsKey(v)){
                Integer t = counts.get(v)+1;
                counts.put(v,t);
            } else {
                counts.put(v,1);
            }
        });

        text_hdr = "edges per node;nodes\n";
        buffer = text_hdr.toString().getBytes();
        fos.write(buffer, 0, buffer.length);
        it = counts.keySet().iterator();

        while(it.hasNext()){
            sb = new StringBuilder();
            Integer i = it.next();
            sb.append(i).append(";").append(counts.get(i)).append("\n");
                buffer = sb.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
                nodesTop.put(i, nodes.get(i));
        }
        fos.flush();
        fos.close();

        inputStream.close();
        sc.close();
    }
}
