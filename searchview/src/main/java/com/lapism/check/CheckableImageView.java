package com.lapism.check;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.lapism.searchview.R;

// AppCompatImageView
public class CheckableImageView extends ImageView implements Checkable {

    /*private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] PRESSED_STATE_SET = {android.R.attr.state_pressed};*/
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;

    private final Context mContext;
    private final Rect mTextBounds;
    private int mType;
    private boolean mPressed;
    private boolean mChecked;
    private char mLetter;
    private Bitmap mImageChecked;
    private Bitmap mImageUnchecked;
    private Paint mPaintText;
    private Paint mPaintBackgroundPressed;
    private Paint mPaintBackgroundChecked;
    private Paint mPaintBackgroundUnchecked;
    private Paint mPaintImageChecked;
    private Paint mPaintImageUnchecked;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mTextBounds = new Rect();
        setClickable(true);
        initEverything();
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.CheckableImageView, defStyleAttr, defStyleRes);
        if (attr != null) {
            if (attr.hasValue(R.styleable.CheckableImageView_checked)) {
                setChecked(attr.getBoolean(R.styleable.CheckableImageView_checked, false));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_type)) {
                setType(attr.getInt(R.styleable.CheckableImageView_type, TYPE_TEXT));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_text)) {
                setText(attr.getString(R.styleable.CheckableImageView_text));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_text_size)) {
                setTextSize(attr.getDimensionPixelSize(R.styleable.CheckableImageView_text_size, mContext.getResources().getDimensionPixelSize(R.dimen.check_text)));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_text_color)) {
                setTextColor(attr.getColor(R.styleable.CheckableImageView_text_color, ContextCompat.getColor(mContext, android.R.color.white)));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_image_checked)) {
                setImageChecked(attr.getResourceId(R.styleable.CheckableImageView_image_checked, android.R.drawable.ic_menu_gallery));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_image_unchecked)) {
                setImageUnchecked(attr.getResourceId(R.styleable.CheckableImageView_image_unchecked, android.R.drawable.ic_dialog_map));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_background_color_checked)) {
                setBackgroundColorChecked(attr.getColor(R.styleable.CheckableImageView_background_color_checked, ContextCompat.getColor(mContext, android.R.color.holo_orange_dark)));
            }
            if (attr.hasValue(R.styleable.CheckableImageView_background_color_unchecked)) {
                setBackgroundColorUnchecked(attr.getColor(R.styleable.CheckableImageView_background_color_unchecked, ContextCompat.getColor(mContext, android.R.color.holo_orange_light)));
            }
            attr.recycle();
        }
    }

    private void initEverything() {
        mType = TYPE_TEXT;
        mPressed = false;
        mChecked = false;
        mLetter = 'D';
        setClickable(true);
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(getResources().getDimensionPixelSize(R.dimen.check_text));
        mPaintText.setColor(ContextCompat.getColor(mContext, android.R.color.white));

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            mPaintText.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        } else {
            mPaintText.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        }

        mImageChecked = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_white_36dp);
        mImageUnchecked = BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_white_36dp);

        mPaintBackgroundPressed = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackgroundPressed.setAntiAlias(true);
        mPaintBackgroundPressed.setStyle(Paint.Style.FILL);
        mPaintBackgroundPressed.setColor(Color.parseColor("#40000000"));// TODO - add ripple effect

        mPaintBackgroundChecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackgroundChecked.setAntiAlias(true);
        mPaintBackgroundChecked.setStyle(Paint.Style.FILL);
        mPaintBackgroundChecked.setColor(ContextCompat.getColor(mContext, android.R.color.holo_orange_dark));

        mPaintBackgroundUnchecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackgroundUnchecked.setAntiAlias(true);
        mPaintBackgroundUnchecked.setStyle(Paint.Style.FILL);
        mPaintBackgroundUnchecked.setColor(ContextCompat.getColor(mContext, android.R.color.holo_orange_light));

        mPaintImageChecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintImageChecked.setAntiAlias(true);
        mPaintImageChecked.setFilterBitmap(true);

        mPaintImageUnchecked = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintImageUnchecked.setAntiAlias(true);
        mPaintImageUnchecked.setFilterBitmap(true);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setText(String text) {
        mLetter = text.charAt(0);
    }

    public void setTextSize(int size) {
        mPaintText.setTextSize(size);
    }

    public void setTextColor(int color) {
        mPaintText.setColor(color);
    }

    public void setImageChecked(int image) {
        mImageChecked = BitmapFactory.decodeResource(getResources(), image);
    }

    public void setImageUnchecked(int image) {
        mImageUnchecked = BitmapFactory.decodeResource(getResources(), image);
    }

    public void setBackgroundColorChecked(int color) {
        mPaintBackgroundChecked.setColor(color);
    }

    public void setBackgroundColorUnchecked(int color) {
        mPaintBackgroundUnchecked.setColor(color);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            invalidate();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // drawableStateChanged
        // refreshDrawableState();
        // postInvalidate();

        if (getDrawable() == null) {

            if (mChecked) {
                canvas.drawCircle(
                        canvas.getWidth() / 2f,
                        canvas.getHeight() / 2f,
                        Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                        mPaintBackgroundChecked);

                canvas.drawBitmap(
                        mImageChecked,
                        (canvas.getWidth() - mImageChecked.getWidth()) / 2f,
                        (canvas.getHeight() - mImageChecked.getHeight()) / 2f,
                        mPaintImageChecked);

            } else {
                canvas.drawCircle(
                        canvas.getWidth() / 2f,
                        canvas.getHeight() / 2f,
                        Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                        mPaintBackgroundUnchecked);

                if (mType == TYPE_TEXT) {
                    mPaintText.getTextBounds(String.valueOf(mLetter), 0, 1, mTextBounds);

                    canvas.drawText(
                            String.valueOf(mLetter),
                            canvas.getWidth() / 2f,
                            canvas.getHeight() / 2f + mTextBounds.height() / 2f - 1,
                            mPaintText);
                }

                if (mType == TYPE_IMAGE) {
                    canvas.drawBitmap(
                            mImageUnchecked,
                            (canvas.getWidth() - mImageUnchecked.getWidth()) / 2f,
                            (canvas.getHeight() - mImageUnchecked.getHeight()) / 2f,
                            mPaintImageUnchecked);
                }

                if (mPressed) {
                    canvas.drawCircle(
                            canvas.getWidth() / 2f,
                            canvas.getHeight() / 2f,
                            Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                            mPaintBackgroundPressed);
                }

            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        if (isPressed()) {
            mPressed = true;
            invalidate();
        } else {
            mPressed = false;
            invalidate();
        }
        super.drawableStateChanged();
    }

}
       /*
   public void setShadow(boolean shadow) {
        if (shadow) {
            setLayerType(LAYER_TYPE_SOFTWARE, mPaintBackgroundChecked);
            setLayerType(LAYER_TYPE_SOFTWARE, mPaintBackgroundUnchecked);
            mPaintBackgroundChecked.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
            mPaintBackgroundUnchecked.setShadowLayer(4.0f, 0.0f, 2.0f, Color.parseColor("#42000000"));
        }
    }
    */

    /*private int getColorAccent() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    private int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    private int getPressedColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = hsv[2] * 0.7f;
        return Color.HSVToColor(hsv);
    }

    private Bitmap getScaledBitmap() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        opts.inSampleSize = 4;
        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_person_white_36dp, opts);
    }*/