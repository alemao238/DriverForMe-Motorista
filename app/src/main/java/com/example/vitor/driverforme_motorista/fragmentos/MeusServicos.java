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
public class MeusServicos extends Fragment {

    private ListView listView;
    private ServicoAdaptador adapter;
    private ArrayList<Servico> servicosEmAtendimento;
    private ArrayList<Servico> servicos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerServicosEmAtendimento;
    private MotoristaEstatico me;
    public MeusServicos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meus_servicos, container, false);

        servicosEmAtendimento = new ArrayList<>();
        servicos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_servicos);
        me = new MotoristaEstatico();
        firebase = FirebaseEstatico.getFirebase();


        adapter = new ServicoAdaptador(getActivity(), servicos);
        listView.setAdapter(adapter);
        final Query qrServicosEmAtendimento = firebase.child("servicosEmAtendimento").orderByChild("motorista").equalTo(me.getMotorista().getEmail());

        valueEventListenerServicosEmAtendimento = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.removeAll(servicosEmAtendimento);
                servicosEmAtendimento.clear();
                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    Log.i("Vamos ver", servico.toString());
                    servicosEmAtendimento.add( servico );
                }
                servicos.addAll(servicosEmAtendimento);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicosEmAtendimento.addValueEventListener( valueEventListenerServicosEmAtendimento );


        return view;
    }

}
