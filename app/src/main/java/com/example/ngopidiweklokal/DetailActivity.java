package com.example.ngopidiweklokal;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView lblNama, lblLokasi, txtDeskripsi, txtMenu, txtExtraInfo, txtFasilitas;
    private ImageView lblGambar;
    private Button btnBack, btnNext, btnPrev;
    private double userLat, userLon;

    private ScrollView detailScrollView;
    private boolean isDarkModeActive = false;

    private ArrayList<Tempat> filteredList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        lblNama = findViewById(R.id.lblNama);
        lblLokasi = findViewById(R.id.lblLokasi);
        txtDeskripsi = findViewById(R.id.txtDeskripsi);
        txtMenu = findViewById(R.id.txtMenu);
        txtExtraInfo = findViewById(R.id.txtExtraInfo);
        txtFasilitas = findViewById(R.id.txtFasilitas);
        lblGambar = findViewById(R.id.lblGambar);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        detailScrollView = (ScrollView) lblNama.getParent().getParent();

        filteredList = (ArrayList<Tempat>) getIntent().getSerializableExtra("FILTERED_LIST");
        currentIndex = getIntent().getIntExtra("CURRENT_INDEX", 0);

        userLat = getIntent().getDoubleExtra("USER_LAT", 0.0);
        userLon = getIntent().getDoubleExtra("USER_LON", 0.0);
        isDarkModeActive = getIntent().getBooleanExtra("DARK_MODE", false);

        if (isDarkModeActive) {
            terapkanDarkModeDetail();
        }

        tampilkanDataCafe();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList != null && currentIndex < filteredList.size() - 1) {
                    currentIndex++;
                    tampilkanDataCafe();
                } else {
                    Toast.makeText(DetailActivity.this, "Sudah mencapai kedai kopi terakhir hasil filter!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList != null && currentIndex > 0) {
                    currentIndex--;
                    tampilkanDataCafe();
                } else {
                    Toast.makeText(DetailActivity.this, "Sudah di kedai kopi urutan pertama!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void tampilkanDataCafe() {
        if (filteredList == null || filteredList.isEmpty()) return;

        Tempat cafe = filteredList.get(currentIndex);

        lblNama.setText(cafe.getNama());
        txtDeskripsi.setText(cafe.getDeskripsi());
        txtMenu.setText(cafe.getMenu());

        txtExtraInfo.setText("⭐ " + cafe.getRating() + "  |  💰 Rentang: " + cafe.getHarga() + "  |  🕒 " + cafe.getJamBuka());
        txtFasilitas.setText("⚡ Fasilitas: " + cafe.getFasilitas());

        double jarak = hitungJarak(userLat, userLon, cafe.getLatitude(), cafe.getLongitude());
        lblLokasi.setText("📍 " + cafe.getLokasi() + " (" + String.format("%.2f", jarak) + " Km dari Anda) - Klik untuk rute");

        final int resId = getResources().getIdentifier(cafe.getImageResName(), "drawable", getPackageName());
        if (resId != 0) {
            lblGambar.setImageResource(resId);
        } else {
            lblGambar.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        lblLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cafe.getLinkGmaps()));
                startActivity(mapIntent);
            }
        });

        lblGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanFullscreenGambar(resId);
            }
        });
    }

    private void terapkanDarkModeDetail() {
        detailScrollView.setBackgroundColor(android.graphics.Color.parseColor("#1C1A17"));
        lblNama.setTextColor(android.graphics.Color.parseColor("#D7CCC8"));
        txtMenu.setTextColor(android.graphics.Color.parseColor("#E0D4CE"));
        txtDeskripsi.setTextColor(android.graphics.Color.parseColor("#E0D4CE"));
        txtExtraInfo.setTextColor(android.graphics.Color.parseColor("#D7CCC8"));
        txtFasilitas.setTextColor(android.graphics.Color.parseColor("#BCAAA4"));
    }

    private void tampilkanFullscreenGambar(int resId) {
        if (resId == 0) return;
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        dialog.setContentView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private double hitungJarak(double lat1, double lon1, double lat2, double lon2) {
        if (lat1 == 0.0 && lon1 == 0.0) return 0.0;
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double aStandard = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(aStandard), Math.sqrt(1 - aStandard));
        return earthRadius * c;
    }
}