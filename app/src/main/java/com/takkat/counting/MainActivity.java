package com.takkat.counting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.Frag.AcceptRequestFragment;
import com.takkat.counting.Frag.DoneRequestFragment;
import com.takkat.counting.Frag.HomeRequestFragment;
import com.takkat.counting.Frag.NewRequestFragment;
import com.takkat.counting.Frag.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.takkat.counting.volleyy.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SaveData sD ;

    private NewRequestFragment newReq = new NewRequestFragment();
    private AcceptRequestFragment accReq = new AcceptRequestFragment();
    private HomeRequestFragment homeReq  = new HomeRequestFragment();
    private DoneRequestFragment doneReq = new DoneRequestFragment();
    private ProfileFragment profile = new ProfileFragment();
    private BottomNavigationView mNavigation ;

    private ImageView iLogOut ;

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;

    private Domain domain ;

    private TextView txtName ;

    private String sName , sEmail , sPhone , sMobile ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lanlanguageg();
        sD = new SaveData(MainActivity.this);
        sD.LoadData();
        idMain();


        txtName = findViewById(R.id.txt_get_name_moaaen);
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putString("name", sName);
                args.putString("email", sEmail);
                args.putString("phone", sPhone);
                args.putString("mobile", sMobile);
                ProfileDialog dialog = new ProfileDialog();
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "MyCustomerLocationDailog");
            }
        });
        iLogOut = findViewById(R.id.image_log_out);
        iLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sD.LogOutM();
                finish();
            }
        });


        getProfile();


    }

    private void idMain(){


        mNavigation = findViewById(R.id.bottom_navigation);
        setFragment(homeReq);

        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){


                    case  R.id.item_home_request :

                        setFragment(homeReq);
                        return true;

                    case  R.id.item_new_request :

                        setFragment(newReq);
                        return true;

                    case R.id.item_accept_request :

                        setFragment(accReq);
                        return true;


                    case R.id.item_done :

                        setFragment(doneReq);
                        return true;

/*


                    case R.id.item_profile :

                        setFragment(profile);
                        return true;

*/


                    default:
                        return false;
                }
            }


        });

    }

    private void lanlanguageg() {
        try {

            //  Databas(this);
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            this.setContentView(R.layout.activity_main);


        } catch (NullPointerException e) {

        }

    }


    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main ,fragment);
        fragmentTransaction.commit();

    }



    private void a(){


        String url = "api.openweathermap.org/data/2.5/weather?q=London";

        JSONArray a = new JSONArray();
        a.put("ss");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                    }
                })
        {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", a);
                headers.put("apiKey", "");
                return headers;
            }

            @Override
            protected Map<String, String>  getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", "");
                params.put("password", "");
                params.put("grant_type", "password");

                return params;
            }
        };
// Adding the request to the queue along with a unique string tag
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, "headerRequest");
    }


    private void getProfile() {


        try {

            requestQueue = Volley.newRequestQueue(MainActivity.this);

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL +
                    "/user/user",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);


                                sName = jsonObject.getString("displayName");
                                txtName.setText(sName);

                                sPhone = jsonObject.getString("phoneNumber");

                                sEmail = jsonObject.getString("email");

                                sMobile = jsonObject.getString("mobileNumber");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();

                    headers.put("ID", sD.Name);
                    headers.put("Authorization", "bearer " + sD.Token);
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
