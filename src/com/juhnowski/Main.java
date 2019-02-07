package com.juhnowski;
// based on https://deparkes.co.uk/2018/04/09/python-networkx-load-graphs-from-file/

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            convert();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void convert() throws Exception{
        FileInputStream inputStream = null;
        Scanner sc = null;

        HashSet<Integer> nodes = new HashSet<>();

        inputStream = new FileInputStream("p2p-Gnutella04.txt");
        FileOutputStream fos=new FileOutputStream("gml_graph.gml");
        sc = new Scanner(inputStream, "UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("graph\n" +
                "[\n" +
                " comment \"This is a gnutella graph\"\n" +
                " directed 0\n" +
                " label \"April\"\n");
        byte[] buffer = sb.toString().getBytes();
        fos.write(buffer, 0, buffer.length);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#")){
                System.out.println(line);
            } else {
                String[] edges = line.split("\t");
                nodes.add(Integer.valueOf(edges[0]));
                nodes.add(Integer.valueOf(edges[1]));
                sb = new StringBuilder();
                sb.append("edge \n");
                sb.append("[ \n");
                sb.append("source ").append(edges[0]).append("\n");
                sb.append("target ").append(edges[1]).append("\n");
                sb.append("label \"").append(edges[0]).append("->").append(edges[1]).append("\"\n");
                sb.append("] \n");
                buffer = sb.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
            }
        }
        //write nodes
        Iterator<Integer> it = nodes.iterator();
        while(it.hasNext()){
            sb = new StringBuilder();
            sb.append("node \n");
            sb.append("[ \n");
            Integer i = it.next();
            sb.append("id ").append(i).append("\n");
            sb.append("label \"").append(i).append("\"\n");
            sb.append("] \n");
            buffer = sb.toString().getBytes();
            fos.write(buffer, 0, buffer.length);
        }

        String text = "] \n";
        buffer = text.toString().getBytes();
        fos.write(buffer, 0, buffer.length);
        fos.flush();
        fos.close();
        inputStream.close();
        sc.close();
    }
}
