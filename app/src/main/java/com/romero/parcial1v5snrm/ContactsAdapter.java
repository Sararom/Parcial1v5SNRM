package com.romero.parcial1v5snrm;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private ArrayList<Contacts> contacts;
    private static boolean fav = false;
    private Dialog myDialog;
    private Context context;
    private int STORAGE_PERMISSION_CODE=2;
    private ImageButton dialog_share_btn;
    private ImageButton dialog_call_btn;

    public ContactsAdapter(ArrayList<Contacts> contacts, Context context) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        final ContactViewHolder vHolder = new ContactViewHolder(v);

        //Inicializacion del dialogo

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.dialog_contact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView dialog_name_tv = (TextView) myDialog.findViewById(R.id.dialog_name_id);
                TextView dialog_phone_tv = (TextView) myDialog.findViewById(R.id.dialog_phone_id);
                ImageView dialog_img_iv = (ImageView) myDialog.findViewById(R.id.dialog_img_id);

                dialog_name_tv.setText(contacts.get(vHolder.getAdapterPosition()).getName());
                dialog_phone_tv.setText(contacts.get(vHolder.getAdapterPosition()).getPhone());
                dialog_img_iv.setImageResource(contacts.get(vHolder.getAdapterPosition()).getPhoto());
                myDialog.show();

                ImageButton dialog_share_btn = (ImageButton) myDialog.findViewById(R.id.share_btn);
                ImageButton dialog_call_btn = (ImageButton) myDialog.findViewById(R.id.call_dialog_btn);

                dialog_share_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sendIntent = null, chooser =null;
                        sendIntent= new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/*");
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Nombre: "+(contacts.get(vHolder.getAdapterPosition()).getName())+
                                        "\nTeléfono: "+(contacts.get(vHolder.getAdapterPosition()).getPhone()));

                        chooser=Intent.createChooser(sendIntent,"Share Contact");
                        context.startActivity(chooser);

                    }
                });

                dialog_call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Primero se asegura de que tenga los permisos para llamar y si no los tiene, los pide
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                            requestStoragePermission();
                        }else {
                            String number = contacts.get(vHolder.getAdapterPosition()).getPhone();
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(("tel:" + number)));
                            context.startActivity(intent);
                        }
                    }
                });
            }

        });


        return (vHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position) {

        holder.tv_name.setText(contacts.get(position).getName());
        holder.tv_phone.setText(contacts.get(position).getPhone());
        holder.img.setImageResource(contacts.get(position).getPhoto());


        if (contacts.get(position).isFav()) {
            holder.favBtn.setImageResource(R.drawable.likefull);
        } else {
            holder.favBtn.setImageResource(R.drawable.likeempty);
        }

        //Se crea un On Click listener para cambiar la imagen del boton fav
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!contacts.get(position).isFav()) {
                    holder.favBtn.setImageResource(R.drawable.likefull);
                    contacts.get(position).setFav(!contacts.get(position).isFav());
                    ((MainActivity) context).addFavourite(contacts.get(position));

                } else {
                    holder.favBtn.setImageResource(R.drawable.likeempty);
                    contacts.get(position).setFav(!contacts.get(position).isFav());
                    ((MainActivity) context).eraseFavourite(contacts.get(position).getName());
                }
            }
        });

        //Darle funcionalidad al boton de llamar
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Primero se asegura de que tenga los permisos para llamar y si no los tiene, los pide
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    
                    requestStoragePermission();
                }else {
                    String number = contacts.get(position).getPhone();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(("tel:" + number)));
                    context.startActivity(intent);
                }

            }
        });
    }



    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale((MainActivity)context,Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder((MainActivity)context)
            .setTitle("Permission needed")
            .setMessage("This permission is needed to start a call")
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((MainActivity)context,new String[]{Manifest.permission.CALL_PHONE},STORAGE_PERMISSION_CODE);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions((MainActivity)context,new String[]{Manifest.permission.CALL_PHONE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private CardView cardV;
        private LinearLayout item_contact;
        private TextView tv_name;
        private TextView tv_phone;
        private ImageView img;
        private ImageButton favBtn;
        private ImageButton callBtn;

        public ContactViewHolder(View itemView) {
            super(itemView);

            cardV = (CardView) itemView.findViewById(R.id.card_view);
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            tv_name = (TextView) itemView.findViewById(R.id.contact_name);
            tv_phone = (TextView) itemView.findViewById(R.id.phone_number);
            img = (ImageView) itemView.findViewById(R.id.img_contact);
            favBtn = (ImageButton) itemView.findViewById(R.id.favBtn);
            callBtn = (ImageButton) itemView.findViewById(R.id.callBtn);

        }
    }

    public void setTrue() {
        fav = true;
    }
    public void setFalse() {
        fav = false;
    }
    public boolean isOnFavS() {
        return fav;
    }

    public void filterList(ArrayList<Contacts> filteredList) {
        contacts = filteredList;
        notifyDataSetChanged();
    }

}
