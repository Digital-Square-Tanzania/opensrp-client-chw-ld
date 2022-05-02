package org.smartregister.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.ld.model.BaseLDRegisterFragmentModel;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;

import java.util.HashSet;
import java.util.Set;

public class BaseLDRegisterFragmentModelLD {

    @Mock
    private BaseLDRegisterFragmentModel baseLDRegisterFragmentModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDefaultRegisterConfiguration() {
        RegisterConfiguration configuration = new RegisterConfiguration();
        Mockito.when(baseLDRegisterFragmentModel.defaultRegisterConfiguration())
                .thenReturn(configuration);
        Assert.assertEquals(configuration, baseLDRegisterFragmentModel.defaultRegisterConfiguration());
    }

    @Test
    public void testGetViewConfiguration() {
        ViewConfiguration viewConfiguration = new ViewConfiguration();
        Mockito.when(baseLDRegisterFragmentModel.getViewConfiguration(Mockito.anyString()))
                .thenReturn(viewConfiguration);
        Assert.assertEquals(viewConfiguration, baseLDRegisterFragmentModel.getViewConfiguration(Mockito.anyString()));
    }

    @Test
    public void testGetRegisterActiveColumns() {
        Set<View> views = new HashSet<View>();
        Mockito.when(baseLDRegisterFragmentModel.getRegisterActiveColumns(Mockito.anyString()))
                .thenReturn(views);
        Assert.assertEquals(views, baseLDRegisterFragmentModel.getRegisterActiveColumns(Mockito.anyString()));
    }

    @Test
    public void testCountSelect() {
        String countString = "0";
        Mockito.when(baseLDRegisterFragmentModel.countSelect(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(countString);
        Assert.assertEquals(countString, baseLDRegisterFragmentModel.countSelect(Mockito.anyString(), Mockito.anyString()));
    }

    @Test
    public void testMainSelect() {
        String countString = "mainSelect";
        Mockito.when(baseLDRegisterFragmentModel.mainSelect(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(countString);
        Assert.assertEquals(countString, baseLDRegisterFragmentModel.mainSelect(Mockito.anyString(), Mockito.anyString()));
    }

}
