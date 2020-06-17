package com.myapp.android.pasti.mahasiswa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.android.pasti.R;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MovieVH> {

    private static final String TAG = "FAQAdapter";
    List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mahasiswa_faq_row, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {

        FAQ faq = faqList.get(position);
        holder.titleTextView.setText(faq.getTitle());
        holder.isiTextView.setText(faq.getIsi());

        boolean isExpanded = faqList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {

        private static final String TAG = "MovieVH";

        ConstraintLayout expandableLayout;
        TextView titleTextView, isiTextView;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            isiTextView = itemView.findViewById(R.id.yearTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FAQ faq = faqList.get(getAdapterPosition());
                    faq.setExpanded(!faq.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
