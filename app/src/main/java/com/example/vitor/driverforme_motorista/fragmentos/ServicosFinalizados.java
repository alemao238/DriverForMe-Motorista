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
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosFinalizados extends Fragment {
    private ListView listView;
    private ServicoAdaptador adapter;
    private ArrayList<Servico> servicos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerServicosFinalizados;
    private MotoristaEstatico ce;
    public ServicosFinalizados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_finalizados, container, false);

        servicos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_servicos);
        ce = new MotoristaEstatico();
        firebase = FirebaseEstatico.getFirebase();

        adapter = new ServicoAdaptador(getActivity(), servicos);
        listView.setAdapter(adapter);
        final Query qrServicos = firebase.child("servicosFinalizados").orderByChild("motorista").equalTo(ce.getMotorista().getEmail());

        valueEventListenerServicosFinalizados = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.clear();
                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    servicos.add( servico );
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicos.addValueEventListener( valueEventListenerServicosFinalizados );


        return view;
    }

}
