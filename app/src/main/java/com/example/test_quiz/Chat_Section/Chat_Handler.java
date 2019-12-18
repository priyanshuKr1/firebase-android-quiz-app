package com.example.test_quiz.Chat_Section;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.test_quiz.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class Chat_Handler extends AppCompatActivity {


    private ListView listView;
    private EditText message;
    private String username;
    private DatabaseReference reference;
    private DatabaseReference database1;
    private ChatAdapter chatAdapter;
    private StorageReference firebaseStorage;
    private boolean isAdmin = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);
        isAdmin = getIntent().getBooleanExtra("ChatAdmin",false);
        listView =  findViewById(R.id.list);
        message =  findViewById(R.id.message);
        username = "Anonymous";
        ImageButton send =  findViewById(R.id.send_message);
        Toolbar toolbar = findViewById(R.id.toolChat);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        chatAdapter = new ChatAdapter(this, R.id.list);
        listView.setAdapter(chatAdapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        database1 = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("chat");

        database1.child("users").child(Objects.requireNonNull(auth.getUid()))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    username = dataSnapshot.child("name").getValue(String.class);
                    if(isAdmin)
                        username = "Admin";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NotNull DataSnapshot dataSnapshot, String s) {
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                chatAdapter.add(msg);
                scrollToBottom();
            }

            public void onChildChanged(@NotNull DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(@NotNull DataSnapshot dataSnapshot) {}
            public void onChildMoved(@NotNull DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(@NotNull DatabaseError databaseError) {}
        });

        final String user_id = "gs://myquiz12.appspot.com/" + auth.getUid();

                send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                ChatMessage msg = new ChatMessage(username, message.getText().toString().trim(),
                        currentDateTimeString,user_id);
                reference.push().setValue(msg);
                message.setText("");
                scrollToBottom();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void scrollToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }
}
