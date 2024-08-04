package com.example.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter
{
    private final List<Fragment> fragments=new ArrayList<>();
    private final List<String> fragmentTitles=new ArrayList<>();
    private FragmentManager fragmentManager;
    public ViewPagerAdapter( @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle)
    {
        super(fragmentManager,lifecycle);
        //this.fragmentManager=fragmentManager;
    }
    public void addFragment(Fragment fragment,String title)
    {
        fragments.add(fragment);
        fragmentTitles.add(title);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
//        switch(position)
//        {
//            case 0:return new NoteFragment();
//            case 1:return new ShoppingFragment();
//        }
//        return null;
        return fragments.get(position);
    }
    @Override
    public int getItemCount()
    {
        return fragments.size();
    }
    public String getFragmentTitle(int position)
    {
        return fragmentTitles.get(position);
    }
}
