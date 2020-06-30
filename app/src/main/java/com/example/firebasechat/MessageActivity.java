package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.firebasechat.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    FirebaseUser fUser;
    DatabaseReference reference;
    Intent intent;

    EditText msg_text;
    ImageButton send_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        imageView = findViewById(R.id.message_img_profile);
        username = findViewById(R.id.message_username);

        send_btn = findViewById(R.id.btn_send);
        msg_text = findViewById(R.id.text_send);



        intent = getIntent();

        final String userid = intent.getStringExtra("userid");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Users users = dataSnapshot.getValue(Users.class);
               // username.setText(users.getUsername());

                imageView.setImageResource(R.mipmap.ic_launcher_round);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_text.getText().toString();
                if(!msg.equals("")){
                    sendMessage(fUser.getUid(), userid, msg);
                }else{
                    Toast.makeText(MessageActivity.this,"No blank message allowed", Toast.LENGTH_SHORT);
                }
                msg_text.setText("");
            }
        });
    }


        private void sendMessage(String sender, String receiver, String message){
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);

            ref.child("Chats").push().setValue(hashMap);
        }


}
