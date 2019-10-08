package nearsoft.academy.bigdata.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class MovieRecommenderTest {
    @Test
    public void testDataInfo() throws IOException, TasteException {
        //download movies.txt.gz from 
        //    http://snap.stanford.edu/data/web-Movies.html
        MovieRecommender recommender = new MovieRecommender();
        assertEquals(7911684, recommender.getTotalReviews("Users/Desktop/LuisMario/AmazonProblem/movies.txt.gz")); //
        assertEquals(253059, recommender.getTotalProducts("Users/Desktop/LuisMario/AmazonProblem/movies.txt.gz")); // product/productId:
        assertEquals(889176, recommender.getTotalUsers("Users/Desktop/LuisMario/AmazonProblem/movies.txt.gz"));

        // get a token with a review and saved it if it's not already defined, but if it's already defined plus one
        //


        // total reviews : 7911684
        // total products : 253059
        // total users : 889176

        List<String> recommendations = recommender.getRecommendationsForUser("A141HP4LYPWMSR");
        assertThat(recommendations, hasItem("B0002O7Y8U"));
        assertThat(recommendations, hasItem("B00004CQTF"));
        assertThat(recommendations, hasItem("B000063W82"));

    }

    public static void main(String[] args) throws IOException, TasteException {
        MovieRecommenderTest call = new MovieRecommenderTest();
        call.testDataInfo();
    }

}
