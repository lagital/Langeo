package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
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
class InitUserAsyncTask extends AsyncTask<String, Void, LocalUser> {
    private static Langeo myApiService = null;
    private static String LOG = "InitUserAsyncTask";
    private static String MY_PREFS_NAME = "LangeoPreferences";
    private static Boolean ERR = false;
    private Context mContext;
    public LocalUser mUser = null;

    public InitUserAsyncTask (Context context){
        mContext = context;
    }

    @Override
    protected LocalUser doInBackground(String... params) {
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
            User user;
            try {
                user = myApiService.langeoAPI().getUser(userId).execute();
            } catch (IOException e) {
                ERR = true;
                user = null;
            }
            if (user == null && !ERR) {
                //new user
                Log.d(LOG, "new user");
                try {
                    myApiService.langeoAPI().putUser(userId, user).execute();
                } catch (IOException e) {
                    ERR = true;
                }
                if (!ERR) {
                    // successful registration
                    LocalUser.getInstance().setId(user.getId());
                    LocalUser.getInstance().setShowSlides(true);
                    LocalUser.getInstance().setIsVisible(true);

                    SharedPreferences.Editor editor =
                            mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString("id", user.getId());
                    editor.putBoolean("slideShow", true);
                    editor.putBoolean("isVidsible", true);
                    editor.commit();
                }
            } else if (user != null) {
                //old user on new device
                Log.d(LOG, "old user on new device");
                LocalUser.getInstance().setId(user.getId());
                LocalUser.getInstance().setShowSlides(false);
                LocalUser.getInstance().setIsVisible(user.getIsVisible());

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString("id", user.getId());
                editor.putBoolean("slideShow", false);
                editor.putBoolean("isVidsible", user.getIsVisible());
                editor.commit();
            }
        } else {
            User user;
            try {
                user = myApiService.langeoAPI().getUser(userId).execute();
            } catch (IOException e) {
                ERR = true;
                user = null;
            }
            if (userId.equals(login)) {
                //standard Nth user login
                Log.d(LOG, "standard Nth user login");
                LocalUser.getInstance().setIsVisible(prefs.getBoolean("isVisible", true));
                LocalUser.getInstance().setId(prefs.getString("id", "idError"));
                LocalUser.getInstance().setShowSlides(prefs.getBoolean("slideShow", false));
            } else if (user != null && !user.getId().equals(login)) {
                //re-login with a new user
                Log.d(LOG, "re-login with a new user");
                LocalUser.getInstance().setIsVisible(prefs.getBoolean("isVisible", true));
                LocalUser.getInstance().setId(prefs.getString("id", "idError"));
                LocalUser.getInstance().setShowSlides(prefs.getBoolean("slideShow", false));

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString("id", user.getId());
                editor.putBoolean("slideShow", false);
                editor.putBoolean("isVisible", user.getIsVisible());
                editor.commit();
            }
        }
        return LocalUser.getInstance();
    }

    @Override
    protected void onPostExecute(LocalUser lUser)
    {
        Log.d(LOG, "finish");
    }
}