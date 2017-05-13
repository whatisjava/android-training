package com.whatisjava.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whatisjava.training.build_your_first_app.FirstActivity;
import com.whatisjava.training.building_a_dynamic_ui_with_fragments.FragmentTestActivity;
import com.whatisjava.training.saving_data.SqliteTestActivity;

import java.util.ArrayList;

/**
 * Created by whatisjava on 17-5-10.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.list_view);
        mMainAdapter = new MainAdapter(this);
        mListView.setAdapter(mMainAdapter);
        mListView.setOnItemClickListener(this);
        assign();
    }

    private void assign() {
        ArrayList<String> items = new ArrayList<String>();

        items.add("build you first app");
        items.add("supporting different devices");
        items.add("building a dynamic ui with fragments");
        items.add("saving data");

        mMainAdapter.assign(items);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, FirstActivity.class));
                break;
            case 1:
//                startActivity(new Intent(this, CameraTest.class));
                break;
            case 2:
                startActivity(new Intent(this, FragmentTestActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, SqliteTestActivity.class));
                break;
            default:
                break;
        }
    }

}
