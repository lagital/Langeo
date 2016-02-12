package com.team.agita.langeo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.id.app.langeo.Langeo;
import com.appspot.id.app.langeo.model.GetCurrentUserResponse;
import com.appspot.id.app.langeo.model.GetMeetingResponse;
import com.appspot.id.app.langeo.model.GetMeetingsResponse;
import com.appspot.id.app.langeo.model.PostOrPutMeetingRequest;
import com.appspot.id.app.langeo.model.PutCurrentUserRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.team.agita.langeo.map.ActivityMaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agita on 09.01.16.
 */
public class AsyncTaskGetMeetings extends AsyncTask<Void, Void, List<GetMeetingResponse>> {
    private static final String LOG = "AsyncTaskGetMeetings";
    private static final String LOCAL_ADDRESS = "http://192.168.100.9:8080";
    private static final String SERVER_ADDRESS = "https://langeoapp.appspot.com/_ah/api/";
    private static final String CRED_ADDRESS = ":1-web-app.apps.googleusercontent.com";

    private static Langeo myApiService = null;
    private ActivityMaps mActivity;

    public AsyncTaskGetMeetings(ActivityMaps activity){
        mActivity = activity;
    }

    @Override
    protected List<GetMeetingResponse> doInBackground(Void... params) {
        Log.d(LOG, "Start doInBackground");
        if (myApiService == null) {  // Only do this once
            Langeo.Builder builder;
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mActivity,
                    "server:client_id:814462762552-e30hbh2d4t6c45oh8sfn6l8ocp6i2gfm.apps.googleusercontent.com");
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
                builder = new Langeo.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
                        .setRootUrl(SERVER_ADDRESS);
            }
            myApiService = builder.build();
        }

        Log.d(LOG, "Finish doInBackground");

        try {
            return myApiService.langeoAPI().getMeetings(LocalUser.getInstance().getCityId())
                    .execute().getMeetings();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<GetMeetingResponse>(){};
        }
    }

    @Override
    protected void onPostExecute(List<GetMeetingResponse> responses)
    {
        mActivity.mMeetings = responses;
        try {
            mActivity.updateUI(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG, "finish");
    }
}