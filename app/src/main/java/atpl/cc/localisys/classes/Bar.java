package atpl.cc.localisys.classes;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
public class Bar {

    // Member Variables ////////////////////////////////////////////////////////

    private final Paint mBarPaint;

    private final Paint mTickPaint;

    // Left-coordinate of the horizontal bar.
    private final float mLeftX;

    private final float mRightX;

    private final float mY;

    private int mNumSegments;

    private float mTickDistance;

    private final float mTickHeight;


    public Bar(Context ctx,
               float x,
               float y,
               float length,
               int tickCount,
               float tickHeightDP,
               int tickColor,
               float barWeight,
               int barColor) {

        mLeftX = x;
        mRightX = x + length;
        mY = y;

        mNumSegments = tickCount - 1;
        mTickDistance = length / mNumSegments;
        mTickHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                tickHeightDP,
                ctx.getResources().getDisplayMetrics());

        // Initialize the paint.
        mBarPaint = new Paint();
        mBarPaint.setColor(barColor);
        mBarPaint.setStrokeWidth(barWeight);
        mBarPaint.setAntiAlias(true);
        mTickPaint = new Paint();
        mTickPaint.setColor(tickColor);
        mTickPaint.setStrokeWidth(barWeight);
        mTickPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {

        canvas.drawLine(mLeftX, mY, mRightX, mY, mBarPaint);
    }


    public float getLeftX() {
        return mLeftX;
    }


    public float getRightX() {
        return mRightX;
    }


    public float getNearestTickCoordinate(atpl.cc.localisys.classes.PinView thumb) {

        final int nearestTickIndex = getNearestTickIndex(thumb);

        return mLeftX + (nearestTickIndex * mTickDistance);
    }


    public int getNearestTickIndex(atpl.cc.localisys.classes.PinView thumb) {

        return (int) ((thumb.getX() - mLeftX + mTickDistance / 2f) / mTickDistance);
    }


    public void setTickCount(int tickCount) {

        final float barLength = mRightX - mLeftX;

        mNumSegments = tickCount - 1;
        mTickDistance = barLength / mNumSegments;
    }


    public void drawTicks(Canvas canvas) {

        // Loop through and draw each tick (except final tick).
        for (int i = 0; i < mNumSegments; i++) {
            final float x = i * mTickDistance + mLeftX;
            canvas.drawCircle(x, mY, mTickHeight, mTickPaint);
        }
        // Draw final tick. We draw the final tick outside the loop to avoid any
        // rounding discrepancies.
        canvas.drawCircle(mRightX, mY, mTickHeight, mTickPaint);
    }
}
