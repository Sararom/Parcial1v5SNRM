package com.romero.parcial1v5snrm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE=23;
    RecyclerView recyclerView;
    EditText nameInput;
    EditText phoneInput;
    ContactsAdapter adapter;
    ArrayList<Contacts> contacts,favorites;
    LinearLayoutManager lManager;
    Button contactBtn, favoritesBtn, searchBtn;
    Dialog myDialog;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts =new ArrayList<>();
        favorites=new ArrayList<>();

        contactBtn=findViewById(R.id.contacts_btn);
        favoritesBtn=findViewById(R.id.favorites_btn);
        searchBtn=findViewById(R.id.search_btn);

        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);

        //fillContacts();

        adapter =new ContactsAdapter(favorites,this);
        recyclerView.setAdapter(adapter);

        //dialog
        myDialog= new Dialog(MainActivity.this);
        myDialog.setContentView(R.layout.input);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Objetos del add contacto
        nameInput = (EditText) myDialog.findViewById(R.id.contact_name_send);
        phoneInput = (EditText) myDialog.findViewById(R.id.contact_phone_send);

        //boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.show();
            }
        });

    }

    private void addContact(Contacts contactoRe) {

        contacts.add(0,contactoRe);

    }
    private void fillContacts() {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
            while (phones.moveToNext())
            {
                Contacts contactCase = new Contacts();
                contactCase.setName(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                contactCase.setPhone(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                contactCase.setFav(false);
                contactCase.setPhoto(R.drawable.contact_pp_pop);
                contacts.add(contactCase);
                //adapter.notifyDataSetChanged();

            }
            phones.close();
        //contacts.add(new Contacts("Desgraciado 1","22222222",R.drawable.contact_pp_pop,false));

    }

    public void addFavourite(Contacts contact) {
        favorites.add(contact);
    }

    public void eraseFavourite(String name) {
        int counter=0;
        for (Contacts contacts : favorites){
            if (contacts.getName()== name){
                break;
            }

            counter++;
        }

        favorites.remove(counter);

        if (adapter.isOnFavS()){
            adapter = new ContactsAdapter(favorites,this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void contactsBtnAction(View v){
        //CAMBIO DE PANTALLA PERO PRIMERO SE VERIFICA SI TIENE PERMISOS PARA ACCEDER A LOS CONTACTOS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }else {
            //Se
            if (count==0){
            fillContacts();
            count++;}
            adapter.setFalse();
            contactBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            favoritesBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            adapter = new ContactsAdapter(contacts,v.getContext());
            recyclerView.setAdapter(adapter);
        }

    }
    public void favouritesBtnAction(View v){
        adapter.setTrue();
        favoritesBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        contactBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        adapter = new ContactsAdapter(favorites,v.getContext());
        recyclerView.setAdapter(adapter);
    }

    //Esta clase maneja los datos del dialogo creado
    public void boton(View view) {
        Contacts contactoRecibido = new Contacts();

        contactoRecibido.setName(nameInput.getText().toString());
        contactoRecibido.setPhone(phoneInput.getText().toString());

        contactoRecibido.setPhoto(R.drawable.contact_pp_pop);
        contactoRecibido.setFav(false);

        addContact(contactoRecibido);
        //Se tiene que el adapter no este en fevoritos pues solo nitifica cambios si se esta en la ventana de contactos
        if(!adapter.isOnFavS()){
            adapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
            }
        myDialog.dismiss();
        nameInput.setText("");
        phoneInput.setText("");
    }

    //metodos para verificar si tiene los permisos y para pedirlos
    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to star a call")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
