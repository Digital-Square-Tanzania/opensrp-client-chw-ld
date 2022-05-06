package org.smartregister.chw.ld.custom_views;

import android.app.Activity;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;

import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.fragment.BaseLDCallDialogFragment;
import org.smartregister.ld.R;

public class BaseLDFloatingMenu extends LinearLayout implements View.OnClickListener {
    private final MemberObject MEMBER_OBJECT;

    public BaseLDFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_ld_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.ld_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ld_fab) {
            Activity activity = (Activity) getContext();
            BaseLDCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }  else if (view.getId() == R.id.refer_to_facility_layout) {
            Activity activity = (Activity) getContext();
            BaseLDCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}