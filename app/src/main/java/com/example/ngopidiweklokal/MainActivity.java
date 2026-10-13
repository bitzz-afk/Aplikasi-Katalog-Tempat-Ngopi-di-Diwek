package com.example.ngopidiweklokal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Tempat> masterList = new ArrayList<>();
    private ArrayList<Tempat> filteredList = new ArrayList<>();

    private Spinner comboTempat, spinnerJenis, spinnerHarga, spinnerRating, spinnerJamBuka;
    private Button btnPilih;

    private Switch switchMode;
    private LinearLayout mainLayout;
    private TextView txtTitleHome, txtLabelFilter, txtLabelPilih;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLat = 0.0, userLon = 0.0;

    private String selJenis = "Semua Jenis";
    private String selHarga = "Semua Harga";
    private String selRating = "Semua Rating";
    private String selJam = "Semua Jam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comboTempat = findViewById(R.id.comboTempat);
        spinnerJenis = findViewById(R.id.spinnerFasilitas);
        spinnerHarga = findViewById(R.id.spinnerHarga);
        spinnerRating = findViewById(R.id.spinnerRating);
        spinnerJamBuka = findViewById(R.id.spinnerJamBuka);
        btnPilih = findViewById(R.id.btnPilih);

        mainLayout = findViewById(R.id.mainLayout);
        switchMode = findViewById(R.id.switchMode);
        txtTitleHome = findViewById(R.id.txtTitleHome);
        txtLabelFilter = findViewById(R.id.txtLabelFilter);
        txtLabelPilih = findViewById(R.id.txtLabelPilih);

        isiDataBenar();
        initIntentGPSRealtime();
        setupSemuaSpinnerFilter();

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = comboTempat.getSelectedItemPosition();
                if (pos >= 0 && pos < filteredList.size()) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("FILTERED_LIST", filteredList);
                    intent.putExtra("CURRENT_INDEX", pos);
                    intent.putExtra("USER_LAT", userLat);
                    intent.putExtra("USER_LON", userLon);
                    intent.putExtra("DARK_MODE", switchMode.isChecked());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Tidak ada kedai kopi yang cocok dengan kombinasi filter!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#1C1A17"));
                    txtTitleHome.setTextColor(android.graphics.Color.parseColor("#D7CCC8"));
                    switchMode.setTextColor(android.graphics.Color.parseColor("#D7CCC8"));
                    txtLabelFilter.setTextColor(android.graphics.Color.parseColor("#A1887F"));
                    txtLabelPilih.setTextColor(android.graphics.Color.parseColor("#A1887F"));
                } else {
                    mainLayout.setBackgroundColor(android.graphics.Color.parseColor("#FDFBF7"));
                    txtTitleHome.setTextColor(android.graphics.Color.parseColor("#4E342E"));
                    switchMode.setTextColor(android.graphics.Color.parseColor("#5D4037"));
                    txtLabelFilter.setTextColor(android.graphics.Color.parseColor("#8D6E63"));
                    txtLabelPilih.setTextColor(android.graphics.Color.parseColor("#8D6E63"));
                }
            }
        });
    }

    private void setupSemuaSpinnerFilter() {
        final String[] arrJenis = {"Semua Jenis", "Warkop", "Cafe Tradisional", "Cafe Modern"};
        final String[] arrHarga = {"Semua Harga", "Ekonomis (bawah 15k)", "Standar (15k - 25k)", "Premium (atas 25k)"};
        final String[] arrRating = {"Semua Rating", "Tertinggi (4.5+ ★)", "Biasa (di bawah 4.5 ★)"};
        final String[] arrJam = {"Semua Jam", "Buka Reguler", "Buka 24 Jam"};

        ArrayAdapter<String> adJenis = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrJenis) {
            @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(android.graphics.Color.parseColor("#CC4E342E"));
                return tv;
            }
        };
        adJenis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenis.setAdapter(adJenis);

        ArrayAdapter<String> adHarga = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrHarga) {
            @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(android.graphics.Color.parseColor("#CC4E342E"));
                return tv;
            }
        };
        adHarga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHarga.setAdapter(adHarga);

        ArrayAdapter<String> adRating = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrRating) {
            @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(android.graphics.Color.parseColor("#CC4E342E"));
                return tv;
            }
        };
        adRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRating.setAdapter(adRating);

        ArrayAdapter<String> adJam = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrJam) {
            @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(android.graphics.Color.parseColor("#CC4E342E"));
                return tv;
            }
        };
        adJam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJamBuka.setAdapter(adJam);

        AdapterView.OnItemSelectedListener multiListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selJenis = spinnerJenis.getSelectedItem().toString();
                selHarga = spinnerHarga.getSelectedItem().toString();
                selRating = spinnerRating.getSelectedItem().toString();
                selJam = spinnerJamBuka.getSelectedItem().toString();
                jalankanMultiFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerJenis.setOnItemSelectedListener(multiListener);
        spinnerHarga.setOnItemSelectedListener(multiListener);
        spinnerRating.setOnItemSelectedListener(multiListener);
        spinnerJamBuka.setOnItemSelectedListener(multiListener);
    }

    private void jalankanMultiFilter() {
        filteredList.clear();
        ArrayList<String> namaSpinner = new ArrayList<>();

        for (Tempat t : masterList) {
            // Perbaikan pencocokan string jenis kategori agar toleran spasi/huruf besar-kecil
            String jenisData = t.getJenis().replace(" ", "").toLowerCase();
            String jenisTarget = selJenis.replace(" ", "").toLowerCase();
            boolean cocokJenis = selJenis.equals("Semua Jenis") || jenisData.contains(jenisTarget) || (selJenis.equals("Warkop") && jenisData.equals("warkop"));

            boolean cocokHarga = false;
            boolean cocokRating = false;
            boolean cocokJam = false;

            // Logika Deteksi Rentang Harga Maksimal
            String[] partHarga = t.getHarga().replace("Rp ", "").replace("k", "").split("-");
            int hgMax = 0;
            if (partHarga.length == 2) {
                hgMax = Integer.parseInt(partHarga[1].trim());
            } else {
                hgMax = Integer.parseInt(partHarga[0].trim());
            }

            if (selHarga.equals("Semua Harga")) {
                cocokHarga = true;
            } else if (selHarga.contains("Ekonomis")) {
                cocokHarga = hgMax <= 15;
            } else if (selHarga.contains("Standar")) {
                cocokHarga = hgMax > 15 && hgMax <= 25;
            } else if (selHarga.contains("Premium")) {
                cocokHarga = hgMax > 25;
            }

            // Logika Sensor Filter Rating Baru Standard Komparasi 4.5 ★
            double valRating = Double.parseDouble(t.getRating().replace(" ★", "").replace(",", "."));
            if (selRating.equals("Semua Rating")) {
                cocokRating = true;
            } else if (selRating.contains("Tertinggi")) {
                cocokRating = valRating >= 4.5;
            } else if (selRating.contains("Biasa")) {
                cocokRating = valRating < 4.5;
            }

            if (selJam.equals("Semua Jam")) {
                cocokJam = true;
            } else if (selJam.contains("24 Jam")) {
                cocokJam = t.getJamBuka().equalsIgnoreCase("24 Jam");
            } else if (selJam.contains("Reguler")) {
                cocokJam = !t.getJamBuka().equalsIgnoreCase("24 Jam");
            }

            if (cocokJenis && cocokHarga && cocokRating && cocokJam) {
                filteredList.add(t);
                namaSpinner.add(t.getNama());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaSpinner) {
            @NonNull @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(android.graphics.Color.parseColor("#CC4E342E"));
                return tv;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboTempat.setAdapter(adapter);
    }

    private void initIntentGPSRealtime() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userLat = location.getLatitude();
                    userLon = location.getLongitude();
                }
            }
        });
    }

    private void isiDataBenar() {
        masterList.clear();
        // DATABASE 15 DATA REVISI TOTAL (Sinkron 100% Antara Filter, Menu, Fasilitas, dan Deskripsi Asli Anda)
        masterList.add(new Tempat("Kayffa", "Keras, Diwek, Jombang", "Suasana asri khas rumah tradisional jawa jaman dulu yang sangat tenang.", "kayffa", "https://maps.app.goo.gl/b7UD29t6EA9p83RE6?g_st=ac", "Kopi Susu Kayffa, Kopi Tubruk Tradisional, Teh Wangi, Aneka Gorengan Hangat.", "Cafe Tradisional", -7.595, 112.231, "4.8 ★", "Rp 2k - 15k", "07.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Kedai Bambo", "Cukir, Diwek, Jombang", "Gazebo bambu alami yang sejuk, sangat cocok untuk tempat berkumpul keluarga.", "bambo", "https://maps.app.goo.gl/jumRSknfvzRutNPR9?g_st=ac", "Kopi Bambu Hitam, Wedang Jahe Keprek, STMJ Jawa, Tempe Mendoan.", "Cafe Tradisional", -7.585, 112.221, "4.4 ★", "Rp 1k - 25k", "07.00 - 22.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Lesehan, Gazebo"));
        masterList.add(new Tempat("Kuyaka", "Diwek, Jombang", "Tempat nongkrong berstruktur kayu klasik dengan area parkir yang sangat luas.", "kuyaka", "https://maps.app.goo.gl/raADZoBrmrvwFcgE8", "Kopi Kayu Klasik, Cokelat Hangat, Es Sirup, Camilan Tahu Crispy.", "Cafe Tradisional", -7.575, 112.211, "4.1 ★", "Rp 4k - 20k", "07.00 - 03.00", "WiFi, Toilet, Parkiran Luas, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Plantkringan", "Kayangan, Diwek, Jombang", "Perpaduan konsep angkringan rakyat modern dengan fasilitas colokan melimpah.", "plankringan", "https://maps.app.goo.gl/EctCzxyy7XuDrBur5?g_st=ac", "Kopi Joss Arang, Wedang Ronde, Sate Usus, Nasi Kucing Teri.", "Cafe Tradisional", -7.565, 112.201, "4.6 ★", "Rp 3k - 20k", "09.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Platarium", "Kayangan, Jombang", "Warkop modern berkonsep estetik minimalis dengan harga menu super terjangkau.", "platarium", "https://maps.app.goo.gl/QbJ4Bv7s2AF1kCbK8?g_st=aw", "Kopi Hitam Giling, Es Kopi Susu Creamy, Roti Bakar Cokelat, Kentang Goreng.", "Warkop", -7.555, 112.191, "5.0 ★", "Rp 2k - 15k", "08.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Doa Ibu", "Kwaron, Diwek, Jombang", "Warkop lesehan legendaris Kwaron yang menyediakan menu bakso urat spesial.", "doa_ibu", "https://maps.app.goo.gl/5YrShL88BRQ7j8AKA", "Bakso Super Urat Doa Ibu, Kopi Hitam Cangkir, Es Teh Manis, Mie Instan Telur.", "Warkop", -7.580, 112.215, "4.2 ★", "Rp 3k - 10k", "08.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Lesehan"));
        masterList.add(new Tempat("Kedai Ngopi On R", "Kayangan, Diwek, Jombang", "Cafe modern indoor ber-AC penuh, dilengkapi panggung hiburan live music.", "on_r", "https://maps.app.goo.gl/rtAzNQ9nCBLey5Pn7", "Espresso Blend, Cafe Latte, Chicken Steak, Iga Bakar Sambal Korek.", "Cafe Modern", -7.568, 112.205, "4.7 ★", "Rp 6k - 20k", "09.00 - 00.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Indoor, AC, Live Music"));
        masterList.add(new Tempat("Mojag Coffee", "Keras, Diwek, Jombang", "Spot ngopi masa kini anak muda Keras untuk mabar game online.", "mojag", "https://maps.app.goo.gl/icXPQabKVkPqaMUx5?g_st=ac", "Mojag Signature Aren, Taro Latte, Long Black Coffee, French Fries.", "Cafe Modern", -7.593, 112.233, "4.7 ★", "Rp 1-25k", "08.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Konoa Kaffee Co", "Bandung, Diwek, Jombang", "Cafe modern indoor ber-AC dingin dengan pemandangan hamparan sawah hijau asri.", "konoa", "https://maps.app.goo.gl/noApBP4W7DujUHb9A", "Konoa V60 Manual Brew, Avocado Coffee, Mocktail Segar, Croissant.", "Cafe Modern", -7.582, 112.235, "4.3 ★", "Rp 10k - 35k", "09.00 - 22.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Views Sawah, Indoor AC"));
        masterList.add(new Tempat("Kedai Meramu", "Balongbesuk, Diwek, Jombang", "Warkop racikan lokal andalan warga Balongbesuk untuk cangkrukan santai.", "meramu", "https://maps.app.goo.gl/etopmTmSiaDYuEvz9", "Kopi Hitam Meramu, Kopi Susu Lelet, Mie Rebus Udud, Aneka Kerupuk.", "Warkop", -7.562, 112.226, "4.7 ★", "Rp 4k - 15k", "07.00 - 01.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Jujugan Coffee", "Diwek, Jombang", "Warkop andalan 24 jam nonstop untuk tempat singgah istirahat kapan saja.", "jujugan", "https://maps.app.goo.gl/tgHkzpcbrKuLuLwc7", "Kopi Cangkir Ijo, STMJ Telur Bebek, Indomie Goreng Double, Pisang Goreng.", "Warkop", -7.571, 112.220, "4.4 ★", "Rp 1k - 15k", "24 Jam", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Kuwopi Coffee", "Kwaron, Diwek, Jombang", "Cafe bernuansa tradisional yang tenang, sangat pas untuk fokus nugas kuliah.", "kuwopi", "https://maps.app.goo.gl/ZZ2Pw9ZWfk9Y4mh88", "Kuwopi Filter, Kopi Susu Jahe, Tempe Bakar Sambal, Tahu Walik.", "Cafe Tradisional", -7.581, 112.213, "4.4 ★", "Rp 1k - 15k", "09.00 - 01.30", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Jawara Kedai Ndalem", "Sugihwaras, Bandung, Jombang", "Cafe modern berestetika tinggi yang dipadukan dengan kenyamanan lesehan santai.", "jawara_ndalem", "https://maps.app.goo.gl/7gZADEvwcVUxAgJTA", "Jawara Cappuccino, Matcha Latte, Ricebowl Chicken Teriyaki, Waffle.", "Cafe Modern", -7.589, 112.240, "4.9 ★", "Rp 1k - 30k", "09.00 - 00.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Lesehan, Estetik"));
        masterList.add(new Tempat("Lana Coffe", "Balongbesuk, Diwek, Jombang", "Cafe tradisional berkonsep interior bata ekspos yang hangat dan ramah.", "lana", "https://maps.app.goo.gl/wCQJmndEG62pVdQ96", "Lana Milk Coffee, Kopi Tubruk Gula Aren, Singkong Keju, Cireng Rujak.", "Cafe Tradisional", -7.561, 112.228, "4.7 ★", "Rp 1k - 25k", "10.00 - 23.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk"));
        masterList.add(new Tempat("Warpat Kopi Jombang", "Diwek, Jombang", "Cafe modern pusat hiburan anak muda yang menyediakan area Billiard dan PlayStation.", "warpat", "https://maps.app.goo.gl/1mnPtc4Vv7m2nXcL7", "Warpat Americano, Caramel Macchiato, Burger Sapi, Platter Snack Camilan.", "Cafe Modern", -7.570, 112.218, "4.8 ★", "Rp 1k - 50k", "09.00 - 02.00", "WiFi, Toilet, Parkiran, Colokan, Tempat Duduk, Billiard, PS"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initIntentGPSRealtime();
        }
    }
}