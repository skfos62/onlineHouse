package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class community_image_tagging_URL extends AppCompatActivity {
    ImageView community_tag;
    EditText tagging_name;
    MaterialButton community_write_image_tagging;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_image_tagging_url);

        community_tag = findViewById(R.id.community_tag);
        community_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //태그이름 저장
        tagging_name = findViewById(R.id.tagging_name);
        community_write_image_tagging = findViewById(R.id.community_write_image_tagging);
        community_write_image_tagging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String tag_name = tagging_name.getText().toString() ;
                Bundle bundle = new Bundle();
                bundle.putString("name",tagging_name.getText().toString());
//                intent.putExtra("result", "some value");
                intent.putExtra("name",tag_name);
//                intent.putExtra("name",bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
