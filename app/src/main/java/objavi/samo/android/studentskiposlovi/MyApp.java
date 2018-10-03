package objavi.samo.android.studentskiposlovi;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
