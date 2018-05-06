package com.romero.parcial1v5snrm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
    Dialog myDialog;
    private Context context;

    public ContactsAdapter(ArrayList<Contacts> contacts, Context context){
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        final ContactViewHolder vHolder = new ContactViewHolder(v);

        //Inicializacion del dialogo

        myDialog= new Dialog(context);
        myDialog.setContentView(R.layout.dialog_contact);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.item_contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                TextView dialog_name_tv = (TextView) myDialog.findViewById(R.id.dialog_name_id);
                TextView dialog_phone_tv = (TextView) myDialog.findViewById(R.id.dialog_phone_id);
                ImageView dialog_img_iv = (ImageView) myDialog.findViewById(R.id.dialog_img_id);
                dialog_name_tv.setText(contacts.get(vHolder.getAdapterPosition()).getName());
                dialog_phone_tv.setText(contacts.get(vHolder.getAdapterPosition()).getPhone());
                dialog_img_iv.setImageResource(contacts.get(vHolder.getAdapterPosition()).getPhoto());

                Toast.makeText(context,"Test Click "+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                myDialog.show();
            }
        });



        return (vHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactViewHolder holder, final int position) {

        holder.tv_name.setText(contacts.get(position).getName());
        holder.tv_phone.setText(contacts.get(position).getPhone());
        holder.img.setImageResource(contacts.get(position).getPhoto());


        if(contacts.get(position).isFav()){
            holder.favBtn.setImageResource(R.drawable.likefull);
        }else{
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

        public ContactViewHolder(View itemView) {
            super(itemView);

            cardV = (CardView) itemView.findViewById(R.id.card_view);
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            tv_name = (TextView) itemView.findViewById(R.id.contact_name);
            tv_phone = (TextView) itemView.findViewById(R.id.phone_number);
            img = (ImageView) itemView.findViewById(R.id.img_contact);
            favBtn = (ImageButton) itemView.findViewById(R.id.favBtn);

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

}
