package com.example.cooclock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login_page extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스 연동
    private EditText mEtID, mEtPwd; // 로그인 입력필드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Firebase 초기화
        FirebaseApp.initializeApp(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock");

        mEtID = findViewById(R.id.et_id); // 뷰 기준이라 아이디가 동일해도  setContentView를 기준으로 아이디를 찾기 때문에 상관이 없다.
        mEtPwd = findViewById(R.id.et_pw);


        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                // 로그인 요청
                String strID = mEtID.getText().toString();
                String strPW = mEtPwd.getText().toString();

                if (strID.isEmpty()){
                    Toast.makeText(login_page.this, "아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else if (strPW.isEmpty()){
                    Toast.makeText(login_page.this, "비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    mFirebaseAuth.signInWithEmailAndPassword(strID, strPW).addOnCompleteListener(login_page.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(login_page.this, MainActivity.class);
                                startActivity(intent);
                                finish(); //현재 액티비티 파괴
                            } else {
                                Toast.makeText(login_page.this, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Button btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_page.this,signin_page.class);
                startActivity(intent);
            }
        });
    }
}