package com.takkat.counting.Frag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Model.gEval;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.adapter.DoneAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoneRequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SaveData sD;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private List<gEval> gEval;
    private RecyclerView mRecyRequest;
    private DoneAdapter mAdapter;
    private int limt = 1;
    private TextView txtRequest;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    public DoneRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_done_request, container, false);


        mRecyRequest = view.findViewById(R.id.recycler_view_done_request);
        mSwipeRefreshLayout = view.findViewById(R.id.progress_bar_done_request_fragment);

        txtRequest = view.findViewById(R.id.txt_home_done_request);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.default_glow_color,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
                getRequest(limt);
                getRequest(2);
                getRequest(3);
            }
        });

        return view;
    }


    private void getRequest(int limit) {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyRequest.setLayoutManager(horizontalLayoutManagaer);


/*
        mRecyToday.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

               *//* if (horizontalLayoutManagaer.findLastCompletelyVisibleItemPosition() == gEval.size()-1){

                  //  getRequest(limt);



                }*//*


            }

        });*/

        gEval = new ArrayList<gEval>();
        try {

            requestQueue = Volley.newRequestQueue(getContext());

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/evals?pageNumber=" + limit + "&pageSize=100",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mSwipeRefreshLayout.setRefreshing(false);

                            if (response.equals("لا يوجد اي معاملات")) {


                                txtRequest.setVisibility(View.VISIBLE);
                                txtRequest.setText("لا يوجد اي معاملات");

                            } else {


                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);


                                        gEval eVa = new gEval();

                                        eVa.setEvaluationID(object.getInt("EvaluationID"));
                                        eVa.setEvalAddedDate(object.getString("EvalAddedDate"));
                                        eVa.setSchemaNumber(object.getString("SchemaNumber"));
                                        eVa.setRequestNumber(object.getString("RequestNumber"));
                                        JSONObject jComp = object.getJSONObject("Company");
                                        eVa.setCompName(jComp.getString("Name"));
                                        gEval.add(eVa);

                                    }


                                    mAdapter = new DoneAdapter(getContext(), gEval);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mRecyRequest.setAdapter(mAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    try {
                        Cache.Entry cachEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cachEntry == null) {

                        cachEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefrshed = 3 * 60 * 1000;
                    final long cacheExpired = 24 * 60 * 60 * 1000;
                    long now = System.currentTimeMillis();
                    final long softExpired = now + cacheHitButRefrshed;
                    final long ttl = now + cacheExpired ;
                    cachEntry.data = response.data ;
                    cachEntry.softTtl = softExpired;
                    cachEntry.ttl = ttl ;
                    String headerValue ;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null){
                        cachEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null){
                        cachEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cachEntry.responseHeaders = response.headers ;

                        final String jsonString = new String(response.data , HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString,cachEntry);
                    } catch (UnsupportedEncodingException e) {

                        return Response.error(new ParseError(e));
                    }




                }

                @Override
                protected void deliverResponse(String response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }

                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("which", "myRequest");
                    headers.put("MoqaeemID", sD.Name);
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

    @Override
    public void onRefresh() {

        getRequest(limt);
        getRequest(2);
        getRequest(3);
    }

}
