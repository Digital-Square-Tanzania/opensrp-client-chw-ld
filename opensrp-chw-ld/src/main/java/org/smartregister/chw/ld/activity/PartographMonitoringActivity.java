package org.smartregister.chw.ld.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ld.dao.LDDao;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.domain.PartographChartObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.ld.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PartographMonitoringActivity extends AppCompatActivity {
    private MemberObject memberObject;
    private String baseEntityId;
    private LineChart cervixDescentChart;
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
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setAxisMaximum(startTimePartographTime + TimeUnit.HOURS.toMillis(25));
        xAxis.setAxisMinimum(startTimePartographTime);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelCount(12, true);
        xAxis.setValueFormatter((value, axis) -> {
            long diff = (long) value - (startTimePartographTime);
            return TimeUnit.MILLISECONDS.toHours(diff) + "";
        });

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
        cervixDescentChart.getLegend().setEnabled(false);

        Legend l = cervixDescentChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
    }

    private LineDataSet setAlert() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(startTimePartographTime, 3));
        values.add(new Entry(TimeUnit.HOURS.toMillis(1) + startTimePartographTime, 4));
        values.add(new Entry(TimeUnit.HOURS.toMillis(2) + startTimePartographTime, 5));
        values.add(new Entry(TimeUnit.HOURS.toMillis(3) + startTimePartographTime, 6));
        values.add(new Entry(TimeUnit.HOURS.toMillis(4) + startTimePartographTime, 7));
        values.add(new Entry(TimeUnit.HOURS.toMillis(5) + startTimePartographTime, 8));
        values.add(new Entry(TimeUnit.HOURS.toMillis(6) + startTimePartographTime, 9));
        values.add(new Entry(TimeUnit.HOURS.toMillis(7) + startTimePartographTime, 10));

        return generateLineDataSet(values, "ALERT", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setAction() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(TimeUnit.HOURS.toMillis(4) + startTimePartographTime, 3));
        values.add(new Entry(TimeUnit.HOURS.toMillis(5) + startTimePartographTime, 4));
        values.add(new Entry(TimeUnit.HOURS.toMillis(6) + startTimePartographTime, 5));
        values.add(new Entry(TimeUnit.HOURS.toMillis(7) + startTimePartographTime, 6));
        values.add(new Entry(TimeUnit.HOURS.toMillis(8) + startTimePartographTime, 7));
        values.add(new Entry(TimeUnit.HOURS.toMillis(9) + startTimePartographTime, 8));
        values.add(new Entry(TimeUnit.HOURS.toMillis(10) + startTimePartographTime, 9));
        values.add(new Entry(TimeUnit.HOURS.toMillis(11) + startTimePartographTime, 10));

        return generateLineDataSet(values, "ACTION", true, 4f, Color.RED, 10f, 1f, false, false);
    }

    private LineDataSet setDilation() {
        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> cervixDilationList = LDDao.getCervixDilationList(baseEntityId);
        if (cervixDilationList != null && !cervixDilationList.isEmpty()) {
            for (PartographChartObject cervixDilation : cervixDilationList) {
                values.add(new Entry(cervixDilation.getDateTime(), cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));

            }
        }

        return generateLineDataSet(values, "Cervix Dilation", false, 2f, Color.BLACK, 10f, 3f, false, true);
    }

    private LineDataSet setDescent() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> descentList = LDDao.getDescentList(baseEntityId);
        if (descentList != null && !descentList.isEmpty()) {
            for (PartographChartObject cervixDilation : descentList) {
                values.add(new Entry(cervixDilation.getDateTime(), cervixDilation.getValue()));
            }
        }

        return generateLineDataSet(values, "Head Descent", false, 2f, Color.BLACK, 10f, 4f, true, false);
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