/**
 * Helper class to fetch JSON data from two free and open APIs:
 * 1. https://opentdb.com/api_config.php
 * 2. https://www.mediawiki.org/wiki/API:Main_page
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * version: 2.0: 11/26/2017
 */

package classes;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchJSONData {

    //The following are the API links
    private static String OPENTDB_API_LINK = "https://opentdb.com/api.php?amount=%s&category=11&difficulty=%s&type=multiple";
    private  static String WIKIPEDIA_API_LINK = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=%s&redirects=1";

    //The following are used for setting the number of questions to fetch from OPENTDB API
    private static final String NUMBER_OF_QUESTIONS_IS_FIVE = "5";
    private static final String NUMBER_OF_QUESTIONS_IS_TEN = "10";
    private static final String NUMBER_OF_QUESTIONS_IS_FIFTEEN = "15";

    //The following are used for setting the difficulty level of questions that are fetched from OPENTDB API
    private static final String DIFFICULTY_LEVEL_IS_EASY = "easy";
    private static final String DIFFICULTY_LEVEL_IS_MEDIUM = "medium";
    private static final String DIFFICULTY_LEVEL_IS_HARD = "hard";

    /**
     * This method returns JSON data depending on the parameters:
     * If number of questions and difficulty level are specified, then OPENTDB API is used
     * If only the answer is specified, then WIKIPEDIA API is used
     *
     * @param numberOfQuestionsPosition
     * @param difficultyLevelPosition
     * @param answer
     * @return JSONObject
     */
    public static JSONObject getJSONData(int numberOfQuestionsPosition, int difficultyLevelPosition, String answer) {
        try {
            URL url;

            if (answer != null && !answer.isEmpty()) {
                url = new URL(String.format(WIKIPEDIA_API_LINK, answer));
            }
            else {
                url = new URL(String.format(OPENTDB_API_LINK,
                        getNumberOfQuestions(numberOfQuestionsPosition),
                        getDifficultyLevel(difficultyLevelPosition)));
            }

            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String temp = "";

            while((temp = reader.readLine()) != null)
                json.append(temp).append("\n");

            reader.close();

            JSONObject data = new JSONObject(json.toString());
            return data;
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * This method returns the number of questions depending on the radio button selected (in OptionsActivity screen)
     *
     * @param numberOfQuestionsPosition
     * @return String
     */
    private static String getNumberOfQuestions(int numberOfQuestionsPosition){

        String numberOfQuestions = "";

        switch (numberOfQuestionsPosition) {
            case 0:
                /* default number of questions */
                numberOfQuestions = NUMBER_OF_QUESTIONS_IS_FIVE;
                break;
            case 1:
                numberOfQuestions = NUMBER_OF_QUESTIONS_IS_TEN;
                break;
            case 2:
                numberOfQuestions = NUMBER_OF_QUESTIONS_IS_FIFTEEN;
                break;
        }
        return numberOfQuestions;
    }

    /**
     * * This method returns the difficulty level depending on the radio button selected (in OptionsActivity screen)
     *
     * @param difficultyLevelPosition
     * @return String
     */
    private static String getDifficultyLevel(int difficultyLevelPosition) {

        String difficultyLevel = "";

        switch (difficultyLevelPosition) {
            case 0:
                /* default difficulty level */
                difficultyLevel = DIFFICULTY_LEVEL_IS_EASY;
                break;
            case 1:
                difficultyLevel = DIFFICULTY_LEVEL_IS_MEDIUM;
                break;
            case 2:
                difficultyLevel = DIFFICULTY_LEVEL_IS_HARD;
                break;
        }
        return difficultyLevel;
    }
}
