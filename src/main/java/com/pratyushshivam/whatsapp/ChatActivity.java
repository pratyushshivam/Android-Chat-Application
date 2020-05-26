package com.pratyushshivam.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ChatActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    String activeUser = "";
    ArrayList<String> messages = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    EditText chatEditText;

    public void sendChat(View view) {
        chatEditText = findViewById(R.id.chatEditText);
        ParseObject message = new ParseObject("Message");
        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("recipient", activeUser);
        message.put("message", chatEditText.getText().toString());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    messages.add(chatEditText.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    chatEditText.setText("");
                    //onResume();

                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        relativeLayout = findViewById(R.id.er);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loop();
            }
        });
        Intent intent = getIntent();
        activeUser = intent.getStringExtra("username");
        //Toast.makeText(this, activeUser, Toast.LENGTH_SHORT).show();
        setTitle(activeUser);

        ListView chatListView = findViewById(R.id.chatListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", activeUser);
        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender", activeUser);
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        messages.clear();
                        for (ParseObject message : objects) {
                            String messageContent = message.getString("message");
                            if (!message.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {
                                messageContent = "> " + messageContent;

                            }
                            messages.add(messageContent);
                        }
                        arrayAdapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }

    public void loop() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
