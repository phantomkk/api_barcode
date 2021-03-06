package com.project.barcodechecker.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.project.barcodechecker.R;
import com.project.barcodechecker.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private static SettingFragment instance = new SettingFragment();
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ScanFragment scanFragment =new ScanFragment();
    private ScanFragment scanFragment2 =new ScanFragment();
    private ScanFragment scanFragment3 =new ScanFragment();
    MenuItem prevMenuItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager_accout);
        setupViewPager(viewPager);
        tabLayout= (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.getTabAt(0).select();
        return view;
    }

    public static SettingFragment getInstance() {
        return instance;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(scanFragment);
        adapter.addFragment(scanFragment2);
        adapter.addFragment(scanFragment3);
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Accout");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_accout_white, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("List Product");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_product_white, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tabThree.setText("Notification");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notifi_white, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }



}
