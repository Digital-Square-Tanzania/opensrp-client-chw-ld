package org.smartregister.chw.ld.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ld.contract.LDRegisterContract;
import org.smartregister.chw.ld.util.AppExecutors;
import org.smartregister.chw.ld.util.LDUtil;

public class BaseLDRegisterInteractor implements LDRegisterContract.Interactor {

    private final AppExecutors appExecutors;

    @VisibleForTesting
    BaseLDRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseLDRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final LDRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                LDUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
