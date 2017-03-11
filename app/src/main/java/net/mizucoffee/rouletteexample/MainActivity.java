package net.mizucoffee.rouletteexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button) Button btn;
    @BindView(R.id.view) RouletteView rouletteView;

    private boolean isRoll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick(){
        if(!isRoll){
            btn.setText("stop");
            rouletteView.start();
            isRoll = true;
        } else {
            rouletteView.stop(new RouletteView.OnSuccessListener() {
                @Override
                public void onSuccess(int pos) {
                    Toast.makeText(getApplicationContext(),"Result: "+pos,Toast.LENGTH_LONG).show();
                    btn.setText("start");
                    btn.setEnabled(true);
                    isRoll = false;
                }
            });
            btn.setText("Please wait");
            btn.setEnabled(false);
            isRoll = false;
        }
    }

}
