package com.common.android.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.common.android.utils.device.Dimension;

import static com.common.android.utils.extensions.DeviceExtensions.getScreenDimension;
import static com.common.android.utils.extensions.ViewExtensions.getContentRoot;
import static com.common.android.utils.extensions.ViewExtensions.getScreenLocation;

/**
 * Created by Jan Rabe on 11/06/15.
 */
public class PorterDuffBitmap {

    public static final String TAG = PorterDuffBitmap.class.getSimpleName();
    @NonNull
    final Bitmap bitmap;
    @NonNull
    final Canvas canvas;
    int topOffset;

    public PorterDuffBitmap(@NonNull final Bitmap bitmap, final int topOffset) {
        this.bitmap = bitmap;
        canvas = new Canvas(bitmap);
        this.topOffset = topOffset;
    }

    @NonNull
    public static PorterDuffBitmap createFullscreenBitmap(final int color) {
        final Dimension dim = getScreenDimension();
        final int topOffset = dim.height - getContentRoot().getMeasuredHeight();
        Log.v(TAG, dim.toString() + " topOffset=" + topOffset + " " + getContentRoot().getMeasuredHeight());
        final PorterDuffBitmap p = new PorterDuffBitmap(Bitmap.createBitmap(dim.width, dim.height - topOffset, Bitmap.Config.ARGB_8888), topOffset);
        p.canvas.drawColor(color);
        return p;
    }

    public void addMask(@NonNull final View view, final int left, final int top, @NonNull final PorterDuff.Mode mode) {
        final int[] locations = getScreenLocation(view);
        Log.v(TAG, view.getWidth() + "x" + view.getHeight());
        drawWithPorterDuff(locations[0] + left, locations[1] + top, view.getWidth(), view.getHeight(), mode);
    }

    private void drawWithPorterDuff(final int left, final int top, final int width, final int height, @NonNull final PorterDuff.Mode mode) {
        final Paint maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(mode));

        final Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(mask);
        c.drawColor(Color.BLACK);

        // apply mask
        Log.v(TAG, "left=" + left + "top= " + top + "topOffset" + topOffset + " topWithOffset=" + (top - topOffset));
        canvas.drawBitmap(mask, left, top - topOffset, maskPaint);
        mask.recycle();
    }

    @NonNull
    public Bitmap getBitmap() {
        return bitmap;
    }
}
