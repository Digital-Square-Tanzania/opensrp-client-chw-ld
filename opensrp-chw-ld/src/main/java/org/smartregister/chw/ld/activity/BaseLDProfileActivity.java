package org.smartregister.chw.ld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ld.contract.LDProfileContract;
import org.smartregister.chw.ld.custom_views.BaseLDFloatingMenu;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.interactor.BaseLDProfileInteractor;
import org.smartregister.chw.ld.presenter.BaseLDProfilePresenter;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.chw.ld.util.LDUtil;
import org.smartregister.domain.AlertStatus;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.ld.R;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class BaseLDProfileActivity extends BaseProfileActivity implements LDProfileContract.View, LDProfileContract.InteractorCallBack {
    protected MemberObject memberObject;
    protected LDProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName, textViewGender, textViewLocation, textViewUniqueID, textViewRecordLD, textViewRecordAnc, textview_positive_date;
    protected View view_last_visit_row, view_most_due_overdue_row, view_family_row, view_positive_date_row;
    protected RelativeLayout rlLastVisit, rlUpcomingServices, rlFamilyServicesDue, visitStatus, rlLabourProgress;
    protected ImageView imageViewCross;
    protected TextView textViewUndo, forecastSVDTime, vaginalExamDate;
    protected RelativeLayout rlLDPositiveDate, forecastSVDTimeLayout;
    private TextView tvUpComingServices, tvFamilyStatus;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    protected TextView textViewVisitDone;
    protected RelativeLayout visitDone;
    protected LinearLayout recordVisits;
    protected TextView textViewVisitDoneEdit,textViewRecordAncNotDone, textViewLabourProgressTitle, textViewLabourProgressSubTitle;


    private ProgressBar progressBar;
    protected BaseLDFloatingMenu baseLDFloatingMenu;

    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseLDProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_ld_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        String baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseLDProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_last_visit_row = findViewById(R.id.view_last_visit_row);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        view_family_row = findViewById(R.id.view_family_row);
        view_positive_date_row = findViewById(R.id.view_positive_date_row);
        imageViewCross = findViewById(R.id.tick_image);
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);
        textview_positive_date = findViewById(R.id.textview_positive_date);
        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);
        rlLDPositiveDate = findViewById(R.id.rlLDPositiveDate);
        textViewVisitDone = findViewById(R.id.textview_visit_done);
        visitStatus = findViewById(R.id.record_visit_not_done_bar);
        visitDone = findViewById(R.id.visit_done_bar);
        recordVisits = findViewById(R.id.record_visits);
        progressBar = findViewById(R.id.progress_bar);
        textViewRecordAncNotDone = findViewById(R.id.textview_record_anc_not_done);
        textViewVisitDoneEdit = findViewById(R.id.textview_edit);
        textViewRecordLD = findViewById(R.id.textview_record_ld);
        textViewRecordAnc = findViewById(R.id.textview_record_anc);
        textViewUndo = findViewById(R.id.textview_undo);
        imageView = findViewById(R.id.imageview_profile);

        textViewLabourProgressTitle = findViewById(R.id.textview_labour_progress);
        textViewLabourProgressSubTitle = findViewById(R.id.tv_view_labour_progress);
        rlLabourProgress = findViewById(R.id.rlLabourProgress);

        forecastSVDTime = findViewById(R.id.forecast_svd_time_value);
        vaginalExamDate = findViewById(R.id.vaginal_exam_date_value);
        forecastSVDTimeLayout = findViewById(R.id.forecast_svd_time_layout);
        forecastSVDTimeLayout.setVisibility(View.GONE);

        textViewRecordAncNotDone.setOnClickListener(this);
        textViewVisitDoneEdit.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        rlUpcomingServices.setOnClickListener(this);
        rlFamilyServicesDue.setOnClickListener(this);
        rlLDPositiveDate.setOnClickListener(this);
        textViewRecordLD.setOnClickListener(this);
        textViewRecordAnc.setOnClickListener(this);
        textViewUndo.setOnClickListener(this);
        rlLabourProgress.setOnClickListener(this);

        imageRenderHelper = new ImageRenderHelper(this);
        memberObject = LDDao.getMember(baseEntityId);
        initializePresenter();
        profilePresenter.fillProfileData(memberObject);
        setupViews();
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
        showLabourProgress(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSVDForecaseTime();
    }

    private void checkSVDForecaseTime(){
        if(LDDao.getForecastSVDTime(memberObject.getBaseEntityId()) != null){
            forecastSVDTimeLayout.setVisibility(View.VISIBLE);
            vaginalExamDate.setText("Date : "+LDDao.getVaginalExaminationDate(memberObject.getBaseEntityId()));
            forecastSVDTime.setText("Time : "+LDDao.getForecastSVDTime(memberObject.getBaseEntityId()));

            DateFormat completeDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String svdTime = LDDao.getForecastSVDTime(memberObject.getBaseEntityId());
            String vaginalExaminationDate = LDDao.getVaginalExaminationDate(memberObject.getBaseEntityId());

            String completeSVDDate = vaginalExaminationDate + " " + svdTime;
            try {
                Date svdDate = completeDateFormat.parse(completeSVDDate);
                Date currentDate = new Date();
                if(svdDate.before(currentDate)){
                    forecastSVDTime.setTextColor(getResources().getColor(R.color.alert_urgent_red));
                    vaginalExamDate.setTextColor(getResources().getColor(R.color.alert_urgent_red));
                }else {
                    forecastSVDTime.setTextColor(getResources().getColor(R.color.text_black));
                    vaginalExamDate.setTextColor(getResources().getColor(R.color.text_black));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //TODO: Check vaginal examination date, if date has changed manage the alert accordingly

        }else{
            forecastSVDTimeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (id == R.id.rlUpcomingServices) {
            this.openUpcomingService();
        } else if (id == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        } else if (id == R.id.rlLabourProgress) {
            this.openLabourProgress();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseLDProfilePresenter(this, new BaseLDProfileInteractor(), memberObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(memberObject.getPhoneNumber())) {
            baseLDFloatingMenu = new BaseLDFloatingMenu(this, memberObject);
            baseLDFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseLDFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    public void hideView() {
        textViewRecordLD.setVisibility(View.GONE);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format("%s %s %s, %d", memberObject.getFirstName(),
                memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewGender.setText(LDUtil.getGenderTranslated(this, memberObject.getGender()));
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());

        if (StringUtils.isNotBlank(memberObject.getFamilyHead()) && memberObject.getFamilyHead().equals(memberObject.getBaseEntityId())) {
            findViewById(R.id.family_ld_head).setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNotBlank(memberObject.getPrimaryCareGiver()) && memberObject.getPrimaryCareGiver().equals(memberObject.getBaseEntityId())) {
            findViewById(R.id.primary_ld_caregiver).setVisibility(View.VISIBLE);
        }
        if (memberObject.getLDTestDate() != null) {
            textview_positive_date.setText(getString(R.string.ld_positive) + " " + formatTime(memberObject.getLDTestDate()));
        }
    }

    @Override
    public void setOverDueColor() {
        textViewRecordLD.setBackground(getResources().getDrawable(R.drawable.record_btn_selector_overdue));
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLabourProgress(boolean showLabourProgress) {
        rlLabourProgress.setVisibility(showLabourProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {
        showProgressBar(false);
        if (status == AlertStatus.complete)
            return;
        view_most_due_overdue_row.setVisibility(View.VISIBLE);
        rlUpcomingServices.setVisibility(View.VISIBLE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(LDUtil.fromHtml(getString(R.string.vaccine_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(LDUtil.fromHtml(getString(R.string.vaccine_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {
        showProgressBar(false);
        if (status == AlertStatus.complete) {
            setFamilyStatus(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            setFamilyStatus(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(LDUtil.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    private void setFamilyStatus(String familyStatus) {
        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);
        tvFamilyStatus.setText(familyStatus);
    }

    @Override
    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void openUpcomingService() {
        //implement
    }

    @Override
    public void openFamilyDueServices() {
        //implement
    }

    @Override
    public void openLabourProgress() {
        PartographMonitoringActivity.startPartographMonitoringActivity(this, memberObject.getBaseEntityId());
    }

    @Nullable
    private String formatTime(Date dateTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(dateTime);
        } catch (Exception e) {
            Timber.d(e);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
            finish();
        }
    }
}
