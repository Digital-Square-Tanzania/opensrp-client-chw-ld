package org.smartregister.activity;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.ld.activity.BaseLDRegisterActivity;

public class BaseTestRegisterActivityLD {
    @Mock
    public Intent data;
    @Mock
    private BaseLDRegisterActivity baseLDRegisterActivity = new BaseLDRegisterActivity();

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseLDRegisterActivity);
    }

    @Test
    public void testFormConfig() {
        Assert.assertNull(baseLDRegisterActivity.getFormConfig());
    }

    @Test
    public void checkIdentifier() {
        Assert.assertNotNull(baseLDRegisterActivity.getViewIdentifiers());
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        Whitebox.invokeMethod(baseLDRegisterActivity, "onActivityResult", 2244, -1, data);
        Mockito.verify(baseLDRegisterActivity.presenter()).saveForm(null);
    }

}
