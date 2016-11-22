package com.eightunity.unitypicker.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.service.CallBackAdaptor;
import com.eightunity.unitypicker.service.ServiceAdaptor;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ErrorDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchText;
    private Spinner searchTypeSpinner;
    private Button searchButton;
    private SearchTypeAdapter searchTypeAdapter;
    private ESearchWordDAO dao;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

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
        searchTypeSpinner = (Spinner)view.findViewById(R.id.searchTypeSpinner);
        configSpinner(searchTypeSpinner);
        searchButton = (Button) view.findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(searchOnClickListener ());

        dao = new ESearchWordDAO();
    }

    private void configSpinner(Spinner searchTypeSpinner) {
        String[] searchTypeDatas = getResources().getStringArray(R.array.search_type);
        searchTypeAdapter = new SearchTypeAdapter(this.getContext(), Arrays.asList(searchTypeDatas));
        searchTypeSpinner.setAdapter(searchTypeAdapter);
    }

    private View.OnClickListener searchOnClickListener () {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Searching search = getContent();
                if (isValidCriteria(search)) {
//                                    addSearchService(search);
//                    addSearchServiceTemp(search);
                    addSearchServiceX(search);
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
        data.setDescription(search.getSearchWord());
        data.setSearch_type(SearchUtility.searhTypeDescToCode(search.getSearchType()));
        return data;
    }

    private Searching getContent() {
        Searching search = new Searching();
        search.setDescription(searchText.getText().toString());
//        search.setSearchTypeCode(searchTypeSpinner.getSelectedItem().toString());
        search.setSearchTypeCode(1);

        return search;
    }

    private void addSearchServiceTemp(final Searching search) {
        Search searchDao = new Search();
        searchDao.setSearchId(20);
        searchDao.setSearchType(searchTypeSpinner.getSelectedItem().toString());
        searchDao.setSearchWord(search.getDescription());

        addSearchDao(searchDao);
    }

    private void addSearchServiceX(final Searching search) {
        new ServiceAdaptor(getActivity()) {
            @Override
            public void callService(String tokenId, ApiService service) {
                search.setTokenId(tokenId);
                Call<Integer> call = service.addSearching(search);
                call.enqueue(new CallBackAdaptor<Integer>(getActivity()) {
                    @Override
                    public void onSuccess(Integer response) {
                        Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response);
                        Search searchDao = new Search();
                        searchDao.setSearchId(response);
                        searchDao.setSearchType(searchTypeSpinner.getSelectedItem().toString());
                        searchDao.setSearchWord(search.getDescription());

                        addSearchDao(searchDao);
                    }
                });
            }
        };
    }

    private void addSearchService(final Searching search) {
        ((BaseActivity)getActivity()).showLoading();

        final ErrorDialog errorDialog = new ErrorDialog();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        fUser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    search.setTokenId(task.getResult().getToken());

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(getString(R.string.base_service_url))
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();

                    ApiService service = retrofit.create(ApiService.class);

                    Call<Integer> call = service.addSearching(search);
                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "SUCCESS ADD SEARCH ID ="+response.body());
                                Search searchDao = new Search();
                                searchDao.setSearchId(response.body());
                                searchDao.setSearchType(searchTypeSpinner.getSelectedItem().toString());
                                searchDao.setSearchWord(search.getDescription());

                                addSearchDao(searchDao);
                                ((BaseActivity)getActivity()).hideLoading();
                            } else {
                                Log.e(TAG, "ERROR" + response.message());
                                errorDialog.showDialog(getActivity(), response.message());
                                ((BaseActivity)getActivity()).hideLoading();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {

                            Log.d(TAG, "ERROR" + t.getMessage());
                            errorDialog.showDialog(getActivity(), t.getMessage());
                            ((BaseActivity)getActivity()).hideLoading();
                        }
                    });
                } else {
                    Log.e(TAG, task.getException().getMessage());
                    errorDialog.showDialog(getActivity(), task.getException().getMessage());
                    ((BaseActivity)getActivity()).hideLoading();
                }
            }
        });
    }

    private void addSearchDao(Search search) {
        ((BaseActivity)getActivity()).showLoading();
        dao.add(getDBData(search));
        ((BaseActivity)getActivity()).hideLoading();
    }


}
