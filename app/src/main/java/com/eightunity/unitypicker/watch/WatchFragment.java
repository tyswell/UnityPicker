package com.eightunity.unitypicker.watch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
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

    private RecyclerView watchRecycler;
    private WatchAdapter watchAdapter;

    private ESearchWordDAO dao;

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
        watchAdapter = new WatchAdapter(rootView.getContext(), watches);
        configRecyclerView(watchRecycler, watchAdapter, watchRecyclerListener, rootView.getContext());
    }

    private void configRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, RecycleClickListener listener, Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.hasFixedSize();
        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(context, recyclerView, listener));
    }

    private RecycleClickListener watchRecyclerListener = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
        }
    };

    private void callWSMytask() {
        watches.clear();
        watches.addAll(getDataFromDB());
        watchAdapter.notifyDataSetChanged();
    }

    private List<Watch> getDataFromDB() {
        List<ESearchWord> eSearchWords = dao.getAllData();
        List<Watch> datas = new ArrayList<>();
        for (ESearchWord eSearchWord : eSearchWords) {
            Watch data = new Watch();
            data.setSearchWord(eSearchWord.getDescription());
            data.setSearchType(SearchUtility.codeToDesc(eSearchWord.getSearch_type()));
            data.setTimeDesc(DateUtil.timeSpent(eSearchWord.getModified_date()));
            datas.add(data);
        }

        return datas;
    }

}
