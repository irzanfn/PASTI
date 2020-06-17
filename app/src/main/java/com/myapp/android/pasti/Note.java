package com.myapp.android.pasti;

public class Note {
    private String nama;
    private String nim;

    public Note() {

    }

    public Note(String nama, String judul, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }
}
