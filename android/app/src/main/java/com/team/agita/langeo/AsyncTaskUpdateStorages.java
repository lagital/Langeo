package com.team.agita.langeo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


import com.appspot.id.app.langeo.Langeo;
import com.appspot.id.app.langeo.model.GetCurrentUserResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import java.io.IOException;

/**
 * Created by agita on 24.01.16.
 */
class AsyncTaskUpdateStorages extends AsyncTask<Void, Void, Integer> {
    private static final String LOG = "AsyncTaskUpdateStorages";
    private static final String MY_PREFS_NAME   = "com.team.agita.langeo.preferences";
    private static final String AUDIENCE        = "server:client_id:814462762552-kjl0ijdqfjf5q90p4p98g0cun3vjt557.apps.googleusercontent.com";
    private static final String SERVER_ADDRESS  = "http://langeoapp.appspot.com:8080/_ah/api";

    // Shared Preferences names
    private static final String SP_EMAIL        = "email";
    private static final String SP_SLIDESHOW    = "slideShow";
    private static final String SP_IS_VISIBLE   = "isVisible";

    private static Langeo myApiService = null;
    private Context mContext;

    public int mResult = 1;

    public AsyncTaskUpdateStorages(Context context){
        mContext = context;
    }

    @Override
    protected Integer doInBackground(Void...params) {
        if (myApiService == null) {  // Only do this once
            Langeo.Builder builder;
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext,
                    AUDIENCE);
            credential.setSelectedAccountName(LocalUser.getInstance().geteMail());
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

        Log.d(LOG, "Updating storages ...");
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(SP_EMAIL, LocalUser.getInstance().geteMail());
        editor.putBoolean(SP_SLIDESHOW, LocalUser.getInstance().getShowSlides());
        editor.putBoolean(SP_IS_VISIBLE, LocalUser.getInstance().getIsVisible());
        editor.commit();

        try {
            GetCurrentUserResponse user = new GetCurrentUserResponse();
            user.setId(LocalUser.getInstance().getId());
            user.setIsVisible(LocalUser.getInstance().getIsVisible());
                myApiService.langeoAPI().getCurrentUser().execute();
            return 0;
        } catch (IOException e) {
            Log.d(LOG, "IOException " + e.getMessage());
            return 1;
        }
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        mResult = result;
        Log.d(LOG, "finish");
    }
}