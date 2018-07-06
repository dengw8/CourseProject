package com.example.duang1996.birthdaymemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {
    private Button addBtn;
    private EditText nameInput;
    private EditText birthInput;
    private EditText giftInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initElement();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "名字为空，请完善", Toast.LENGTH_SHORT).show();
                    return;
                }
                String birth = birthInput.getText().toString();
                String gift = giftInput.getText().toString();
                Intent mIntent = new Intent();
                mIntent.putExtra("name", name);
                mIntent.putExtra("birth", birth);
                mIntent.putExtra("gift", gift);
                setResult(0, mIntent);
                finish();
            }
        });

    }

    private void initElement() {
        addBtn = findViewById(R.id.addBtn);
        nameInput = findViewById(R.id.nameInput);
        birthInput = findViewById(R.id.birthInput);
        giftInput = findViewById(R.id.giftInput);
    }
}
