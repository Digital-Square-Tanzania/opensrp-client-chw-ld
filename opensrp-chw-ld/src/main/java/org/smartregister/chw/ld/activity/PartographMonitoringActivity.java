package org.smartregister.chw.ld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.ld.R;

import java.util.Locale;

public class PartographMonitoringActivity extends AppCompatActivity {
    private MemberObject memberObject;
    private String baseEntityId;

    public static void startPartographMonitoringActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, PartographMonitoringActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partograph_monitoring);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        this.memberObject = LDDao.getMember(baseEntityId);
        setUpViews();
    }

    public void setUpViews() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        getSupportActionBar().setTitle(String.format(Locale.getDefault(), "%s %s %s, %d",
                memberObject.getFirstName(),
                memberObject.getMiddleName(),
                memberObject.getLastName(),
                age));
    }
}