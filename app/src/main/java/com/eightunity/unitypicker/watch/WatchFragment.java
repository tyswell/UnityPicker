package com.eightunity.unitypicker.watch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.commonpage.OptionDialog;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.LinearLayoutManager;
import com.eightunity.unitypicker.ui.NonSwipeViewPager;
import com.eightunity.unitypicker.ui.recyclerview.DividerItemDecoration;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchFragment extends Fragment {

    private static final String TAG = "WatchFragment";

    public static final String PARACEL_WATCHFRAGMENT = "PARACEL_WATCHFRAGMENT";

    private RecyclerView watchRecycler;
    private WatchAdapter watchAdapter;

    private ESearchWordDAO dao;

    private OptionDialog dialog;

    private OnHeadlineSelectedListener mCallback;

    private NonSwipeViewPager vp;

    List<Watch> watches = new ArrayList<>();

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
            setDataOnPageOpen();
        } else {
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

    public interface OnHeadlineSelectedListener{
        public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) ;
    }

    public static WatchFragment newInstance() {
        return new WatchFragment();
    }

    private void initView(View rootView) {
        watchRecycler = (RecyclerView) rootView.findViewById(R.id.watchRecycler);
        watchAdapter = new WatchAdapter(rootView.getContext(), watches, recycleClick, optionClick);
        configRecyclerView(watchRecycler, watchAdapter, rootView.getContext());

        vp = (NonSwipeViewPager) getActivity().findViewById(R.id.viewpager);

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

    private void setDataOnPageOpen() {
        List<Watch> watches = getDataFromDB();
        updateUI(watches);
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

    private void updateUI(List<Watch> datas) {
        watches.clear();
        watches.addAll(datas);
        watchAdapter.notifyDataSetChanged();
    }

    private RecycleClickListener recycleClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            vp.setCurrentItem(MainActivity.MATCH_PAGE);

            String username = ((BaseActivity)getActivity()).getUser().getUsername();
            mCallback.onArticleSelected(
                    ((BaseActivity)getActivity()).getUser().getUsername(),
                    watches.get(position).getId(),
                    watches.get(position).getSearchWord(),
                    watches.get(position).getSearchType());
        }
    };

    public void setMyFragmentListener(OnHeadlineSelectedListener listener) {
        mCallback = listener;
    }

    private RecycleClickListener optionClick = new RecycleClickListener() {
        @Override
        public void onClick(View view, int position) {
            dialog.show(position);
        }
    };



}
