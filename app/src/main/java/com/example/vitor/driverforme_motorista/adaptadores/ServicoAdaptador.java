package com.example.vitor.driverforme_motorista.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vitor.driverforme_motorista.R;
import com.example.vitor.driverforme_motorista.entidades.Servico;

import java.util.ArrayList;


public class ServicoAdaptador extends ArrayAdapter<Servico> {

    private ArrayList<Servico> servicos;
    private Context context;

    public ServicoAdaptador(Context c, ArrayList<Servico> objects) {
        super(c, 0, objects);
        this.servicos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if( servicos != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.modelo_servico, parent, false);

            // recupera elemento para exibição
            TextView titulo = (TextView) view.findViewById(R.id.tv_titulo);
            TextView informacoes = (TextView) view.findViewById(R.id.tv_informacoes);
            Servico servico = servicos.get( position );
            titulo.setText( servico.getTipo());
            informacoes.setText( servico.toString() );

        }

        return view;

    }
}
