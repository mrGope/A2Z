package com.example.a2z;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPaggerAdapter extends FragmentStateAdapter {

    public ViewPaggerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {   case 0:return new GiveAttendanceFragment();
            case 1:return new ShowAttendanceFragment();
            default:return new GiveAttendanceFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
