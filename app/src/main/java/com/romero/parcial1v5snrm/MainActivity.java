package com.romero.parcial1v5snrm;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ContactsAdapter adapter;
    ArrayList<Contacts> contacts,favorites;
    LinearLayoutManager lManager;
    Button contactBtn, favoritesBtn, searchBtn;

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

        fillContacts();

        adapter =new ContactsAdapter(contacts,this);
        recyclerView.setAdapter(adapter);

        //boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void fillContacts() {
        contacts.add(new Contacts("Desgraciado 1","22222222",R.drawable.contact_pp_pop,false));
        contacts.add(new Contacts("Desgraciado 2","88888888",R.drawable.contact_pp_pop,false));
        contacts.add(new Contacts("Desgraciado 3","77777777",R.drawable.contact_pp_pop,false));
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
        adapter.setFalse();
        contactBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        favoritesBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        adapter = new ContactsAdapter(contacts,v.getContext());
        recyclerView.setAdapter(adapter);
    }
    public void favouritesBtnAction(View v){
        adapter.setTrue();
        favoritesBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        contactBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        searchBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        adapter = new ContactsAdapter(favorites,v.getContext());
        recyclerView.setAdapter(adapter);
    }

}
