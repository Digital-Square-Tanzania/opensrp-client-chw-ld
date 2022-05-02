package org.smartregister.chw.ld.presenter;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ld.contract.LDRegisterFragmentContract;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.chw.ld.util.DBConstants;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseLDRegisterFragmentPresenter implements LDRegisterFragmentContract.Presenter {

    protected WeakReference<LDRegisterFragmentContract.View> viewReference;

    protected LDRegisterFragmentContract.Model model;

    protected RegisterConfiguration config;

    protected Set<View> visibleColumns = new TreeSet<>();
    protected String viewConfigurationIdentifier;

    public BaseLDRegisterFragmentPresenter(LDRegisterFragmentContract.View view, LDRegisterFragmentContract.Model model, String viewConfigurationIdentifier) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.viewConfigurationIdentifier = viewConfigurationIdentifier;
        this.config = model.defaultRegisterConfiguration();
    }

    @Override
    public String getMainCondition() {
        return "";
    }

    @Override
    public String getDefaultSortQuery() {
        return Constants.TABLES.LD_CONFIRMATION + "." + DBConstants.KEY.LAST_INTERACTED_WITH + " DESC ";
    }

    @Override
    public void processViewConfigurations() {
        if (StringUtils.isBlank(viewConfigurationIdentifier)) {
            return;
        }

        ViewConfiguration viewConfiguration = model.getViewConfiguration(viewConfigurationIdentifier);
        if (viewConfiguration != null) {
            config = (RegisterConfiguration) viewConfiguration.getMetadata();
            this.visibleColumns = model.getRegisterActiveColumns(viewConfigurationIdentifier);
        }

        if (config.getSearchBarText() != null && getView() != null) {
            getView().updateSearchBarHint(config.getSearchBarText());
        }
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = Constants.TABLES.LD_CONFIRMATION;
        mainCondition = trim(getMainCondition()).equals("") ? mainCondition : getMainCondition();
        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        if (getView() != null) {

            getView().initializeQueryParams(tableName, countSelect, mainSelect);
            getView().initializeAdapter(visibleColumns);

            getView().countExecute();
            getView().filterandSortInInitializeQueries();
        }
    }

    protected LDRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
//        implement

    }

    @Override
    public void searchGlobally(String s) {
//        implement

    }

    @Override
    public String getMainTable() {
        return Constants.TABLES.LD_CONFIRMATION;
    }

    @Override
    public String getDueFilterCondition() {
        return " (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(ld_test_date,7,4)|| '-' || SUBSTR(ld_test_date,4,2) || '-' || SUBSTR(ld_test_date,1,2),'')) as integer) between 7 and 14) ";
    }
}
