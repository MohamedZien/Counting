package com.takkat.counting;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ProfileDialog  extends DialogFragment {

    private View view ;
    private TextView tName , tEmail , tMobile , tPhone ;
    private String sName , sEmail , sMobile , sPhone ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_dialog, container, false);

        Bundle args = getArguments();
        sName = args.getString("name");
        sEmail = args.getString("email");
        sMobile = args.getString("mobile");
        sPhone = args.getString("address");


        tName = view.findViewById(R.id.txt_profile_dialog_name);
        tName.setText(sName);
        tEmail = view.findViewById(R.id.txt_profile_dialog_email);
        tEmail.setText(sEmail);
        tMobile = view.findViewById(R.id.txt_profile_dialog_mobile);
        tMobile.setText(sMobile);
        tPhone = view.findViewById(R.id.txt_profile_dialog_phone);
        tPhone.setText(sPhone);

        return view ;
    }

}
