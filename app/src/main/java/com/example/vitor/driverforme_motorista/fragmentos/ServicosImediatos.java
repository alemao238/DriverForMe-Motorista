package com.example.vitor.driverforme_motorista.fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.adaptadores.ServicoAdaptador;
import com.example.vitor.driverforme_motorista.entidades.Servico;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosImediatos extends Fragment {

    private ListView listView;
    private ServicoAdaptador adapter;
    private ArrayList<Servico> servicosImediatos;
    private ArrayList<Servico> servicos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerServicosImediatos;
    private MotoristaEstatico me;
    public ServicosImediatos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_imediatos, container, false);

        servicosImediatos = new ArrayList<>();
        servicos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_servicos);
        me = new MotoristaEstatico();
        firebase = FirebaseEstatico.getFirebase();


        adapter = new ServicoAdaptador(getActivity(), servicos);
        listView.setAdapter(adapter);
        final Query qrServicosAgendados = firebase.child("servicosImediatosAbertos");

        valueEventListenerServicosImediatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.removeAll(servicosImediatos);
                servicosImediatos.clear();
                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    Log.i("Vamos ver", servico.toString());
                    servicosImediatos.add( servico );
                }
                servicos.addAll(servicosImediatos);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicosAgendados.addValueEventListener( valueEventListenerServicosImediatos );


        return view;
    }

}
