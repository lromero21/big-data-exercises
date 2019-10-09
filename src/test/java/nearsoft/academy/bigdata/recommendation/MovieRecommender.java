package nearsoft.academy.bigdata.recommendation;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class MovieRecommender {


    private HashMap<String, Long> products = new HashMap();
    private HashMap<Long, String> productsToString = new HashMap();
    private HashMap<String, Long> users = new HashMap();
    private int keyProducts = 0;
    private int keyPUsers = 0;
    int totalReviews = 0;
    int totalProducts = 0;
    int totalUsers = 0;


    public MovieRecommender(String movies) throws IOException, TasteException {
        createReccommendedFile(movies);

    }

    int getTotalReviews() throws IOException {
        return totalReviews;
    }

    int getTotalProducts() throws FileNotFoundException, IOException {
        return totalProducts;
    }


    int getTotalUsers() throws FileNotFoundException, IOException {
        return totalUsers;
    }

    void createReccommendedFile(String pathToMovies) throws IOException, TasteException {
        InputStream stream = new GZIPInputStream(new FileInputStream(pathToMovies));
        BufferedReader b = new BufferedReader(new InputStreamReader(stream, "US-ASCII"));
        String line;
        File csv = new File("recommenderFile.csv");
        if (!csv.exists()) {
            csv.createNewFile();
        }
        FileWriter fw = new FileWriter(csv);
        BufferedWriter bw = new BufferedWriter(fw);
        long productId = 0, userId = 0;
        while((line = b.readLine())!=null) {
            String prefix = line.split(" ")[0];
            if(prefix.equals("product/productId:")) {
                String pdID = line.split(" ")[1];
                if (products.containsKey(pdID)) {
                    productId = products.get(pdID);
                }else {
                    products.put(pdID, (long) keyProducts);
                    productsToString.put((long) keyProducts, pdID);
                    productId = keyProducts;
                    keyProducts++;
                    totalProducts++;
                }
            }
            if(prefix.equals("review/userId:")){
                String usID = line.split(" ")[1];
                if (users.containsKey(usID)) {
                    userId = users.get(usID);
                }else {
                    users.put(usID, (long) keyPUsers);
                    userId = keyPUsers;
                    keyPUsers++;
                    totalUsers++;
                }
            }
            if(prefix.equals("review/score:")){
                totalReviews++;
                String rvSC = line.split(" ")[1];
                bw.write(userId + "," + productId + "," + rvSC+"\n");
            }
        }
        bw.close();
    }

    public List<String> getRecommendationsForUser(String user) throws IOException, TasteException {
        List<String> stringList = new ArrayList<String>();
        DataModel model = new FileDataModel(new File("recommenderFile.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        for (RecommendedItem recommendation : recommender.recommend(users.get(user), 3)) {
            stringList.add(productsToString.get(recommendation.getItemID()));
        }
        return stringList;
    }


}
