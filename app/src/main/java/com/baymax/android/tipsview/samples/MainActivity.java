package com.baymax.android.tipsview.samples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showLeft(View view) {
        ShowLeftTipsPopupActivity.start(this);
    }

    public void showTop(View view) {
        ShowTopTipsPopupActivity.start(this);
    }

    public void showRight(View view) {
        ShowRightTipsPopupActivity.start(this);
    }

    public void showBottom(View view) {
        ShowBottomTipsPopupActivity.start(this);
    }
}
