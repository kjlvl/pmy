package com.example.myaquarium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaquarium.R;
import com.example.myaquarium.adapter.view.ForumUserThemesViewHolder;
import com.example.myaquarium.model.Theme;

import java.util.List;

public class ForumUserThemesAdapter extends RecyclerView.Adapter<ForumUserThemesViewHolder> {
    private final Context context;
    private final List<Theme> themesList;
    private final OnThemeClickListener onClickListener;
    private final OnEditClickListener onEditListener;

    public interface OnThemeClickListener {
        void onStateClick(String themeId, String category);
    }

    public interface OnEditClickListener {
        void onStateClick(Theme themeId);
    }

    public ForumUserThemesAdapter(
            Context context,
            List<Theme> themesList,
            OnThemeClickListener onClickListener,
            OnEditClickListener onEditListener
    ) {
        this.context = context;
        this.themesList = themesList;
        this.onClickListener = onClickListener;
        this.onEditListener = onEditListener;
    }

    @NonNull
    @Override
    public ForumUserThemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View themes = LayoutInflater
                .from(context)
                .inflate(R.layout.forum_my_sections_item, parent, false);

        return new ForumUserThemesViewHolder(themes);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumUserThemesViewHolder holder, int position) {
        holder.theme.setText(themesList.get(position).getTitle());
        holder.date.setText(themesList.get(position).getDate());
        this.setBackground(holder, position);

        holder.sectionsItem.setOnClickListener(
                v -> onClickListener.onStateClick(themesList.get(position).getId(), themesList.get(position).getCategoryId())
        );
        holder.edit.setOnClickListener(
                v -> onEditListener.onStateClick(themesList.get(position))
        );
    }

    private void setBackground(ForumUserThemesViewHolder holder, int position) {
        switch (themesList.get(position).getSections()) {
            case "Общие вопросы содержания":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.general_issues));
                break;
            case "Аквариумные рыбки":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_fish));
                break;
            case "Болезни":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.diseases));
                break;
            case "Дизайн":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.design));
                break;
            case "Аквариумное оборудование и прочее":
            case "Аквариумы и оборудование":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_equipment_other));
                break;
            case "Морские аквариумы и оборудование":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.marine_aquariums_equipment));
                break;
            case "Корма для рыбок и креветок":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.food_fish));
                break;
            case "Аквариумные растения":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_plants));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return themesList.size();
    }
}
