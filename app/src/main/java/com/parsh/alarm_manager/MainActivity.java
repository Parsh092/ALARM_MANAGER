package com.parsh.alarm_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
//    Button button;
//    EditText editText;
    TextView ct;
    Button setTime;
    Button setAlarm;
    Button cancelAlarm;
    static final int ALRAM_CODE = 100;
    private MaterialTimePicker picker;
    Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        button = findViewById(R.id.buttonset);
        ct = findViewById(R.id.ct);
        setTime = findViewById(R.id.setTime);
        setAlarm = findViewById(R.id.setAlarm);
        cancelAlarm = findViewById(R.id.cancelAlarm);
//        editText = findViewById(R.id.editTextTextPersonName);


        SimpleDateFormat sdf = new SimpleDateFormat("'Date: 'dd-MM-yyyy '\nTime: 'HH:mm");
        String currentDateAndTime = sdf.format(new Date());
        ct.setText(currentDateAndTime);

        createNotificationChannel();

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                long time = Integer.parseInt(editText.getText().toString());
//                long triggerTime = System.currentTimeMillis() + (time * 1000);
//                Intent ibroadcast = new Intent(MainActivity.this, Tones.class);
//                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALRAM_CODE, ibroadcast, PendingIntent.FLAG_UPDATE_CURRENT);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
//            }
//        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarms();
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarms();
            }
        });
    }

    private void cancelAlarms() {
        Intent intent = new Intent(this, myBroadCastReceiver.class);
        pendingIntent = pendingIntent.getBroadcast(this, ALRAM_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }
    private void setAlarms() {
        Intent intent = new Intent(this, myBroadCastReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntent = pendingIntent.getBroadcast(this, ALRAM_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder().
                setTimeFormat(TimeFormat.CLOCK_12H).
                setHour(12).
                setMinute(0).
                setTitleText("Selected Alarm Time").
                build();

        picker.show(getSupportFragmentManager(), "Notification");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picker.getHour() > 12) {
                    setTime.setText(
                            String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + "PM");
                } else {
                    setTime.setText(picker.getHour() + " : " + picker.getMinute() + "AM");
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }

        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Testing the notification";
            String discription = "This channel is to send the notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(discription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}