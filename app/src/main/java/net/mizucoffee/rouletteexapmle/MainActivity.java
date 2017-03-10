package net.mizucoffee.rouletteexapmle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view1) TextView view1;

    @BindView(R.id.view2)
    TextView view2;

    @BindView(R.id.view3)
    TextView view3;

    @BindView(R.id.view4)
    TextView view4;

    @BindView(R.id.view5)
    TextView view5;

    @BindView(R.id.button)
    Button btn;

    TextView[] arrayTv = new TextView[VIEW_NUM];

    boolean isRoll = false;

    Handler handler;
    int[] colors = new int[VIEW_NUM];

    public static final int VIEW_NUM = 5;
    int nowNumber = 0; // 0~4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        colors[0] = getResources().getColor(android.R.color.holo_red_light);
        colors[1] = getResources().getColor(android.R.color.holo_orange_light);
        colors[2] = getResources().getColor(android.R.color.holo_green_light);
        colors[3] = getResources().getColor(android.R.color.holo_blue_light);
        colors[4] = getResources().getColor(android.R.color.holo_purple);

        arrayTv[0] = view1;
        arrayTv[1] = view2;
        arrayTv[2] = view3;
        arrayTv[3] = view4;
        arrayTv[4] = view5;
    }

    @OnClick(R.id.button)
    public void onClick(){
        if(!isRoll){
            btn.setText("stop");
            isRoll = true;

            handler = new Handler();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(isRoll){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int before = nowNumber - 1;
                                int now = nowNumber;
                                if(before < 0) before = VIEW_NUM - 1;
                                arrayTv[before].setBackgroundColor(colors[before]);
                                arrayTv[now].setBackgroundColor(Color.WHITE);

                                nowNumber++;
                                if(nowNumber > VIEW_NUM - 1) nowNumber = 0;
                            }
                        });
                        
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        } else {
            isRoll = false;
        }
    }

}
