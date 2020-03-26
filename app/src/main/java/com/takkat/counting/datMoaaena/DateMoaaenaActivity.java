package com.takkat.counting.datMoaaena;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DateMoaaenaActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;

    private int Year, Month, Day;

    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";

    private Calendar calendar;
    private static final String TAG = "Date_Moaina_Activity";
    private Domain domain;
    private SaveData sD;
    private int sRequestID;
    private int tiYear, tiMonth, tiDay , tiHour , tiMinute;

    private String sTime , sDate , sTotalTD ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_moaaena);

        sRequestID = getIntent().getIntExtra("idRequest" , 0);
        Calendar c = Calendar.getInstance();

        tiYear = c.get(Calendar.YEAR);
        tiMonth = c.get(Calendar.MONTH);
        tiDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dialog = new DatePickerDialog(DateMoaaenaActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                sDate = year + "-" +  (month +1 ) + "-" + dayOfMonth ;

                Toast.makeText(DateMoaaenaActivity.this, " " + sDate, Toast.LENGTH_SHORT).show();
                timeRequest(sDate);

                finish();

            }
        }, tiYear, tiMonth, tiDay);

        dialog.show();


      /*  calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        sRequestID = getIntent().getIntExtra("idRequest", 0);
        Toast.makeText(this, " " + sRequestID, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: Date_Moaina_Activity " + sRequestID);


        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", java.util.Locale.getDefault());


        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "",
                "OK",
                "Cancel"
        );

// Assign values
        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.set24HoursMode(true);
        dateTimeDialogFragment.setDefaultDateTime(new GregorianCalendar(2020, Calendar.MARCH, Month, Day, Year).getTime());

// Define new day and month format
        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

// Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                // Do something
                Log.d(TAG, "onCreate: myDateFormat : " + myDateFormat.format(date));

                // timeRequest(myDateFormat.format(date));



                String ymd = Year + "-" + Month + "-" + Day;
                  //  timeRequest(ymd);
                //  timeRequest(ymd);
                   String gYMD = String.valueOf(date.getYear());

                Toast.makeText(DateMoaaenaActivity.this, "" + gYMD, Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
//                Creat_date_moaina(myDateFormat.format(date));

                finish();
            }
        });

// Show
        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");

*/
            }







    private void timeRequest(String date) {

        try {

            requestQueue = Volley.newRequestQueue(DateMoaaenaActivity.this);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +
                    "/moqaeemActions/addMeatingDate",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toast.makeText(DateMoaaenaActivity.this, " " + response, Toast.LENGTH_SHORT).show();

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DateMoaaenaActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();


                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "bearer " + sD.Token);
                    headers.put("EvalID", String.valueOf(sRequestID));
                    headers.put("MeatingDate", date + "T14:04:29.097");
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }


    }


}
