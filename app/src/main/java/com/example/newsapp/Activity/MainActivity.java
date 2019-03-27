package com.example.newsapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.Adapter.NewsListAdapter;
import com.example.newsapp.Model.NewsListModel;
import com.example.newsapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Activity context;
    private RecyclerView rv_newsList;
    private ImageView img_noInternet;
    private NewsListAdapter adapter;
    private ArrayList<NewsListModel> newsListModelArrayList;
    private String URL = "https://newsapi.org/v2/top-headlines?apiKey=83019f880e3b49fe9886a1df5c3a077a&country=id&category=technology";//"https://newsapi.org/v2/top-headlines";//?country=us&apiKey=API_KEY";
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        TAG = getClass().getName();
        context = MainActivity.this;
        rv_newsList = findViewById(R.id.rv_newsList);
        img_noInternet = findViewById(R.id.img_noInternet);
        newsListModelArrayList = new ArrayList<>();

        setRecyclerview(rv_newsList);

    }

    public void setRecyclerview(RecyclerView recyclerview) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(linearLayoutManager);

        adapter = new NewsListAdapter(context, newsListModelArrayList);
        recyclerview.setAdapter(adapter);

        getData();
    }

    private void getData() {

        if (isNetworkAvailable(context)) {
            setDataLayout();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                String status = response.has("status") ? response.getString("status") : "Not Available";
                                if (response.has("articles")) {
                                    JSONArray articlesArray = response.getJSONArray("articles");
                                    for (int i = 0; i < articlesArray.length(); i++) {
                                        JSONObject articlesObject = articlesArray.getJSONObject(i);

                                        String sourceId = "", sourceName = "", title = "", description = "", content = "", clickURL = "", thumbnailURL = "", authorName = "";
                                        if (articlesObject.has("source")) {
                                            JSONObject sourceObject = articlesObject.getJSONObject("source");
                                            sourceId = sourceObject.has("id") ? sourceObject.getString("id") : "ID unnavailable ";
                                            sourceName = sourceObject.has("name") ? sourceObject.getString("name") : "SourceName unnavailable ";

                                        }
                                        title = articlesObject.has("author") ? articlesObject.getString("author") : "Author Unavilable";
                                        description = articlesObject.has("description") ? articlesObject.getString("description") : "Description Unavilable";
                                        content = articlesObject.has("content") ? articlesObject.getString("content") : "Content Unavilable";
                                        clickURL = articlesObject.has("url") ? articlesObject.getString("url") : "ClickURL Unavilable";
                                        thumbnailURL = articlesObject.has("urlToImage") ? articlesObject.getString("urlToImage") : "Thumbnail Unavilable";
                                        authorName = articlesObject.has("author") ? articlesObject.getString("author") : "Author Name";

                                        NewsListModel newsListModel = new NewsListModel(sourceId, sourceName, title, description, content, authorName, clickURL, thumbnailURL);
                                        newsListModelArrayList.add(newsListModel);

                                    }

                                }
                                Log.d(TAG, "getData response " + response.toString());
                                Log.d(TAG, "newsListModelArrayList size " + newsListModelArrayList.size());
                                adapter = new NewsListAdapter(context, newsListModelArrayList);
                                rv_newsList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error " + error.getMessage());
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(new JSONObject(jsonString), cacheEntry);
                    } catch (UnsupportedEncodingException | JSONException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(JSONObject response) {
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("country", "id");
                    params.put("category", "technology");
                    params.put("apiKey", "83019f880e3b49fe9886a1df5c3a077a");
                    Log.d(TAG, "Header" + params.toString());

                    return params;
                }
            };


            //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
            //Volley does retry for you if you have specified the policy.
            jsonObjectRequest.setRetryPolicy(new

                    DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);

        } else {
            setNoInternetLayout();
        }
    }

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setNoInternetLayout() {
        rv_newsList.setVisibility(View.GONE);
        img_noInternet.setVisibility(View.VISIBLE);
    }

    private void setDataLayout() {
        img_noInternet.setVisibility(View.GONE);
        rv_newsList.setVisibility(View.VISIBLE);
    }
}
