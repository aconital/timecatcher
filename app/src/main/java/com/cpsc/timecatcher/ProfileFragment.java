package com.cpsc.timecatcher;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginFragment;

/**
 * Created by junyishen on 2016-02-25.
 */


public class ProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ImageView profile;
    private TextView userName;
    private Switch notification;
    private Button invitation;
    private Button logOut;

    public ProfileFragment(){}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle("TimeCatcher");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile=(ImageView) view.findViewById(R.id.user_profile);
        userName=(TextView) view.findViewById(R.id.user_name);
        notification=(Switch) view.findViewById(R.id.notified_switch);
        invitation=(Button) view.findViewById(R.id.invite_friend);
        logOut=(Button) view.findViewById(R.id.user_logout);



        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                if(ParseUser.getCurrentUser()==null){
                    mListener.onUserLogOut();
                }



            }
        });

            return view;
        }

        public interface OnFragmentInteractionListener{
            public void onUserLogOut();
    }



}
