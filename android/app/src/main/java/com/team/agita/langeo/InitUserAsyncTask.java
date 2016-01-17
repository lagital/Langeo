package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.myapplicationid.langeo.Langeo;
import com.appspot.myapplicationid.langeo.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import java.io.IOException;

/**
 * Created by agita on 09.01.16.
 */
class InitUserAsyncTask extends AsyncTask<String, Void, Integer> {
    private static Langeo myApiService = null;
    private static String LOG = "InitUserAsyncTask";
    private static String MY_PREFS_NAME = "LangeoPreferences";
    private Context mContext;
    public Integer mResult = 0;

    public InitUserAsyncTask (Context context){
        mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (myApiService == null) {  // Only do this once
            //For local tests:
            Langeo.Builder builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("http://192.168.100.9:8080/_ah/api")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // For deploy tests:
            /*
            Langeo.Builder builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://langeoapp.appspot.com/_ah/api/");
            */
            myApiService = builder.build();
        }

        String userId = params[0];
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String login = prefs.getString("id", "undefined");
        if (login.equals("undefined")) {
            Log.d(LOG, "undefined login");
            User user = null;
            try {
                user = myApiService.langeoAPI().getUser(userId).execute();
            } catch (IOException e) {
                Log.d(LOG, "IOException " + e.getMessage());
                return 1;
            }
            if (user != null) {
                //old user on new device
                Log.d(LOG, "old user on new device");
                LocalUser.getInstance().setShowSlides(false);
                LocalUser.getInstance().setId(user.getId());
                LocalUser.getInstance().setIsVisible(user.getIsVisible());

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString("id", user.getId());
                editor.putBoolean("slideShow", false);
                editor.putBoolean("isVisible", user.getIsVisible());
                editor.commit();

                return 0;
            } else {
                //new user
                Log.d(LOG, "new user");
                try {
                    user = new User();
                    user.setId(userId);
                    user.setIsVisible(true);
                    myApiService.langeoAPI().putUser(userId, user).execute();
                } catch (IOException e) {
                    Log.d(LOG, "IOException " + e.getMessage());
                    return 2;
                }
                // successful registration
                Log.d(LOG, "successful registration");
                LocalUser.getInstance().setShowSlides(true);
                LocalUser.getInstance().setId(user.getId());
                LocalUser.getInstance().setIsVisible(true);

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean("slideShow", true);
                editor.putString("id", user.getId());
                editor.putBoolean("isVisible", true);
                editor.commit();

                return 0;
            }
        } else {
            Log.d(LOG,"Some login is stored on device!");
            if (userId.equals(login)) {
                //standard Nth user login
                LocalUser.getInstance().setShowSlides(prefs.getBoolean("slideShow", false));
                LocalUser.getInstance().setIsVisible(prefs.getBoolean("isVisible", true));
                LocalUser.getInstance().setId(prefs.getString("id", "idError"));
                Log.d(LOG, "standard Nth user login");

                return 0;
            } else {
                //re-login with a new user
                User user;
                try {
                    user = myApiService.langeoAPI().getUser(userId).execute();
                } catch (IOException e) {
                    Log.d(LOG, "IOException " + e.getMessage());
                    return 1;
                }

                LocalUser.getInstance().setShowSlides(prefs.getBoolean("slideShow", false));
                LocalUser.getInstance().setIsVisible(prefs.getBoolean("isVisible", true));
                LocalUser.getInstance().setId(prefs.getString("id", "idError"));
                Log.d(LOG, "re-login with a new user");

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString("id", user.getId());
                editor.putBoolean("slideShow", false);
                editor.putBoolean("isVisible", user.getIsVisible());
                editor.commit();

                return 0;
            }
        }
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        LocalUser.getInstance().setInitialized(result);
        Log.d(LOG, "finish");
    }
}