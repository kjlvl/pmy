package com.example.myaquarium.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaquarium.R;
import com.example.myaquarium.adapter.view.ForumUserThemesViewHolder;

import org.json.JSONObject;

import java.util.List;

public class ForumUserThemesAdapter extends RecyclerView.Adapter<ForumUserThemesViewHolder> {
    private Context context;
    private List<JSONObject> themesList;
    private final OnThemeClickListener onClickListener;
    private final OnEditClickListener onEditListener;

    public interface OnThemeClickListener {
        void onStateClick(JSONObject themeId, String category);
    }

    public interface OnEditClickListener {
        void onStateClick(JSONObject themeId);
    }

    public ForumUserThemesAdapter(
            Context context,
            List<JSONObject> themesList,
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
        holder.theme.setText(themesList.get(position).optString("title"));
        holder.date.setText(themesList.get(position).optString("date"));
        this.setBackground(holder, position);

        holder.sectionsItem.setOnClickListener(
                v -> onClickListener.onStateClick(themesList.get(position), themesList.get(position).optString("category_id"))
        );
        holder.edit.setOnClickListener(
                v -> onEditListener.onStateClick(themesList.get(position))
        );
    }

    private void setBackground(ForumUserThemesViewHolder holder, int position) {
        switch (themesList.get(position).optString("sections")) {
            case "?????????? ?????????????? ????????????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.general_issues));
                break;
            case "?????????????????????? ??????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_fish));
                break;
            case "??????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.diseases));
                break;
            case "????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.design));
                break;
            case "?????????????????????? ???????????????????????? ?? ????????????":
            case "?????????????????? ?? ????????????????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_equipment_other));
                break;
            case "?????????????? ?????????????????? ?? ????????????????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.marine_aquariums_equipment));
                break;
            case "?????????? ?????? ?????????? ?? ????????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.food_fish));
                break;
            case "?????????????????????? ????????????????":
                holder.sectionsItem.setBackgroundColor(context.getResources().getColor(R.color.aquarium_plants));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return themesList.size();
    }
}
