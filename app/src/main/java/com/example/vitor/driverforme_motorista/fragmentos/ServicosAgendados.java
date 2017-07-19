package com.example.vitor.driverforme_motorista.fragmentos;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.adaptadores.ServicoAdaptador;
import com.example.vitor.driverforme_motorista.entidades.Servico;
import com.example.vitor.driverforme_motorista.estaticos.FirebaseEstatico;
import com.example.vitor.driverforme_motorista.estaticos.MotoristaEstatico;
import com.example.vitor.driverforme_motorista.gui.TelaInicial;
import com.example.vitor.driverforme_motorista.gui.TelaLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosAgendados extends Fragment {

    private ListView listView;
    private ServicoAdaptador adapter;
    private ArrayList<Servico> servicosAgendados;
    private ArrayList<Servico> servicos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerServicosAgendados;
    private MotoristaEstatico me;
    public ServicosAgendados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_agendados, container, false);

        servicosAgendados = new ArrayList<>();
        servicos = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.lv_servicos);
        me = new MotoristaEstatico();
        firebase = FirebaseEstatico.getFirebase();


        adapter = new ServicoAdaptador(getActivity(), servicos);
        listView.setAdapter(adapter);
        final Query qrServicosAgendados = firebase.child("servicosAgendadosAbertos");

        valueEventListenerServicosAgendados = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                servicos.removeAll(servicosAgendados);
                servicosAgendados.clear();
                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Servico servico = dados.getValue( Servico.class );
                    Log.i("Vamos ver", servico.toString());
                    servicosAgendados.add( servico );
                }
                servicos.addAll(servicosAgendados);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        qrServicosAgendados.addValueEventListener( valueEventListenerServicosAgendados );


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int aux = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Atendimento");
                builder.setMessage("Você deseja atender esse pedido?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "O cliente lhe espera", Toast.LENGTH_LONG).show();
                        Log.i("Id do ser", servicos.get(aux).getId());
                        firebase.child("servicosAgendadosAbertos").child(servicos.get(aux).getId()).removeValue();
                        servicos.get(aux).setMotorista(MotoristaEstatico.getMotorista().getEmail());
                        firebase.child("servicosEmAtendimento").child(servicos.get(aux).getId()).setValue(servicos.get(aux));
                        Log.i("Servico", servicos.get(aux).toString());
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
            }
        });

        return view;
    }

}
