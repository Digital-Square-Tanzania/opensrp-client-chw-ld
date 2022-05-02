package org.smartregister.chw.ld.model;

import org.json.JSONObject;
import org.smartregister.chw.ld.contract.LDRegisterContract;
import org.smartregister.chw.ld.util.LDJsonFormUtils;

public class BaseLDRegisterModel implements LDRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = LDJsonFormUtils.getFormAsJson(formName);
        LDJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
