package com.qitest.journal;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {
    private ImageButton slideButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }private void initView() {
        slideButton = (ImageButton)findViewById(R.id.imageButton);
    }

    

    @Override
    public void onClick(View v) {

    }
}
