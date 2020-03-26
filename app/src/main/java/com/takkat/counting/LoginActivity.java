package com.takkat.counting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Domain.SaveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView txtUser , txtPass ;
    private Button btnLogin ;

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;

    private Domain domain ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idLogin();



    }


    private void idLogin(){

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });
        txtUser = findViewById(R.id.txt_user_name);
        txtPass = findViewById(R.id.txt_password);
    }

    private void login( final String names, final String password) {

        try {

            requestQueue = Volley.newRequestQueue(this);

            jsonObjectRequest = new StringRequest(Request.Method.POST, domain.URL +"/token", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject j = new JSONObject(response);

                        String name = j.getString("user_ID");
                        String tok = j.getString("access_token");

                        SaveData.Name = name;
                        SaveData.Token = tok ;
                        SaveData.Password = txtPass.getText().toString();

                        SaveData sv = new SaveData(LoginActivity.this);
                        sv.SaveData();


                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(intent);

                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(LoginActivity.this, "يوجد خطأ في البيانات...", Toast.LENGTH_SHORT).show();


                }

            }) {


                @Override
                protected Map<String, String>  getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", names);
                    params.put("password", password);
                    params.put("grant_type", "password");

                    return params;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {

        }

    }



    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(3);
        super.onBackPressed();
    }
}
