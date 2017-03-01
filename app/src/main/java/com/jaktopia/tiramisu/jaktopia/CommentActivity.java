package com.jaktopia.tiramisu.jaktopia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

public class CommentActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText insertCommentEdt;
    Button postCommentBtn;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = (Toolbar)findViewById(R.id.comment_toolbar);
        insertCommentEdt = (EditText)findViewById(R.id.comment_insert_comment_box);
        postCommentBtn = (Button)findViewById(R.id.comment_post_button);

        /* set toolbar element */
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_back);

        /* get userId from intent */
    }
}
