package nearsoft.academy.bigdata.recommendation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class MovieRecommender {

    String path;
    private DataModel model;
    private UserSimilarity similarity;
    private UserNeighborhood neighborhood;
    private UserBasedRecommender recommender;

    private HashMap<String, Long> products = new HashMap();
    private HashMap<Long, String> productsToString = new HashMap();
    private HashMap<String, Long> users = new HashMap();

    private int keyProducts = 0;
    private int keyPUsers = 0;


    public MovieRecommender() throws IOException, TasteException {
        createReccommendedFile("/Users/lromero/Desktop/LuisMario/AmazonProblem/recommenderFile.csv");


    }


    int getTotalReviews(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        String [] arreglo_datos;
        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz"));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        int i = 0;
        String path = "";
        String TRV = "";
        char slash;
        char u;
        int contador = 0;
        //char e;
        HashSet<String> set = new HashSet<String>();
        while((cadena = b.readLine())!=null) {
            try{
                if(cadena.contains("/summary:")){
                    contador++;
                }
            }catch (IndexOutOfBoundsException ioub){
//                System.out.println("LLEGO VACIO EXCEPTION");
            }
        }

        return contador;
    }


    int getTotalProducts(String archivo) throws FileNotFoundException, IOException {
        String cadena;
        String [] arreglo_datos;
        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz"));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        int i = 0;
        String path = "";
        String TP = "";
        char slash;
        char u;
        //char e;
        HashSet<String> set = new HashSet<String>();
        while((cadena = b.readLine())!=null) {
            try{
                if(cadena.contains("/productId:")){
                    TP = cadena.split(" ")[1];
                    set.add(TP);
                }
            }catch (IndexOutOfBoundsException ioub){
//                System.out.println("LLEGO VACIO EXCEPTION");
            }
        }
        Iterator<String> itr = set.iterator();
        int productID = 0;
        while(itr.hasNext()) {
            itr.next();
            productID++;
        }
        System.out.println("Total PID: "+productID);
        b.close();
        return productID;
    }


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

    void createReccommendedFile(String archivo) throws IOException, TasteException {
        InputStream stream = new GZIPInputStream(new FileInputStream("/Users/lromero/Desktop/LuisMario/AmazonProblem/movies.txt.gz"));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        String cadena;
        File csv = new File("/Users/lromero/Desktop/LuisMario/AmazonProblem/recommenderFile.csv");
        if (!csv.exists()) {
            csv.createNewFile();
        }
        FileWriter fw = new FileWriter(csv);
        BufferedWriter bw = new BufferedWriter(fw);

        long productId = 0, userId = 0;
        while((cadena = b.readLine())!=null) {
            String prefijo = cadena.split(" ")[0];
            if(prefijo.equals("product/productId:")) {
                String pdID = cadena.split(" ")[1];
                if (products.containsKey(pdID)) {
                    productId = products.get(pdID);
                }else {
//                    System.out.println("Product " + key);
                    products.put(pdID, (long) keyProducts);
                    productsToString.put((long) keyProducts, pdID);
                    productId = keyProducts;
                    keyProducts++;
                }
            }

            if(prefijo.equals("review/userId:")){
                String usID = cadena.split(" ")[1];
                if (users.containsKey(usID)) {
                    userId = users.get(usID);
                }else {
//                    System.out.println("User: " + key);
                    users.put(usID, (long) keyPUsers);
                    userId = keyPUsers;
                    keyPUsers++;

                }
            }

            if(prefijo.equals("review/score:")){
                String rvSC = cadena.split(" ")[1];
//                System.out.println("Score " + rvSC);
                bw.write(userId + "," + productId + "," + rvSC+"\n");
            }

        }
        bw.close();




    }



    public List<String> getRecommendationsForUser(String user) throws IOException, TasteException {
        List<String> stringList = new ArrayList<String>();
        //List<String> recommendations = new ArrayList<String>();
        DataModel model = new FileDataModel(new File("/Users/lromero/Desktop/LuisMario/AmazonProblem/recommenderFile.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        System.out.println("User: " + users.get(user));
        //List<RecommendedItem> recommendations = recommender.recommend(users.get(user), 3);
        for (RecommendedItem recommendation : recommender.recommend(users.get(user), 3)) {
            System.out.println(recommendation.getItemID());
            stringList.add(productsToString.get(recommendation.getItemID()));

        }
//        List<RecommendedItem> getValues = new ArrayList<RecommendedItem>();
        System.out.println(stringList);
        return stringList;
    }

//    public static void main(String[] args) throws IOException, TasteException {
//        MovieRecommender call = new MovieRecommender();
//        call.getRecommendationsForUser("A141HP4LYPWMSR");
//    }

}
