package com.cyberpanterra.book.Adapters;

/* 
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

import java.util.ArrayList;
import java.util.List;

public class ThemesDataAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<Theme> themeList = new ArrayList<>();
    private OnClickListener<Data> onActionListener;
    private OnClickListener<Data> mOnClickListener;
    private String searchText = "";

    public ThemesDataAdapter(Chapter chapter) {
        getList(chapter);
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
                    onActionListener.onClick(((ViewHolder) viewHolder).data);
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        new ItemTouchHelper(getOnRemoveListener()).attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false))
                .setOnClickListener(mOnClickListener)
                .setSearchText(searchText);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) { holder.bindDataView(getItem(position)); }

    @Override
    public int getItemCount() { return themeList.size(); }

    public Theme getItem(int position) { return themeList.get(position); }

    public ThemesDataAdapter setChapter(Chapter chapter) {
        getList(chapter);
        if (chapter != null) notifyDataSetChanged();
        return this;
    }

    private void getList(Chapter chapter) {
        if (chapter != null)
            for (int i = 0; i < chapter.size(); i++)
                themeList.add(chapter.get(i));
        else themeList.clear();
    }

    public ThemesDataAdapter setOnActionListener(OnClickListener<Data> listener) {
        onActionListener = listener;
        return this;
    }

    public ThemesDataAdapter setOnClickListener(OnClickListener<Data> listener) {
        mOnClickListener = listener;
        return this;
    }

    public ThemesDataAdapter setSearchText(String searchText) {
        this.searchText = searchText;
        return this;
    }
}
