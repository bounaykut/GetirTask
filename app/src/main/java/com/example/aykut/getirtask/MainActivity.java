package com.example.aykut.getirtask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    ImageButton startDateButton, endDateButton;
    EditText minCountText, maxCountText;
    Button postButton;

    JsonObjectRequest jsonObjectRequest;
    RequestQueue queue;
    public static final String TAG = "getirpost";
    String url ="https://getir-bitaksi-hackathon.herokuapp.com/searchRecords";


    String startDate, endDate = "2018-10-10";
    int minCount, maxCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);


        startDateButton = (ImageButton) findViewById(R.id.startYearButton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("which","start");
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        endDateButton = (ImageButton) findViewById(R.id.endYearButton);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("which","end");
                newFragment.setArguments(bundle);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


        minCountText = (EditText) findViewById(R.id.minCountText);

        maxCountText = (EditText) findViewById(R.id.maxCountText);


        postButton = (Button) findViewById(R.id.sendButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String min = minCountText.getText().toString();
                String max = maxCountText.getText().toString();

                minCount = (!min.equals("")) ? Integer.parseInt(minCountText.getText().toString()) : 0;
                maxCount = (!max.equals("")) ? Integer.parseInt(maxCountText.getText().toString()) : 0;


                JSONObject data = new JSONObject();
                try {
                    data.put("startDate",startDate);
                    data.put("endDate",endDate);
                    data.put("minCount",minCount);
                    data.put("maxCount",maxCount);

                    Log.d("aykut",data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("aykut",response.toString()+" responseeeee");
                                StaticData.data = response.toString();
                                Intent intent = new Intent(MainActivity.this,ShowRecords.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.d("aykut",error.getMessage());
                            }
                        }
                );


                jsonObjectRequest.setTag(TAG);

                queue.add(jsonObjectRequest);



            }
        });


    }




    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String which = getArguments().getString("which");

            if(which.equals("start")){

                if(day < 10 && month < 10){
                    ( (MainActivity) getActivity() ).startDate = year+"-0"+(month+1)+"-0"+day;
                }else if(day < 10){
                    ( (MainActivity) getActivity() ).startDate = year+"-"+(month+1)+"-0"+day;
                }else if(month < 10){
                    ( (MainActivity) getActivity() ).startDate = year+"-0"+(month+1)+"-"+day;
                }else{
                    ( (MainActivity) getActivity() ).startDate = year+"-"+(month+1)+"-"+day;
                }


            }else if(which.equals("end")){

                if(day < 10 && month < 10){
                    ( (MainActivity) getActivity() ).endDate = year+"-0"+(month+1)+"-0"+day;
                }else if(day < 10){
                    ( (MainActivity) getActivity() ).endDate = year+"-"+(month+1)+"-0"+day;
                }else if(month < 10){
                    ( (MainActivity) getActivity() ).endDate = year+"-0"+(month+1)+"-"+day;
                }else{
                    ( (MainActivity) getActivity() ).endDate = year+"-"+(month+1)+"-"+day;
                }

            }

        }
    }


    public static class StaticData{

        static String data = "";

    }

}
