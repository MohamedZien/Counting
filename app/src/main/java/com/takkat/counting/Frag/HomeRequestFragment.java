package com.takkat.counting.Frag;


import android.database.Cursor;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.takkat.counting.Domain.Domain;
import com.takkat.counting.Model.EvalOther;
import com.takkat.counting.Model.EvalToday;
import com.takkat.counting.Model.EvalTomorrow;
import com.takkat.counting.Model.gEval;
import com.takkat.counting.R;
import com.takkat.counting.Domain.SaveData;
import com.takkat.counting.adapter.DoneAdapter;
import com.takkat.counting.adapter.OtherAdapter;
import com.takkat.counting.adapter.ToDayAdapter;
import com.takkat.counting.adapter.TomorrowAdapter;
import com.takkat.counting.netWor.CheckNetwork;
import com.takkat.counting.storgeLocal.adapterLocal.AdapterLocalTomorow;
import com.takkat.counting.storgeLocal.modelLocal.ModelLocalTomorow;
import com.takkat.counting.storgeLocal.sqlLocal.SQLiteHelperToDay;
import com.takkat.counting.storgeLocal.adapterLocal.AdapterLocalToday;
import com.takkat.counting.storgeLocal.modelLocal.ModelLocalToDay;
import com.takkat.counting.storgeLocal.sqlLocal.SQLiteHelperTomorow;

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
public class HomeRequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SaveData sD;
    private RequestQueue requestQueueToDay, requestQueueTomorrow, requestQueueOther;
    private StringRequest jsonObjectRequestToDay, jsonObjectRequestTomorrow, jsonObjectRequestOther;
    private Domain domain;
    private List<EvalToday> gEval;
    private List<EvalTomorrow> gEvalTomorrow;
    private List<EvalOther> gEvalOther;
    private RecyclerView mRecyToday, mRecTomorrow, mRecOther;
    private ToDayAdapter mAdapter;
    private TomorrowAdapter mAdapterTomorrow;
    private OtherAdapter mAdapterOther;
    private int limt = 1;
    private TextView txtToday, txtTomorro, txtOther;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RequestQueue requestQueue;
    private StringRequest jsonObjectRequest;
    private List<com.takkat.counting.Model.gEval> gEvall;
    private DoneAdapter mAdapterDone;


    private static SQLiteHelperToDay sqLiteHelperToday;
    private List<ModelLocalToDay> modelLocalToDayList;
    private AdapterLocalToday adapterLocalToday;

    private static SQLiteHelperTomorow sqLiteHelperTomorow;
    private List<ModelLocalTomorow> modelLocalTomorowList;
    private AdapterLocalTomorow adapterLocalTomorow;

    public HomeRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_request, container, false);

        createTabeInDataBaseToDay();
        createTabeInDataBaseTomorow();

        idddd();

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if (CheckNetwork.isInternetAvailable(getContext())) {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                    getToday(limt);
                    getTomorrow(limt);
                    getRequestThree(limt);

                } else {

                    mSwipeRefreshLayout.setRefreshing(false);
                    localToday();
                    localTomorow();
                }
            }


        });


        return view;
    }


    private void idddd(){
        mRecyToday = view.findViewById(R.id.recycler_view_today_request);
        mRecTomorrow = view.findViewById(R.id.recycler_view_tomorrow_request);
        mRecOther = view.findViewById(R.id.recycler_view_other_request);

        mSwipeRefreshLayout = view.findViewById(R.id.progress_bar_home_request_fragment);

        txtToday = view.findViewById(R.id.txt_home_today_request);
        txtTomorro = view.findViewById(R.id.txt_home_tomorrow_request);
        txtOther = view.findViewById(R.id.txt_home_other_request);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.default_glow_color,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    private void getToday(int limit) {

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyToday.setLayoutManager(horizontalLayoutManagaer);

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


/*
        mRecyToday.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

               *//* if (horizontalLayoutManagaer.findLastCompletelyVisibleItemPosition() == gEval.size()-1){

                  //  getRequest(limt);



                }*//*


            }

        });*/

        sqLiteHelperToday.deleteAll();

        gEval = new ArrayList<EvalToday>();
        try {

            requestQueueToDay = Volley.newRequestQueue(getContext());

            jsonObjectRequestToDay = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/evals?pageNumber=" + limit + "&pageSize=100",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mSwipeRefreshLayout.setRefreshing(false);

                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);


                                    EvalToday eVa = new EvalToday();

                                    eVa.setEvaluationID(object.getInt("EvaluationID"));
                                    eVa.setEvalAddedDate(object.getString("EvalAddedDate"));
                                    eVa.setSchemaNumber(object.getString("SchemaNumber"));
                                    eVa.setRequestNumber(object.getString("RequestNumber"));
                                    JSONObject jComp = object.getJSONObject("Company");
                                    eVa.setCompName(jComp.getString("Name"));
                                    gEval.add(eVa);


                                    sqLiteHelperToday.insertData(
                                            eVa.getEvaluationID(),
                                            eVa.getRequestNumber()
                                    );


                                }


                                mAdapter = new ToDayAdapter(getContext(), gEval);
                                mSwipeRefreshLayout.setRefreshing(false);
                                mRecyToday.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("which", "today");
                    headers.put("MoqaeemID", sD.Name);
                    headers.put("Authorization", "bearer " + sD.Token);

                    return headers;
                }
            };
            jsonObjectRequestToDay.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueueToDay.add(jsonObjectRequestToDay);
        } catch (Exception e) {

        }

    }

    private void getTomorrow(int limit) {

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecTomorrow.setLayoutManager(horizontalLayoutManagaer);

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


/*
        mRecTomorrow.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

               *//* if (horizontalLayoutManagaer.findLastCompletelyVisibleItemPosition() == gEval.size()-1){

                  //  getRequest(limt);



                }*//*


            }

        });*/

        sqLiteHelperTomorow.deleteAll();
        gEvalTomorrow = new ArrayList<EvalTomorrow>();
        try {

            requestQueueTomorrow = Volley.newRequestQueue(getContext());

            jsonObjectRequestTomorrow = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/evals?pageNumber=" + limit + "&pageSize=20",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (response.equals("لا يوجد اي معاملات")) {

                                txtTomorro.setVisibility(View.VISIBLE);
                                txtTomorro.setText("لا يوجد اي معاملات");

                            } else {

                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);


                                        EvalTomorrow eVa = new EvalTomorrow();

                                        eVa.setEvaluationID(object.getInt("EvaluationID"));
                                        eVa.setEvalAddedDate(object.getString("EvalAddedDate"));
                                        eVa.setSchemaNumber(object.getString("SchemaNumber"));
                                        eVa.setRequestNumber(object.getString("RequestNumber"));
                                        JSONObject jComp = object.getJSONObject("Company");
                                        eVa.setCompName(jComp.getString("Name"));
                                        gEvalTomorrow.add(eVa);

                                        sqLiteHelperTomorow.insertData(
                                                eVa.getEvaluationID(),
                                                eVa.getRequestNumber()
                                        );
                                    }


                                    mAdapterTomorrow = new TomorrowAdapter(getContext(), gEvalTomorrow);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mRecTomorrow.setAdapter(mAdapterTomorrow);

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
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("which", "tomorrow");
                    headers.put("MoqaeemID", sD.Name);
                    headers.put("Authorization", "bearer " + sD.Token);
                    return headers;
                }
            };
            jsonObjectRequestTomorrow.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueueTomorrow.add(jsonObjectRequestTomorrow);
        } catch (Exception e) {

        }

    }

    private void getRequestThree(int limit) {

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecOther.setLayoutManager(horizontalLayoutManagaer);

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }


/*
        mRecyToday.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

               *//* if (horizontalLayoutManagaer.findLastCompletelyVisibleItemPosition() == gEval.size()-1){

                  //  getRequest(limt);



                }*//*


            }

        });*/

        gEvall = new ArrayList<gEval>();
        try {

            requestQueue = Volley.newRequestQueue(getContext());

            jsonObjectRequest = new StringRequest(Request.Method.GET, domain.URL +
                    "/eval/evals?pageNumber=" + limit + "&pageSize=20",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mSwipeRefreshLayout.setRefreshing(false);
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
                                    gEvall.add(eVa);

                                }


                                mAdapterDone = new DoneAdapter(getContext(), gEvall);
                                mSwipeRefreshLayout.setRefreshing(false);
                                mRecOther.setAdapter(mAdapterDone);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }) {
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("which", "other");
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

        if (CheckNetwork.isInternetAvailable(getContext())) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            getToday(limt);
            getTomorrow(limt);
            getRequestThree(limt);

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            localToday();
            localTomorow();
        }

}


    private void localToday() {


        modelLocalToDayList = new ArrayList<>();


        Cursor cursor = HomeRequestFragment.sqLiteHelperToday.getData("SELECT * FROM TODAY");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            int name = cursor.getInt(1);
            String price = cursor.getString(2);


            ModelLocalToDay modelLocalToDay = new ModelLocalToDay(name, price);

            modelLocalToDayList.add(modelLocalToDay);

            adapterLocalToday = new AdapterLocalToday(getContext(), modelLocalToDayList);
            mRecyToday.setAdapter(adapterLocalToday);

        }
    }
    private void localTomorow() {


        modelLocalTomorowList = new ArrayList<>();


        Cursor cursor = HomeRequestFragment.sqLiteHelperTomorow.getData("SELECT * FROM TOMOROW");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            int name = cursor.getInt(1);
            String price = cursor.getString(2);


            ModelLocalTomorow modelLocalToDay = new ModelLocalTomorow(name, price);

            modelLocalTomorowList.add(modelLocalToDay);

            adapterLocalTomorow = new AdapterLocalTomorow(getContext(), modelLocalTomorowList);
            mRecTomorrow.setAdapter(adapterLocalTomorow);

        }
    }

    private void createTabeInDataBaseToDay() {
        sqLiteHelperToday = new SQLiteHelperToDay(getContext(), "FoodDB.sqlite", null, 1);
        sqLiteHelperToday.queryData("CREATE TABLE IF NOT EXISTS TODAY " +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR)");


    }

    private void createTabeInDataBaseTomorow() {
        sqLiteHelperTomorow = new SQLiteHelperTomorow(getContext(), "FoodDB.sqlite", null, 1);
        sqLiteHelperTomorow.queryData("CREATE TABLE IF NOT EXISTS TOMOROW " +
                "(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR)");


    }


}
