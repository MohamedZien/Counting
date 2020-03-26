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
import com.takkat.counting.Model.gEval;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.adapter.NewRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewRequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private SaveData sD;
    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private Domain domain;
    private List<gEval> gEval;
    private RecyclerView mRecy;
    private NewRequestAdapter mAdapter;
    private int limt = 1;
    private TextView txtReq ;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NewRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_request, container, false);

        mRecy = view.findViewById(R.id.recycler_view_new_request);
        mSwipeRefreshLayout = view.findViewById(R.id.progress_bar_new_request_fragment);

        txtReq = view.findViewById(R.id.txt_new_new_request);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecy.setLayoutManager(horizontalLayoutManagaer);

/*
        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {

                    Toast.makeText(getContext(), "la", Toast.LENGTH_SHORT).show();

                }
            }
        });*/

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
            }
        });

        return view;
    }

    private void getRequest(int limit) {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


        gEval = new ArrayList<gEval>();
        try {

            requestQueue = Volley.newRequestQueue(getContext());

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL + "/eval/evals?pageNumber=1&pageSize=100",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        gEval eVa = new gEval();

                                        eVa.setEvaluationID(object.getInt("EvaluationID"));
                                        eVa.setRequestNumber(object.getString("RequestNumber"));
                                        eVa.setEvalAddedDate(object.getString("EvalAddedDate"));
                                        eVa.setSchemaNumber(object.getString("SchemaNumber"));
                                        JSONObject jComp = object.getJSONObject("Company");
                                        eVa.setCompName(jComp.getString("Name"));
                                        gEval.add(eVa);

                                    }

                                    mAdapter = new NewRequestAdapter(getContext(), gEval);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mRecy.setAdapter(mAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();



                }

            }) {


                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("which", "noAction");
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
    }

}
