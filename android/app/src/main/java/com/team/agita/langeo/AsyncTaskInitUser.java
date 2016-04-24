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
import java.sql.Time;

/**
 * Created by agita on 09.01.16.
 */
class AsyncTaskInitUser extends AsyncTask<String, Void, Void> {
    private static final String TAG = "SyncTaskInitUser";
    private static final String MY_PREFS_NAME = "com.team.agita.langeo.preferences";
    private static final String SERVER_ADDRESS = "https://langeoapp.appspot.com/_ah/api/";
    private static final String AUDIENCE = "server:client_id:814462762552-kjl0ijdqfjf5q90p4p98g0cun3vjt557.apps.googleusercontent.com";

    // Shared Preferences names
    private static final String SP_UNDEFINED = "u";
    private static final String SP_EMAIL = "email";
    private static final String SP_SLIDESHOW = "slideShow";
    private static final String SP_IS_VISIBLE = "isVisible";

    private static Langeo myApiService = null;

    private Context mContext;
    private String eMail;
    private String eMailLocal;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mPrefsEditor;

    public AsyncTaskInitUser(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        eMail = params[0];

        if (myApiService == null) {  // Only do this once
            Langeo.Builder builder;
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext,
                    AUDIENCE);
            credential.setSelectedAccountName(eMail);
            if (BuildConfig.DEBUG) {
                builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), credential)
                        .setRootUrl(mContext.getResources().getString(R.string.local_server_ip) +
                                mContext.getResources().getString(R.string.local_server_port))
                        .setApplicationName(mContext.getResources().getString(R.string.app_name))
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

        mPrefs = mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        mPrefsEditor = mPrefs.edit();

        eMailLocal = mPrefs.getString(SP_EMAIL, SP_UNDEFINED);
        if (eMailLocal.equals("u")) {
            Log.d(TAG, "undefined login");
            GetCurrentUserResponse gUser;
            try {
                gUser = myApiService.langeoAPI().getCurrentUser().execute();
            } catch (IOException e) {
                Log.d(TAG, "IOException " + e.getMessage());
                e.printStackTrace();
                gUser = null;
            }

            if (gUser != null) {
                //old user on a new device
                Log.d(TAG, "old user on a new device");
                LocalUser.getInstance().fill(eMail, false, gUser.getIsVisible());
                fillPreferences(eMail, false, gUser.getIsVisible());
                LocalUser.getInstance().setInitialized(true);
            } else {
                //new user
                LocalUser.getInstance().setInitialized(registerUser());
            }
        } else {
            Log.d(TAG, "Some email is stored on device!");
            if (eMail.equals(eMailLocal)) {
                //standard user login
                LocalUser.getInstance().fill(
                        mPrefs.getString(SP_EMAIL, SP_UNDEFINED),
                        mPrefs.getBoolean(SP_SLIDESHOW, false),
                        mPrefs.getBoolean(SP_IS_VISIBLE, true));
                Log.d(TAG, "standard user log in");
                LocalUser.getInstance().setInitialized(true);
            } else {
                //re-login with a new user
                Log.d(TAG, "re-login with a new user - " + eMail + " != " + eMailLocal);
                GetCurrentUserResponse gUser;
                try {
                    gUser = myApiService.langeoAPI().getCurrentUser().execute();
                } catch (IOException e) {
                    Log.d(TAG, "IOException " + e.getMessage());
                    clearPreferences();
                    gUser = null;
                }
                if (gUser != null) {
                    LocalUser.getInstance().fill(
                            gUser.getEmail(),
                            false,
                            gUser.getIsVisible());
                    fillPreferences(gUser.getEmail(), false, gUser.getIsVisible());
                    LocalUser.getInstance().setInitialized(true);
                } else {
                    //new user
                    LocalUser.getInstance().setInitialized(registerUser());
                }
            }
        }
        return null;
    }


    private void fillPreferences (String eMail, Boolean slideshow, Boolean isVisible) {
        mPrefsEditor.putString(SP_EMAIL, eMail);
        mPrefsEditor.putBoolean(SP_SLIDESHOW, slideshow);
        mPrefsEditor.putBoolean(SP_IS_VISIBLE, isVisible);
        mPrefsEditor.commit();
    }

    private void clearPreferences () {
        mPrefsEditor.putString(SP_EMAIL, "u");
        mPrefsEditor.putBoolean(SP_SLIDESHOW, true);
        mPrefsEditor.putBoolean(SP_IS_VISIBLE, true);
        mPrefsEditor.commit();
    }

    private Boolean registerUser () {
        //new user
        Log.d(TAG, "new user");
        PutCurrentUserRequest pUser = new PutCurrentUserRequest();
        pUser.setIsVisible(true);
        try {
            myApiService.langeoAPI().putCurrentUser(pUser).execute();
        } catch (IOException e) {
            Log.d(TAG, "IOException " + e.getMessage());
            return false;
        }

        // successful registration
        Log.d(TAG, "successful registration");
        LocalUser.getInstance().fill(eMail, true, true);
        fillPreferences(eMail, true, true);

        return true;
    }
}