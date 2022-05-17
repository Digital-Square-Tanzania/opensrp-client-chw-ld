package org.smartregister.chw.ld.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.domain.PartographChartObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.ld.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PartographMonitoringActivity extends AppCompatActivity {
    private MemberObject memberObject;
    private String baseEntityId;
    private LineChart cervixDescentChart;
    private LineChart fetalHeartRateChart;
    private long startTimePartographTime;

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
        startTimePartographTime = LDDao.getPartographStartTime(baseEntityId);
        setUpViews();
    }

    public void setUpViews() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        getSupportActionBar().setTitle(String.format(Locale.getDefault(), "%s %s %s, %d",
                memberObject.getFirstName(),
                memberObject.getMiddleName(),
                memberObject.getLastName(),
                age));
        setUpCervixDescentLineChart();
        setUpFetalHeartRateLineChart();
    }

    private void setUpCervixDescentLineChart() {
        cervixDescentChart = findViewById(R.id.cervix_descent_chart);
        cervixDescentChart.setBackgroundColor(Color.WHITE);
        cervixDescentChart.getDescription().setEnabled(false);
        cervixDescentChart.setTouchEnabled(true);
        cervixDescentChart.setDrawGridBackground(false);
        // force pinch zoom along both axis
        cervixDescentChart.setPinchZoom(true);

        XAxis xAxis = cervixDescentChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMaximum(24);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelCount(25, true);

        YAxis yAxis = cervixDescentChart.getAxisLeft();
        cervixDescentChart.getAxisRight().setEnabled(false);
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(10);

        // axis range
        yAxis.setAxisMaximum(10f);
        yAxis.setAxisMinimum(0f);

        // create a data object with the data sets
        LineData data = new LineData(setDescent(), setDilation(), setAlert(), setAction());

        // set data
        cervixDescentChart.setData(data);

        cervixDescentChart.animateX(1000);
        cervixDescentChart.getLegend().setEnabled(true);

        Legend l = cervixDescentChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setStackSpace(20f);

        cervixDescentChart.setExtraOffsets(5f, 5f, 5f, 15f);
    }

    private void setUpFetalHeartRateLineChart() {
        fetalHeartRateChart = findViewById(R.id.fetal_heart_rate_chart);
        fetalHeartRateChart.setBackgroundColor(Color.WHITE);
        fetalHeartRateChart.getDescription().setEnabled(false);
        fetalHeartRateChart.setTouchEnabled(true);
        fetalHeartRateChart.setDrawGridBackground(false);

        XAxis xAxis = fetalHeartRateChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMaximum(24);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelCount(49, true);
        xAxis.setDrawLabels(false);

        YAxis yAxis = fetalHeartRateChart.getAxisLeft();
        fetalHeartRateChart.getAxisRight().setEnabled(false);
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(13);

        // axis range
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisMinimum(60f);

        // Create Limit Lines//
        LimitLine ll1 = new LimitLine(160f, "");
        ll1.setLineWidth(4f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setLineColor(Color.GRAY);

        LimitLine ll2 = new LimitLine(120f, "");
        ll2.setLineWidth(4f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setLineColor(Color.GRAY);

        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true);

        // add limit lines
        yAxis.addLimitLine(ll1);
        yAxis.addLimitLine(ll2);


        // create a data object with the data sets
        LineData data = new LineData(setFetalHeartRate());

        // set data
        fetalHeartRateChart.setData(data);

        fetalHeartRateChart.animateX(1000);
        fetalHeartRateChart.getLegend().setEnabled(true);

        Legend l = fetalHeartRateChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setStackSpace(20f);

        fetalHeartRateChart.setExtraOffsets(5f, 5f, 5f, 15f);
    }

    private LineDataSet setAlert() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(0, 3));
        values.add(new Entry(1, 4));
        values.add(new Entry(2, 5));
        values.add(new Entry(3, 6));
        values.add(new Entry(4, 7));
        values.add(new Entry(5, 8));
        values.add(new Entry(6, 9));
        values.add(new Entry(7, 10));

        return generateLineDataSet(values, "ALERT", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setAction() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(4, 3));
        values.add(new Entry(5, 4));
        values.add(new Entry(6, 5));
        values.add(new Entry(7, 6));
        values.add(new Entry(8, 7));
        values.add(new Entry(9, 8));
        values.add(new Entry(10, 9));
        values.add(new Entry(11, 10));

        return generateLineDataSet(values, "ACTION", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setDilation() {
        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> cervixDilationList = LDDao.getCervixDilationList(baseEntityId);
        if (cervixDilationList != null && !cervixDilationList.isEmpty()) {
            for (PartographChartObject cervixDilation : cervixDilationList) {
                float x = (cervixDilation.getDateTime() - startTimePartographTime) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));

            }
        }

        return generateLineDataSet(values, "Cervix Dilation", false, 2f, Color.BLACK, 10f, 3f, false, true);
    }

    private LineDataSet setDescent() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> descentList = LDDao.getDescentList(baseEntityId);
        if (descentList != null && !descentList.isEmpty()) {
            for (PartographChartObject cervixDilation : descentList) {
                float x = (cervixDilation.getDateTime() - startTimePartographTime) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));
            }
        }

        return generateLineDataSet(values, "Head Descent", false, 2f, Color.BLACK, 10f, 4f, true, false);
    }

    private LineDataSet setFetalHeartRate() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> fetalHeartRateList = LDDao.getFetalHeartRateList(baseEntityId);
        if (fetalHeartRateList != null && !fetalHeartRateList.isEmpty()) {
            for (PartographChartObject fetalHeartRate : fetalHeartRateList) {
                float x = (fetalHeartRate.getDateTime() - startTimePartographTime) * 1f / 3600000;
                values.add(new Entry(x, fetalHeartRate.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));
            }
        }

        return generateLineDataSet(values, "Fetal Heart Rate", false, 2f, Color.BLUE, 10f, 4f, false, false);
    }

    public LineDataSet generateLineDataSet(ArrayList<Entry> values, String label, boolean showLabelOnTopOfLine,
                                           float lineWidth, int lineColor, float labelTextSize, float circleRadius, boolean drawCircleHole, boolean setDrawIcon) {
        LineDataSet alarmDataSet = new LineDataSet(values, label);

        alarmDataSet.setShowLabelOnTopOfLine(showLabelOnTopOfLine);

        alarmDataSet.setTextSize(labelTextSize);
//        alarmDataSet.setTypeface(tfRegular);


        alarmDataSet.setDrawIcons(setDrawIcon);
        // draw dashed line
//            alarmDataSet.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        alarmDataSet.setColor(lineColor);
        alarmDataSet.setCircleColor(lineColor);

        // line thickness and point size
        alarmDataSet.setLineWidth(lineWidth);
        alarmDataSet.setCircleRadius(circleRadius);

        // draw points as solid circles
        alarmDataSet.setDrawCircleHole(drawCircleHole);

        // customize legend entry
        alarmDataSet.setFormLineWidth(1f);
//        alarmDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        alarmDataSet.setFormSize(15.f);

        // text size of values
        alarmDataSet.setValueTextSize(0f);

        // set the filled area
        alarmDataSet.setDrawFilled(false);

        return alarmDataSet;
    }
}