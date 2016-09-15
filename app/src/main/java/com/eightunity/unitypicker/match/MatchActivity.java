package com.eightunity.unitypicker.match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.match.Match;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.ui.recyclerview.RecyclerTouchListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.eightunity.unitypicker.watch.WatchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 9/14/2016.
 */
public class MatchActivity extends BaseActivity {

    private TextView searchWordView;
    private ImageView logo;
    private TextView searchTypeView;
    private RecyclerView matchRecycler;
    private MatchAdapter matchAdapter;

    private OptionDialog dialog;

    private EMatchingDAO dao;

    private List<MatchDetail> matchDetails = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        searchWordView = (TextView) findViewById(R.id.searchWordView);
        logo = (ImageView) findViewById(R.id.logo);
        searchTypeView = (TextView) findViewById(R.id.searchTypeView);

        matchRecycler = (RecyclerView) findViewById(R.id.matchRecycler);
        matchAdapter = new MatchAdapter(this, matchDetails, recycleClick, optionClick);
        configRecyclerView(matchRecycler, matchAdapter, this);

        dao = new EMatchingDAO();

        dialog = new OptionDialog(this);
        dialog.setDialogResult(optionDialogResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callWSMytask();
    }

    private void configRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
    }

    private void callWSMytask() {
        int SearchWordId = getSearchWordId();
        matchDetails.clear();
        matchDetails.addAll(getMatchDetailFromDB(SearchWordId));
        matchAdapter.notifyDataSetChanged();

        setMatchData();
    }

    private void setMatchData() {
        Match match = getMatchFromIntent();
        searchWordView.setText(match.getSearchWord());
        searchTypeView.setText(match.getSearchType());
        logo.setImageResource(SearchUtility.searchTypeLogo(match.getSearchType()));
    }

    private List<MatchDetail> getMatchDetailFromDB(int searchWordId) {
        String username = getUser().getUsername();
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

    public int getSearchWordId() {
        int id = getIntent().getIntExtra(WatchFragment.SEARCH_WORD_ID_PARAM, 0);
        return id;
    }

    public Match getMatchFromIntent() {
        Match match = new Match();
        match.setSearchWord(getIntent().getStringExtra(WatchFragment.SEARCH_WORD_DETAIL_PARAM));
        match.setSearchType(getIntent().getStringExtra(WatchFragment.SEARCH_WORD_TYPE_PARAM));
        return match;
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
}
