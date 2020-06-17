package com.myapp.android.pasti.mahasiswa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.myapp.android.pasti.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MahasiswaFAQFragment extends Fragment {
    RecyclerView recyclerView;

    List<FAQ> faqList;

    public MahasiswaFAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mahasiswa_faq,container,false);

        recyclerView = v.findViewById(R.id.recyclerView);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(faqAdapter);
    }

    private void initData() {
        faqList = new ArrayList<>();
        faqList.add(new FAQ("Apa saja lampiran yang dibutuhkan ?",
                "Lampiran yang diserahkan dimasukan dalam map ditulis NRP dan Nama. \n " +
                "1. Buku Proposal Eksemplar jilid Plastik \n " +
                "2. Buku Bimbingan Fotocopy \n " +
                "3. Bukti pembayaran sidang proposal \n\n" +
                "MAP \n " +
                "1. S1-TI (map Merah)\n" +
                " 2. S1-SI (map Hijau)\n" +
                " 3. D3-SI (map Kuning)\n"));
        faqList.add(new FAQ("Siapa saja yang bisa menjadi dosen pembimbing ?",
                "1. Indra Permana Solihin, S.Kom, M.Kom.\n" +
                "2. Henki Bayu Seta, S.Kom, MTI. \n" +
                "3. Bambang Tri Wahyono, S.Kom, M.Si. \n" +
                "4. Jayanta, S.Kom,M.Si. \n" +
                "5. Noor Falih,  S.Kom., M.T. \n" +
                "6. Mayanda Mega Santoni, S.Komp, M.Kom. \n"+
                "7. Nurul Chamidah, S.Kom, M.Kom. \n"+
                "8. Desta Sandya Prasvita, S. Komp., M.Kom. \n"));
        faqList.add(new FAQ("Tanggal-tanggal penting ?",
                "Jadwal penyerahan berkas : \n" +
                "Tahap 1 = 5 September 2020 \n" +
                "Tahap 2 = 7 September 2020 \n" +
                "Tahap 3 = 9 September 2020 \n"));
    }

}
