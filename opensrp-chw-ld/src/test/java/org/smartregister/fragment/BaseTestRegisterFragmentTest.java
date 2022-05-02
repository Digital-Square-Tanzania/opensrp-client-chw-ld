package org.smartregister.fragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.ld.activity.BaseLDProfileActivity;
import org.smartregister.chw.ld.fragment.BaseLDRegisterFragment;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.times;

public class BaseTestRegisterFragmentTest {
    @Mock
    public BaseLDRegisterFragment baseLDRegisterFragment;

    @Mock
    public CommonPersonObjectClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void openProfile() throws Exception {
        Whitebox.invokeMethod(baseLDRegisterFragment, "openProfile", client);
        PowerMockito.mockStatic(BaseLDProfileActivity.class);
        BaseLDProfileActivity.startProfileActivity(null, null);
        PowerMockito.verifyStatic(times(1));

    }
}
