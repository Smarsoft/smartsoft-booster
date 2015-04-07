package io.smartsoft.booster.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ProgressBar;
import butterknife.ButterKnife;
import io.smartsoft.booster.R;
import io.smartsoft.booster.ui.adapters.ListAdapter;
import io.smartsoft.booster.ui.adapters.OnRecyclerViewItemClickListener;
import io.smartsoft.booster.ui.fragments.base.BaseFragment;
import io.smartsoft.booster.ui.views.error.ErrorView;
import io.smartsoft.booster.ui.views.error.RetryListener;
import io.smartsoft.booster.ui.views.itemdecorations.LineDividerItemDecoration;
import io.smartsoft.booster.utils.BundleConst;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

abstract public class ListFragment<ViewHolderType extends RecyclerView.ViewHolder, ModelType>
        extends BaseFragment {
    /**
     * Views
     */
    protected RecyclerView mRecyclerView;
    protected ProgressBar mProgressView;
    protected ViewStub mEmpty;
    protected ErrorView mErrorView;

    /**
     * Data
     */
    protected Subscription mSubscription = Subscriptions.empty();
    protected ListAdapter<ViewHolderType, ModelType> mAdapter;
    protected LinearLayoutManager mLinearLayoutManager;
    protected boolean mEnableSelection;

    public ListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mEnableSelection =
                    getArguments().getBoolean(BundleConst.ARG_ENABLE_SELECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mRecyclerView = ButterKnife.findById(view, R.id.list);
        mProgressView = ButterKnife.findById(view, R.id.progress_view);
        mEmpty = ButterKnife.findById(view, R.id.empty);
        mErrorView = ButterKnife.findById(view, R.id.error_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupData();
    }

    @Override
    public void onDestroyView() {
        mSubscription.unsubscribe();
        ButterKnife.reset(this);

        super.onDestroyView();
    }

    private void setupData() {
        showProgress();

        mAdapter = getAdapter();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity()));
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setSelectionEnabled(mEnableSelection);

        fetchData();

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<ModelType>() {
            @Override
            public void onItemClick(View view, ModelType dataItem, int position) {
                onDataItemClicked(dataItem);
            }
        });

        mErrorView.setOnRetryListener(new RetryListener() {
            @Override
            public void onRetry() {
                showProgress();
                fetchData();
            }
        });
    }

    protected abstract void onDataItemClicked(ModelType dataItem);

    protected abstract ListAdapter<ViewHolderType, ModelType> getAdapter();

    protected abstract void fetchData();

    protected void showProgress() {
        hideEmptyView();
        hideErrorView();

        mRecyclerView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    protected void hideProgress() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
    }

    protected void showEmptyView() {
        mEmpty.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyView() {
        if (mEmpty.getVisibility() == View.VISIBLE) {
            mEmpty.setVisibility(View.GONE);
        }
    }

    protected void showErrorView() {
        mErrorView.setVisibility(View.VISIBLE);
    }

    protected void hideErrorView() {
        if (mErrorView.getVisibility() == View.VISIBLE) {
            mErrorView.setVisibility(View.GONE);
        }
    }
}
