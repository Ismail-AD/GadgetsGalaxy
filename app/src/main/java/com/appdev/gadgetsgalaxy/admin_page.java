package com.appdev.gadgetsgalaxy;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdev.gadgetsgalaxy.data.Admin_panel_data;
import com.appdev.gadgetsgalaxy.recyclerview.Admin_card_adapter;
import com.appdev.gadgetsgalaxy.databinding.FragmentAdminPageBinding;
import com.appdev.gadgetsgalaxy.utils.OnAdminCardClickListener;

import java.util.ArrayList;
import java.util.List;


public class admin_page extends Fragment {

    private FragmentAdminPageBinding adminPageBinding;
    Admin_card_adapter adminCardAdapter;
    List<Admin_panel_data> panelDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminPageBinding = FragmentAdminPageBinding.inflate(inflater, container, false);
        panelDataList.add(new Admin_panel_data(267, "Customers"));
        panelDataList.add(new Admin_panel_data(267, "Categories"));
        panelDataList.add(new Admin_panel_data(267, "Products"));
        panelDataList.add(new Admin_panel_data(267, "Earnings"));
        panelDataList.add(new Admin_panel_data(267, "Pending Orders"));
        panelDataList.add(new Admin_panel_data(267, "Orders In Progress"));
        panelDataList.add(new Admin_panel_data(267, "Orders shipped"));
        panelDataList.add(new Admin_panel_data(267, "Delivered Orders"));
        panelDataList.add(new Admin_panel_data(267, "Cancel Orders"));
        panelDataList.add(new Admin_panel_data(267, "Banners"));
        panelDataList.add(new Admin_panel_data(267, "Discount"));
        adminCardAdapter = new Admin_card_adapter(panelDataList,this::onAdminCardClick);
        adminPageBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adminPageBinding.rv.setAdapter(adminCardAdapter);
        return adminPageBinding.getRoot();
    }


    public void onAdminCardClick(String title) {
        switch (title) {
            case "Customers":
                findNavController(this).navigate(R.id.action_admin_page_to_userInfo_screen);
                break;
            case "Categories":
                findNavController(this).navigate(R.id.action_admin_page_to_categoryInfo);
                break;
            case "Products":
                // Set vector drawable for Title 2
                break;
            case "Earnings":
                // Set vector drawable for Title 2

                break;
            case "Pending Orders":
                // Set vector drawable for Title 2

                break;
            case "Orders In Progress":
                // Set vector drawable for Title 2

                break;

            case "Orders shipped":
                // Set vector drawable for Title 2

                break;

            case "Delivered":
                // Set vector drawable for Title 2

                break;
            case "Cancel Orders":
                // Set vector drawable for Title 2

                break;
            case "Banners":
                // Set vector drawable for Title 2

                break;
            case "Discount":
                // Set vector drawable for Title 2

                break;

            default:
                Toast.makeText(getContext(), "Event Listener Not Implemented for following !", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}