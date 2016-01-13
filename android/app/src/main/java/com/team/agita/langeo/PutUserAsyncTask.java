package com.team.agita.langeo;

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
class PutUserAsyncTask extends AsyncTask<User, Void, Boolean> {
    private static Langeo myApiService = null;
    public boolean successfullPut = false;

    @Override
    protected Boolean doInBackground(User... params) {
        if(myApiService == null) {  // Only do this once
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

        User user = params[0];

        try {
            myApiService.langeoAPI().putUser(user.getId(), user).execute();
            return true;
        } catch (IOException e) {
            Log.d("LOG", e.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        successfullPut = result;
    }
}