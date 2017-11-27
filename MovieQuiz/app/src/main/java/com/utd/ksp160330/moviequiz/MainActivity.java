/**
 * Movie Quiz app for  CS6326.001, Final Project
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * version: 2.0: 11/26/2017
 *
 * Description of the activity:
 * This is a Movie Quiz app, which will allow a user (for eg., a movie buff) to answer questions related to Hollywood movie trivia.
 * The app uses four activities - The first activity is the main screen which displays the name of the app,
 * the description of the app and the three buttons: Play, Options and Rules.
 *
 * Clicking on the Play button takes the user to the QuestionActivity where questions get displayed one by one.
 * Clicking on the Options button takes the user to the OptionsActivity where various options can be set by the user.
 * Clicking on the Rules button takes the user to the RulesActivity where the three rules of playing the quiz is displayed.
 *
 * The user can exit the activity and therefore the app anytime by pressing the back key in the android navigation bar.
 *
 */

package com.utd.ksp160330.moviequiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //The following are used for the options set by the user
    public static final String NUMBER_OF_QUESTIONS_POSITION = "NUMBER_OF_QUESTIONS_POSITION";
    public static final String DIFFICULTY_LEVEL_POSITION = "DIFFICULTY_LEVEL_POSITION";
    public static final String SOUND_POSITION = "SOUND_POSITION";

    //The following are used for sound control
    public static MediaPlayer mediaPlayer;
    private int soundPosition;

    /**
     * onCreate method displays the three buttons on the main activity screen
     * This method also determines whether sound stays ON or OFF
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check sound - ON/OFF
        if(getIntent().getSerializableExtra(SOUND_POSITION) != null) {
            soundPosition = (int) getIntent().getSerializableExtra(SOUND_POSITION);
        }

        if (soundPosition == 1) {
            //if soundPosition is 1: turn OFF sound
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
        else {
            //if soundPosition is 0: keep sound ON
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.game_play);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    /**
     * onBackPressed method finishes the main activity when the back key is pressed
     */

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        MainActivity.this.finish();

        //open up the top activity in the stack
        Intent newIntent = new Intent(Intent.ACTION_MAIN);
        newIntent.addCategory(Intent.CATEGORY_HOME);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

    /**
     * onClick method performs an operation based on the button clicked.
     * If the 'start' button is clicked, open QuestionActivity with the chosen number of questions and the chosen difficulty level
     * If the 'options' button is clicked, open OptionsActivity to show the appropriate options to the quiz player.
     * If the 'rules' button is clicked, open RulesActivity to show the rules for playing the quiz
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.b_play:

                //use the below values to pass on to QuestionActivity as intents
                int numberOfQuestionsPosition = 0;
                int difficultyLevelPosition = 0;

                if(getIntent().getSerializableExtra(NUMBER_OF_QUESTIONS_POSITION) != null) {
                    numberOfQuestionsPosition = (int) getIntent().getSerializableExtra(NUMBER_OF_QUESTIONS_POSITION);
                }
                if(getIntent().getSerializableExtra(DIFFICULTY_LEVEL_POSITION) != null) {
                    difficultyLevelPosition = (int) getIntent().getSerializableExtra(DIFFICULTY_LEVEL_POSITION);
                }

                intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra(MainActivity.NUMBER_OF_QUESTIONS_POSITION, numberOfQuestionsPosition);
                intent.putExtra(MainActivity.DIFFICULTY_LEVEL_POSITION, difficultyLevelPosition);

                startActivity(intent);
                break;

            case R.id.b_options:
                intent = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(intent);
                break;

            case R.id.b_rules:
                intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
                break;
        }
    }
}