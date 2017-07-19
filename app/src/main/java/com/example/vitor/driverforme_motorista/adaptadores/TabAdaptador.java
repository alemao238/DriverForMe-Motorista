package com.example.vitor.driverforme_motorista.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vitor.driverforme_motorista.fragmentos.MeusServicos;
import com.example.vitor.driverforme_motorista.fragmentos.ServicosAgendados;
import com.example.vitor.driverforme_motorista.fragmentos.ServicosFinalizados;
import com.example.vitor.driverforme_motorista.fragmentos.ServicosImediatos;


/**
 * Created by Vitor on 04/06/2017.
 */

public class TabAdaptador extends FragmentStatePagerAdapter {
    private String[] abas = {"Meus Serviços", "Serviços Agendados", "Serviços Imediatos", "Finalizados"};

    public TabAdaptador(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MeusServicos();
                break;
            case 1:
                fragment = new ServicosAgendados();
                break;
            case 2:
                fragment = new ServicosImediatos();
                break;
            case 3:
                fragment = new ServicosFinalizados();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return abas.length;
    }

    public CharSequence getPageTitle(int position) {

        return abas[position];
    }
}
