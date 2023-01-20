package com.example.myaquarium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaquarium.R;
import com.example.myaquarium.adapter.view.ForumThemesListViewHolder;

import java.util.List;

public class ForumThemesListAdapter extends RecyclerView.Adapter<ForumThemesListViewHolder> {
    private Context context;
    private List<String> themesList;
    private final OnThemeClickListener onClickListener;

    public interface OnThemeClickListener {
        void onStateClick(String theme, CheckBox checkBox);
    }

    public ForumThemesListAdapter(
            Context context,
            List<String> themesList,
            OnThemeClickListener onClickListener
    ) {
        this.context = context;
        this.themesList = themesList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ForumThemesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fishItems = LayoutInflater
                .from(context)
                .inflate(R.layout.forum_sections_list_items, parent, false);

        return new ForumThemesListViewHolder(fishItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumThemesListViewHolder holder, int position) {
        holder.section.setText(themesList.get(position));
        holder.checkBox.setOnClickListener(
                view -> onClickListener.onStateClick(themesList.get(position), holder.checkBox)
        );
    }

    @Override
    public int getItemCount() {
        return themesList.size();
    }

}