package com.team.agita.langeo.notifications;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.appspot.id.app.langeo.model.User;
import com.team.agita.langeo.R;
import com.team.agita.langeo.map.ActivityMaps;

import java.util.Random;

/**
 * Created by agita on 04.02.16.
 */

public class NotificationManager extends IntentService {

    private final static String NOTIFICATION_NEW_USER = "wants to join!";
    private final static String NOTIFICATION_NEW_MEETING = "appeared on the map.";
    private final static String NOTIFICATION_USER_LEFT = "left the chat.";
    private final static String NOTIFICATION_CHAT_UPDATE = "New messages in your chat.";

    private int chatUpdatesID;

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        //...
        // Do work here, based on the contents of dataString
        //...
    }

    public NotificationManager() {
        super("default");
    }

    //TODO: Meeting meeting and Chat chat as input optional parms:
    public void createNotification (User user, Integer notificationType) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

        switch (notificationType) {
            case 0:
                //user wants to connect to your meeting
                mBuilder.setSmallIcon(R.drawable.notification_new_user_in_meeting)
                        //TODO: change id to login
                        .setContentTitle(user.getId() + NOTIFICATION_NEW_USER)
                        //TODO: change text to the first user message (?)
                        .setContentText("Hello World!")
                        .setPriority(2);
                chatUpdatesID = 0;
            case 1:
                //there is a new meeting on the map
                mBuilder.setSmallIcon(R.drawable.notification_new_meeting)
                        //TODO: add Meeting name
                        .setContentTitle(NOTIFICATION_NEW_MEETING)
                        //TODO: add Meeting address
                        .setContentText("Hello World!")
                        .setPriority(-1);
                chatUpdatesID = 0;
            case 2:
                //user N left the meeting
                mBuilder.setSmallIcon(R.drawable.notification_user_left_meeting)
                        //TODO: change id to login
                        .setContentTitle(user.getId() + NOTIFICATION_USER_LEFT)
                        .setPriority(-1);
                chatUpdatesID = 0;
            case 3:
                //there are some updates in your chat
                mBuilder.setSmallIcon(R.drawable.notification_chat_update)
                        .setContentTitle(NOTIFICATION_CHAT_UPDATE)
                        //TODO: Number of messages
                        .setContentText("Hello World!")
                        .setPriority(1);
                Random rand = new Random();
                int randomNum = rand.nextInt(731);
                chatUpdatesID = rand.nextInt();
            default:
                chatUpdatesID = 0;
        }

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ActivityMaps.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ActivityMaps.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify();
    }
}