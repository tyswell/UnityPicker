package com.eightunity.unitypicker.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.authenticator.LoginActivity;
import com.eightunity.unitypicker.ui.AuthenticaterActivity;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.facebook.login.LoginManager;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private CircleImageView profileImage;
    private TextView usernameView;
    private Button logoutBtn;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        callWSMytask();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = (CircleImageView) view.findViewById(R.id.profileImage);
        usernameView = (TextView) view.findViewById(R.id.usernameView);
        logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthenticaterActivity)getActivity()).logout();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
//                LoginManager.getInstance().logOut();
            }
        });
    }

    private void callWSMytask() {
        String name = ((BaseActivity)getActivity()).getUser().getDisplayName();
        usernameView.setText(name);
        String imageURL = ((BaseActivity)getActivity()).getUser().getProfileURL();

        RequestManager rm = Glide.with(getContext());
        DrawableTypeRequest dr = null;
        if (imageURL != null) {
            dr = rm.load(imageURL);
        } else {
            dr = rm.load(R.mipmap.ic_default_profile);
        }
        dr.crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_default_profile)
                .error(R.mipmap.ic_default_profile)
                .into(profileImage);
    }
}
