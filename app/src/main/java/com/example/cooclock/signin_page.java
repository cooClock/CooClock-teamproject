package com.example.cooclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class signin_page extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스 연동
    private EditText mEtID, mEtPwd,mEtName; // 회원가입 입력필드
    private Button mBtnRegister;    //회원가입 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("cooclock");

        mEtID = findViewById(R.id.et_id);
        mEtPwd = findViewById(R.id.et_pw);
        mEtName = findViewById(R.id.et_name);
        mBtnRegister = findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 처리 시작
                String strID = mEtID.getText().toString().trim();
                String strPW = mEtPwd.getText().toString().trim();
                String strName = mEtName.getText().toString().trim();

                if (TextUtils.isEmpty(strID) || TextUtils.isEmpty(strPW)) {
                    Toast.makeText(signin_page.this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strID, strPW)
                        .addOnCompleteListener(signin_page.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        UserAccount account = new UserAccount();
                                        account.setIdToken(firebaseUser.getUid());
                                        account.setEmailId(firebaseUser.getEmail());
                                        account.setUsername(strName);
                                        // 비밀번호를 저장하지 않음 (Firebase Authentication에서 관리)

                                        // mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                        Toast.makeText(signin_page.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                                        // 회원가입 성공 후, 로그인 화면으로 이동 또는 다음 작업 수행
                                        Intent intent = new Intent(signin_page.this, login_page.class);
                                        startActivity(intent);
                                        finish(); // 현재 액티비티 종료
                                    }
                                } else {
                                    if (task.getException() != null) {
                                        Toast.makeText(signin_page.this, "회원가입에 실패하셨습니다. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}
