package com.example.amarlubovac.postapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Screen2 extends AppCompatActivity {
    private TextView titleTV;
    private TextView bodyTV;
    private TextView usernameTV;
    private TextView commentsTV;
    private ImageView imageView;
    private String username;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        titleTV = findViewById(R.id.textView1);
        bodyTV = findViewById(R.id.textView2);
        usernameTV = findViewById(R.id.textView3);
        commentsTV = findViewById(R.id.textView4);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        final Bundle b = intent.getExtras();
        titleTV.setText(b.getString("title"));
        bodyTV.setText(b.getString("body"));
        Integer id = b.getInt("id");
        setUsername(b.getInt("userId"));
        setNumberOfComments(b.getInt("id"));
    }

    private void setUsername(Integer id) {

        String url = "http://jsonplaceholder.typicode.com/users/" + Integer.toString(id);

        final RequestQueue requestQueue = Volley.newRequestQueue(Screen2.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    usernameTV.setText(jsonObject.getString("username"));
                    email = jsonObject.getString("email");
                    Picasso.get().load("https://api.adorable.io/avatars/200/" + email + ".png").into(imageView);

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

    private void setNumberOfComments(final Integer postId) {

        String url = "http://jsonplaceholder.typicode.com/comments";

        final RequestQueue requestQueue = Volley.newRequestQueue(Screen2.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Integer brojac = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i< jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.getInt("postId") == postId) brojac = brojac + 1;
                    }
                    commentsTV.setText(Integer.toString(brojac));

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

}
