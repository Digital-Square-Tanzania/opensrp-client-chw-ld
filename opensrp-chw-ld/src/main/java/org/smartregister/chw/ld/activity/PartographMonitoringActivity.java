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
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.domain.PartographChartObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.ld.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PartographMonitoringActivity extends AppCompatActivity {
    private MemberObject memberObject;
    private String baseEntityId;
    private LineChart cervixDescentChart;
    private LineChart fetalHeartRateChart;
    private long startTimePartographTime;
    private ArrayList<Float> timeLabelsForDilationAndDescentGraphsXValues = new ArrayList<>();

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

        LineDataSet dilationLineDataSet = setDilation();
        LineDataSet descentLineDataSet = setDescent();
        LineData data = new LineData(descentLineDataSet, dilationLineDataSet, setLatentPhase(), setAlert(), setAction(), setTimeLabelsForDilationAndDescentGraphs());

        // set data
        cervixDescentChart.setData(data);

        cervixDescentChart.animateX(1000);
        cervixDescentChart.getLegend().setEnabled(false);

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

    private LineDataSet setLatentPhase() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(0, 3));
        values.add(new Entry(8, 3));

        return generateLineDataSet(values, "Latent Phase", true, 4f, Color.BLACK, 10f, 1f, false, false);
    }

    private LineDataSet setAlert() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(8, 3));
        values.add(new Entry(9, 4));
        values.add(new Entry(10, 5));
        values.add(new Entry(11, 6));
        values.add(new Entry(12, 7));
        values.add(new Entry(13, 8));
        values.add(new Entry(14, 9));
        values.add(new Entry(15, 10));

        return generateLineDataSet(values, "ALERT", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setAction() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(12, 3));
        values.add(new Entry(13, 4));
        values.add(new Entry(14, 5));
        values.add(new Entry(15, 6));
        values.add(new Entry(16, 7));
        values.add(new Entry(17, 8));
        values.add(new Entry(18, 9));
        values.add(new Entry(19, 10));

        return generateLineDataSet(values, "ACTION", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setDilation() {
        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> cervixDilationList = LDDao.getCervixDilationList(baseEntityId);
        if (cervixDilationList != null && !cervixDilationList.isEmpty()) {
            for (PartographChartObject cervixDilation : cervixDilationList) {
                //Adding 28800000 milliseconds to offset the graph to 8 hours as per tanzania guidelines
                //this should be refactored to make it more configurable
                float x = (cervixDilation.getDateTime() - startTimePartographTime + 28800000) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));

                //Setting values for displaying time labels at the bottom of the partograph
                timeLabelsForDilationAndDescentGraphsXValues.add(x);
            }
        }

        return generateLineDataSet(values, "Cervix Dilation", false, 2f, Color.BLACK, 10f, 3f, false, true);
    }

    private LineDataSet setDescent() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> descentList = LDDao.getDescentList(baseEntityId);
        if (descentList != null && !descentList.isEmpty()) {
            for (PartographChartObject cervixDilation : descentList) {
                //Adding 28800000 milliseconds to offset the graph to 8 hours as per tanzania guidelines
                //this should be refactored to make it more configurable
                float x = (cervixDilation.getDateTime() - startTimePartographTime + 28800000) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));

                //Setting values for displaying time labels at the bottom of the partograph
                if (!timeLabelsForDilationAndDescentGraphsXValues.contains(x)) {
                    timeLabelsForDilationAndDescentGraphsXValues.add(x);
                }
            }
        }

        return generateLineDataSet(values, "Head Descent", false, 2f, Color.BLACK, 10f, 4f, true, false);
    }

    private LineDataSet setFetalHeartRate() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> fetalHeartRateList = LDDao.getFetalHeartRateList(baseEntityId);
        if (fetalHeartRateList != null && !fetalHeartRateList.isEmpty()) {
            for (PartographChartObject fetalHeartRate : fetalHeartRateList) {
                //Adding 28800000 milliseconds to offset the graph to 8 hours as per tanzania guidelines
                //this should be refactored to make it more configurable
                float x = (fetalHeartRate.getDateTime() - startTimePartographTime + 28800000) * 1f / 3600000;
                values.add(new Entry(x, fetalHeartRate.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));
            }
        }

        return generateLineDataSet(values, "Fetal Heart Rate", false, 2f, Color.BLUE, 10f, 4f, false, false);
    }

    private LineDataSet setTimeLabelsForDilationAndDescentGraphs() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < timeLabelsForDilationAndDescentGraphsXValues.size(); i++) {
            values.add(new Entry(timeLabelsForDilationAndDescentGraphsXValues.get(i), 0));
        }

        LineDataSet timeLabels = generateLineDataSet(values, "", false, 0f, Color.TRANSPARENT, 10f, 4f, false, false);

        // text size of labels
        timeLabels.setValueTextSize(9f);

        timeLabels.setValueFormatter(new IValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                long timeInMilliseconds = (long) (entry.getX() * 3600000 + startTimePartographTime - 28800000);

                return mFormat.format(new Date(timeInMilliseconds));
            }
        });
        return timeLabels;
    }

    public LineDataSet generateLineDataSet(ArrayList<Entry> values, String label, boolean showLabelOnTopOfLine,
                                           float lineWidth, int lineColor, float labelTextSize, float circleRadius, boolean drawCircleHole, boolean setDrawIcon) {
        LineDataSet alarmDataSet = new LineDataSet(values, label);

        alarmDataSet.setShowLabelOnTopOfLine(showLabelOnTopOfLine);

        alarmDataSet.setTextSize(labelTextSize);
        alarmDataSet.setDrawIcons(setDrawIcon);

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