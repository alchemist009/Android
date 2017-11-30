/**
 * Question activity Screen for Movie Quiz app
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * Description of the activity:
 * This activity is for individual questions and answer choices.
 *
 * This activity has the trivia question followed by 4 option choices (buttons). The user clicks on one of the option choices available.
 * If the answer is wrong, the chosen button is turned red. Also, a toast comes up which says "Get a hint by SHAKING your phone".
 * If the user decides to shake the phone, a hint to that question appears as a toast.
 * If the answer is wrong twice, the app moves to the next question after showing the toast which says "Moving on to the next question".
 *
 * If the answer is correct at any time, the chosen button is turned green.
 * A toast comes up which says "Swipe RIGHT to go to the next question".
 * The user can then swipe to the next question to answer. The new question comes up in the same activity and the process repeats, until the ‘End-Condition’ is reached.
 *
 * Every time the user swipes in this screen, he/she moves on to the next random question chosen from the question bank.
 *
 * There is a ‘Score’ label on the top right corner of the screen which keeps track of the no. of questions answered correctly.
 * If the user answers correctly for the given question, the score gets increased by one.
 * If the user swipes without choosing any option in the current question, the score does not increase.
 *
 * ‘End-Condition’:
 * When the no. of questions reaches the limit (either the default value, i.e. 5 or the no. set by the user) the app will show a Dialog,
 * which will display the score (for eg.: “You scored: 8 out of 10”) and a question below that: “Want to play again?” with two options “Yes” and “No”.
 * Clicking on the “Yes” button takes the user to the QuestionActivity with a random question in the default level.
 * Clicking on the “No” button takes the user to the “MainActivity”.
 *
 */

package com.utd.ksp160330.moviequiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import classes.FetchJSONData;
import classes.Question;
import classes.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    //The following are used for the options set by the user
    public static final String NUMBER_OF_QUESTIONS_POSITION = "NUMBER_OF_QUESTIONS_POSITION";
    public static final String DIFFICULTY_LEVEL_POSITION = "DIFFICULTY_LEVEL_POSITION";

    //This is used for handling the new thread to fetch JSON data
    private Handler handler;

    //The following are used for storing the questions and answers
    private ArrayList<Question> questionsList;
    private ArrayList<String> correctAnswers;
    private ArrayList<String> answers;

    //The following are used for keeping track of the question being answered and the score
    private int numberOfClicks;
    private int currentQuestionNumber;
    private int computedScore;
    private boolean correctlyAnswered;

    // This is used for gesture/swipe detection
    private GestureDetectorCompat gestureObject;

    // The following are used for the shake detection
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    /**
     * onCreate method initializes the following things:
     * 1. gesture object
     * 2. ShakeDetector objects
     * This method also fetches JSON data pertaining to the question and answer choices and subsequently shows the first question on the app screen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //gesture object initialization
        gestureObject = new GestureDetectorCompat(this, new LearnGesture());

        //handler initialization
        handler = new Handler();

        //ShakeDetector initialization
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();

        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });

        //The following gets the number of questions and the difficulty level set by the user in the OptionsActivity
        int numberOfQuestionsPosition = (int) getIntent().getSerializableExtra(NUMBER_OF_QUESTIONS_POSITION);
        int difficultyLevelPosition = (int) getIntent().getSerializableExtra(DIFFICULTY_LEVEL_POSITION);

        //fetch the JSON data and show the first question
        fetchJSONDataAndShowFirstQuestion(numberOfQuestionsPosition, difficultyLevelPosition);
    }

    /**
     * onResume method registers the Session Manager Listener
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        // register the Session Manager Listener onResume
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * onPause method unregisters the Sensor Manager
     *
     */
    @Override
    public void onPause() {
        // unregister the Sensor Manager onPause
        sensorManager.unregisterListener(shakeDetector);
        super.onPause();
    }

    /**
     * onTouchEvent method calls the onTouchEvent method of the parent class
     *
     * @param event
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * onBackPressed method finishes the question activity when the back key is pressed
     *
     */
    @Override
    public void onBackPressed() {
        QuestionActivity.this.finish();
    }

    /**
     * onClick method checks whether the answer choice chosen by the user is correct or not.
     * This method also shows appropriate toast messages based on the answer choice chosen.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

            String toastMessage;

            //update the no. of clicks
            numberOfClicks++;

            Button clickedButton = (Button) view;
            String buttonText = clickedButton.getText().toString();

            //the user answered correctly
            if(!correctlyAnswered && correctAnswers.get(currentQuestionNumber).equals(buttonText)) {

                correctlyAnswered = true;

                //change button color to green
                clickedButton.setBackgroundColor(Color.GREEN);

                TextView score = findViewById(R.id.scoreText);
                computedScore++;
                score.setText(Integer.toString(computedScore));

                //when the current question number is the last question in the question bank
                if (currentQuestionNumber == (questionsList.size() - 1)) {
                    //show dialog box
                    showInputDialog();
                }
                else { //show the next question
                    toastMessage = "Swipe RIGHT to go to the next question";
                    Toast.makeText(QuestionActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
                    //swipe right to the next question
                }
            }

            //the user answered incorrectly twice
            else if (!correctlyAnswered && numberOfClicks == 2){

                //change button color to red
                clickedButton.setBackgroundColor(Color.RED);

                //when the current question number is the last question in the question bank
                if (currentQuestionNumber == (questionsList.size() - 1)) {
                    //show dialog box
                    showInputDialog();
                }
                else {
                    toastMessage = "Moving on to the next question";
                    Toast.makeText(QuestionActivity.this, toastMessage, Toast.LENGTH_SHORT).show();

                    //get new question, display it
                    numberOfClicks = 0;
                    showQuestion(++currentQuestionNumber);
                }
            }

            //the user answered incorrectly for the first time
            //show the toast for Hint
            else if (!correctlyAnswered && numberOfClicks < 3) {

                //change button color to red
                clickedButton.setBackgroundColor(Color.RED);
                toastMessage = "Get a HINT by shaking your phone";
                Toast.makeText(QuestionActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
    }

    /**
     * This method fetches JSON data and shows the first question
     *
     * @param numberOfQuestionsPosition
     * @param difficultyLevelPosition
     */
    private void fetchJSONDataAndShowFirstQuestion(final int numberOfQuestionsPosition, final int difficultyLevelPosition) {
        new Thread() {
            public void run(){
                final JSONObject jsonData = FetchJSONData.getJSONData(numberOfQuestionsPosition, difficultyLevelPosition, "");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        createQuestionsAndShowFirstQuestion(jsonData);
                    }
                });
            }
        }.start();
    }

    /**
     * This method creates the question object (using the Question class) from the JSON data and shuffles the answers
     * This method also shows the first question on the app screen
     *
     * @param json
     */
    private void createQuestionsAndShowFirstQuestion(JSONObject json){
        try {
            String responseCode = json.getString("response_code");
            if (responseCode != "" && responseCode.equals("0")){
                questionsList = new ArrayList<>();
                correctAnswers = new ArrayList<>();
                Question question;

                JSONArray questionsAndAnswers = json.getJSONArray("results");

                for (int iQA = 0; iQA < questionsAndAnswers.length(); iQA++) {
                    String correctAnswer = Jsoup.parse(questionsAndAnswers.getJSONObject(iQA).getString("correct_answer")).text();
                    correctAnswers.add(correctAnswer);
                    JSONArray incorrectAnswers = questionsAndAnswers.getJSONObject(iQA).getJSONArray("incorrect_answers");

                    //add all the answers to the array
                    answers = new ArrayList<>();
                    answers.add(Jsoup.parse(questionsAndAnswers.getJSONObject(iQA).getString("correct_answer")).text());
                    answers.add(Jsoup.parse(incorrectAnswers.get(0).toString()).text());
                    answers.add(Jsoup.parse(incorrectAnswers.get(1).toString()).text());
                    answers.add(Jsoup.parse(incorrectAnswers.get(2).toString()).text());

                    //shuffle the answers
                    Collections.shuffle(answers);

                    question = new Question(Jsoup.parse(questionsAndAnswers.getJSONObject(iQA).getString("question")).text(),
                            answers.get(0),
                            answers.get(1),
                            answers.get(2),
                            answers.get(3));

                    questionsList.add(question);
                }

                LinearLayout linearLayout = findViewById(R.id.questionLinearLayout);
                linearLayout.setVisibility(View.VISIBLE);
                //show the first question
                showQuestion(0);
            }
        }

        catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method sets the colors for the four answer choices
     * This method also sets the text values for the question and the four answer choices
     *
     * @param id
     */
    private void showQuestion(int id) {

        //set the button colors
        Button optionOne = findViewById(R.id.optionOne);
        optionOne.setBackgroundColor(Color.WHITE);

        Button optionTwo = findViewById(R.id.optionTwo);
        optionTwo.setBackgroundColor(Color.WHITE);

        Button optionThree = findViewById(R.id.optionThree);
        optionThree.setBackgroundColor(Color.WHITE);

        Button optionFour = findViewById(R.id.optionFour);
        optionFour.setBackgroundColor(Color.WHITE);

        //set the text views
        TextView tvQues = findViewById(R.id.question);
        tvQues.setText(questionsList.get(id).getQuestion());

        TextView tvOptionOne = findViewById(R.id.optionOne);
        tvOptionOne.setText(questionsList.get(id).getOptionChoiceOne());

        TextView tvOptionTwo = findViewById(R.id.optionTwo);
        tvOptionTwo.setText(questionsList.get(id).getOptionChoiceTwo());

        TextView tvOptionThree = findViewById(R.id.optionThree);
        tvOptionThree.setText(questionsList.get(id).getOptionChoiceThree());

        TextView tvOptionFour = findViewById(R.id.optionFour);
        tvOptionFour.setText(questionsList.get(id).getOptionChoiceFour());
    }

    /**
     * This method shows the input dialog when the 'End-Condition' is reached
     *
     */
    private void showInputDialog() {

        final Dialog dialog = new Dialog(QuestionActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_end_condition);

        // set the custom dialog components - text, and buttons
        TextView text = (TextView) dialog.findViewById(R.id.scoreInDialog);
        text.setText("You scored: " + Integer.toString(computedScore) + " out of " + Integer.toString(questionsList.size()));

        Button yesButton = (Button) dialog.findViewById(R.id.yesButton);
        // if yes button is clicked, reload the activity
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.noButton);
        // if no button is clicked, open MainActivity
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                MainActivity.mediaPlayer.reset();
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        });

        dialog.show();
    }

    /**
     * This method handles the shake event when the device is shaken to get a hint to the current question
     * This method fetches JSON data from the Wikipedia API and shows the appropriate hint as toasts to the user
     *
     */
    private void handleShakeEvent(){

        new Thread() {
            public void run(){
                final JSONObject jsonData = FetchJSONData.getJSONData(0,0, correctAnswers.get(currentQuestionNumber));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //get the first line from 'extract' as the hint
                            JSONObject query = (JSONObject) jsonData.get("query");
                            JSONObject pages = (JSONObject) query.get("pages");
                            JSONObject pageDetails = (JSONObject) pages.get(pages.names().get(0).toString());

                            String extract = Jsoup.parse(pageDetails.getString("extract")).text();
                            String hint ="No hints available. Sorry.";
                            String wordInAnswer = "";

                            if (correctAnswers.get(currentQuestionNumber).indexOf(" ") > 1) {
                                wordInAnswer = correctAnswers.get(currentQuestionNumber).substring(0, correctAnswers.get(currentQuestionNumber).indexOf(" "));
                            }
                            else {
                                wordInAnswer = correctAnswers.get(currentQuestionNumber);
                            }

                            if (extract != null && !extract.isEmpty()) {
                                String[] sentences = extract.split("\\.");

                                if (sentences != null) {
                                    if (sentences.length == 1) {
                                        if (!sentences[0].endsWith(":")) {
                                            hint = sentences[0].replace(correctAnswers.get(currentQuestionNumber), "_____");
                                        }
                                    }
                                    else if (sentences.length > 1) {
                                        for (String iS : sentences) {
                                            if (iS.toLowerCase().contains(wordInAnswer.toLowerCase())) {
                                                hint = iS.replace(correctAnswers.get(currentQuestionNumber), "_____");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            Toast toast = Toast.makeText(getApplicationContext(), hint, Toast.LENGTH_LONG);
                            toast.show();
                        }
                        catch (JSONException ex) {
                            Toast toast = Toast.makeText(getApplicationContext(), "No hints available. Sorry.", Toast.LENGTH_LONG);
                            toast.show();
                            ex.printStackTrace();
                        }

                    }
                });
            }
        }.start();
    }

    /**
     * This is the LearnGesture class which tracks the swipe made by an user
     * SimpleOnGestureListener is the listener
     *
     */
    class LearnGesture extends GestureDetector.SimpleOnGestureListener {

        /**
         * onFling method checks which swipe (right-to-left or left-to-right) is performed
         * This method only takes action for a right-to-left swipe
         *
         * @param event1
         * @param event2
         * @param velocityX
         * @param velocityY
         * @return boolean
         */
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event2.getX() > event1.getX()) {
                //left to right
                //do nothing here
            }
            else if (event2.getX() < event1.getX()) {
                //right to left

                //update the current question number
                currentQuestionNumber++;

                correctlyAnswered = false;
                if (currentQuestionNumber < questionsList.size()) {
                    //get new question, display it
                    numberOfClicks = 0;
                    showQuestion(currentQuestionNumber);
                }
                //when the current question number is the last question in the question bank
                else if (currentQuestionNumber == questionsList.size()) {
                    //show dialog box
                    showInputDialog();
                }
            }
            return true;
        }
    }
}