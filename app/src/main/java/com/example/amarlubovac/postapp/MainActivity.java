package com.example.amarlubovac.postapp;

import android.content.Intent;
import android.os.FileObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private List<Post> posts;
    private List<String> postTitles;
    private ArrayAdapter<String> arrayAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        progressBar = findViewById(R.id.progressBar);
        posts = new ArrayList<>();
        postTitles = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Screen2.class);
                intent.putExtra("id", posts.get(i).getID());
                intent.putExtra("userId", posts.get(i).getUserID());
                intent.putExtra("title", posts.get(i).getTitle());
                intent.putExtra("body", posts.get(i).getBody());
                startActivity(intent);
            }
        });

        callWebService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    private void callWebService() {

        String url = "http://jsonplaceholder.typicode.com/posts";

        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i< jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Post p = new Post();
                        p.setUserID(object.getInt("userId"));
                        p.setID(object.getInt("id"));
                        p.setTitle(object.getString("title"));
                        p.setBody(object.getString("body"));
                        posts.add(p);
                        postTitles.add(p.getTitle());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("Error!");
                error.printStackTrace();
                requestQueue.stop();
            }
        });

        requestQueue.add(request);

    }

    private void setUserIcon(ImageView imageView, Integer id) {

        final ImageView imageView1 = imageView;

        String url = "http://jsonplaceholder.typicode.com/users/" + Integer.toString(id);

        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String email = jsonObject.getString("email");
                    Picasso.get().load("https://api.adorable.io/avatars/50/" + email + ".png").into(imageView1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("Error!");
                error.printStackTrace();
                requestQueue.stop();
            }
        });

        requestQueue.add(request);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return posts.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.customlistview, null);

            TextView textView_title = view.findViewById(R.id.textView);
            ImageView imageView = view.findViewById(R.id.imageView2);

            textView_title.setText(posts.get(i).getTitle());
            setUserIcon(imageView, posts.get(i).getUserID());

            return view;
        }

    }

}
