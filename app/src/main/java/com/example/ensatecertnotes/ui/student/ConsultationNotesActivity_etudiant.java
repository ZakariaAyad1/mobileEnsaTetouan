/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ConsultationNotesActivity_etudiant extends AppCompatActivity {
    
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private SemestresPagerAdapter_etudiant pagerAdapter;
    
    private SessionManager sessionManager;
    private int etudiantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_notes_etudiant);

        // Initialize session manager
        sessionManager = new SessionManager(this);
        etudiantId = sessionManager.getEtudiantId();

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mes Résultats");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        tabLayout = findViewById(R.id.tab_layout_semesters);
        viewPager = findViewById(R.id.view_pager_semesters);

        // Set up ViewPager with adapter
        pagerAdapter = new SemestresPagerAdapter_etudiant(this, etudiantId);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
            new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(TabLayout.Tab tab, int position) {
                    tab.setText("S" + (position + 1));
                }
            }
        ).attach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
/*salma*/
