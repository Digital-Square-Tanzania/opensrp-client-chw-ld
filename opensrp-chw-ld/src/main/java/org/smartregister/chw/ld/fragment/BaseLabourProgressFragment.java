package org.smartregister.chw.ld.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.smartregister.chw.ld.contract.BaseLabourProgressContract;
import org.smartregister.chw.ld.model.BaseLabourProgressFragmentModel;
import org.smartregister.chw.ld.presenter.BaseLabourProgressFragmentPresenter;
import org.smartregister.chw.ld.provider.LabourProgressProvider;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.ld.R;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;
import java.util.Set;

import androidx.appcompat.widget.Toolbar;

public class BaseLabourProgressFragment extends BaseRegisterFragment implements  BaseLabourProgressContract.View {
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        LabourProgressProvider labourProgressProvider = new LabourProgressProvider(getActivity(), paginationViewHandler, registerActionHandler, visibleColumns);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, labourProgressProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }


    @Override
    protected void initializePresenter() {
        if(getActivity() == null) {
            return;
        }
        presenter = new BaseLabourProgressFragmentPresenter(this, new BaseLabourProgressFragmentModel(), null);
    }

    @Override
    public void setUniqueID(String s) {
        //overriden
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        // Update top left icon
        qrCodeScanImageView = view.findViewById(org.smartregister.R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(android.view.View.GONE);
        }

        android.view.View searchBarLayout = view.findViewById(org.smartregister.R.id.search_bar_layout);
        searchBarLayout.setVisibility(View.GONE);

        if (getSearchView() != null) {
            getSearchView().setBackgroundResource(R.color.white);
            getSearchView().setVisibility(View.GONE);
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
        }
        //remove toolbar
        Toolbar toolbar = view.findViewById(org.smartregister.R.id.register_toolbar);
        toolbar.setVisibility(View.GONE);

        android.view.View topLeftLayout = view.findViewById(R.id.top_left_layout);
        topLeftLayout.setVisibility(android.view.View.GONE);

        android.view.View topRightLayout = view.findViewById(R.id.top_right_layout);
        topRightLayout.setVisibility(android.view.View.VISIBLE);

        android.view.View sortFilterBarLayout = view.findViewById(R.id.register_sort_filter_bar_layout);
        sortFilterBarLayout.setVisibility(android.view.View.GONE);

        android.view.View filterSortLayout = view.findViewById(R.id.filter_sort_layout);
        filterSortLayout.setVisibility(android.view.View.GONE);

        TextView filterView = view.findViewById(org.smartregister.R.id.filter_text_view);
        if (filterView != null) {
            filterView.setVisibility(View.GONE);
        }
        ImageView logo = view.findViewById(org.smartregister.R.id.opensrp_logo_image_view);
        if (logo != null) {
            logo.setVisibility(android.view.View.GONE);
        }
        CustomFontTextView titleView = view.findViewById(R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(android.view.View.VISIBLE);
            titleView.setText(getString(R.string.labour_progress));
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {
        //overridden
    }

    @Override
    protected String getMainCondition() {
        return presenter().getMainCondition();
    }

    @Override
    protected String getDefaultSortQuery() {
        return presenter().getDefaultSortQuery();
    }

    @Override
    protected void startRegistration() {
        //implement
    }

    @Override
    protected void onViewClicked(View view) {
        if (getActivity() == null || !(view.getTag() instanceof CommonPersonObjectClient)) {
            return;
        }
        CommonPersonObjectClient client = (CommonPersonObjectClient) view.getTag();
        if (view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            openEditForm(client);
        }
    }

    @Override
    public void showNotFoundPopup(String s) {

    }

    @Override
    public BaseLabourProgressContract.Presenter presenter() {
        return (BaseLabourProgressContract.Presenter) presenter;
    }

    public void openEditForm(CommonPersonObjectClient client) {
        //implement
    }
}
