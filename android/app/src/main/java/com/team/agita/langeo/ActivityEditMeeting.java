package com.team.agita.langeo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by pborisenko on 4/27/2016.
 */

public class ActivityEditMeeting extends AppCompatActivity {

    private static final String TAG = "ActivityEditMeeting";

    private Button      mOKButton;
    private Button      mCancelButton;
    private TimePicker  mTimePicker;
    private DatePicker  mDatePicker;
    private EditText    mEditTitle;
    private EditText    mEditDescription;
    private Double      mLatitude;
    private Double      mLongitude;
    //TODO: language level picker and language picker

    //public constants for pack/unpack meeting parameters:
    public static String meetingTitle          = "meeting_title";
    public static String meetingDescription    = "meeting_description";
    public static String meetingDateDay        = "meeting_date_day";
    public static String meetingDateMonth      = "meeting_date_month";
    public static String meetingTimeHour       = "meeting_time_hour";
    public static String meetingTimeMinute     = "meeting_time_minute";
    public static String meetingLatitude       = "meeting_latitude";
    public static String meetingLongitude      = "meeting_longitude";
    public static String meetingLanguage       = "meeting_language";
    public static String meetingLanguageLvl    = "meeting_language_lvl";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        Intent intent = new Intent();
        mLatitude = intent.getDoubleExtra(meetingLatitude, 0.0);
        mLongitude = intent.getDoubleExtra(meetingLongitude, 0.0);

        //initialize views
        mOKButton = (Button) findViewById(R.id.meeting_edit_ok_button);
        mCancelButton = (Button) findViewById(R.id.meeting_edit_cancel_button);
        mDatePicker = (DatePicker) findViewById(R.id.meeting_date_picker);
        mTimePicker = (TimePicker) findViewById(R.id.meeting_time_picker);
        mTimePicker.setIs24HourView(true);
        mEditTitle = (EditText) findViewById(R.id.meeting_title_edit);
        mEditDescription = (EditText) findViewById(R.id.meeting_description_edit);

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = packMeeting(intent, mEditTitle.getText().toString(),
                        mEditDescription.getText().toString(), null, null,
                        mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(),
                        mDatePicker.getDayOfMonth(), mDatePicker.getMonth());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    private Intent packMeeting (Intent intent, String title, String description,
                                String language, Integer level, Integer timeHour,
                                Integer timeMinute, Integer dateDay, Integer dateMonth) {

        intent.putExtra(meetingTitle,       title);
        intent.putExtra(meetingDescription, description);
        intent.putExtra(meetingDateDay,     dateDay);
        intent.putExtra(meetingDateMonth,   dateMonth);
        intent.putExtra(meetingTimeHour,    timeHour);
        intent.putExtra(meetingTimeMinute,  timeMinute);
        intent.putExtra(meetingLanguage,    language);
        intent.putExtra(meetingLatitude,    mLatitude);
        intent.putExtra(meetingLongitude,   mLongitude);
        //intent.putExtra(meetingLanguageLvl)
        return intent;
    }
}
