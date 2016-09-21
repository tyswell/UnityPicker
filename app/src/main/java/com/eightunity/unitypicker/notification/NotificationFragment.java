package com.eightunity.unitypicker.notification;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.ui.recyclerview.RecyclerTouchListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";

    private RecyclerView notificationRecycler;
    private NotificationAdapter notificationAdapter;
    private EMatchingDAO dao;

    private OptionDialog dialog;

    private boolean isReceiverRegistered;

    List<Notification> notifications = new ArrayList<>();

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

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

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void initView(View rootView) {
        notificationRecycler = (RecyclerView) rootView.findViewById(R.id.notificationRecycler);
        notificationAdapter = new NotificationAdapter(rootView.getContext(), notifications, recycleClick, optionClick);
        configRecyclerView(notificationRecycler, notificationAdapter, rootView.getContext());

        dialog = new OptionDialog(getContext());
        dialog.setDialogResult(optionDialogResult);
    }

    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, int position) {
            if (OptionDialog.STOP_WATCHING_MODE == mode) {

            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {
                dao.delete(notifications.get(position).getMatchId());
                notificationAdapter.removeAt(position);
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

    private RecycleClickListener recycleClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notifications.get(position).getUrl()));
            startActivity(browserIntent);
        }
    };

    private RecycleClickListener optionClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            dialog.show(position);
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
            data.setMatchId(eMatching.getId());
            data.setSearchWord(eMatching.getSearch_word_desc());
            data.setTimeDesc(DateUtil.timeSpent(eMatching.getMatching_date()));
            data.setTitleContent(eMatching.getTitle_content());
            data.setWebName(eMatching.getWeb_name());
            data.setUrl(eMatching.getUrl());
            datas.add(data);
        }

        return datas;
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            callWSMytask();
        }
    };

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(FirebaseMsgService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

//    public void openBottomSheet() {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_option, null);
//        TextView stopWatchingView = (TextView)view.findViewById(R.id.stopWatchingView);
//        TextView removeFromListView = (TextView)view.findViewById(R.id.removeFromListView);
//        TextView cancelView = (TextView)view.findViewById( R.id.cancelView);
//
//        final Dialog mBottomSheetDialog = new Dialog (getActivity(),
//                R.style.MaterialDialogSheet);
//        mBottomSheetDialog.setContentView(view);
//        mBottomSheetDialog.setCancelable(true);
//        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
//        mBottomSheetDialog.show();
//
//
//        stopWatchingView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(),"Clicked Backup",Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        removeFromListView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                dao.delete(notifications.get(position).getMatchId());
////                notificationAdapter.removeAt(position);
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//        cancelView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked Open", Toast.LENGTH_SHORT).show();
//                mBottomSheetDialog.dismiss();
//            }
//        });
//
//    }

}
