package com.eightunity.unitypicker.watch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.model.server.search.DeleteSearching;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchFragment extends Fragment {

    public interface OnHeadlineSelectedListener{
        public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) ;
    }

    private static final String TAG = "WatchFragment";

    public static final String PARACEL_WATCHFRAGMENT = "PARACEL_WATCHFRAGMENT";

    private RecyclerView watchRecycler;
    private WatchAdapter watchAdapter;

    private ESearchWordDAO dao;

    private OptionDialog dialog;

    private OnHeadlineSelectedListener mCallback;

    List<Watch> watches = new ArrayList<>();

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
        updateUI(watches);
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
//                dao.delete(watches.get(position).getId());
//                watchAdapter.removeAt(position);

//                deleteSearchService(watches.get(position).getId(), position);
                deleteSearchServiceTemp(watches.get(position).getSearchId(), position);
//                deleteSearchServiceX(watches.get(position).getSearchId(), position);
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

    private void setDataOnPageOpen() {
        List<Watch> watches = getDataFromDB();
        Log.d(TAG, "watches = "+ watches.size());
        updateUI(watches);
    }

    private List<Watch> getDataFromDB() {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();
        List<ESearchWord> eSearchWords = dao.getAllData(userId);
        List<Watch> datas = new ArrayList<>();
        for (ESearchWord eSearchWord : eSearchWords) {
            Log.d(TAG, "eSearchWord.getSearch_id():"+eSearchWord.getSearch_id());
            Watch data = new Watch();
            data.setSearchId(eSearchWord.getSearch_id());
            data.setSearchWord(eSearchWord.getDescription());
            data.setSearchType(SearchUtility.searchTypeCodeToDesc(eSearchWord.getSearch_type()));
            data.setTimeDesc(DateUtil.timeSpent(eSearchWord.getModified_date()));
            datas.add(data);
        }

        return datas;
    }

    private void updateUI(List<Watch> datas) {
        watches.clear();
        watches.addAll(datas);
        watchAdapter.notifyDataSetChanged();
    }

    private RecycleClickListener recycleClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            String userId = ((BaseActivity)getActivity()).getUser().getUserId();

            mCallback.onArticleSelected(
                    userId,
                    watches.get(position).getSearchId(),
                    watches.get(position).getSearchWord(),
                    watches.get(position).getSearchType());
        }
    };

    private RecycleClickListener optionClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            dialog.show(position);
        }
    };

    private void deleteSearchServiceTemp(final int searchingId, final int position) {
        deleteSearching(searchingId, position);
    }

    private void deleteSearchServiceX(final int searchingId, final int position) {
        new ServiceAdaptor(getActivity()) {
            @Override
            public void callService(FirebaseUser fUser, String tokenId, ApiService service) {
                DeleteSearching deleteObj = new DeleteSearching();
                deleteObj.setTokenId(tokenId);
                deleteObj.setSearchingId(searchingId);

                Call<Boolean> call = service.deleteSearching(deleteObj);
                call.enqueue(new CallBackAdaptor<Boolean>(getActivity()) {
                    @Override
                    public void onSuccess(Boolean response) {
                        Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response);
                        deleteSearching(searchingId, position);
                    }
                });
            }
        };
    }

    private void deleteSearchService(final int searchingId, final int position) {
        ((BaseActivity)getActivity()).showLoading();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        fUser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                DeleteSearching deleteObj = new DeleteSearching();
                deleteObj.setTokenId(task.getResult().getToken());
                deleteObj.setSearchingId(searchingId);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_service_url))
                        .addConverterFactory(JacksonConverterFactory.create())
                        .build();

                ApiService service = retrofit.create(ApiService.class);

                Call<Boolean> call = service.deleteSearching(deleteObj);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response.body());
                            deleteSearching(searchingId, position);
                            ((BaseActivity)getActivity()).hideLoading();
                        } else {
                            Log.e(TAG, "ERROR" + response.message());
                            ((BaseActivity)getActivity()).hideLoading();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d(TAG, "ERROR" + t.getMessage());
                        ((BaseActivity)getActivity()).hideLoading();
                    }
                });
            }
        });
    }

    private void deleteSearching(int searchingId, int position) {
        dao.delete(searchingId);
        watchAdapter.removeAt(position);
    }

}
