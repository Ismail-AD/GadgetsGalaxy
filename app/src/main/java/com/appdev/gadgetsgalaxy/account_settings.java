package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;

import com.appdev.gadgetsgalaxy.databinding.FragmentAccountSettingsBinding;
import com.appdev.gadgetsgalaxy.utils.Utility;


public class account_settings extends Fragment {

    FragmentAccountSettingsBinding accountSettingsBinding;
    private static final String PREFS_NAME = "TypePrefs";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utility.isDarkModeActivated(requireActivity())) {
            Utility.status_bar_dark(requireActivity(), R.color.black);
        } else {
            Utility.status_bar(requireActivity(), R.color.white);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountSettingsBinding = FragmentAccountSettingsBinding.inflate(inflater, container, false);
        accountSettingsBinding.moveForwardOrders.setOnClickListener(v -> {
            findNavController(this).navigate(R.id.action_account_settings_to_user_side_orders);
        });
        accountSettingsBinding.modeSwitch.setChecked(Utility.isDarkModeActivated(requireActivity()));
        accountSettingsBinding.modeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Utility.toggleTheme(requireActivity());
            findNavController(this).popBackStack();
        });

        accountSettingsBinding.moveForwardLogout.setOnClickListener(v->{
            SharedPreferences.Editor editor = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            editor.clear().apply();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build();
            findNavController(this).navigate(R.id.login_screen, null, navOptions);
        });
        return accountSettingsBinding.getRoot();
    }
}