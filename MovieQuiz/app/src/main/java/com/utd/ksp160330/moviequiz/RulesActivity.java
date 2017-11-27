/**
 * Rules activity Screen for Movie Quiz app
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * Description of the activity:
 * This activity shows the rules for playing this game. The rules are as follows:
 * 1. There are only TWO (in bold) chances to guess the correct answer
 * 2. Swipe RIGHT (in bold) to go to the next question
 * 3. You can SHAKE (in bold) the phone to get a hint to the current question
 *
 */

package com.utd.ksp160330.moviequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class RulesActivity extends AppCompatActivity {

    /**
     * onCreate method displays the rules for playing the quiz to the user on the rules activity screen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        TextView tvRulesList = findViewById(R.id.tvRules);
        tvRulesList.setText(Html.fromHtml(getString(R.string.rulesBulletList)));
    }

    /**
     * onBackPressed method finishes the rules activity when the back key is pressed
     */
    @Override
    public void onBackPressed() {
        RulesActivity.this.finish();
    }
}
