package org.smartregister.chw.ld.contract;

import android.content.Context;

public interface BaseLDCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
