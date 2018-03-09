package com.example.wra1th.drawableandanimationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.transitionseverywhere.TransitionManager;

public class AnimationActivity extends AppCompatActivity {

    ViewGroup containerView;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        bindViews();
        button.setOnClickListener(new View.OnClickListener() {
            boolean visible;

            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(containerView);
                visible = !visible;
                textView.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void bindViews() {

        button = findViewById(R.id.btn_anim);
        textView = findViewById(R.id.text_anim);
        containerView = findViewById(R.id.container);
    }
}
