package com.example.vitor.driverforme_motorista.gui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.entidades.Motorista;
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;

public class Teste extends AppCompatActivity {
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DriverForMe");
        setSupportActionBar(toolbar);


        Motorista m = MotoristaEstatico.getMotorista();

        Log.i("Motorista", m.toString());
    }
}
