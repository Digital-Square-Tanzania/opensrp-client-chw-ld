package org.smartregister.chw.ld.activity;

import org.json.JSONObject;
import org.smartregister.chw.ld.fragment.BaseLabourProgressFragment;
import org.smartregister.ld.R;
import org.smartregister.view.activity.SecuredActivity;

import androidx.fragment.app.FragmentTransaction;

public class BaseLabourProgressViewActivity extends SecuredActivity {


    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_base_labour_progress_view);
        loadFragment();
    }

    @Override
    protected void onResumption() {
        //overridden
    }

    public BaseLabourProgressFragment getBaseFragment() {
        return new BaseLabourProgressFragment();
    }

    private void loadFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_placeholder, getBaseFragment());
        ft.commit();
    }

    public void startFormActivity(JSONObject jsonObject) {
        //implement
    }
}