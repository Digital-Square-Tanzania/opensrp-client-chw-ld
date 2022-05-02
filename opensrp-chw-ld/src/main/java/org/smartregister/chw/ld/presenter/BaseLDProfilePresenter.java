package org.smartregister.chw.ld.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import org.smartregister.chw.ld.contract.LDProfileContract;
import org.smartregister.chw.ld.domain.MemberObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BaseLDProfilePresenter implements LDProfileContract.Presenter {
    protected WeakReference<LDProfileContract.View> view;
    protected MemberObject memberObject;
    protected LDProfileContract.Interactor interactor;
    protected Context context;

    public BaseLDProfilePresenter(LDProfileContract.View view, LDProfileContract.Interactor interactor, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.memberObject = memberObject;
        this.interactor = interactor;
    }

    @Override
    public void fillProfileData(MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void recordLDButton(@Nullable String visitState) {
        if (getView() == null) {
            return;
        }

        if (("OVERDUE").equals(visitState) || ("DUE").equals(visitState)) {
            if (("OVERDUE").equals(visitState)) {
                getView().setOverDueColor();
            }
        } else {
            getView().hideView();
        }
    }

    @Override
    @Nullable
    public LDProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo(memberObject, getView());
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, getView());
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
