package com.cyberpanterra.book.Adapters;

/*
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Datas.SimpleChapter;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

import java.util.ArrayList;
import java.util.List;

public class ChaptersDataAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private List<SimpleChapter> chapterList = new ArrayList<>();
    private final List<SimpleChapter> mFullChapters = new ArrayList<>();
    private OnClickListener<Data> onActionListener;
    private OnClickListener<Data> onClickListener;
    private String searchedText = "";
    private boolean isSelected = false;

    public ChaptersDataAdapter() { }

    public ChaptersDataAdapter(List<SimpleChapter> chapterList) {
        setHasStableIds(false);
        this.chapterList = chapterList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        new ItemTouchHelper(getOnRemoveListener()).attachToRecyclerView(recyclerView);
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ItemTouchHelper.SimpleCallback getOnRemoveListener() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean isItemViewSwipeEnabled() { return isSelected; }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (onActionListener != null)
                    onActionListener.onClick(((ViewHolder) viewHolder).data);
            }
        };
    }

    public ChaptersDataAdapter setChapterList(List<SimpleChapter> list) {
        if (list != null) {
            mFullChapters.clear();
            mFullChapters.addAll(list);
            chapterList = list;
            notifyDataSetChanged();
        }
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false))
                .setOnClickListener(onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchText(searchedText)
                .bindDataView(getItem(position));
    }

    @Override
    public int getItemCount() { return chapterList.size(); }

    public SimpleChapter getItem(int position) { return chapterList.get(position); }

    @Override
    public Filter getFilter() { return filter; }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SimpleChapter> filteredList = new ArrayList<>();

            searchedText = charSequence.toString().toUpperCase().trim();
            if (searchedText.isEmpty()) {
                filteredList.addAll(mFullChapters);
                StaticClass.forEach(filteredList, chapter -> {
                    if (chapter.getClass() == Chapter.class) ((Chapter) chapter).resetList();
                });
            }
            else filteredList.addAll(StaticClass.whereAll(mFullChapters,
                    data -> (data.getClass() == Chapter.class && ((Chapter) data).isSearchResult(searchedText)) ||
                            data.getName().toUpperCase().contains(searchedText) ||
                            data.getValue().toUpperCase().contains(searchedText)));

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values != null) {
                chapterList = (List<SimpleChapter>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    };

    public List<SimpleChapter> getFullChapters() { return mFullChapters; }

    public ChaptersDataAdapter setOnActionListener(OnClickListener<Data> Listener) {
        onActionListener = Listener;
        return this;
    }

    public ChaptersDataAdapter setOnClickListener(OnClickListener<Data> listener) {
        onClickListener = listener;
        return this;
    }

    public class MyViewHolder extends ViewHolder {
        private final RecyclerView themesRecyclerView;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            themesRecyclerView = itemView.findViewById(R.id.themes);
            themesRecyclerView.setHasFixedSize(false);

            itemView.setOnTouchListener((view, motionEvent) -> {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) isSelected = true;
                else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
                    isSelected = false;
                return false;
            });
        }

        @Override
        public ViewHolder bindDataView(Data data) {
            if (data.getClass() == Chapter.class)
                themesRecyclerView.setAdapter(new ThemesDataAdapter((Chapter) data)
                        .setOnClickListener(onClickListener)
                        .setOnActionListener(onActionListener)
                        .setSearchText(searchedText));

            else themesRecyclerView.setAdapter(null);
            return super.bindDataView(data);
        }
    }
}
