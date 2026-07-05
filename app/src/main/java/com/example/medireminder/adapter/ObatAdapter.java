package com.example.medireminder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medireminder.R;
import com.example.medireminder.database.DatabaseHelper;
import com.example.medireminder.model.Obat;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ObatAdapter extends RecyclerView.Adapter<ObatAdapter.ViewHolder> {

    private Context context;
    private List<Obat> listObat;
    private DatabaseHelper databaseHelper;

    public ObatAdapter(Context context, List<Obat> listObat) {
        this.context = context;
        this.listObat = listObat;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_obat, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Obat obat = listObat.get(position);

        holder.txtNamaObat.setText(obat.getNamaObat());

        holder.txtDosis.setText("Dosis : " + obat.getDosis());

        holder.txtJam.setText("Jam : " + obat.getJamMinum());

        holder.txtStatus.setText(obat.getStatus());

        // Cek apakah telat
        if (obat.getStatus().equals("Belum diminum")) {
            if (isTelat(obat.getJamMinum())) {
                holder.txtStatus.setText("TELAT!");
                holder.txtStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.txtStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            }
        } else {
            holder.txtStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        if (obat.getStatus().equals("Sudah diminum")) {

            holder.btnSelesai.setVisibility(View.GONE);

        }

        holder.btnSelesai.setOnClickListener(v -> {

            boolean berhasil =
                    databaseHelper.updateStatusObat(obat.getId());

            if (berhasil) {

                Toast.makeText(
                        context,
                        "Status berhasil diubah",
                        Toast.LENGTH_SHORT
                ).show();

                listObat.remove(position);

                notifyItemRemoved(position);

                notifyItemRangeChanged(position, listObat.size());

            }

        });

        holder.btnHapus.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus obat ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {

                        boolean hasil =
                                databaseHelper.deleteObat(obat.getId());

                        if (hasil) {

                            listObat.remove(position);

                            notifyItemRemoved(position);

                            notifyItemRangeChanged(position, listObat.size());

                            Toast.makeText(
                                    context,
                                    "Data berhasil dihapus",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    })
                    .setNegativeButton("Tidak", null)
                    .show();

        });

    }

    @Override
    public int getItemCount() {
        return listObat.size();
    }

    private boolean isTelat(String jamMinum) {
        try {
            String[] parts = jamMinum.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            java.util.Calendar now = java.util.Calendar.getInstance();
            int currentHour = now.get(java.util.Calendar.HOUR_OF_DAY);
            int currentMinute = now.get(java.util.Calendar.MINUTE);

            if (currentHour > hour) return true;
            if (currentHour == hour && currentMinute > minute) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNamaObat;
        TextView txtDosis;
        TextView txtJam;
        TextView txtStatus;

        MaterialButton btnSelesai;
        MaterialButton btnHapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNamaObat = itemView.findViewById(R.id.txtNamaObat);
            txtDosis = itemView.findViewById(R.id.txtDosis);
            txtJam = itemView.findViewById(R.id.txtJam);
            txtStatus = itemView.findViewById(R.id.txtStatus);

            btnSelesai = itemView.findViewById(R.id.btnSelesai);
            btnHapus = itemView.findViewById(R.id.btnHapus);

        }

    }

}