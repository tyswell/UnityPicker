package com.eightunity.unitypicker.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.search.Search;
import com.eightunity.unitypicker.ui.BaseActivity;

import java.util.Arrays;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class SearchFragment extends Fragment {

    private EditText searchText;
    private Spinner searchTypeSpinner;
    private Button searchButton;
    private SearchTypeAdapter searchTypeAdapter;

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
                Search search = getContent();
            }
        };
    }

    private Search getContent() {
        Search search = new Search();
        search.setSearchWord(searchText.getText().toString());
        search.setSearchType(searchTypeSpinner.getSelectedItem().toString());
        return search;
    }


}
