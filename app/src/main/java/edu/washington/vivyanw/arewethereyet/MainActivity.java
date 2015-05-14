package edu.washington.vivyanw.arewethereyet;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    String message;
    String phone;
    int interval;
    boolean valid;

    Button startStop;
    PendingIntent alarmIntent;

    BroadcastReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText msg = (EditText) findViewById(R.id.txtMessage);
        final EditText num = (EditText) findViewById(R.id.txtPhoneNum);
        final EditText timeGap = (EditText) findViewById(R.id.numInterval);
        startStop = (Button) findViewById(R.id.btnStart);

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int millisToMin = 1000 * 60;

                String action = (String) startStop.getText();
                if (action.equalsIgnoreCase("start")) {
                    message = msg.getText().toString();
                    phone = num.getText().toString();
                    interval = timeGap.getInputType();
                    if (message == null || phone == null || interval == 0) {
                        valid = false;
                    } else if (interval < 0) {
                        valid = false;
                    } /*else if (interval.length() == 1 && interval.contains("0")) {
                        valid = false;
                    } */else {
                        valid = true;
                    }

                    if (valid) {
                        alarmReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Toast.makeText(MainActivity.this, phone + ": " + message, Toast.LENGTH_SHORT).show();
                            }
                        };
                        registerReceiver(alarmReceiver, new IntentFilter("edu.washington.vivyanw.alarm"));
                        Intent i = new Intent();
                        i.setAction("edu.washington.vivyanw.alarm");
                        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 5000, interval * millisToMin, alarmIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter all fields!", Toast.LENGTH_LONG).show();
                    }

                    startStop.setText(R.string.end);
                } else {
                    am.cancel(alarmIntent);
                    alarmIntent.cancel();
                    startStop.setText(R.string.go);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
