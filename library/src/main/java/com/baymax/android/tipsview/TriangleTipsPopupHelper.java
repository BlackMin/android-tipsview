package com.baymax.android.tipsview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class TriangleTipsPopupHelper {

    public static class PopupWindowBuilder {

        private Context mContext;

        private PopupWindow.OnDismissListener mOnDismissListener;

        private TriangleTipsView mContentView;

        private View anchorView;

        public PopupWindowBuilder(@NonNull Context context) {
            mContext = context;
        }

        public PopupWindowBuilder setDismissListener(PopupWindow.OnDismissListener listener) {
            this.mOnDismissListener = listener;
            return this;
        }

        public PopupWindowBuilder setContentView(@NonNull TriangleTipsView contentView) {
            this.mContentView = contentView;
            return this;
        }

        public PopupWindowBuilder setAnchorView(@NonNull View anchorView) {
            this.anchorView = anchorView;
            return this;
        }

        public PopupWindow create() {
            final PopupWindow popupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable(null, (Bitmap) null));
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(mOnDismissListener);
            return popupWindow;
        }

        public PopupWindow show() {
            final PopupWindow popupWindow = create();
            anchorView.post(new Runnable() {
                @Override
                public void run() {
                    int[] offset = calculateOffSet(anchorView, mContentView);
                    popupWindow.showAtLocation(anchorView, Gravity.LEFT|Gravity.TOP, offset[0] ,offset[1]);
                }
            });
            return popupWindow;
        }
    }

    private static int[] calculateOffSet(@NonNull View anchorView, TriangleTipsView contentView) {
        int[] offset = new int[2];
        int[] location = new int[2];
        anchorView.getLocationInWindow(location);
        contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int contentWidth = contentView.getMeasuredWidth();
        int contentHeight = contentView.getMeasuredHeight();
        int anchorViewCenterX = location[0] + anchorView.getWidth() / 2;
        int anchorViewCenterY = location[1] + anchorView.getHeight() / 2;
        int triangleOffsetX = 0;
        int triangleOffsetY = 0;
        switch (contentView.getTriangleGravity()) {
            case TriangleTipsView.TriangleGravity.LEFT:
                if((anchorViewCenterY - contentHeight / 2) < 0) {
                    triangleOffsetY = anchorViewCenterY - contentHeight / 2;
                }else if(anchorViewCenterY + contentHeight / 2 > Resources.getSystem().getDisplayMetrics().heightPixels) {
                    triangleOffsetY = anchorViewCenterY + contentHeight / 2 - Resources.getSystem().getDisplayMetrics().heightPixels;
                }
                contentView.setOffSetY(triangleOffsetY);
                offset[0] = location[0] + anchorView.getWidth();
                offset[1] = Math.min(Resources.getSystem().getDisplayMetrics().heightPixels - contentHeight, anchorViewCenterY - contentHeight / 2);
                offset[1] = Math.max(0, offset[1]);
                break;
            case TriangleTipsView.TriangleGravity.TOP:
                if((anchorViewCenterX - contentWidth / 2) < 0) {
                    triangleOffsetX = anchorViewCenterX - contentWidth / 2;
                } else if(anchorViewCenterX + contentWidth / 2  > Resources.getSystem().getDisplayMetrics().widthPixels) {
                    triangleOffsetX = anchorViewCenterX + contentWidth / 2 - Resources.getSystem().getDisplayMetrics().widthPixels;
                }
                contentView.setOffSetX(triangleOffsetX);
                offset[0] = Math.min(Resources.getSystem().getDisplayMetrics().widthPixels - contentWidth,anchorViewCenterX - contentWidth / 2);
                offset[0] = Math.max(0, offset[0]);
                offset[1] = location[1] + anchorView.getHeight();
                break;
            case TriangleTipsView.TriangleGravity.RIGHT:
                if((anchorViewCenterY - contentHeight / 2) < 0) {
                    triangleOffsetY = anchorViewCenterY - contentHeight / 2;
                }else if(anchorViewCenterY + contentHeight / 2 > Resources.getSystem().getDisplayMetrics().heightPixels) {
                    triangleOffsetY = anchorViewCenterY + contentHeight / 2 - Resources.getSystem().getDisplayMetrics().heightPixels;
                }
                contentView.setOffSetY(triangleOffsetY);
                offset[0] = location[0] - contentWidth;
                offset[1] = Math.min(Resources.getSystem().getDisplayMetrics().heightPixels - contentHeight, anchorViewCenterY - contentHeight / 2);
                offset[1] = Math.max(0, offset[1]);
                break;
            case TriangleTipsView.TriangleGravity.BOTTOM:
                if((anchorViewCenterX - contentWidth / 2) < 0) {
                    triangleOffsetX = anchorViewCenterX - contentWidth / 2;
                } else if(anchorViewCenterX + contentWidth / 2  > Resources.getSystem().getDisplayMetrics().widthPixels) {
                    triangleOffsetX = anchorViewCenterX + contentWidth / 2 - Resources.getSystem().getDisplayMetrics().widthPixels;
                }
                contentView.setOffSetX(triangleOffsetX);
                offset[0] = Math.min(Resources.getSystem().getDisplayMetrics().widthPixels - contentWidth,anchorViewCenterX - contentWidth / 2);
                offset[0] = Math.max(0, offset[0]);
                offset[1] = location[1] - contentHeight;
                break;
        }

        return offset;
    }
}
