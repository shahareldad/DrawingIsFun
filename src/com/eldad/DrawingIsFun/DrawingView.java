package com.eldad.DrawingIsFun;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by se250071 on 01/12/2015.
 */
public class DrawingView extends View {

    private final String TAG = "DrawingView";

    private Path _drawPath;
    private Paint _drawPaint, _canvasPaint;
    private int _paintColor = 0xFF660000;
    private Canvas _drawCanvas;
    private Bitmap _canvasBitmap;
    private float _brushSize, _lastBrushSize;
    private boolean _erase = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        SetupDrawing();
    }

    private void SetupDrawing(){
        _brushSize = getResources().getInteger(R.integer.medium_size);
        _lastBrushSize = _brushSize;

        _drawPath = new Path();
        _drawPaint = new Paint();

        _drawPaint.setColor(_paintColor);

        _drawPaint.setAntiAlias(true);
        _drawPaint.setStrokeWidth(_brushSize);
        _drawPaint.setStyle(Paint.Style.STROKE);
        _drawPaint.setStrokeJoin(Paint.Join.ROUND);
        _drawPaint.setStrokeCap(Paint.Cap.ROUND);

        _canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void StartNew(){
        _drawCanvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void SetEraseMode(boolean isErase){
        _erase = isErase;
        if (_erase){
            _drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else{
            _drawPaint.setXfermode(null);
        }
    }

    public void SetBrushSize(float newBrushSize){
        float pixleAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newBrushSize, getResources().getDisplayMetrics());
        _drawPaint.setStrokeWidth(pixleAmount);
    }

    public void SetLastBrushSize(float lastSize){
        _lastBrushSize = lastSize;
    }

    public float GetLastBrushSize(){
        return _lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _drawCanvas = new Canvas(_canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(_canvasBitmap, 0, 0, _canvasPaint);
        canvas.drawPath(_drawPath, _drawPaint);
    }

    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                _drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                _drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                _drawCanvas.drawPath(_drawPath, _drawPaint);
                _drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(String newColor){
        invalidate();
        _paintColor = Color.parseColor(newColor);
        _drawPaint.setColor(_paintColor);
    }
}
