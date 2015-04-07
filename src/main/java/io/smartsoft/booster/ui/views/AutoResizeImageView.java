package io.smartsoft.booster.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class AutoResizeImageView extends ImageView {
    private float aspectRatio = 1;
    private RequestCreator request;

    public AutoResizeImageView(Context context) {
        super(context);
    }

    public AutoResizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoResizeImageView(Context context,
            AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void downloadImage(String url, int height, int width, Picasso picasso) {
        request = picasso.load(url);

        aspectRatio = 1f * width / height;
        requestLayout();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("layout_width must be match_parent");
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        // Honor aspect ratio for height but no larger than 2x width.
        int height = Math.min((int) (width / aspectRatio), width * 2);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (request != null) {
            request.resize(width, height).centerInside().into(this);

            request = null;
        }
    }
}