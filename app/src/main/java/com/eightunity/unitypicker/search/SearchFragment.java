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

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.ESearchWordDAO;
import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.model.server.device.DeviceToken;
import com.eightunity.unitypicker.model.server.search.Searching;
import com.eightunity.unitypicker.model.service.ResponseService;
import com.eightunity.unitypicker.service.ApiService;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.utility.DeviceUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

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
                addSearchService(search);
            }
        };
    }

    private ESearchWord getDBData(Search search) {
        String username = ((BaseActivity)getActivity()).getUser().getUsername();

        ESearchWord data = new ESearchWord();
        data.setUsername(username);
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

    private void addSearchService(final Searching search) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        fUser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
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
                        } else {
                            Log.e(TAG, "ERROR" + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d(TAG, "ERROR" + t.getMessage());
                    }
                });
            }
        });
    }

    private void addSearchDao(Search search) {
        dao.add(getDBData(search));
    }


}
