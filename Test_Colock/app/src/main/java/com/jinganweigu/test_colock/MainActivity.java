package com.jinganweigu.test_colock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_refursh;
    private ClockView clockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clockView=findViewById(R.id.cv_clock);

        btn_refursh=findViewById(R.id.btn_refursh);

        btn_refursh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockView.refrush(100,true);
                clockView.invalidate();
            }
        });

        findViewById(R.id.btn_unrefursh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clockView.refrush(10,false);
                clockView.invalidate();

            }
        });

    }
}
