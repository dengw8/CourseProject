package com.example.jiamoufang.threekingdoms.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jiamoufang.threekingdoms.MainActivity;
import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.adapter.SelectHeroToEditorAdapter;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;

import static com.example.jiamoufang.threekingdoms.MainActivity.NonEditedHeroList;

public class SelectHeroToEdite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hero_to_edite);

        SelectHeroToEditorAdapter editorAdapter = new SelectHeroToEditorAdapter(SelectHeroToEdite.this,R.layout.non_edited_hero_item, NonEditedHeroList);

        ListView noneEditedListView = (ListView) findViewById(R.id.non_edited_listview);

        noneEditedListView.setAdapter(editorAdapter);
        noneEditedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NonEditedHero nonEditedHero = NonEditedHeroList.get(position);
                Intent ret = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("imageId", nonEditedHero.getImageViewId());
                bundle.putString("heroName", nonEditedHero.getHeroName());
                ret.putExtras(bundle);
                setResult(RESULT_OK, ret);
                finish();
            }
        });
    }
}
