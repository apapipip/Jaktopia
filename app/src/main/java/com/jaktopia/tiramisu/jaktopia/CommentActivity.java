package com.jaktopia.tiramisu.jaktopia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jaktopia.tiramisu.jaktopia.CommentRecycler.CommentRecyclerAdapter;
import com.jaktopia.tiramisu.jaktopia.Interface.IVolleyCallBack;
import com.jaktopia.tiramisu.jaktopia.ObjectClass.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class CommentActivity extends AppCompatActivity {
    List<Comment> comments = new ArrayList<Comment>();

    Toolbar toolbar;
    EditText insertCommentEdt;
    Button postCommentBtn;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    CommentRecyclerAdapter commentRecyclerAdapter;

    int eventId;
    RequestQueue requestQueue;
    String commentGetReqUrl;
    String commentPostReqUrl;
    IVolleyCallBack volleyCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = (Toolbar)findViewById(R.id.comment_toolbar);
        insertCommentEdt = (EditText)findViewById(R.id.comment_insert_comment_box);
        postCommentBtn = (Button)findViewById(R.id.comment_post_button);
        progressBar = (ProgressBar)findViewById(R.id.comment_progress_bar);
        recyclerView = (RecyclerView)findViewById(R.id.comment_recycler_view);

        /* set on postCommentBtn onClickListener */
        postCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCommentToAPI();
            }
        });

        /* set toolbar element */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back2);

        /* get eventId from intent */
        Intent intent = getIntent();
        eventId = intent.getIntExtra("event_id", 0);

        /* instantiate callback interface */
        volleyCallBack = new IVolleyCallBack() {
            @Override
            public void onSuccess() {
                /* show recyclerView and hide progressBar */
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                insertCommentEdt.getText().clear();
                recyclerView.scrollToPosition(comments.size()-1);

                commentRecyclerAdapter.setComments(new ArrayList<Comment>(comments));
                commentRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {

            }
        };

        /* get comment from API */
        getCommentsFromAPI(volleyCallBack);

        commentRecyclerAdapter = new CommentRecyclerAdapter(this, new ArrayList<Comment>(comments));
        recyclerView.setAdapter(commentRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCommentsFromAPI(final IVolleyCallBack volleyCallBack) {
         /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        commentGetReqUrl = "https://aegis.web.id/jaktopia/api/v1/comment/"+ eventId;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, commentGetReqUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject dataObj = response.getJSONObject("data");
                            JSONArray commentDataArr = dataObj.getJSONArray("comments");
                            JSONArray eventDataArr = dataObj.getJSONArray("event");
                            for(int i=0;i<eventDataArr.length();i++) {
                                JSONObject eventDataObj = eventDataArr.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setUserId(eventDataObj.getInt("account_id"));
                                comment.setUsername(eventDataObj.getString("account_name"));
                                if(eventDataObj.has("event_caption") && !eventDataObj.isNull("event_caption"))
                                    comment.setContent(eventDataObj.getString("event_caption"));
                                else
                                    comment.setContent("");
                                comment.setUserIconUrl(eventDataObj.getString("account_photo"));
                                //comment.setPostTime(Long.parseLong(eventDataObj.getString("event_time")));
                                comments.add(comment);
                            }
                            for(int i = 0; i< commentDataArr.length(); i++) {
                                JSONObject commentDataObj = commentDataArr.getJSONObject(i);
                                Comment comment = new Comment();
                                comment.setUserId(commentDataObj.getInt("account_id"));
                                comment.setUsername(commentDataObj.getString("account_name"));
                                comment.setContent(commentDataObj.getString("comment_content"));
                                comment.setUserIconUrl(commentDataObj.getString("account_photo"));
                                //comment.setPostTime(Long.parseLong(commentDataObj.getString("comment_time")));
                                comments.add(comment);
                            }
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley", "error");
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    private void postCommentToAPI() {
        /* clear comments list */
        comments.clear();

        /* show progressBar and hide recyclerView */
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(GONE);

        /* create JSONObject data */
        JSONObject commentObj = new JSONObject();
        try {
            commentObj.put("accountID", "1");
            commentObj.put("eventID", eventId);
            commentObj.put("content", insertCommentEdt.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        commentPostReqUrl = "https://aegis.web.id/jaktopia/api/v1/comment";
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, commentPostReqUrl, commentObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String successStatus = response.getString("success");
                            String message = response.getString("message");
                            if(successStatus.equals("true")) {
                                JSONObject dataObj = response.getJSONObject("data");
                                JSONArray commentDataArr = dataObj.getJSONArray("comments");
                                JSONArray eventDataArr = dataObj.getJSONArray("event");
                                for (int i = 0; i < eventDataArr.length(); i++) {
                                    JSONObject eventDataObj = eventDataArr.getJSONObject(i);
                                    Comment comment = new Comment();
                                    comment.setUserId(eventDataObj.getInt("account_id"));
                                    comment.setUsername(eventDataObj.getString("account_name"));
                                    if (eventDataObj.has("event_caption") && !eventDataObj.isNull("event_caption"))
                                        comment.setContent(eventDataObj.getString("event_caption"));
                                    else
                                        comment.setContent("");
                                    comment.setUserIconUrl(eventDataObj.getString("account_photo"));
                                    //comment.setPostTime(Long.parseLong(eventDataObj.getString("event_time")));
                                    comments.add(comment);
                                }
                                for (int i = 0; i < commentDataArr.length(); i++) {
                                    JSONObject commentDataObj = commentDataArr.getJSONObject(i);
                                    Comment comment = new Comment();
                                    comment.setUserId(commentDataObj.getInt("account_id"));
                                    comment.setUsername(commentDataObj.getString("account_name"));
                                    comment.setContent(commentDataObj.getString("comment_content"));
                                    comment.setUserIconUrl(commentDataObj.getString("account_photo"));
                                    //comment.setPostTime(Long.parseLong(commentDataObj.getString("comment_time")));
                                    comments.add(comment);
                                }
                                volleyCallBack.onSuccess();
                            } else {
                                volleyCallBack.onFailed();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Network error. Failed to post comment", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
