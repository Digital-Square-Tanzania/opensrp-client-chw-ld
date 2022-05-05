package org.smartregister.chw.ld.presenter;

import org.smartregister.chw.ld.contract.BaseLabourProgressContract;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseLabourProgressFragmentPresenter implements BaseLabourProgressContract.Presenter {
    protected WeakReference<BaseLabourProgressContract.View> viewReference;
    protected BaseLabourProgressContract.Model model;


    protected RegisterConfiguration config;
    protected Set<View> visibleColumns = new TreeSet<>();
    protected String viewConfigurationIdentifier;


    public BaseLabourProgressFragmentPresenter(BaseLabourProgressContract.View view, BaseLabourProgressContract.Model model, String viewConfigurationIdentifier) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.viewConfigurationIdentifier = viewConfigurationIdentifier;
        this.config = model.defaultRegisterConfiguration();
    }

    @Override
    public String getMainCondition() {
        return " ";
    }

    @Override
    public String getDefaultSortQuery() {
        return " ";
    }

    @Override
    public String getMainTable() {
        return "";
    }

    @Override
    public String getDueFilterCondition() {
        return null;
    }

    @Override
    public void processViewConfigurations() {
     //implement
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = getMainTable();
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

    public BaseLabourProgressContract.View getView() {
        if (viewReference != null) {
            return viewReference.get();
        }
        return null;
    }

    @Override
    public void startSync() {
        //implement
    }

    @Override
    public void searchGlobally(String s) {
        //implement
    }

}
