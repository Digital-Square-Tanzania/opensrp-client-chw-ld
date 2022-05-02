package org.smartregister.chw.ld.listener;


import android.view.View;

import org.smartregister.chw.ld.fragment.BaseLDCallDialogFragment;
import org.smartregister.chw.ld.util.LDUtil;
import org.smartregister.ld.R;

import timber.log.Timber;

public class BaseLDCallWidgetDialogListener implements View.OnClickListener {

    private BaseLDCallDialogFragment callDialogFragment;

    public BaseLDCallWidgetDialogListener(BaseLDCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ld_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.ld_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                LDUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_ld_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                LDUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
