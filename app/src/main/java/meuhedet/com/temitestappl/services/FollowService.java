package meuhedet.com.temitestappl.services;

import android.util.Log;
import java.time.LocalDateTime;
import meuhedet.com.temitestappl.MainActivity;

public class FollowService extends Thread {

    @Override
    public void run() {
//        LocalDateTime localTime = LocalDateTime.now().plusSeconds(10);
        int count = 0;
        while (true) {
            Log.i("FollowService", "Is interrupted " + isInterrupted());
            if (isInterrupted()) {
                Log.i("FollowService", "Robot founded you");
                return;
            }
            Log.i("FollowService", "Temi asked how are you " + count);
            if (count == 3) {
                MainActivity.instance.callHelp("ניקיטה דורושנקו");
                return;
            }
            Log.i("FollowService", "Robot can't find you");
            MainActivity.instance.askQuestion("אני לא רואה אותך, אתה בסדר?");
            try {
                Thread.sleep(Long.parseLong("10000"));
                count++;
            } catch (InterruptedException e) {
                Log.i("FollowService", "Robot founded you");
                return;
            }
        }
    }
}
