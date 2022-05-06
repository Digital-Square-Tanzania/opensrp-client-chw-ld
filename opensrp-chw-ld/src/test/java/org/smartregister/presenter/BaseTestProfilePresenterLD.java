package org.smartregister.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.ld.contract.LDProfileContract;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.presenter.BaseLDProfilePresenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseTestProfilePresenterLD {

    @Mock
    private final LDProfileContract.View view = Mockito.mock(LDProfileContract.View.class);

    @Mock
    private final LDProfileContract.Interactor interactor = Mockito.mock(LDProfileContract.Interactor.class);

    @Mock
    private final MemberObject memberObject = new MemberObject();

    private final BaseLDProfilePresenter profilePresenter = new BaseLDProfilePresenter(view, interactor, memberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(memberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void ldTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordLDButton("");
        verify(view).hideView();
    }

    @Test
    public void ldTestDatePeriodGreaterThanTen() {
        profilePresenter.recordLDButton("OVERDUE");
        verify(view).setOverDueColor();
    }

    @Test
    public void ldTestDatePeriodIsMoreThanFourteen() {
        profilePresenter.recordLDButton("EXPIRED");
        verify(view).hideView();
    }

    @Test
    public void refreshProfileBottom() {
        profilePresenter.refreshProfileBottom();
        verify(interactor).refreshProfileInfo(memberObject, profilePresenter.getView());
    }

    @Test
    public void saveForm() {
        profilePresenter.saveForm(null);
        verify(interactor).saveRegistration(null, view);
    }
}
