package com.myapp.android.pasti.mahasiswa;

public class FAQ {

    private String title;
    private String isi;
    private boolean expanded;

    public FAQ(String title, String isi) {
        this.title = title;
        this.isi = isi;
        this.expanded = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getIsi() {
        return isi;
    }

    public void setYear(String isi) {
        this.isi = isi;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + isi + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}
