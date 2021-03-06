package com.eightunity.unitypicker.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.model.server.search.AddSearchingResponse;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.Random;

import retrofit2.Call;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchText;
    private RadioGroup searchTypeRadioGroup;
    private Button searchButton;
    private ESearchWordDAO dao;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

    }

    @Override
    public void onStart() {
        super.onStart();

        searchText.setText("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchText = (EditText)view.findViewById(R.id.searchText);
        searchText.setSingleLine();
        searchTypeRadioGroup = (RadioGroup) view.findViewById(R.id.searchTypeRadioGroup);
        configSearchTypeRadio(searchTypeRadioGroup);
        searchButton = (Button) view.findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(searchOnClickListener ());

        dao = new ESearchWordDAO();
    }

    private void configSearchTypeRadio(RadioGroup searchTypeRadioGroup) {
        String [] searchType = getResources().getStringArray(R.array.search_type);

        RadioButton firstButton = null;
        for(int i = 0; i < searchType.length; i++) {
            RadioButton button = new RadioButton(getContext());
            button.setText(searchType[i]);
//            button.setTextColor(ContextCompat.getColor(getContext(), R.color.click));
            RadioGroup.LayoutParams params
                    = new RadioGroup.LayoutParams(getContext(), null);
            params.setMargins(0, 5, 0, 0);
            button.setLayoutParams(params);
            searchTypeRadioGroup.addView(button);

            if (i == 0) {
                firstButton = button;
            }
        }

        searchTypeRadioGroup.check(firstButton.getId());
    }

    private View.OnClickListener searchOnClickListener () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Searching search = getContent();
                if (isValidCriteria(search)) {
                    addSearchServiceTemp(search);
//                    addSearchServiceX(search);
                }
            }
        };
    }

    private boolean isValidCriteria(Searching search) {
        ErrorDialog errorDialog = new ErrorDialog();

        String word = search.getDescription();

        if (word == null || word.trim().equals("")) {
            errorDialog.showDialog(getActivity(), getString(R.string.validate_search_notinput));
            return false;
        }

        word = word.trim();

        if (word.length() < 3) {
            errorDialog.showDialog(getActivity(), getString(R.string.validate_search_notinput));
            return false;
        }

        return true;
    }

    private ESearchWord getDBData(Search search) {
        String userId = ((BaseActivity)getActivity()).getUser().getUserId();

        ESearchWord data = new ESearchWord();
        data.setUser_id(userId);
        data.setSearch_id(search.getSearchId());
        data.setDescription(search.getSearchWord());
        data.setSearch_type(SearchUtility.searhTypeDescToCode(search.getSearchType()));
        data.setWatchingStatus(true);
        data.setModified_date(search.getCreateTime());

        return data;
    }

    private Searching getContent() {
        Searching search = new Searching();
        search.setDescription(searchText.getText().toString());
        search.setSearchTypeCode(SearchUtility.searhTypeDescToCode(getSelectedRadioValue()));

        return search;
    }

    private String getSelectedRadioValue(){
        int id = searchTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadio = (RadioButton) getView().findViewById(id);
        return ""+selectedRadio.getText();
    }

    private void addSearchServiceTemp(final Searching search) {
        hide_keyboard();
        Search searchDao = new Search();
        Random rn = new Random();
        searchDao.setSearchId(rn.nextInt());
        searchDao.setSearchType(getSelectedRadioValue());
        searchDao.setSearchWord(search.getDescription());
        searchDao.setCreateTime(new Date());

        addSearchDao(searchDao);
        ((MainActivity)getActivity()).opentPage(MainActivity.WATCH_PAGE);
        searchText.setText("");
    }

    private void addSearchServiceX(final Searching search) {
        hide_keyboard();
        new ServiceAdaptor(getActivity()) {
            @Override
            public void callService(FirebaseUser fUser, String tokenId, ApiService service) {
                search.setTokenId(tokenId);
                Call<AddSearchingResponse> call = service.addSearching(search);
                call.enqueue(new CallBackAdaptor<AddSearchingResponse>(getActivity()) {
                    @Override
                    public void onSuccess(AddSearchingResponse response) {
                        Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response);
                        Search searchDao = new Search();
                        searchDao.setSearchId(response.getSearchingId());
                        searchDao.setCreateTime(response.getCreateDate());
                        searchDao.setSearchType(getSelectedRadioValue());
                        searchDao.setSearchWord(search.getDescription());

                        addSearchDao(searchDao);
                        ((MainActivity)getActivity()).opentPage(MainActivity.WATCH_PAGE);
                        searchText.setText("");
                    }
                });
            }
        };
    }

    private void addSearchDao(Search search) {
        ((BaseActivity)getActivity()).showLoading();
        dao.add(getDBData(search));
        ((BaseActivity)getActivity()).hideLoading();
    }

    private void hide_keyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
