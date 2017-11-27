/**
 * Options activity Screen for Movie Quiz app
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * Description of the activity:
 * This activity allows the user to choose options related to the user experience while using the app.
 *
 * This activity has three items:
 * 1. No. of questions to answer: Three radio buttons to show three options (5, 10, 15) as the no. of questions
 * the user wants to answer. Setting this number will allow the app to pick that many questions from the question bank.
 *
 * 2. Difficulty Level: Three radio buttons to show three options (“Casual Film Watcher”, “I know my stuff”, “Legendary”).
 * Setting the level here by choosing one of the radio buttons, along with the no. of questions (in the previous option)
 * will allow the app to pick the correct level and the correct no. of questions from the question bank.
 *
 * If the user does not choose any of the two options mentioned above, the defaults for both the options will be chosen.
 * Default for (1) is "5" questions and default for (2) is “Casual Film Watcher”.
 *
 * 3. Sound – Two radio buttons to show two choices (On, Off). Default is "On".
 *
 * After the user selects these options and clicks the 'SET' button, the user will go back to the MainActivity with these options.
 * The chosen options will get reflected once the user starts by pressing the “Play” button in MainActivity,
 * except Sound (which gets affected as soon as MainActivity starts)
 *
 */

package com.utd.ksp160330.moviequiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    //The following are used for the three groups of radio buttons present on the options screen
    private RadioGroup radioNumberOfQuestionsGroup;
    private RadioButton radioNumberOfQuestionsButton;

    private RadioGroup radioDifficultyLevelGroup;
    private RadioButton radioDifficultyLevelButton;

    private RadioGroup radioSoundGroup;
    private RadioButton radioSoundButton;

    /**
     * onCreate method displays the options available for the user on the options activity screen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        radioNumberOfQuestionsGroup = findViewById(R.id.radioNumberOfQuestions);
        radioDifficultyLevelGroup = findViewById(R.id.radioDifficultyLevel);
        radioSoundGroup = findViewById(R.id.radioSound);
    }

    /**
     * onClick method for the 'SET' button
     * This method starts MainActivity with the selected value of the radio buttons
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        // get selected radio buttons from the three radioGroups
        int selectedNumberOfQuestions = radioNumberOfQuestionsGroup.getCheckedRadioButtonId();
        int selectedDifficultyLevel = radioDifficultyLevelGroup.getCheckedRadioButtonId();
        int selectedSound = radioSoundGroup.getCheckedRadioButtonId();

        // find the radio buttons by returned ids
        radioNumberOfQuestionsButton = findViewById(selectedNumberOfQuestions);
        radioDifficultyLevelButton = findViewById(selectedDifficultyLevel);
        radioSoundButton = findViewById(selectedSound);

        //get positions of the three radio buttons
        int numberOfQuestionsPosition = radioNumberOfQuestionsGroup.indexOfChild(radioNumberOfQuestionsButton);
        int difficultyLevelPosition = radioDifficultyLevelGroup.indexOfChild(radioDifficultyLevelButton);
        int soundPosition = radioSoundGroup.indexOfChild(radioSoundButton);

        //start MainActivity with the above button positions as intents
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MainActivity.NUMBER_OF_QUESTIONS_POSITION, numberOfQuestionsPosition);
        intent.putExtra(MainActivity.DIFFICULTY_LEVEL_POSITION, difficultyLevelPosition);
        intent.putExtra(MainActivity.SOUND_POSITION, soundPosition);

        MainActivity.mediaPlayer.reset();

        startActivity(intent);
    }

    /**
     * onBackPressed method finishes the options activity when the back key is pressed
     */
    @Override
    public void onBackPressed() {
        OptionsActivity.this.finish();
    }
}