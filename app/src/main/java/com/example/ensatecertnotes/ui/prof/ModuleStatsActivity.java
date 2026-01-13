package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.NoteDao;

public class ModuleStatsActivity extends AppCompatActivity {

    private NoteDao noteDao;
    private int moduleId;
    private String moduleName;

    private TextView tvTitle;
    private TextView tvAvg, tvMin, tvMax;
    private TextView tvVal, tvNonVal, tvRattr;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_stats);

        noteDao = new NoteDao(this);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");

        if (moduleId == -1) {
            finish();
            return;
        }

        tvTitle = findViewById(R.id.tv_stats_title);
        tvAvg = findViewById(R.id.tv_stats_average);
        tvMin = findViewById(R.id.tv_stats_min);
        tvMax = findViewById(R.id.tv_stats_max);
        tvVal = findViewById(R.id.tv_stats_validated);
        tvNonVal = findViewById(R.id.tv_stats_non_validated);
        tvRattr = findViewById(R.id.tv_stats_rattrapage);
        btnBack = findViewById(R.id.btn_back);

        tvTitle.setText("Statistiques : " + moduleName);

        loadStats();

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadStats() {
        // Fetch stats from DAO
        double avg = noteDao.getAverageGrade(moduleId);
        double min = noteDao.getMinGrade(moduleId);
        double max = noteDao.getMaxGrade(moduleId);

        int countVal = noteDao.getCountByStatus(moduleId, "VALIDE");
        int countNonVal = noteDao.getCountByStatus(moduleId, "NON_VALIDE");
        int countRattr = noteDao.getCountByStatus(moduleId, "RATTRAPAGE"); // Assuming RATTRAPAGE exists or logic
                                                                           // handles it

        tvAvg.setText(String.format(java.util.Locale.US, "%.2f / 20", avg));
        tvMin.setText(String.format(java.util.Locale.US, "%.2f", min));
        tvMax.setText(String.format(java.util.Locale.US, "%.2f", max));

        tvVal.setText(String.valueOf(countVal));
        tvNonVal.setText(String.valueOf(countNonVal));
        tvRattr.setText(String.valueOf(countRattr));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }
}
