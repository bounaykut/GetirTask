package com.example.aykut.getirtask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowRecords extends AppCompatActivity {


    private int FIRST_PAGE_NO = 0;
    private int LAST_PAGE_NO = 0;
    private int TOTAL_LAST_PAGE_ITEM = 0;
    private final int ITEMS_PER_PAGE = 10;

    ArrayList<String> items;
    ArrayList<String> item10;
    ArrayAdapter<String> adapter;

    ListView listView;
    Button nextButton, prevButton;

    int pageNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);

        final String response = MainActivity.StaticData.data;

        listView = (ListView) findViewById(R.id.listView);

        items = new ArrayList<>();
        item10 = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(response);
            JSONArray records = json.getJSONArray("records");
            for (int i = 0; i < records.length(); i++) {
                int count = (records.getJSONObject(i)).getInt("totalCount");
                if (i < 10) items.add(i + ". " + count);
                item10.add(i + ". " + count);
            }
            TOTAL_LAST_PAGE_ITEM = records.length() % ITEMS_PER_PAGE;
            LAST_PAGE_NO = (records.length() / ITEMS_PER_PAGE) - 1;
            if (TOTAL_LAST_PAGE_ITEM != 0) LAST_PAGE_NO++;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<String>(this, R.layout.adapter_view, items);

        listView.setAdapter(adapter);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextListPage();
            }
        });

        prevButton = (Button) findViewById(R.id.prevButton);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrevListPage();
            }
        });

    }


    public void setNextListPage() {

        if (pageNo == LAST_PAGE_NO) {
            nextButton.setEnabled(false);
            nextButton.setClickable(false);
            nextButton.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
            adapter.clear();
            items.addAll(item10.subList(pageNo*10,(pageNo*10+TOTAL_LAST_PAGE_ITEM)));
        } else {
            adapter.clear();
            items.addAll(item10.subList(pageNo * 10, (pageNo + 1) * 10));
            pageNo++;
        }


        if (!prevButton.isEnabled()) {
            prevButton.setEnabled(true);
            prevButton.setClickable(true);
            prevButton.setBackgroundColor(getResources().getColor(R.color.colorEnabled));
        }


    }

    public void setPrevListPage() {

        if (pageNo == FIRST_PAGE_NO) {
            prevButton.setEnabled(false);
            prevButton.setClickable(false);
            prevButton.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        } else {
            adapter.clear();
            pageNo--;
            items.addAll(item10.subList(pageNo * 10, (pageNo + 1) * 10));
        }


        if (!nextButton.isEnabled()) {
            nextButton.setEnabled(true);
            nextButton.setClickable(true);
            nextButton.setBackgroundColor(getResources().getColor(R.color.colorEnabled));
        }


    }


}
