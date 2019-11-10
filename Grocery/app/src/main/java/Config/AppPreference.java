package Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by android on 8/8/2018.
 */

public class AppPreference {

    public static final String SHARED_PREFERENCE_NAME = "RESTAURANT";
    public static final String USER_ID = "user_id";

    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(USER_ID, "");
    }
    public static void setUserId(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_ID, value);
        editor.commit();
    }

}
