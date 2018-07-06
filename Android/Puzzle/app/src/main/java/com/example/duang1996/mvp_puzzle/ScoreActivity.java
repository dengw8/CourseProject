package com.example.duang1996.mvp_puzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import config.Config;

public class ScoreActivity extends Activity {
    private TextView timeValue;
    private TextView stepValue;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        timeValue = findViewById(R.id.timeValue);
        stepValue = findViewById(R.id.stepValue);
        button = findViewById(R.id.restart);

        timeValue.setText("" + Config.time);
        stepValue.setText("" + Config.bushu);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, SelectImage.class);
                startActivity(intent);
            }
        });
    }
}
