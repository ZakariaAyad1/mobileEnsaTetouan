/*salma*/
package com.example.ensatecertnotes.ui.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * Adapter for ViewPager2 to display semesters (S1-S6)
 */
public class SemestresPagerAdapter_etudiant extends FragmentStateAdapter {
    
    private static final int NUM_SEMESTERS = 6;
    private int etudiantId;

    public SemestresPagerAdapter_etudiant(@NonNull FragmentActivity fragmentActivity, int etudiantId) {
        super(fragmentActivity);
        this.etudiantId = etudiantId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Position 0 = S1, Position 1 = S2, etc.
        return SemestreFragment_etudiant.newInstance(etudiantId, position + 1);
    }

    @Override
    public int getItemCount() {
        return NUM_SEMESTERS;
    }
}
/*salma*/
