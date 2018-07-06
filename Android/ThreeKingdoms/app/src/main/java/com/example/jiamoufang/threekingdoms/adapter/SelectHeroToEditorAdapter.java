package com.example.jiamoufang.threekingdoms.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.entities.NonEditedHero;

import java.util.List;

/**
 * Created by jiamoufang on 2017/11/18.
 */

public class SelectHeroToEditorAdapter extends ArrayAdapter<NonEditedHero> {
    private int ItemlayoutId;

    public SelectHeroToEditorAdapter(Context context, int layoutId, List<NonEditedHero> objects) {
        super(context, layoutId, objects);
        this .ItemlayoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NonEditedHero nonEditedHero = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(ItemlayoutId, parent,false);
        ImageView heroImage = (ImageView) view.findViewById(R.id.editor_image);
        TextView heroName = (TextView) view.findViewById(R.id.text_editor);
        heroImage.setImageResource(nonEditedHero.getImageViewId());
        heroName.setText(nonEditedHero.getHeroName());
        return view;
    }
}
