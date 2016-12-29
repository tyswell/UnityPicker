package com.eightunity.unitypicker.commonpage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.watch.Watch;

/**
 * Created by chokechaic on 9/15/2016.
 */
public class OptionDialog extends Dialog {

    private TextView stopWatchingView;
    private TextView removeFromListView;
    private TextView cancelView;
    private OnOptionDialogResult mDialogResult;

    public static final int STOP_WATCHING_MODE = 1;
    public static final int REMOVE_FROM_LIST_MODE = 2;
    public static final int CACEL_MODE = 0;

    public Integer searchId;
    public Integer matchingId;
    public boolean watchingStatus;

    public OptionDialog(Context context) {
        super(context/*, R.style.MaterialDialogSheet*/);
    }

    public void show(Integer searchId, Integer matchingId, boolean watchingStatus) {
        super.show();

        this.searchId = searchId;
        this.matchingId = matchingId;
        this.watchingStatus = watchingStatus;

        if (watchingStatus) {
            stopWatchingView.setVisibility(View.VISIBLE);
        } else  {
            stopWatchingView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.dialog_option, null);
        setContentView(view);

        stopWatchingView = (TextView)findViewById(R.id.stopWatchingView);
        removeFromListView = (TextView)findViewById(R.id.removeFromListView);
        cancelView = (TextView)findViewById( R.id.cancelView);


        setCancelable(true);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        stopWatchingView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( mDialogResult != null ){
                    mDialogResult.finish(STOP_WATCHING_MODE, searchId, matchingId, watchingStatus);
                }
                dismiss();
            }
        });

        removeFromListView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( mDialogResult != null ){
                    mDialogResult.finish(REMOVE_FROM_LIST_MODE, searchId, matchingId, watchingStatus);
                }
                dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( mDialogResult != null ){
                    mDialogResult.finish(CACEL_MODE, searchId, matchingId,watchingStatus);
                }
                dismiss();
            }
        });
    }

    public void setDialogResult(OnOptionDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnOptionDialogResult{
        void finish(int mode, Integer searchingId, Integer matchingId, boolean watchingStatus);
    }
}
