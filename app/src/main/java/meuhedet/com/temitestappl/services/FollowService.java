package meuhedet.com.temitestappl.services;

import android.util.Log;
import java.time.LocalDateTime;
import meuhedet.com.temitestappl.MainActivity;

public class FollowService extends Thread {

    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now().plusSeconds(10);
        while (true) {
            Log.i("FollowService", "Is interrupted " + isInterrupted());
            if (isInterrupted()) {
                Log.i("FollowService", "Robot founded you");
                return;
            }
            if (LocalDateTime.now().isAfter(localTime)) {
                MainActivity.instance.callHelp("ניקיטה דורושנקו");
                return;
            }
            Log.i("FollowService", "Robot can't find you");
            try {
                Thread.sleep(Long.parseLong("10000"));
            } catch (InterruptedException e) {
                Log.i("FollowService", "Robot founded you");
                return;
            }
        }
    }
}
