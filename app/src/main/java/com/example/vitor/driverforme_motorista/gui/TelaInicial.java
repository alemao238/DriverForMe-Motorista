package com.example.vitor.driverforme_motorista.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.adaptadores.TabAdaptador;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.google.firebase.auth.FirebaseAuth;

public class TelaInicial extends AppCompatActivity {
    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        Log.i("Vamos", "Criou");
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DriverForMe");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //Configurar adapter
        TabAdaptador tabAdapter = new TabAdaptador( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                Intent intent = new Intent(TelaInicial.this, TelaLogin.class);
                startActivity(intent);
                FirebaseAuth auth = FirebaseEstatico.getFirebaseAutenticacao();
                auth.signOut();
                return true;
            case R.id.item_editar:
                intent = new Intent(TelaInicial.this, Cadastro.class);
                startActivity(intent);
                return true;
            case R.id.item_excluir:
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicial.this);
                builder.setTitle("Exclusao");
                builder.setMessage("Você deseja excluir seu perfil?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TelaInicial.this, "Seu perfil foi desativado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TelaInicial.this, TelaLogin.class);
                        startActivity(intent);
                        FirebaseAuth auth = FirebaseEstatico.getFirebaseAutenticacao();
                        auth.signOut();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
