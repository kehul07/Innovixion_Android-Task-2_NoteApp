package com.kehuldroid.noteapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    List<NoteModel> noteModelList = new ArrayList<>();
    private PopupMenu popupMenu;

    public NoteAdapter(List<NoteModel> noteModelList) {
        this.noteModelList = noteModelList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NoteModel nm = noteModelList.get(position);
        holder.title.setText(nm.getTitle());
        holder.content.setText(nm.getContent());
        holder.timeline.setText("Last Edited: "+nm.getTimeline());

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });

        holder.pinImg.setImageResource(nm.isPinned() ? R.drawable.baseline_push_pin_24 : 0);


    }

    @Override
    public int getItemCount() {
        return noteModelList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title,content,timeline;
        ImageView check , pinImg;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            timeline = itemView.findViewById(R.id.timeline);
            check = itemView.findViewById(R.id.check);
            pinImg = itemView.findViewById(R.id.pinImg);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = noteModelList.get(getAdapterPosition()).getId();
                    Intent i = new Intent(itemView.getContext(),EditNoteActivity.class);
                    i.putExtra("ID",id);
                    itemView.getContext().startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                AlertDialog alertDialog;
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.custom_dialouge, null);
                    builder.setView(dialogView);

                    Button buttonYes = dialogView.findViewById(R.id.dialog_button_yes);
                    Button buttonNo = dialogView.findViewById(R.id.dialog_button_no);

                    buttonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper db = new DatabaseHelper(itemView.getContext());
                            db.deleteNoteById(noteModelList.get(getAdapterPosition()).getId());
                            noteModelList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            alertDialog.dismiss();
                        }
                    });

                    buttonNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    });
                     alertDialog = builder.create();
                    alertDialog.show();
                    return  true;
                }
            });
        }
    }
    private void showPopupMenu(View view, int position) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenuStyle);
        popupMenu = new PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_pin) {
                    pinTask(position);
                    return true;
                } else if (id == R.id.action_delete) {
                    deleteTask(view,position);
                    return  true;
                }
                return  true;
            }

        });
        popupMenu.show();
        Menu menu = popupMenu.getMenu();
        MenuItem pinItem = menu.findItem(R.id.action_pin);
        pinItem.setTitle(noteModelList.get(position).isPinned() ? "Unpin" : "Pin");
    }



    private void deleteTask(View itemView,int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.custom_dialouge, null);
        builder.setView(dialogView);

        Button buttonYes = dialogView.findViewById(R.id.dialog_button_yes);
        Button buttonNo = dialogView.findViewById(R.id.dialog_button_no);

        AlertDialog alertDialog = builder.create();

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(itemView.getContext());
                db.deleteNoteById(noteModelList.get(pos).getId());
                noteModelList.remove(pos);
                notifyItemRemoved(pos);
                alertDialog.dismiss();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void pinTask(int position) {
        NoteModel note = noteModelList.get(position);
        note.setPinned(!note.isPinned());
        sortTasks();
        notifyDataSetChanged();

    }

    private void sortTasks() {
        Collections.sort(noteModelList, new Comparator<NoteModel>() {
            @Override
            public int compare(NoteModel o1, NoteModel o2) {
                return Boolean.compare(o2.isPinned(), o1.isPinned());
            }
        });
    }


}


