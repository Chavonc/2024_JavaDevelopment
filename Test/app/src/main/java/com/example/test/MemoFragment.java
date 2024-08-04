package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;
    public MemoFragment()
    {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoFragment newInstance(String param1, String param2)
    {
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View contentView=inflater.inflate(R.layout.fragment_memo,container,false);
        viewPager2=contentView.findViewById(R.id.memo_viewPager);
        tabLayout=contentView.findViewById(R.id.memo_tab_layout);
        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),getLifecycle());
        viewPagerAdapter.addFragment(new NoteFragment(),"備忘錄");
        viewPagerAdapter.addFragment(new ShoppingFragment(),"採購");
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout,viewPager2,(tab,position)->tab.setText(viewPagerAdapter.getFragmentTitle(position))).attach();
        return contentView;
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //change view shopping+wallet
        tabLayout=view.findViewById(R.id.memo_tab_layout);
        viewPager2=view.findViewById(R.id.memo_viewPager);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback()
        {
            @Override
            public void onPageSelected(int position)
            {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

}