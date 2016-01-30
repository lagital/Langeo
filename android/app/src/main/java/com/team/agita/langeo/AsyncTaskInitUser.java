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
class AsyncTaskInitUser extends AsyncTask<String, Void, Integer> {
    private static final String LOG = "AsyncTaskInitUser";
    private static final String MY_PREFS_NAME = "LangeoPreferences";
    private static final String LOCAL_ADDRESS = "http://192.168.100.9:8080/_ah/api";

    // Shared Preferences names
    private static final String SP_UNDEFINED = "undefined";
    private static final String SP_ID = "id";
    private static final String SP_SLIDESHOW = "slideShow";
    private static final String SP_IS_VISIBLE = "isVisible";
    private static final String SP_ID_ERROR = "isVisible";


    private static Langeo myApiService = null;
    private Context mContext;

    public AsyncTaskInitUser(Context context){
        mContext = context;
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (myApiService == null) {  // Only do this once
            //For local tests:
            Langeo.Builder builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(LOCAL_ADDRESS)
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
        String login = prefs.getString(SP_ID, SP_UNDEFINED);
        if (login.equals(SP_UNDEFINED)) {
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
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, user.getIsVisible()),
                        prefs.getString(SP_ID, SP_ID_ERROR));


                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(SP_ID, user.getId());
                editor.putBoolean(SP_SLIDESHOW, false);
                editor.putBoolean(SP_IS_VISIBLE, user.getIsVisible());
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
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, true),
                        prefs.getBoolean(SP_IS_VISIBLE, true),
                        prefs.getString(SP_ID, SP_ID_ERROR));

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean("slideShow", true);
                editor.putString(SP_ID, user.getId());
                editor.putBoolean(SP_IS_VISIBLE, true);
                editor.commit();

                return 0;
            }
        } else {
            Log.d(LOG,"Some login is stored on device!");
            if (userId.equals(login)) {
                //standard Nth user login
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, true),
                        prefs.getString(SP_ID, SP_ID_ERROR));
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
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, true),
                        prefs.getString(SP_ID, SP_ID_ERROR));
                Log.d(LOG, "re-login with a new user");

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(SP_ID, user.getId());
                editor.putBoolean(SP_SLIDESHOW, false);
                editor.putBoolean(SP_IS_VISIBLE, user.getIsVisible());
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