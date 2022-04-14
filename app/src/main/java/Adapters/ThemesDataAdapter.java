package Adapters;

/* 
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import Datas.Chapter;
import Datas.Data;

import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.databinding.ItemThemeBinding;

import Interfaces.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import Datas.Theme;

public class ThemesDataAdapter extends RecyclerView.Adapter<ViewHolder<ItemThemeBinding>> implements Filterable {

    private List<Theme> themeList = new ArrayList<>();
    private final List<Theme> fullThemeList = new ArrayList<>();
    private OnClickListener<Data> onActionListener;
    private OnClickListener<Data> onClickListener;
    private String searchText = "";

    public ThemesDataAdapter(Chapter chapter) {
        fullThemeList.clear();
        if (chapter != null)
            for (int i = 0; i < chapter.size(); i++)
                fullThemeList.add(chapter.get(i));
        themeList.addAll(fullThemeList);
    }

    public ItemTouchHelper.SimpleCallback getOnRemoveListener() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (onActionListener != null && !themeList.isEmpty())
                    onActionListener.onClick(((ViewHolder<ItemThemeBinding>) viewHolder).binding.getTheme());
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        new ItemTouchHelper(getOnRemoveListener()).attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ViewHolder<ItemThemeBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemThemeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemThemeBinding> holder, int position) {
        holder.binding.setTheme((Theme) getItem(position));
        holder.binding.setSearchText(searchText);
        holder.binding.setClick(onClickListener);
    }

    @Override
    public int getItemCount() { return themeList.size(); }

    public Theme getItem(int position) { return themeList.get(position); }

    public ThemesDataAdapter setOnActionListener(OnClickListener<Data> listener) {
        onActionListener = listener;
        return this;
    }

    public ThemesDataAdapter setOnClickListener(OnClickListener<Data> listener) {
        onClickListener = listener;
        return this;
    }

    @Override
    public Filter getFilter() { return filter; }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Theme> filteredList = new ArrayList<>();

            searchText = constraint.toString().toUpperCase().trim();
            if (searchText.isEmpty()) filteredList.addAll(fullThemeList);
            else filteredList.addAll(StaticClass.whereAll(fullThemeList,
                    data -> (data.getName().toUpperCase().contains(searchText) ||
                            data.getValue().toUpperCase().contains(searchText))));

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                themeList = (List<Theme>) results.values;
                notifyDataSetChanged();
            }
        }
    };
}
