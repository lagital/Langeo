package com.team.agita.langeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.id.app.langeo.Langeo;
import com.appspot.id.app.langeo.model.GetCurrentUserResponse;
import com.appspot.id.app.langeo.model.PutCurrentUserRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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
    private static final String SERVER_ADDRESS = "https://langeoapp.appspot.com/_ah/api/";

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
            Langeo.Builder builder;
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext,
                    "server:client_id:814462762552-kjl0ijdqfjf5q90p4p98g0cun3vjt557.apps.googleusercontent.com");
            credential.setSelectedAccountName(LocalUser.getInstance().eMail);
            if (BuildConfig.DEBUG) {
                builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), credential)
                        .setRootUrl(LOCAL_ADDRESS)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
            } else {
                builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), credential)
                        .setRootUrl(SERVER_ADDRESS);
            }
            myApiService = builder.build();
        }

        String userID = params[0];
        SharedPreferences prefs = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String userIDLocal = prefs.getString(SP_ID, SP_UNDEFINED);
        if (userIDLocal.equals(SP_UNDEFINED)) {
            Log.d(LOG, "undefined login");
            GetCurrentUserResponse gUser = null;
            try {
                gUser = myApiService.langeoAPI().getCurrentUser().execute();
            } catch (IOException e) {
                Log.d(LOG, "IOException " + e.getMessage());
                gUser = null;
            }
            if (gUser != null) {
                //old user on new device
                Log.d(LOG, "old user on new device");
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, gUser.getIsVisible()),
                        prefs.getString(SP_ID, SP_ID_ERROR));


                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(SP_ID, gUser.getEmail());
                editor.putBoolean(SP_SLIDESHOW, false);
                editor.putBoolean(SP_IS_VISIBLE, gUser.getIsVisible());
                editor.commit();

                return 0;
            } else {
                //new user
                PutCurrentUserRequest pUser = new PutCurrentUserRequest();
                Log.d(LOG, "new user");
                try {
                    pUser.setIsVisible(true);
                    myApiService.langeoAPI().putCurrentUser(pUser).execute();
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
                //editor.putString(SP_ID, pUser.get);
                editor.putBoolean(SP_IS_VISIBLE, true);
                editor.commit();

                return 0;
            }
        } else {
            Log.d(LOG,"Some id_local is stored on device!");
            if (userID.equals(userIDLocal)) {
                //standard Nth user login
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, true),
                        prefs.getString(SP_ID, SP_ID_ERROR));
                Log.d(LOG, "standard Nth user email_local");

                return 0;
            } else {
                //re-login with a new user
                GetCurrentUserResponse user;
                try {
                    user = myApiService.langeoAPI().getCurrentUser().execute();
                } catch (IOException e) {
                    Log.d(LOG, "IOException " + e.getMessage());
                    return 1;
                }
                LocalUser.getInstance().fill(prefs.getBoolean(SP_SLIDESHOW, false),
                        prefs.getBoolean(SP_IS_VISIBLE, true),
                        prefs.getString(SP_ID, SP_ID_ERROR));
                Log.d(LOG, "re-email_local with a new user");

                SharedPreferences.Editor editor =
                        mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(SP_ID, user.getEmail());
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