package nearsoft.academy.bigdata.recommendation;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class test {

//    void muestraContenido(String archivo) throws FileNotFoundException, IOException {
//        String cadena;
//        String [] arreglo_datos;
////        FileReader f = new FileReader("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz");
////        BufferedReader b = new BufferedReader(f);
//        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/moviesTest2.txt.gz"));
//        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
//        int i = 0;
//        String path = "";
//        String pdID = "";
//        HashSet<String> set = new HashSet<String>();
//        while((cadena = b.readLine())!=null) {
//            if(!(cadena.equals(""))){
//                pdID = "";
//                path = "";
//                for (int j = 0; j < cadena.length(); j++) {
//                    if(!(path.equals("product/productId:"))){
//                        path += cadena.charAt(j);
//                    }else{
//                        for (int k = 0; k < 10; k++) {
//                            pdID += cadena.charAt(j+1);
//                            j++;
//                            if(pdID.length() == 10) {
//                                j = cadena.length();
//                                break;
//                            }
//                        }
//                    }
//                }
//                if(!(pdID.equals(""))) {
//                    //System.out.println("PRODUCT ID CATCHED: " + pdID);
//                    set.add(pdID);
//                }
//
//            }else {
//                for (int j = 0; j < cadena.length(); j++) {
//                    if(!(path.equals("product/productId:"))){
//                        path += cadena.charAt(j);
//                    }else{
//                        for (int k = 0; k < 10; k++) {
//                            pdID += cadena.charAt(j+1);
//                            j++;
//                            if(pdID.length() == 10){
//                                j = cadena.length();
//                                break;
//                            }
//
//                        }
//                    }
//                }
//                if(!(pdID.equals(""))) {
//                    //System.out.println("PRODUCT ID CATCHED: " + pdID);
//                    set.add(pdID);
//                }
//            }
//        }
//        Iterator<String> itr = set.iterator();
//        int pdIDCounter = 0;
//        while(itr.hasNext()) {
//            System.out.println(itr.next());
//            pdIDCounter++;
//        }
//        System.out.println("Numero sin repetir de Products Id: "+pdIDCounter);
//        b.close();
//    }


    int getTotalUsers(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        String [] arreglo_datos;
        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz"));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        int i = 0;
        String path = "";
        String pdID = "";
        char slash;
        char u;
        //char e;
        HashSet<String> set = new HashSet<String>();
        while((cadena = b.readLine())!=null) {
            try{
                if(cadena.startsWith("review/user")){
                    pdID = cadena.split(" ")[1];
                    set.add(pdID);
                }
            }catch (IndexOutOfBoundsException ioub){
//                System.out.println("LLEGO VACIO EXCEPTION");
            }
        }
        Iterator<String> itr = set.iterator();
        int totalUsers = 0;
        while(itr.hasNext()) {
            itr.next();
            totalUsers++;
        }
        System.out.println("Total users: "+totalUsers);
        b.close();
        return totalUsers;
    }



    int getTotalReviews(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        String [] arreglo_datos;
//        FileReader f = new FileReader("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz");
//        BufferedReader b = new BufferedReader(f);
        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/moviesTest3.txt.gz"));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        int i = 0;
        String path = "";
        String pdID = "";
        char slash;
        char t;
        char e;
        int reviewCounter = 0;

        HashSet<String> set = new HashSet<String>();
        while((cadena = b.readLine())!=null) {
            try{
                slash = cadena.charAt(6);
                t = cadena.charAt(7);
                e = cadena.charAt(8);
                if(slash == '/' && t == 't' && e == 'e'){
                    reviewCounter++;
                }
//
            }catch (IndexOutOfBoundsException ioub){
//                System.out.println("LLEGO VACIO EXCEPTION");
            }


        }

        System.out.println("Total Reviews: "+reviewCounter);
        return reviewCounter;
    }

    public Map findDuplicateString(String str) {
        String[] stringArrays = str.split(" ");
        Map<String, Integer> map = new HashMap<String, Integer>();
        Set<String> words = new HashSet<String>(Arrays.asList(stringArrays));
        int count = 0;
        for (String word : words) {
            for (String temp : stringArrays) {
                if (word.equals(temp)) {
                    ++count;
                }
            }
            map.put(word, count);
            count = 0;
        }

        return map;

    }

    public static void main(String[] args) throws IOException {
        test call = new test();
        //call.muestraContenido("/Users/lromero/Desktop/LuisMario/AmazonProblem/smalltest.txt ");
        //call.findDuplicateString("d");
        //call.getTotalReviews("/Users/lromero/Desktop/LuisMario/AmazonProblem/smalltest.txt ");
        //call.getTotalReviews("/Users/lromero/Desktop/LuisMario/AmazonProblem/smalltest.txt ");
        call.getTotalUsers("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz ");

    }

}
