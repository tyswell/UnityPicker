package com.eightunity.unitypicker.match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.match.Match;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.model.server.search.InactiveSearching;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
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
public class MatchFragment extends Fragment{

    private static final String TAG = "MatchFragment";

    private TextView searchWordView;
    private ImageView logo;
    private TextView searchTypeView;
    private RecyclerView matchRecycler;
    private MatchAdapter matchAdapter;
    private OptionDialog dialog;

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
            setDataUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);

        searchWordView = (TextView) rootView.findViewById(R.id.searchWordView);
        logo = (ImageView) rootView.findViewById(R.id.logo);
        searchTypeView = (TextView) rootView.findViewById(R.id.searchTypeView);

        matchRecycler = (RecyclerView) rootView.findViewById(R.id.matchRecycler);
        matchAdapter = new MatchAdapter(getContext(), matchDetails, recycleClick, optionClick);
        configRecyclerView(matchRecycler, matchAdapter, getContext());

        dao = new EMatchingDAO();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
    }

    private void setMatchData(Match match) {
        searchWordView.setText(match.getSearchWord());
        searchTypeView.setText(match.getSearchType());
        logo.setImageResource(SearchUtility.searchTypeLogo(match.getSearchType()));
    }

    private List<MatchDetail> getMatchDetailFromDB(String username, int searchWordId) {
        List<EMatching> eMatchings = dao.getBySearchWord(username, searchWordId);
        List<MatchDetail> datas = new ArrayList<>();
        for (EMatching eMatching : eMatchings) {
            MatchDetail data = new MatchDetail();
            data.setMatchID(eMatching.getId());
            data.setSearchId(eMatching.getSeacrh_word_id());
            data.setTimeDesc(DateUtil.timeSpent(eMatching.getMatching_date()));
            data.setTitleContent(eMatching.getTitle_content());
            data.setWebName(eMatching.getWeb_name());
            data.setUrl(eMatching.getUrl());
            datas.add(data);
        }

        return datas;
    }


    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, int position) {
            if (OptionDialog.STOP_WATCHING_MODE == mode) {
//   TODO             inactiveSearchServiceX(notifications.get(position).getSearchId(), position);
                stopSearchServiceTemp(matchDetails.get(position).getSearchId(), position);
            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {
                dao.delete(matchDetails.get(position).getMatchID());
                matchAdapter.removeAt(position);
            } else {

            }
        }
    };

    private RecycleClickListener recycleClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(matchDetails.get(position).getUrl()));
            startActivity(browserIntent);
        }
    };

    private RecycleClickListener optionClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            dialog.show(position);
        }
    };

    public void startArtical(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) {
        List<MatchDetail> list = getMatchDetailFromDB(username, searchWordID);
        match = new Match();
        match.setSearchType(searchTypeDesc);
        match.setSearchWord(searchWordDetail);
        match.setMatchDetails(list);

        setDataUI();
    }

    public void setDataUI() {
        setMatchData(match);

        matchDetails.clear();
        matchDetails.addAll(match.getMatchDetails());
        matchAdapter.notifyDataSetChanged();
    }

    private void inactiveSearchServiceX(final int searchingId, final int position) {
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
                        ErrorDialog er = new ErrorDialog();
                        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
//                        deleteSearching(searchingId, position);
                    }
                });
            }
        };
    }

    private void stopSearchServiceTemp(final int searchingId, final int position) {
//        deleteSearching(searchingId, position);
        ErrorDialog er = new ErrorDialog();
        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
    }

}
