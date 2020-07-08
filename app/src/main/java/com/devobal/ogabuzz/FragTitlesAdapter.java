package com.devobal.ogabuzz;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;

import java.util.ArrayList;

public class FragTitlesAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragment = new ArrayList<>();
    ArrayList<String> tabTitles = new ArrayList<>();
    /*private Map<Integer, String> mFragmentTags;
    private FragmentManager fragmentManager;*/

    public void addFragments(Fragment fragments, String titles){

        this.fragment.add(fragments);
        this.tabTitles.add(titles);
//        mFragmentTags = new HashMap<Integer,String>();
    }

    public FragTitlesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return fragment.size();
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container,position);
        if (obj instanceof Fragment){
            // record the fragment tag here
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position,tag);
        }
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);
        if (tag == null){
            return null;
        }else{
            return mFragmentTags.
        }
    }*/

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
