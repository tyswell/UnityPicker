package com.eightunity.unitypicker.watch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.match.MatchActivity;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.ui.recyclerview.RecyclerTouchListener;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchFragment extends Fragment {

    public static final String SEARCH_WORD_ID_PARAM = "SEARCH_WORD_ID_PARAM";
    public static final String SEARCH_WORD_DETAIL_PARAM = "SEARCH_WORD_DETAIL_PARAM";
    public static final String SEARCH_WORD_TYPE_PARAM = "SEARCH_WORD_TYPE_PARAM";

    private RecyclerView watchRecycler;
    private WatchAdapter watchAdapter;

    private ESearchWordDAO dao;

    private OptionDialog dialog;

    List<Watch> watches = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_watch, container, false);

        initView(rootView);

        dao = new ESearchWordDAO();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        callWSMytask();
    }

    private void initView(View rootView) {
        watchRecycler = (RecyclerView) rootView.findViewById(R.id.watchRecycler);
        watchAdapter = new WatchAdapter(rootView.getContext(), watches, recycleClick, optionClick);
        configRecyclerView(watchRecycler, watchAdapter, rootView.getContext());

        dialog = new OptionDialog(getContext());
        dialog.setDialogResult(optionDialogResult);
    }

    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, int position) {
            if (OptionDialog.STOP_WATCHING_MODE == mode) {

            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {
                dao.delete(watches.get(position).getId());
                watchAdapter.removeAt(position);
            } else {

            }
        }
    };

    private void configRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
    }

    private void callWSMytask() {
        watches.clear();
        watches.addAll(getDataFromDB());
        watchAdapter.notifyDataSetChanged();
    }

    private List<Watch> getDataFromDB() {
        String username = ((BaseActivity)getActivity()).getUser().getUsername();
        List<ESearchWord> eSearchWords = dao.getAllData(username);
        List<Watch> datas = new ArrayList<>();
        for (ESearchWord eSearchWord : eSearchWords) {
            Watch data = new Watch();
            data.setId(eSearchWord.getId());
            data.setSearchWord(eSearchWord.getDescription());
            data.setSearchType(SearchUtility.searchTypeCodeToDesc(eSearchWord.getSearch_type()));
            data.setTimeDesc(DateUtil.timeSpent(eSearchWord.getModified_date()));
            datas.add(data);
        }

        return datas;
    }

    private RecycleClickListener recycleClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent intent = new Intent(getContext(), MatchActivity.class);
            intent.putExtra(SEARCH_WORD_ID_PARAM, watches.get(position).getId());
            intent.putExtra(SEARCH_WORD_DETAIL_PARAM, watches.get(position).getSearchWord());
            intent.putExtra(SEARCH_WORD_TYPE_PARAM, watches.get(position).getSearchType());
            startActivity(intent);
        }
    };

    private RecycleClickListener optionClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            dialog.show(position);
        }
    };



}
