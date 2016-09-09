package com.eightunity.unitypicker.notification;

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
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.notificaiton.Notification;
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
public class NotificationFragment extends Fragment {

    private RecyclerView notificationRecycler;
    private NotificationAdapter notificationAdapter;
    private EMatchingDAO dao;

    List<Notification> notifications = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        initView(rootView);

        dao = new EMatchingDAO();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        callWSMytask();
    }

    private void initView(View rootView) {
        notificationRecycler = (RecyclerView) rootView.findViewById(R.id.notificationRecycler);
        notificationAdapter = new NotificationAdapter(rootView.getContext(), notifications);
        configRecyclerView(notificationRecycler, notificationAdapter, notificationRecyclerListener, rootView.getContext());
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

    private RecycleClickListener notificationRecyclerListener = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
        }
    };

    private void callWSMytask() {
        notifications.clear();
        notifications.addAll(getDataFromDB());
        notificationAdapter.notifyDataSetChanged();
    }

    private List<Notification> getDataFromDB() {
        String username = ((BaseActivity)getActivity()).getUser().getUsername();
        List<EMatching> eMatchings = dao.getAllData(username);
        List<Notification> datas = new ArrayList<>();
        for (EMatching eMatching : eMatchings) {
            Notification data = new Notification();
            data.setSearchWord(eMatching.getSearch_word_desc());
            data.setTimeDesc(DateUtil.timeSpent(eMatching.getMatching_date()));
            data.setTitleContent(eMatching.getTitle_content());
            data.setWebName(eMatching.getWeb_name());
            datas.add(data);
        }

        return datas;
    }
}
