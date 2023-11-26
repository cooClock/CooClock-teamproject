package com.example.cooclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.annotation.Nullable; // Import the @Nullable annotation from the correct package

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class camera_scan_page extends AppCompatActivity {
    private ImageView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan_page);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);  // 바코드 형식 설정
        integrator.setPrompt("Scan a barcode or QR code");  // 스캔 화면에 표시되는 메시지 설정
        integrator.setCameraId(1);  // 후면 카메라 사용
        integrator.setBeepEnabled(false);  // 스캔 소리 사용 여부
        integrator.setOrientationLocked(false);

        integrator.initiateScan();  // 스캔 시작
    }

    // 스캔 결과 처리
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                setResult(Activity.RESULT_CANCELED);
            } else {
                // 스캔된 코드를 처리합니다.
                String scannedCode = result.getContents();
                Intent intent = new Intent();
                intent.putExtra("scannedCode", scannedCode);
                setResult(Activity.RESULT_OK, intent);
            }
            Intent intent = new Intent(getApplicationContext(),my_refrigerator_page.class);
            startActivity(intent);
        }
    }
}