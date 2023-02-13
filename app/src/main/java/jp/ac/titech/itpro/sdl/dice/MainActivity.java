package jp.ac.titech.itpro.sdl.dice;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mAccSensor;

    private final int NormalMode = 0;
    private final int ShigoroMode = 1;
    private final int PinzoroMode = 2;
    private int Mode = 0;

    private Random r = new Random();
    private int dice = 1;
    private int turn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //縦画面固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //最初のTextviewの文字を見えなくする
        TextView diceNumber = (TextView) findViewById(R.id.diceNumber);
        diceNumber.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        TextView modeText = (TextView) findViewById(R.id.modeText);
        modeText.setVisibility(View.INVISIBLE);

        //ボタンが押された時の変化
        Button tapHere = (Button) findViewById(R.id.tapHere);
        tapHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //サイコロを振るターン
                if (turn == 0) {

                    dice = throwTheDice();

                    //数値表示
                    TextView diceNumber = (TextView) findViewById(R.id.diceNumber);
                    diceNumber.setText("サイコロの値は" + dice);
                    //見えなくする
                    diceNumber.setVisibility(View.INVISIBLE);

                    //画像表示
                    ImageView diceImage = (ImageView) findViewById(R.id.dice);


                    switch (dice) {
                        case 1:
                            diceImage.setImageResource(R.drawable.diceone);
                            break;
                        case 2:
                            diceImage.setImageResource(R.drawable.dicetwo);
                            break;
                        case 3:
                            diceImage.setImageResource(R.drawable.dicethree);
                            break;
                        case 4:
                            diceImage.setImageResource(R.drawable.dicefour);
                            break;
                        case 5:
                            diceImage.setImageResource(R.drawable.dicefive);
                            break;
                        case 6:
                            diceImage.setImageResource(R.drawable.dicesix);
                            break;
                    }

                    turn = 1;
                } else {
                    //サイコロをリセットして終了
                    ImageView diceImage = (ImageView) findViewById(R.id.dice);
                    diceImage.setImageResource(R.drawable.dicerandom);

                    turn = 0;
                }

            }
        });

    }

    //センサに変化があった時に呼び出されるメソッド
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("加速度", "x=" + sensorEvent.values[0] + "y=" + sensorEvent.values[1] + "z=" + sensorEvent.values[2]);
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("x=" + sensorEvent.values[0] + "y=" + sensorEvent.values[1] + "z=" + sensorEvent.values[2]);
            //見えなくする
            textView.setVisibility(View.INVISIBLE);
            //モードを切り替える

            if (sensorEvent.values[1] <= -3.0) {
                Mode = NormalMode;
            } else if (sensorEvent.values[1] >= 3.0) {
                Mode = ShigoroMode;
            } else if (sensorEvent.values[0] >= 3.0) {
                Mode = PinzoroMode;
            }


            TextView modeText = (TextView) findViewById(R.id.modeText);
            if (Mode == ShigoroMode) {
                modeText.setText("456モードです");
            } else if (Mode == PinzoroMode) {
                modeText.setText("ピンゾロモードです");
            } else if (Mode == NormalMode) {
                modeText.setText("ノーマルモードです");
            }
            //見えなくする
            modeText.setVisibility(View.INVISIBLE);


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //画面が表示された時に呼ばれるメソッド
    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    //サイコロの目を決める関数
    public int throwTheDice() {
        int Number = 1;

        if (Mode == NormalMode) {
            Number = r.nextInt(6) + 1;
        } else if (Mode == ShigoroMode) {
            Number = r.nextInt(3) + 4;
        } else if (Mode == PinzoroMode) {
            Number = 1;
        }

        return Number;
    }

}

// Comment Test 20230213