package com.example.vitor.driverforme_motorista.estaticos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vitor on 25/05/17.
 */

public class FirebaseEstatico {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){

        if( referenciaFirebase==null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){

        if( autenticacao==null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
