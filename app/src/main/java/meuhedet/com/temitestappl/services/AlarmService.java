package meuhedet.com.temitestappl.services;

import android.util.Log;

import meuhedet.com.temitestappl.MainActivity;

public class AlarmService extends Thread {

    @Override
    public void run() {
        while (true) {
            Log.i("FollowService", "Is interrupted " + isInterrupted());
            if (isInterrupted()) {
                Log.i("AlarmService", "Alarm finished");
                return;
            }
            MainActivity.instance.speak("אני מבצעת סריקה בחדרי המרפאה");
            try {
                Thread.sleep(Long.parseLong("5000"));
            } catch (InterruptedException e) {
                Log.i("AlarmService", "Alarm finished");
                return;
            }
        }
    }
}
