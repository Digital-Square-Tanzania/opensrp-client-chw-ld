package org.smartregister.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.ld.activity.BaseLDProfileActivity;
import org.smartregister.chw.ld.contract.LDProfileContract;
import org.smartregister.domain.AlertStatus;
import org.smartregister.ld.R;

import static org.mockito.Mockito.validateMockitoUsage;

public class BaseTestProfileActivityLD {
    @Mock
    public BaseLDProfileActivity baseLDProfileActivity;

    @Mock
    public LDProfileContract.Presenter profilePresenter;

    @Mock
    public View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseLDProfileActivity);
    }

    @Test
    public void setOverDueColor() {
        baseLDProfileActivity.setOverDueColor();
        Mockito.verify(view, Mockito.never()).setBackgroundColor(Color.RED);
    }

    @Test
    public void formatTime() {
        BaseLDProfileActivity activity = new BaseLDProfileActivity();
        try {
            Assert.assertEquals("25 Oct 2019", Whitebox.invokeMethod(activity, "formatTime", "25-10-2019"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkHideView() {
        baseLDProfileActivity.hideView();
        Mockito.verify(view, Mockito.never()).setVisibility(View.GONE);
    }

    @Test
    public void checkProgressBar() {
        baseLDProfileActivity.showProgressBar(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void medicalHistoryRefresh() {
        baseLDProfileActivity.refreshMedicalHistory(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void onClickBackPressed() {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.title_layout);
        Mockito.doNothing().when(baseLDProfileActivity).onBackPressed();
        baseLDProfileActivity.onClick(view);
        Mockito.verify(baseLDProfileActivity).onBackPressed();
    }

    @Test
    public void onClickOpenMedicalHistory() {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlLastVisit);
        Mockito.doNothing().when(baseLDProfileActivity).openMedicalHistory();
        baseLDProfileActivity.onClick(view);
        Mockito.verify(baseLDProfileActivity).openMedicalHistory();
    }

    @Test
    public void onClickOpenUpcomingServices() {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlUpcomingServices);
        Mockito.doNothing().when(baseLDProfileActivity).openUpcomingService();
        baseLDProfileActivity.onClick(view);
        Mockito.verify(baseLDProfileActivity).openUpcomingService();
    }

    @Test
    public void onClickOpenFamlilyServicesDue() {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlFamilyServicesDue);
        Mockito.doNothing().when(baseLDProfileActivity).openFamilyDueServices();
        baseLDProfileActivity.onClick(view);
        Mockito.verify(baseLDProfileActivity).openFamilyDueServices();
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusComplete() throws Exception {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(baseLDProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(baseLDProfileActivity).showProgressBar(false);
        baseLDProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(baseLDProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(baseLDProfileActivity).invoke("setFamilyStatus", "Family has nothing due");
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusNormal() throws Exception {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(baseLDProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(baseLDProfileActivity).showProgressBar(false);
        baseLDProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(baseLDProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(baseLDProfileActivity).invoke("setFamilyStatus", "Family has services due");
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        baseLDProfileActivity = Mockito.spy(new BaseLDProfileActivity());
        Whitebox.invokeMethod(baseLDProfileActivity, "onActivityResult", 2244, -1, null);
        Mockito.verify(profilePresenter).saveForm(null);
    }

}
