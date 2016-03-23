package com.cpsc.timecatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.cpsc.timecatcher.helper.Constants;
import com.facebook.FacebookException;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.ui.ParseLoginFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by junyishen on 2016-02-25.
 */


public class ProfileFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ImageView profile;
    private TextView userName;
    private Switch notify;
    private Button invitation;
    private Button logOut;
    private ProfilePictureView profilePictureView;
//    public static final String URL="https://graph.facebook.com/+userId()+/picture?type=large";

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
//        profile=(ImageView) view.findViewById(R.id.user_profile);
        userName=(TextView) view.findViewById(R.id.user_name);
        notify=(Switch) view.findViewById(R.id.notified_switch);
        invitation=(Button) view.findViewById(R.id.invite_friend);
        logOut=(Button) view.findViewById(R.id.user_logout);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.profile);

        final ParseUser currentUser=ParseUser.getCurrentUser();
        try{
            if(currentUser!=null){
                userName.setText(currentUser.getString("name"));
//                GetXMLTask task = new GetXMLTask();
//                // Execute the task
//                task.execute(new String[]{URL});
                HashMap authDataMap = (HashMap)currentUser.get("authData");
                if(authDataMap!=null) {
                    HashMap facebookMap = (HashMap) authDataMap.get("facebook");
                    String facebookId = (String) facebookMap.get("id");
                    profilePictureView.setProfileId(facebookId);
                    profilePictureView.setPresetSize(ProfilePictureView.LARGE);
                }
            }
        }catch(Exception e){
            Log.e(Constants.PROFILE_TAG + "UserProfile",e.getMessage());
        }

        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences setting= PreferenceManager.getDefaultSharedPreferences(getContext());
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setting.edit().putBoolean("notification",true).commit();
//                    PushService.setDefaultPushCallback(getActivity().getApplicationContext(),AlarmReceiver.class);class
                }else{
                    setting.edit().putBoolean("notification",false).commit();
//                    PushService.setDefaultPushCallback(getActivity().getApplicationContext(), null);
                }
            }
        });

        invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLinkUrl, previewImageUrl;

                appLinkUrl = "https://fb.me/1255752534439555";
               // previewImageUrl = "invite_image.jpg";

                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                //            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(ProfileFragment.this, content);
                }
            }

        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                try{
                    if(ParseUser.getCurrentUser()==null){
                        mListener.onClickUserLogOut();
                    }
                }catch(Exception e){
                    Log.e(Constants.PROFILE_TAG + "LogOut", e.getMessage());

                }
            }
        });

        return view;
    }

//    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            Bitmap map = null;
//            for (String url : urls) {
//                map = downloadImage(url);
//            }
//            return map;
//        }
//
//        // Sets the Bitmap returned by doInBackground
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            profile.setImageBitmap(result);
//        }
//
//        // Creates Bitmap from InputStream and returns it
//        private Bitmap downloadImage(String url) {
//            Bitmap bitmap = null;
//            InputStream stream = null;
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bmOptions.inSampleSize = 1;
//
//            try {
//                stream = getHttpConnection(url);
//                bitmap = BitmapFactory.
//                        decodeStream(stream, null, bmOptions);
//                stream.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        // Makes HttpURLConnection and returns InputStream
//        private InputStream getHttpConnection(String urlString)
//                throws IOException {
//            InputStream stream = null;
//            URL url = new URL(urlString);
//            URLConnection connection = url.openConnection();
//
//            try {
//                HttpURLConnection httpConnection = (HttpURLConnection) connection;
//                httpConnection.setRequestMethod("GET");
//                httpConnection.connect();
//
//                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    stream = httpConnection.getInputStream();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            return stream;
//        }
//    }

    public interface OnFragmentInteractionListener{
        public void onClickUserLogOut();

    }

}
