package com.eightunity.unitypicker.match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.match.adapter.MatchAdapter;
import com.eightunity.unitypicker.match.adapter.model.MatchHeaderItem;
import com.eightunity.unitypicker.match.adapter.model.MatchItem;
import com.eightunity.unitypicker.match.adapter.utility.MatchAdapterConverter;
import com.eightunity.unitypicker.match.adapter.utility.MatchDiffCallback;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.match.Match;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.model.server.search.InactiveSearching;
import com.eightunity.unitypicker.notification.adapter.utility.NotificationAdapterConverter;
import com.eightunity.unitypicker.notification.adapter.utility.NotificationDiffCallback;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.WrapContentLinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by chokechaic on 9/14/2016.
 */
public class MatchFragment extends Fragment implements MatchAdapter.OnItemClickListener{

    private static final String TAG = "MatchFragment";

    private RecyclerView matchRecycler;
    private MatchAdapter matchAdapter;
    private OptionDialog dialog;

    private ESearchWordDAO searchDao;
    private EMatchingDAO dao;

    private Match match;
    private List<MatchDetail> matchDetails = new ArrayList<>();

    public static final String PARAM_USERNAME = "PARAM_USERNAME";
    public static final String PARAM_SEARCH_WORD_ID = "PARAM_SEARCH_WORD_ID";
    public static final String PARAM_SEARCH_WORD_DETAIL = "PARAM_SEARCH_WORD_DETAIL";
    public static final String PARAM_SEARCH_TYPE_DESC = "PARAM_SEARCH_TYPE_DESC";

    private static final String PARAM_MATCH_FRAGMENT_DATA = "PARAM_MATCH_FRAGMENT_DATA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated is called and don't load data again");
            match = Parcels.unwrap(savedInstanceState.getParcelable(PARAM_MATCH_FRAGMENT_DATA));

            List<BaseRecyclerViewType> emptyList = new ArrayList<>();
            updateDetailList(emptyList, createDetailList(match));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        matchRecycler = (RecyclerView) rootView.findViewById(R.id.matchRecycler);
        matchAdapter = new MatchAdapter();
        matchAdapter.setOnItemClickListener(this);
        configRecyclerView(matchRecycler, matchAdapter, getContext());

        dao = new EMatchingDAO();
        searchDao = new ESearchWordDAO();

        dialog = new OptionDialog(getContext());
        dialog.setDialogResult(optionDialogResult);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARAM_MATCH_FRAGMENT_DATA, Parcels.wrap(match));
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart is called");

        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            String username = args.getString(PARAM_USERNAME);
            int searchWordID = args.getInt(PARAM_SEARCH_WORD_ID);
            String searchWordDetail = args.getString(PARAM_SEARCH_WORD_DETAIL);
            String searchTypeDesc = args.getString(PARAM_SEARCH_TYPE_DESC);
            startArtical(username, searchWordID, searchWordDetail, searchTypeDesc);
        }
    }

    private void configRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
    }

    private Match getHeaderDataFromDB(String userId, int searchId) {
        ESearchWord searchWord = searchDao.getByKey(searchId, userId);
        Match match = new Match();
        match.setWatchingStatus(searchWord.getWatchingStatus());
        match.setCountFound(dao.getCountBySearchWord(userId, searchId));
        match.setSearchId(searchWord.getSearch_id());
        match.setSearchType(SearchUtility.searchTypeCodeToDesc(searchWord.getSearch_type()));
        match.setSearchWord(searchWord.getDescription());
        match.setTimeDesc(DateUtil.timeSpent(searchWord.getModified_date()));
        return match;
    }

    private List<MatchDetail> getMatchDetailFromDB(String username, int searchWordId, boolean watchStatus) {
        List<EMatching> eMatchings = dao.getBySearchWord(username, searchWordId);
        List<MatchDetail> datas = new ArrayList<>();
        for (EMatching eMatching : eMatchings) {
            MatchDetail data = new MatchDetail();
            data.setMatchID(eMatching.getId());
            data.setSearchId(eMatching.getSeacrh_word_id());
            data.setTimeDesc(DateUtil.timeSpent(eMatching.getMatching_date()));
            data.setTitleContent(eMatching.getTitle_content());
            data.setWebName(eMatching.getWeb_name());
            data.setWatchingStatus(watchStatus);
            data.setUrl(eMatching.getUrl());
            datas.add(data);
        }

        return datas;
    }


    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, Integer searchId, Integer matchingId, boolean watchingStatus) {
            int positionMatching = getPositionMatchDetail(matchingId);
            if (OptionDialog.STOP_WATCHING_MODE == mode) {
                inactiveDao(searchId);
//                inactiveSearchServiceX(searchId, false);
            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {
                deleteMatching(matchingId, positionMatching);
            } else {

            }
        }
    };

    public void startArtical(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) {
        match = getHeaderDataFromDB(username, searchWordID);
        List<MatchDetail> list = getMatchDetailFromDB(username, searchWordID, match.getWatchingStatus());
        match.setMatchDetails(list);

        List<BaseRecyclerViewType> emptyList = new ArrayList<>();
        updateDetailList(emptyList, createDetailList(match));
    }

    private void inactiveSearchServiceX(final int searchingId, final boolean isDelete) {
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
                        if (isDelete) {
                            deleteSearching(searchingId);
                        } else {
                            inactiveDao(searchingId);
                            ErrorDialog er = new ErrorDialog();
                            er.showDialog(getActivity(), getString(R.string.stop_watching_message));
                        }

//                        deleteSearching(searchingId, position);
                    }
                });
            }
        };
    }

    @Override
    public void onStopWatchClickListener(MatchHeaderItem item) {
        inactiveDao(item.getSearchId());
//                inactiveSearchServiceX(item.getSearchId(), false);
    }

    @Override
    public void onDeleteClickListener(MatchHeaderItem item) {
        if (item.getWatchStatus()) {
//            inactiveSearchServiceX(item.getSearchId(), true);
            deleteSearching(item.getSearchId());
        } else {
            deleteSearching(item.getSearchId());
        }

    }

    @Override
    public void onRowItemClickListener(MatchItem item) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onOptionItemClickListener(MatchItem item) {
        dialog.show(item.getSearchId(), item.getMatchID(), item.getWatchingStatus());
    }

    private List<BaseRecyclerViewType> createDetailList(Match match) {
        List<BaseRecyclerViewType> itemList = new ArrayList<>();
        itemList.add(MatchAdapterConverter.getMatchHeaderItem(match, getResources()));
        itemList.add(MatchAdapterConverter.getMatchCountItem(match, getResources()));
        if (match.getCountFound() > 0) {
            itemList.addAll(MatchAdapterConverter.getMatchItem(match.getMatchDetails()));
        } else {
            itemList.add(MatchAdapterConverter.getMatchNoItem());
        }

        return itemList;
    }

    private void updateDetailList(List<BaseRecyclerViewType> oldItemList, List<BaseRecyclerViewType> newItemList) {
        matchAdapter.setMatchDetails(newItemList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MatchDiffCallback(oldItemList, newItemList));
        diffResult.dispatchUpdatesTo(matchAdapter);
    }

    private void deleteSearching(int searchingId) {
        searchDao.delete(searchingId);
        dao.deleteBySearchId(searchingId);
        ((MainActivity)getActivity()).opentPage(MainActivity.WATCH_PAGE);
    }

    private void deleteMatching(Integer matchingId, int position) {
        dao.delete(matchingId);
        List<BaseRecyclerViewType> oldList = createDetailList(match);
        match.getMatchDetails().remove(position);
        match.setCountFound(match.getMatchDetails().size());
        List<BaseRecyclerViewType> newList = createDetailList(match);
        updateDetailList(oldList, newList);
    }

    private void inactiveDao(final int searchingId) {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();
        searchDao.updateWatchingStatus(searchingId, userId, false);
        List<BaseRecyclerViewType> oldList = createDetailList(match);
        match.setWatchingStatus(false);

        for (MatchDetail detail : match.getMatchDetails()) {
            detail.setWatchingStatus(false);
        }

        List<BaseRecyclerViewType> newList = createDetailList(match);
        updateDetailList(oldList, newList);
    }

    private int getPositionMatchDetail(Integer matchingId) {
        int i = 0;
        for (MatchDetail detail : match.getMatchDetails()) {
            if (detail.getMatchID() == matchingId) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
