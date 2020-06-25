package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    EditText nameText, passText, emailText;

    Button registerBtn;

    FirebaseAuth auth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = findViewById(R.id.nameText);
        passText = findViewById(R.id.passwordText);
        emailText = findViewById(R.id.emailText);

        registerBtn = findViewById(R.id.signInButton);

        auth = FirebaseAuth.getInstance();

        //Add Event Listener

        registerBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username_text = nameText.getText().toString();
                String email_text = emailText.getText().toString();
                String passWord_text = passText.getText().toString();

                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(passWord_text) || TextUtils.isEmpty(email_text)){
                    Toast.makeText(RegisterActivity.this, "Man, complete all of them", Toast.LENGTH_SHORT).show();
                }else{
                    RegisterNow(username_text, email_text, passWord_text);
                }
            }
        });

    }

    private void RegisterNow(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                    @Override
                    public  void  onComplete(Task<AuthResult> task){
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageUrl", "default");

                            //Opening Main Activity after Success

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
