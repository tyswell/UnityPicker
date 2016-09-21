package com.eightunity.unitypicker.match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.match.Match;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

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

    private List<MatchDetail> matchDetails = new ArrayList<>();

    public static MatchFragment newInstance() {
        return new MatchFragment();
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
    public void onStart() {
        super.onStart();

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

    private List<MatchDetail> getMatchDetailFromDB(int searchWordId) {
        String username = ((BaseActivity)getActivity()).getUser().getUsername();
        Log.d(TAG, "searchWordId="+searchWordId);
        List<EMatching> eMatchings = dao.getBySearchWord(username, searchWordId);
        List<MatchDetail> datas = new ArrayList<>();
        for (EMatching eMatching : eMatchings) {
            MatchDetail data = new MatchDetail();
            data.setMatchID(eMatching.getId());
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

    public void startArtical(int searchWordID, String searchWordDetail, String searchTypeDesc) {
        Log.d(TAG, "searchWordID="+searchWordID + " ||searchWordDetail="+searchWordDetail + " ||searchTypeDesc="+searchTypeDesc);

        matchDetails.clear();
        matchDetails.addAll(getMatchDetailFromDB(searchWordID));
        matchAdapter.notifyDataSetChanged();

        Match match = new Match();
        match.setSearchWord(searchWordDetail);
        match.setSearchType(searchTypeDesc);
        setMatchData(match);
    }
}
