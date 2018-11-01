package atpl.cc.localisys.classes;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import atpl.cc.localisys.R;


class PinView extends View {


    private static final float MINIMUM_TARGET_RADIUS_DP = 24;


    private static final float DEFAULT_THUMB_RADIUS_DP = 14;


    private float mTargetRadiusPx;

    // Indicates whether this thumb is currently pressed and active.
    private boolean mIsPressed = false;

    // The y-position of the thumb in the parent view. This should not change.
    private float mY;

    // The current x-position of the thumb in the parent view.
    private float mX;

    // mPaint to draw the thumbs if attributes are selected

    private Paint mTextPaint;

    private Drawable mPin;

    private String mValue;

    // Radius of the new thumb if selected
    private int mPinRadiusPx;

    private ColorFilter mPinFilter;

    private float mPinPadding;

    private float mTextYPadding;

    private Rect mBounds = new Rect();

    private Resources mRes;

    private float mDensity;

    private Paint mCirclePaint;

    private float mCircleRadiusPx;

    private IRangeBarFormatter formatter;

    private float mMinPinFont = RangeBar.DEFAULT_MIN_PIN_FONT_SP;

    private float mMaxPinFont = RangeBar.DEFAULT_MAX_PIN_FONT_SP;

    private boolean mPinsAreTemporary;

    private boolean mHasBeenPressed = false;

    // Constructors ////////////////////////////////////////////////////////////

    public PinView(Context context) {
        super(context);
    }

    // Initialization //////////////////////////////////////////////////////////

    public void setFormatter(IRangeBarFormatter mFormatter) {
        this.formatter = mFormatter;
    }


    public void init(Context ctx, float y, float pinRadiusDP, int pinColor, int textColor,
                     float circleRadius, int circleColor, float minFont, float maxFont, boolean pinsAreTemporary) {

        mRes = ctx.getResources();
        mPin = ContextCompat.getDrawable(ctx, R.drawable.circle);

        mDensity = getResources().getDisplayMetrics().density;
        mMinPinFont = minFont / mDensity;
        mMaxPinFont = maxFont / mDensity;
        mPinsAreTemporary = pinsAreTemporary;

        mPinPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                15, mRes.getDisplayMetrics());
        mCircleRadiusPx = circleRadius;
        mTextYPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                3.5f, mRes.getDisplayMetrics());
        // If one of the attributes are set, but the others aren't, set the
        // attributes to default
        if (pinRadiusDP == -1) {
            mPinRadiusPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DEFAULT_THUMB_RADIUS_DP,
                    mRes.getDisplayMetrics());
        } else {
            mPinRadiusPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    pinRadiusDP,
                    mRes.getDisplayMetrics());
        }
        //Set text size in px from dp
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                15, mRes.getDisplayMetrics());

        // Creates the paint and sets the Paint values
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        //mTextPaint.setStyle(Paint.Style.STROKE);
        // Creates the paint and sets the Paint values

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setShadowLayer(mCircleRadiusPx+5,mX,mY,circleColor);

       /* mCirclePaint = new Paint();
        mCirclePaint.setColor(circleColor);
        mCirclePaint.setAntiAlias(true);
       // mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);*/

        //Color filter for the selection pin
        mPinFilter = new LightingColorFilter(pinColor, pinColor);

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        int targetRadius = (int) Math.max(MINIMUM_TARGET_RADIUS_DP, mPinRadiusPx);

        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                targetRadius,
                mRes.getDisplayMetrics());
        mY = y;
    }

    /**
     * Set the x value of the pin
     *
     * @param x set x value of the pin
     */
    @Override
    public void setX(float x) {
        mX = x;
    }


    /**
     * Get the x value of the pin
     *
     * @return x float value of the pin
     */
    @Override
    public float getX() {
        return mX;
    }


    public void setXValue(String x) {
        mValue = x;
    }


    @Override
    public boolean isPressed() {
        return mIsPressed;
    }

    /**
     * Sets the state of the pin to pressed
     */
    public void press() {
        mIsPressed = true;
        mHasBeenPressed = true;
    }

    /**
     * Set size of the pin and padding for use when animating pin enlargement on press
     *
     * @param size    the size of the pin radius
     * @param padding the size of the padding
     */
    public void setSize(float size, float padding) {
        mPinPadding = (int) padding;
        mPinRadiusPx = (int) size;
        invalidate();
    }

    /**
     * Release the pin, sets pressed state to false
     */
    public void release() {
        mIsPressed = false;
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     * false otherwise
     */
    public boolean isInTargetZone(float x, float y) {
        return (Math.abs(x - mX) <= mTargetRadiusPx
                && Math.abs(y - mY + mPinPadding) <= mTargetRadiusPx);
    }

    //Draw the circle regardless of pressed state. If pin size is >0 then also draw the pin and text
    @Override
    public void draw(Canvas canvas) {

        if (mPinRadiusPx > 0 && (mHasBeenPressed || !mPinsAreTemporary)) {
            mBounds.set((int) mX - mPinRadiusPx,
                    (int) mY - (mPinRadiusPx * 2) - (int) mPinPadding,
                    (int) mX + mPinRadiusPx, (int) mY - (int) mPinPadding);
            mPin.setBounds(mBounds);
            String text = mValue;

            if (this.formatter != null) {
                text = formatter.format(text);
            }
            calibrateTextSize(mTextPaint, text, mBounds.width());
            mTextPaint.getTextBounds(text, 0, text.length(), mBounds);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mPin.setColorFilter(mPinFilter);
            mPin.draw(canvas);
            canvas.drawText(text,
                    mX, mY - mPinRadiusPx - mPinPadding + mTextYPadding,
                    mTextPaint);
        }
        canvas.drawCircle(mX, mY, mCircleRadiusPx, mCirclePaint);
        //Draw pin if pressed

        super.draw(canvas);
    }

    // Private Methods /////////////////////////////////////////////////////////////////

    //Set text size based on available pin width.
    private void calibrateTextSize(Paint paint, String text, float boxWidth) {
        paint.setTextSize(10);

        float textSize = paint.measureText(text);
        float estimatedFontSize = boxWidth * 8 / textSize / mDensity;

        if (estimatedFontSize < mMinPinFont) {
            estimatedFontSize = mMinPinFont;
        } else if (estimatedFontSize > mMaxPinFont) {
            estimatedFontSize = mMaxPinFont;
        }
        paint.setTextSize(estimatedFontSize * mDensity);
    }
}
