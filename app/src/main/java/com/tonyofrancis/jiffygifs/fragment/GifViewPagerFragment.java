package com.tonyofrancis.jiffygifs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyofrancis.jiffygifs.R;
import com.tonyofrancis.jiffygifs.helpers.Searchable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyofrancis on 5/22/16.
 *
 * The GifViewPagerFragment implements a ViewPager that displays two fragments.
 * The GifListFragment and the SearchGifListFragment.
 * The GifViewPagerFragment implements the FragmentStatePagerAdapter that is used to preserve
 * the state of the pager's fragments.
 */

public class GifViewPagerFragment extends Fragment {

    @BindView(R.id.gif_view_pager)
    protected ViewPager mViewPager;

    private Fragment[] mFragments;

    /**Static method used to get a properly formatted GifViewPagerFragment
     * @return - properly formatted GifViewPagerFragment
     * */
    public static Fragment newInstance() {
        return new GifViewPagerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        if (savedInstanceState == null) {
            mFragments = new Fragment[]{
                    GifListFragment.newInstance(),
                    SearchGifListFragment.newInstance()};
        } else {
            mFragments = new Fragment[2]; //Fragments will be restored by the viewpager's adapter
        }

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_gif_view_pager,container,false);
        ButterKnife.bind(this,view);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.gif_tab_layout);

        /*Setup ViewPager with a state adapter. The adapter saves the state
        * of the fragments*/
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                switch (position) {
                    case 0:
                        return getResources().getString(R.string.trending_list_title);

                    case 1:
                        return getResources().getString(R.string.search_title);

                    default:
                        return null;
                }
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                //Restore saved Fragment
                Fragment fragment = (Fragment) super.instantiateItem(container,position);
                mFragments[position] = fragment;

                return fragment;
            }

        });

        /*Setup TabLayout with ViewPager*/
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.fragment_gif_list_menu,menu);

        /*When the user types in a query in the searchView located in the actionbar the
        * query is then submitted to the SearchFragment that performs the query and displays it
        * in a separate list*/

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1); //Switch to SearchView
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mViewPager.setCurrentItem(1);
                searchView.clearFocus();
                return ((Searchable) mFragments[1]).performSearch(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
