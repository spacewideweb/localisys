package atpl.cc.localisys.classes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;


public class ConnectingLine {

    private final Paint mPaint;

    private final float mY;


    public ConnectingLine(Context ctx, float y, float connectingLineWeight,
                          int connectingLineColor) {

        final Resources res = ctx.getResources();

        float connectingLineWeight1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                connectingLineWeight,
                res.getDisplayMetrics());

        // Initialize the paint, set values
        mPaint = new Paint();
        mPaint.setColor(connectingLineColor);
        mPaint.setStrokeWidth(connectingLineWeight1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mY = y;
    }


    public void draw(Canvas canvas, atpl.cc.localisys.classes.PinView leftThumb, atpl.cc.localisys.classes.PinView rightThumb) {
      try {
          canvas.drawLine(leftThumb.getX(), mY, rightThumb.getX(), mY, mPaint);
      }
      catch (Exception ex){
          ex.printStackTrace();
      }
    }


    public void draw(Canvas canvas, float leftMargin, atpl.cc.localisys.classes.PinView rightThumb) {
        canvas.drawLine(leftMargin, mY, rightThumb.getX(), mY, mPaint);
    }

}
