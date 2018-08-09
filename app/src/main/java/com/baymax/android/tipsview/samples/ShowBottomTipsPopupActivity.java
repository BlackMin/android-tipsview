package com.baymax.android.tipsview.samples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baymax.android.tipsview.TriangleTipsPopupHelper;
import com.baymax.android.tipsview.TriangleTipsView;

public class ShowBottomTipsPopupActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ShowBottomTipsPopupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bottom_tips);
        TextView textView = findViewById(R.id.anchor);
        TriangleTipsView triangleTipsView = (TriangleTipsView) LayoutInflater.from(this).inflate(R.layout.view_tips, null);
        triangleTipsView.setTriangleGravity(TriangleTipsView.TriangleGravity.BOTTOM);
        TriangleTipsPopupHelper.PopupWindowBuilder builder = new TriangleTipsPopupHelper.PopupWindowBuilder(this);
        PopupWindow popupWindow = builder.setAnchorView(textView)
                .setContentView(triangleTipsView)
                .setDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                }).show();
    }
}
