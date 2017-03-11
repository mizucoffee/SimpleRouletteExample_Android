package net.mizucoffee.rouletteexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class RouletteView extends View {
 
    private Paint mPaint = new Paint();
    private int piece = 6;
    private int[] colors = null;
    private int currentId = 0;
    private boolean isRoll = false;
    private boolean isBlink = false;

    interface OnSuccessListener{
        void onSuccess(int position);
    }
 
    public RouletteView(Context context) {
        super(context);
    }

    public RouletteView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouletteView);
        piece = a.getInt(R.styleable.RouletteView_roulette_piece, 6);
        a.recycle();
    }

    public RouletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RouletteView);
        piece = a.getInt(R.styleable.RouletteView_roulette_piece, 6);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        hh = h;
        ww = w;
    }
    int hh;
    int ww;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int s = ww > hh ? hh : ww;
        int l = ww < hh ? hh : ww;
        RectF rectf = new RectF(0, 0, s,s);
        int offset = (l - s) / 2;
        rectf.offset(0,offset);

        int angle = 360 / piece;


        if (colors == null) {
            Random r = new Random();
            colors = new int[piece];
            for (int i = 0; i != piece; i++) {
                colors[i] = Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            }
        }

        for (int i = 0; i != piece; i++){
            mPaint.setColor(colors[i]);
            canvas.drawArc(rectf, -90 + (angle * (i-1)), angle, true, mPaint);
        }

        if(isRoll || isBlink){
            mPaint.setColor(Color.parseColor("#BBFFFFFF"));
            int n = currentId == 0 ? piece - 1: currentId-1;
            canvas.drawArc(rectf, -90 + (angle * n), angle, true, mPaint);
        }


    }

    public void start(){
        if(isRoll) return;
        final Handler h = new Handler(Looper.getMainLooper());
        isRoll = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRoll) {
                    currentId = currentId > piece - 2 ? 0 : currentId + 1;
                    reroad(h);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int count = new Random().nextInt(5) + 7;
                isBlink = true;
                for(int i = 0;count != i;i++) {
                    currentId = currentId > piece - 2 ? 0 : currentId + 1;
                    reroad(h);
                    try {
                        Thread.sleep(100 + (i * 2) * (i * 3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(mListener != null) h.post(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onSuccess(currentId);
                    }
                });

                for(int i = 0;3 != i;i++) {
                    isBlink = false;
                    reroad(h);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isBlink = true;
                    reroad(h);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private OnSuccessListener mListener;

    public void stop(OnSuccessListener listener){
        isRoll = false;
        mListener = listener;
    }

    private void reroad(Handler h){
        new AwaitInvoker().invokeAndWait(h, new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }
}