package com.eightunity.unitypicker.watch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.server.search.InactiveSearching;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.eightunity.unitypicker.watch.adapter.WatchAdapter;
import com.eightunity.unitypicker.watch.adapter.model.WatchItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;
import com.eightunity.unitypicker.watch.adapter.utility.WatchAdapterConverter;
import com.eightunity.unitypicker.watch.adapter.utility.WatchDiffCallback;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchFragment extends Fragment implements WatchAdapter.OnItemClickListener{

    public interface OnHeadlineSelectedListener{
        public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) ;
    }

    private static final String TAG = "WatchFragment";

    public static final String PARACEL_WATCHFRAGMENT = "PARACEL_WATCHFRAGMENT";

    private RecyclerView watchRecycler;
    private WatchAdapter watchAdapter;

    private ESearchWordDAO dao;
    private EMatchingDAO matchingDAO;

    private OptionDialog dialog;

    private OnHeadlineSelectedListener mCallback;

    List<Watch> watches;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;

            mCallback = (OnHeadlineSelectedListener) a;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_watch, container, false);

        initView(rootView);

        dao = new ESearchWordDAO();
        matchingDAO = new EMatchingDAO();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            Log.d(TAG, "onActivityCreated is called with init data");
            setDataOnPageOpen();
        } else {
            Log.d(TAG, "onActivityCreated is called and don't load data again");
            restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Watch> datas = new ArrayList<>();
        datas.addAll(watches);
        outState.putParcelableArrayList(PARACEL_WATCHFRAGMENT, datas);
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        List<Watch> watches = savedInstanceState.getParcelableArrayList(PARACEL_WATCHFRAGMENT);
        List<BaseRecyclerViewType> emptyList = new ArrayList<>();
        updateDetailList(emptyList, createDetailList(watches));
    }

    private void initView(View rootView) {
        watchRecycler = (RecyclerView) rootView.findViewById(R.id.watchRecycler);
        watchAdapter = new WatchAdapter();
        watchAdapter.setOnItemClickListener(this);
        configRecyclerView(watchRecycler, watchAdapter, rootView.getContext());

        dialog = new OptionDialog(getContext());
        dialog.setDialogResult(optionDialogResult);
    }

    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, Integer searchId, Integer matchingId, boolean watchingStatus) {
            int position = getPositionDatas(searchId);
            if (OptionDialog.STOP_WATCHING_MODE == mode) {
//                deleteSearchServiceTemp(watches.get(position).getSearchId(), position);
                inactiveDao(watches.get(position).getSearchId());
//                inactiveSearchServiceX(watches.get(position).getSearchId(), position);
            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {

//                dao.delete(watches.get(position).getId());
//                watchAdapter.removeAt(position);

//                deleteSearchService(watches.get(position).getId(), position);
                if (watchingStatus) {
                    //inactiveSearchServiceX(watches.get(position).getSearchId(), position);
                    deleteSearching(watches.get(position).getSearchId(), position);
                } else {
                    deleteSearching(watches.get(position).getSearchId(), position);
                }

            } else {

            }
        }
    };

    private int getPositionDatas(int searchId) {
        int i = 0;
        for (Watch w : watches) {
            if (w.getSearchId() == searchId) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void configRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
    }

    private void setDataOnPageOpen() {
        watches = getDataFromDB();
        Log.d(TAG, "watches = "+ watches.size());
        List<BaseRecyclerViewType> emptyList = new ArrayList<>();
        updateDetailList(emptyList, WatchAdapterConverter.getWatchDetail(watches, getResources()));
//        updateUI(watches);
    }

    private List<Watch> getDataFromDB() {
        User user = ((BaseActivity)getActivity()).getUser();
        if (user != null) {
            String userId = user.getUserId();
            List<ESearchWord> eSearchWords = dao.getAllData(userId);
            List<Watch> datas = new ArrayList<>();
            for (ESearchWord eSearchWord : eSearchWords) {
                Log.d(TAG, "eSearchWord.getSearch_id():"+eSearchWord.getSearch_id());
                Watch data = new Watch();
                data.setSearchId(eSearchWord.getSearch_id());
                data.setSearchWord(eSearchWord.getDescription());
                data.setSearchType(SearchUtility.searchTypeCodeToDesc(eSearchWord.getSearch_type()));
                data.setWatchingStatus(eSearchWord.getWatchingStatus());
                data.setCountFound(countMatchingFound(userId, eSearchWord.getSearch_id()));
                data.setTimeDesc(DateUtil.timeSpent(eSearchWord.getModified_date()));
                datas.add(data);
            }

            return datas;
        } else {
            return new ArrayList<>();
        }
    }

    private void deleteSearchServiceTemp(final int searchingId, final int position) {
        ErrorDialog er = new ErrorDialog();
        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
    }

    private void inactiveDao(final int searchingId) {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();
        dao.updateWatchingStatus(searchingId, userId, false);
        List<BaseRecyclerViewType> oldList = createDetailList(watches);
        watches.get(getPositionWatches(searchingId)).setWatchingStatus(false);
        List<BaseRecyclerViewType> newList = createDetailList(watches);
        updateDetailList(oldList, newList);
    }

    private int getPositionWatches(Integer searchId) {
        int i = 0;
        for (Watch watch : watches) {
            if (watch.getSearchId() == searchId) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void inactiveSearchServiceX(final int searchingId, final int position, final boolean isDeleteRow) {
        new ServiceAdaptor(getActivity()) {
            @Override
            public void callService(FirebaseUser fUser, String tokenId, ApiService service) {
                InactiveSearching deleteObj = new InactiveSearching();
                deleteObj.setTokenId(tokenId);
                deleteObj.setSearchingId(searchingId);

                Call<Boolean> call = service.deleteSearching(deleteObj);
                call.enqueue(new CallBackAdaptor<Boolean>(getActivity()) {
                    @Override
                    public void onSuccess(Boolean response) {
                        Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response);
                        if (isDeleteRow) {
                            deleteSearching(searchingId, position);
                        } else {
                            inactiveDao(searchingId);
                        }

                        ErrorDialog er = new ErrorDialog();
                        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
                    }
                });
            }
        };
    }

    private void deleteSearching(int searchingId, int position) {
        dao.delete(searchingId);
        matchingDAO.deleteBySearchId(searchingId);
        List<BaseRecyclerViewType> oldList = createDetailList(watches);
        watches.remove(position);
        List<BaseRecyclerViewType> newList = createDetailList(watches);
        updateDetailList(oldList, newList);
    }

    @Override
    public void onRowItemClickListener(WatchItem item) {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();

        mCallback.onArticleSelected(
                userId,
                item.getSearchId(),
                item.getSearchWord(),
                item.getSearchType());
    }

    @Override
    public void onOptionItemClickListener(WatchItem item) {
        dialog.show(item.getSearchId(), null, item.getWatchingStatus());
    }

    private List<BaseRecyclerViewType> createDetailList(List<Watch> watches) {
        List<BaseRecyclerViewType> itemList = new ArrayList<>();
        itemList.addAll(WatchAdapterConverter.getWatchDetail(watches, getResources()));
        return itemList;
    }

    private void updateDetailList(List<BaseRecyclerViewType> oldItemList, List<BaseRecyclerViewType> newItemList) {
        watchAdapter.setWatchDetails(newItemList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new WatchDiffCallback(oldItemList, newItemList));
        diffResult.dispatchUpdatesTo(watchAdapter);
    }

    private int countMatchingFound(String userId, int searchingId) {
        return matchingDAO.getCountBySearchWord(userId, searchingId);
    }

}
