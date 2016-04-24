package com.team.agita.langeo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.id.app.langeo.Langeo;
import com.appspot.id.app.langeo.model.PostOrPutMeetingRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.team.agita.langeo.map.ActivityMaps;

import java.io.IOException;

/**
 * Created by agita on 09.01.16.
 */
public class AsyncTaskPostOrPutMeeting extends AsyncTask<PostOrPutMeetingRequest, Void, Void> {
    private static final String LOG = "AsyncTaskPostOrPut";
    private static final String AUDIENCE        = "server:client_id:814462762552-kjl0ijdqfjf5q90p4p98g0cun3vjt557.apps.googleusercontent.com";
    private static final String SERVER_ADDRESS = "https://langeoapp.appspot.com/_ah/api/";
    private static final String CRED_ADDRESS = "";

    private static Langeo myApiService = null;
    private ActivityMaps mActivity;

    public AsyncTaskPostOrPutMeeting(ActivityMaps activity){
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(PostOrPutMeetingRequest... params) {
        Log.d(LOG, "Start doInBackground");
        if (myApiService == null) {  // Only do this once
            Langeo.Builder builder;
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mActivity,
                    AUDIENCE);
            credential.setSelectedAccountName(LocalUser.getInstance().eMail);
            if (BuildConfig.DEBUG) {
                builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), credential)
                        .setRootUrl(mActivity.getResources().getString(R.string.local_server_ip) +
                                mActivity.getResources().getString(R.string.local_server_port))
                        .setApplicationName(mActivity.getResources().getString(R.string.app_name))
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

        PostOrPutMeetingRequest request = params[0];
        try {
            myApiService.langeoAPI().postMeeting(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "Finish doInBackground");
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        try {
            mActivity.updateUI(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "finish");
    }
}