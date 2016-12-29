package com.eightunity.unitypicker.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.server.search.InactiveSearching;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.notification.adapter.NotificationAdapter;
import com.eightunity.unitypicker.notification.adapter.model.NotificationItem;
import com.eightunity.unitypicker.notification.adapter.utility.NotificationAdapterConverter;
import com.eightunity.unitypicker.notification.adapter.utility.NotificationDiffCallback;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.eightunity.unitypicker.watch.adapter.WatchAdapter;
import com.eightunity.unitypicker.watch.adapter.utility.WatchAdapterConverter;
import com.eightunity.unitypicker.watch.adapter.utility.WatchDiffCallback;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationFragment extends Fragment implements NotificationAdapter.OnItemClickListener {

    private static final String TAG = "NotificationFragment";

    public static final String PARACEL_NOTIFICATIONFRAGMENT = "PARACEL_NOTIFICATIONFRAGMENT";

    private RecyclerView notificationRecycler;
    private NotificationAdapter notificationAdapter;
    private EMatchingDAO dao;
    private ESearchWordDAO searchDao;

    private OptionDialog dialog;

    private boolean isReceiverRegistered;

    List<Notification> notifications;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is called");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView is called");
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        initView(rootView);

        dao = new EMatchingDAO();
        searchDao = new ESearchWordDAO();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (savedInstanceState == null) {
//            Log.d(TAG, "onActivityCreated is called with init data");
//            setDataOnPageOpen();
//        } else {
//            Log.d(TAG, "onActivityCreated is called and don't load data again");
//            restoreInstanceState(savedInstanceState);
//        }
        setDataOnPageOpen();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState is called");

        ArrayList<Notification> datas = new ArrayList<>();
        datas.addAll(notifications);
        outState.putParcelableArrayList(PARACEL_NOTIFICATIONFRAGMENT, datas);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume is called");

        registerReceiver();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart is called");
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        List<Notification> notifications = savedInstanceState.getParcelableArrayList(PARACEL_NOTIFICATIONFRAGMENT);
        List<BaseRecyclerViewType> emptyList = new ArrayList<>();
        updateDetailList(emptyList, createDetailList(notifications));
    }

    private void initView(View rootView) {
        notificationRecycler = (RecyclerView) rootView.findViewById(R.id.notificationRecycler);
        notificationAdapter = new NotificationAdapter();
        notificationAdapter.setOnItemClickListener(this);
        configRecyclerView(notificationRecycler, notificationAdapter, rootView.getContext());

        dialog = new OptionDialog(getContext());
        dialog.setDialogResult(optionDialogResult);
    }

    private OptionDialog.OnOptionDialogResult optionDialogResult = new OptionDialog.OnOptionDialogResult() {
        @Override
        public void finish(int mode, Integer searchId, Integer matchingId, boolean watchingStatus) {
            int positionMatching = getPositionNotification(matchingId);
            if (OptionDialog.STOP_WATCHING_MODE == mode) {
                inactiveDao(searchId);
//                inactiveSearchServiceX(notifications.get(position).getSearchId(), position);
//                stopSearchServiceTemp(notifications.get(position).getSearchId(), position);
            } else if (OptionDialog.REMOVE_FROM_LIST_MODE == mode) {
                deleteMatching(matchingId, positionMatching);
            } else {

            }
        }
    };

    private void deleteMatching(Integer matchingId, int position) {
        dao.delete(matchingId);
        List<BaseRecyclerViewType> oldList = createDetailList(notifications);
        notifications.remove(position);
        List<BaseRecyclerViewType> newList = createDetailList(notifications);
        updateDetailList(oldList, newList);
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
        notifications = getDataFromDB();
        List<BaseRecyclerViewType> emptyList = new ArrayList<>();
        updateDetailList(emptyList, NotificationAdapterConverter.getNotificationDetail(notifications));
    }

    private List<Notification> getDataFromDB() {
        String userId = ((AuthenticaterActivity)getActivity()).getUser().getUserId();
        List<EMatching> eMatchings = dao.getAllData(userId);
        List<Notification> datas = new ArrayList<>();
        for (EMatching eMatching : eMatchings) {
            Notification data = new Notification();
            data.setMatchId(eMatching.getId());
            data.setSearchId(eMatching.getSeacrh_word_id());
            data.setSearchWord(eMatching.getSearch_word_desc());
            data.setTimeDesc(DateUtil.timeSpent(eMatching.getMatching_date()));
            data.setTitleContent(eMatching.getTitle_content());
            data.setWebName(eMatching.getWeb_name());
            data.setWatchingStatus(getWatchingStatus(eMatching.getSeacrh_word_id(), userId));
            data.setUrl(eMatching.getUrl());
            datas.add(data);
        }

        return datas;
    }

    private boolean getWatchingStatus(int searchingId, String userId) {
        ESearchWord value = searchDao.getByKey(searchingId, userId);
        Log.d(TAG, "value.getWatchingStatus()="+value.getWatchingStatus());
        return value.getWatchingStatus();
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataOnPageOpen();
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

    private void stopSearchServiceTemp(final int searchingId, final int position) {
//        deleteSearching(searchingId, position);
        ErrorDialog er = new ErrorDialog();
        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
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
                        inactiveDao(searchingId);
                        ErrorDialog er = new ErrorDialog();
                        er.showDialog(getActivity(), getString(R.string.stop_watching_message));
//                        deleteSearching(searchingId, position);
                    }
                });
            }
        };
    }

    private void inactiveDao(final int searchingId) {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();
        searchDao.updateWatchingStatus(searchingId, userId, false);
        List<BaseRecyclerViewType> oldList = createDetailList(notifications);

        for (Notification notification : notifications) {
            if (notification.getSearchId() == searchingId) {
                notification.setWatchingStatus(false);
            }
        }
        List<BaseRecyclerViewType> newList = createDetailList(notifications);
        updateDetailList(oldList, newList);
    }

    @Override
    public void onRowItemClickListener(NotificationItem item) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        startActivity(browserIntent);
    }

    @Override
    public void onOptionItemClickListener(NotificationItem item) {
        dialog.show(item.getSearchId(), item.getMatchId(), item.getWatchingStatus());
    }

    private List<BaseRecyclerViewType> createDetailList(List<Notification> notifications) {
        List<BaseRecyclerViewType> itemList = new ArrayList<>();
        itemList.addAll(NotificationAdapterConverter.getNotificationDetail(notifications));
        return itemList;
    }

    private void updateDetailList(List<BaseRecyclerViewType> oldItemList, List<BaseRecyclerViewType> newItemList) {
        notificationAdapter.setNotifications(newItemList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationDiffCallback(oldItemList, newItemList));
        diffResult.dispatchUpdatesTo(notificationAdapter);
    }

    private int getPositionNotification(Integer matchingId) {
        int i = 0;
        for (Notification notification : notifications) {
            if (notification.getMatchId() == matchingId) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
