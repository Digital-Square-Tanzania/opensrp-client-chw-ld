package org.smartregister.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.ld.contract.LDRegisterFragmentContract;
import org.smartregister.chw.ld.presenter.BaseLDRegisterFragmentPresenter;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.chw.ld.util.DBConstants;
import org.smartregister.configurableviews.model.View;

import java.util.Set;
import java.util.TreeSet;

public class BaseTestRegisterFragmentPresenterLD {
    @Mock
    protected LDRegisterFragmentContract.View view;

    @Mock
    protected LDRegisterFragmentContract.Model model;

    private BaseLDRegisterFragmentPresenter baseLDRegisterFragmentPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        baseLDRegisterFragmentPresenter = new BaseLDRegisterFragmentPresenter(view, model, "");
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(baseLDRegisterFragmentPresenter);
    }

    @Test
    public void getMainCondition() {
        Assert.assertEquals("", baseLDRegisterFragmentPresenter.getMainCondition());
    }

    @Test
    public void getDueFilterCondition() {
        Assert.assertEquals(" (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(ld_test_date,7,4)|| '-' || SUBSTR(ld_test_date,4,2) || '-' || SUBSTR(ld_test_date,1,2),'')) as integer) between 7 and 14) ", baseLDRegisterFragmentPresenter.getDueFilterCondition());
    }

    @Test
    public void getDefaultSortQuery() {
        Assert.assertEquals(Constants.TABLES.LD_CONFIRMATION + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ", baseLDRegisterFragmentPresenter.getDefaultSortQuery());
    }

    @Test
    public void getMainTable() {
        Assert.assertEquals(Constants.TABLES.LD_CONFIRMATION, baseLDRegisterFragmentPresenter.getMainTable());
    }

    @Test
    public void initializeQueries() {
        Set<View> visibleColumns = new TreeSet<>();
        baseLDRegisterFragmentPresenter.initializeQueries(null);
        Mockito.doNothing().when(view).initializeQueryParams("ec_ld_confirmation", null, null);
        Mockito.verify(view).initializeQueryParams("ec_ld_confirmation", null, null);
        Mockito.verify(view).initializeAdapter(visibleColumns);
        Mockito.verify(view).countExecute();
        Mockito.verify(view).filterandSortInInitializeQueries();
    }

}