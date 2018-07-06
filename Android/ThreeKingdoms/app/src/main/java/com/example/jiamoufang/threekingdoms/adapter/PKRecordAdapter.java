package com.example.jiamoufang.threekingdoms.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jiamoufang.threekingdoms.R;
import com.example.jiamoufang.threekingdoms.entities.PkRecords;

import java.util.List;

/**
 * Created by jiamoufang on 2017/11/5.
 */

public class PKRecordAdapter extends ArrayAdapter {
    private int resourceId;

    public PKRecordAdapter(Context context, int pkItemId, List<PkRecords> pkRecordsList) {
        super(context,pkItemId,pkRecordsList);
        resourceId = pkItemId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PkRecords pkRecords = (PkRecords) getItem(position); // fetch current object
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
        TextView text_record_time = (TextView) view.findViewById(R.id.text_record_time);
        TextView text_record_heros = (TextView) view.findViewById(R.id.text_record_heros);
        TextView text_record_result = (TextView) view.findViewById(R.id.text_record_result);
        text_record_time.setText(pkRecords.getPKtime());
        text_record_heros.setText(pkRecords.getHeroNameA() + " VS " + pkRecords.getHeroNameB());
        text_record_result.setText(pkRecords.getPKresult());
        return view;
    }
}
