package org.smartregister.chw.ld.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import org.smartregister.chw.ld.domain.PartographChartBloodPressureObject;
import org.smartregister.chw.ld.domain.PartographChartObject;
import org.smartregister.chw.ld.domain.PartographContractionObject;
import org.smartregister.chw.ld.domain.PartographDataObject;
import org.smartregister.chw.ld.domain.PartographOxytocinObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.ld.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class PartographMonitoringActivity extends AppCompatActivity {
    private MemberObject memberObject;
    private String baseEntityId;
    private LineChart cervixDescentChart;
    private LineChart fetalHeartRateChart;
    private LineChart pulseRateChart;
    private long startTimePartographTime;
    private ArrayList<Float> timeLabelsForDilationAndDescentGraphsXValues = new ArrayList<>();
    private long partographOffset;

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
        int startingCervixDilation;
        try {
            startingCervixDilation = LDDao.getPartographCervixDilationList(baseEntityId).get(0).getValue();
        } catch (Exception e) {
            Timber.e(e);
            startingCervixDilation = 3;
        }

        //TimeUnit.HOURS.toMillis(8 + startingCervixDilation - 3) is used to offset the starting of the partograph based on the starting cervix dilation
        //this should be refactored to make it more configurable
        partographOffset = TimeUnit.HOURS.toMillis(8 + startingCervixDilation - 3);
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
        setPulseRateLineChart();
        setTemperature();
        setUrineProtein();
        setUrineAcetone();
        setUrineVolume();
        setAmnioticFluid();
        setMoulding();
        setCaput();
        setContractions();
        setOxytocin();
        setDrugsAndIVFluids();
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

    private void setPulseRateLineChart() {
        pulseRateChart = findViewById(R.id.pulse_and_bp);
        pulseRateChart.setBackgroundColor(Color.WHITE);
        pulseRateChart.getDescription().setEnabled(false);
        pulseRateChart.setTouchEnabled(true);
        pulseRateChart.setDrawGridBackground(false);

        XAxis xAxis = pulseRateChart.getXAxis();
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

        YAxis yAxis = pulseRateChart.getAxisLeft();
        pulseRateChart.getAxisRight().setEnabled(false);
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(13);

        // axis range
        yAxis.setAxisMaximum(180f);
        yAxis.setAxisMinimum(60f);

        // create a data object with the data sets
        LineData data = new LineData(setPulseRate());

        for (LineDataSet lineDataSet : setBloodPressure()) {
            data.addDataSet(lineDataSet);
        }

        // set data
        pulseRateChart.setData(data);


        pulseRateChart.animateX(1000);
        pulseRateChart.getLegend().setEnabled(false);

        Legend l = pulseRateChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setStackSpace(20f);

        pulseRateChart.setExtraOffsets(5f, 5f, 5f, 15f);
    }

    private LineDataSet setLatentPhase() {

        ArrayList<Entry> values = new ArrayList<>();

        values.add(new Entry(0, 3));
        values.add(new Entry(8, 3));

        return generateLineDataSet(values, "Latent Phase", true, 4f, Color.BLACK, 10f, 1f, false, false, false);
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

        return generateLineDataSet(values, "ALERT", true, 4f, Color.RED, 10f, 1f, false, false, false);
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

        return generateLineDataSet(values, "ACTION", true, 4f, Color.RED, 10f, 1f, false, false, false);
    }

    private LineDataSet setDilation() {
        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> cervixDilationList = LDDao.getPartographCervixDilationList(baseEntityId);
        if (cervixDilationList != null && !cervixDilationList.isEmpty()) {
            for (PartographChartObject cervixDilation : cervixDilationList) {
                float x = (cervixDilation.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close)));

                //Setting values for displaying time labels at the bottom of the partograph
                timeLabelsForDilationAndDescentGraphsXValues.add(x);
            }
        }

        return generateLineDataSet(values, "Cervix Dilation", false, 2f, Color.BLACK, 10f, 6f, true, true, false);
    }

    private LineDataSet setDescent() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> descentList = LDDao.getPartographDescentList(baseEntityId);
        if (descentList != null && !descentList.isEmpty()) {
            for (PartographChartObject cervixDilation : descentList) {
                float x = (cervixDilation.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                values.add(new Entry(x, cervixDilation.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));

                //Setting values for displaying time labels at the bottom of the partograph
                if (!timeLabelsForDilationAndDescentGraphsXValues.contains(x)) {
                    timeLabelsForDilationAndDescentGraphsXValues.add(x);
                }
            }
        }

        return generateLineDataSet(values, "Head Descent", false, 2f, Color.BLACK, 10f, 6f, true, false, false);
    }

    private LineDataSet setFetalHeartRate() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> fetalHeartRateList = LDDao.getPartographFetalHeartRateList(baseEntityId);
        if (fetalHeartRateList != null && !fetalHeartRateList.isEmpty()) {
            for (PartographChartObject fetalHeartRate : fetalHeartRateList) {
                float x = (fetalHeartRate.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                values.add(new Entry(x, fetalHeartRate.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));
            }
        }

        return generateLineDataSet(values, "Fetal Heart Rate", false, 2f, Color.BLUE, 10f, 4f, false, false, false);
    }

    private LineDataSet setPulseRate() {

        ArrayList<Entry> values = new ArrayList<>();

        List<PartographChartObject> pulseList = LDDao.getPartographPulseList(baseEntityId);
        if (pulseList != null && !pulseList.isEmpty()) {
            for (PartographChartObject fetalHeartRate : pulseList) {
                float x = (fetalHeartRate.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                values.add(new Entry(x, fetalHeartRate.getValue(), getResources().getDrawable(R.drawable.ic_close_icon)));
            }
        }

        return generateLineDataSet(values, "Pulse Rate", false, 2f, Color.BLUE, 10f, 4f, false, false, false);
    }

    private ArrayList<LineDataSet> setBloodPressure() {

        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        List<PartographChartBloodPressureObject> bloodPressureObjectList = LDDao.getPartographSystolicDiastolicPressure(baseEntityId);
        if (bloodPressureObjectList != null && !bloodPressureObjectList.isEmpty()) {
            for (PartographChartBloodPressureObject bloodPressure : bloodPressureObjectList) {
                float x = (bloodPressure.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                ArrayList<Entry> values = new ArrayList<>();
                values.add(new Entry(x, bloodPressure.getSystolic(), getResources().getDrawable(R.drawable.ic_horizontal_rule)));
                values.add(new Entry(x, bloodPressure.getDiastolic(), getResources().getDrawable(R.drawable.ic_horizontal_rule)));

                dataSets.add(generateLineDataSet(values, "BP", false, 2f, Color.BLACK, 10f, 1f, false, true, true));
            }
        }

        return dataSets;
    }

    private LineDataSet setTimeLabelsForDilationAndDescentGraphs() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < timeLabelsForDilationAndDescentGraphsXValues.size(); i++) {
            values.add(new Entry(timeLabelsForDilationAndDescentGraphsXValues.get(i), 0));
        }

        LineDataSet timeLabels = generateLineDataSet(values, "", false, 0f, Color.TRANSPARENT, 10f, 4f, false, false, false);

        // text size of labels
        timeLabels.setValueTextSize(9f);

        timeLabels.setValueFormatter(new IValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                long timeInMilliseconds = (long) (entry.getX() * 3600000 + startTimePartographTime - partographOffset);

                return mFormat.format(new Date(timeInMilliseconds));
            }
        });
        return timeLabels;
    }

    public LineDataSet generateLineDataSet(ArrayList<Entry> values, String label, boolean showLabelOnTopOfLine,
                                           float lineWidth, int lineColor, float labelTextSize, float circleRadius, boolean drawCircleHole, boolean setDrawIcon, boolean showDashEffect) {
        LineDataSet lineDataSet = new LineDataSet(values, label);

        lineDataSet.setShowLabelOnTopOfLine(showLabelOnTopOfLine);

        lineDataSet.setTextSize(labelTextSize);
        lineDataSet.setDrawIcons(setDrawIcon);

        // black lines and points
        lineDataSet.setColor(lineColor);
        lineDataSet.setCircleColor(lineColor);

        // line thickness and point size
        lineDataSet.setLineWidth(lineWidth);
        lineDataSet.setCircleRadius(circleRadius);

        // draw points as solid circles
        lineDataSet.setDrawCircleHole(drawCircleHole);
        lineDataSet.setCircleHoleRadius(4f);

        // customize legend entry
        lineDataSet.setFormLineWidth(1f);

        if (showDashEffect)
            lineDataSet.enableDashedLine(10f, 5f, 0f);

        lineDataSet.setFormSize(15.f);

        // text size of values
        lineDataSet.setValueTextSize(0f);

        // set the filled area
        lineDataSet.setDrawFilled(false);

        return lineDataSet;
    }

    private void setTemperature() {
        List<PartographChartObject> temperatureList = LDDao.getPartographTemperatureList(baseEntityId);
        if (temperatureList != null && !temperatureList.isEmpty()) {
            for (PartographChartObject temperature : temperatureList) {
                try {
                    float x = (temperature.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("temp_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(temperature.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setUrineProtein() {
        List<PartographDataObject> urineProteinList = LDDao.getPartographUrineProteinList(baseEntityId);
        if (urineProteinList != null && !urineProteinList.isEmpty()) {
            for (PartographDataObject temperature : urineProteinList) {
                try {
                    float x = (temperature.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("protein_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(temperature.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setUrineAcetone() {
        List<PartographDataObject> temperatureList = LDDao.getPartographUrineAcetoneList(baseEntityId);
        if (temperatureList != null && !temperatureList.isEmpty()) {
            for (PartographDataObject temperature : temperatureList) {
                try {
                    float x = (temperature.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("acetone_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(temperature.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setUrineVolume() {
        List<PartographDataObject> urineVolumeList = LDDao.getPartographUrineVolumeList(baseEntityId);
        if (urineVolumeList != null && !urineVolumeList.isEmpty()) {
            for (PartographDataObject urineVolume : urineVolumeList) {
                try {
                    float x = (urineVolume.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("volume_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(urineVolume.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setAmnioticFluid() {
        List<PartographDataObject> amnioticFluidList = LDDao.getPartographAmnioticFluidList(baseEntityId);
        if (amnioticFluidList != null && !amnioticFluidList.isEmpty()) {
            for (PartographDataObject amnioticFluid : amnioticFluidList) {
                try {
                    float x = (amnioticFluid.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("amniotic_fluid" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);

                    String amnioticFluidValue = "";
                    switch (amnioticFluid.getValue()) {
                        case "membrane_intact":
                            amnioticFluidValue = "I";
                            break;
                        case "membrane_ruptured_liquor_clear":
                            amnioticFluidValue = "C";
                            break;
                        case "meconium_stained_liquor":
                            amnioticFluidValue = "M";
                            break;
                        case "blood_stained_liquor":
                            amnioticFluidValue = "B";
                            break;
                    }

                    tv.setText(amnioticFluidValue);
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setMoulding() {
        List<PartographDataObject> mouldingList = LDDao.getPartographMouldingList(baseEntityId);
        if (mouldingList != null && !mouldingList.isEmpty()) {
            for (PartographDataObject moulding : mouldingList) {
                try {
                    float x = (moulding.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("moulding" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(moulding.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setCaput() {
        List<PartographDataObject> caputList = LDDao.getPartographCaputList(baseEntityId);
        if (caputList != null && !caputList.isEmpty()) {
            for (PartographDataObject caput : caputList) {
                try {
                    float x = (caput.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("caput" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(caput.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setContractions() {
        List<PartographContractionObject> contractionsList = LDDao.getPartographContractionsList(baseEntityId);
        if (contractionsList != null && !contractionsList.isEmpty()) {
            for (PartographContractionObject contraction : contractionsList) {
                try {
                    float x = (contraction.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    for (int i = 1; i <= contraction.getContractionsFrequency(); i++) {
                        String resIdString = "contractions_" + i + "_" + xValue;
                        int resID = getResources().getIdentifier(resIdString, "id", getPackageName());
                        ImageView imageView = findViewById(resID);
                        if (contraction.getContractionsLengthInTime().equals("less_than_20_secs"))
                            imageView.setImageResource(R.drawable.dots);
                        if (contraction.getContractionsLengthInTime().equals("over_20_less_40_secs"))
                            imageView.setImageResource(R.drawable.diagonal_lines);
                        if (contraction.getContractionsLengthInTime().equals("over_40_secs"))
                            imageView.setImageResource(R.color.black);
                    }
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setOxytocin() {
        List<PartographOxytocinObject> oxytocinList = LDDao.getPartographOxytocinList(baseEntityId);
        if (oxytocinList != null && !oxytocinList.isEmpty()) {
            for (PartographOxytocinObject oxytocinObject : oxytocinList) {
                try {
                    float x = (oxytocinObject.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("oxytocin_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(oxytocinObject.getOxytocinUL()));

                    int resDropsID = getResources().getIdentifier("drops_" + xValue, "id", getPackageName());
                    TextView tvDropsPerMinute = findViewById(resDropsID);
                    tvDropsPerMinute.setText(String.valueOf(oxytocinObject.getOxytocinDropsPerMinute()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    private void setDrugsAndIVFluids() {
        List<PartographDataObject> drugsList = LDDao.getPartographDrugsAndIVFluidsList(baseEntityId);
        if (drugsList != null && !drugsList.isEmpty()) {
            for (PartographDataObject drugObject : drugsList) {
                try {
                    float x = (drugObject.getDateTime() - startTimePartographTime + partographOffset) * 1f / 3600000;
                    int xValue = Math.round(x);

                    int resID = getResources().getIdentifier("drugs_given_" + xValue, "id", getPackageName());
                    TextView tv = findViewById(resID);
                    tv.setText(String.valueOf(drugObject.getValue()));
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}