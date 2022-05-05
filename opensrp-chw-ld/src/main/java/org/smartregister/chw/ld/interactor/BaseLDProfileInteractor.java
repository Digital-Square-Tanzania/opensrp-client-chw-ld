package org.smartregister.chw.ld.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ld.contract.LDProfileContract;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.util.AppExecutors;
import org.smartregister.chw.ld.util.LDUtil;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BaseLDProfileInteractor implements LDProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseLDProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseLDProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, LDProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("LD Visit", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final LDProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                LDUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
