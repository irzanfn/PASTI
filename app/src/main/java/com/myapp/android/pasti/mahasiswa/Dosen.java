package com.myapp.android.pasti.mahasiswa;

public class Dosen {
    private String nameDosen;
    private String idDosen;

    public Dosen(String nameDosen, String id) {
        this.nameDosen = nameDosen;
        this.idDosen = idDosen;
    }

    public String getNameDosen() {
        return nameDosen;
    }

    public void setNameDosen(String nameDosen) {
        this.nameDosen = nameDosen;
    }

    public String getIdDosen() {
        return idDosen;
    }

    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
    }

    @Override
    public String toString() {
        return nameDosen;
    }
}
