package com.example.medireminder.model;

public class Obat {

    private int id;
    private int idUser;
    private String namaObat;
    private String dosis;
    private String jamMinum;
    private String status;

    public Obat() {
    }

    public Obat(int id, int idUser, String namaObat, String dosis, String jamMinum, String status) {
        this.id = id;
        this.idUser = idUser;
        this.namaObat = namaObat;
        this.dosis = dosis;
        this.jamMinum = jamMinum;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getJamMinum() {
        return jamMinum;
    }

    public void setJamMinum(String jamMinum) {
        this.jamMinum = jamMinum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}