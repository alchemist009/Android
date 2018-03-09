package com.example.wra1th.drawableandanimationdemo;

import android.content.SharedPreferences;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

public class AnimationActivity extends AppCompatActivity {

    ViewGroup containerView;
    Button button;
    TextView textView, mSharedPrefText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        bindViews();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.button_count_sharedPrefs), MODE_PRIVATE);
        String buttonCount = sharedPreferences.getString(getString(R.string.click_count), "No value is stored");

        mSharedPrefText.setText(buttonCount);

        button.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View view) {
                //TransitionManager.beginDelayedTransition(containerView); // For a normal transition
                Transition slideTransition = new Slide(Gravity.RIGHT);
                slideTransition.setStartDelay(500);
                slideTransition.setDuration(200);
                slideTransition.setInterpolator(new FastOutSlowInInterpolator());

                TransitionSet transitionSet = new TransitionSet();
                transitionSet.addTransition(new Fade());
                transitionSet.addTransition(new Slide(Gravity.LEFT));
                TransitionManager.beginDelayedTransition(containerView, transitionSet);
                visible = !visible;
                textView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });


    }

    private void bindViews() {

        button = findViewById(R.id.btn_anim);
        textView = findViewById(R.id.text_anim);
        containerView = findViewById(R.id.container);
        mSharedPrefText = findViewById(R.id.text_shared_pref);
    }
}
