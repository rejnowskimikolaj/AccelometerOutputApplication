package com.example.rent.accelometeroutputapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccOutputActivity extends AppCompatActivity implements SensorEventListener {

    @BindView(R.id.x_textView)
    TextView xTextView;
    @BindView(R.id.y_textView)
    TextView yTextView;
    @BindView(R.id.z_textView)
    TextView zTextView;

    private SensorManager mSensorManager;
    private Sensor mAcc;
    FileManager fileManager;
    @BindView(R.id.stop_button)
    Button stopButton;
    @BindView(R.id.read_button)
    Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_output);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcc=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        fileManager = FileManager.instance;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            xTextView.setText(x+"");
            yTextView.setText(y+"");
            zTextView.setText(z+"");

            float[] arr = {x,y,z};
            fileManager.saveAccOutputToFile(arr);

            // Log.d("X:Y:Z:", x+":"+y+":"+z);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        fileManager.openOutputStreamWriter(System.currentTimeMillis()+"");
        mSensorManager.registerListener(this,mAcc, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fileManager.closeOutputStream();
        mSensorManager.unregisterListener(this);
    }

    @OnClick(R.id.read_button)
    public void onReadButtonClicked(){
        try {
            Log.d("OUTPUT", "onStopButtonClicked: "+fileManager.readFile() );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.stop_button)
    public void onStopButtonClicked(){
        mSensorManager.unregisterListener(this);
    }

}
