package com.cyberpanterra.book.Adapters;

/*
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.Interfaces.RemoveOnClickListener;
import com.cyberpanterra.book.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChaptersDataAdapter extends RecyclerView.Adapter<ChaptersDataAdapter.MyViewHolder> {

    private List<Chapter> mChapters;
    private final List<Chapter> mFullChapters;
    private final OnClickListener<Theme> mOnClickListener;
    private final RemoveOnClickListener<Theme, Boolean> mRemoveOnClickListener;
    private String mSearchedText = "";
    private final boolean mIsFavourites;
    private Boolean mTurnOnDelete = false;

    public ChaptersDataAdapter(boolean isFavourites, List<Chapter> chapters, OnClickListener<Theme> listener, RemoveOnClickListener<Theme, Boolean> removeListener) {
        mIsFavourites = isFavourites;
        mChapters = chapters;
        mFullChapters = new ArrayList<>();
        if(mChapters != null) mFullChapters.addAll(mChapters);
        mOnClickListener = listener;
        mRemoveOnClickListener = removeListener;

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { holder.bindDataView(position); }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) { super.onViewRecycled(holder); }

    @Override
    public int getItemCount() { return mChapters.size(); }

    public List<Chapter> getChapters() { return mChapters; }

    public Boolean getTurnOnDelete() { return mTurnOnDelete; }

    public void setTurnOnDelete(Boolean turnOnDelete) { mTurnOnDelete = turnOnDelete; notifyDataSetChanged(); }

    public void filter(String searchText){
        mSearchedText = searchText.toUpperCase().trim();

        List<Chapter> filteredList = new ArrayList<>();

        if (mSearchedText == null || mSearchedText.isEmpty()) filteredList.addAll(mFullChapters);
        else StaticClass.forEach(mFullChapters, data -> {
            if (data.isSearchResult(mSearchedText) || data.getName().toUpperCase().contains(mSearchedText) || data.getValue().toUpperCase().contains(mSearchedText))
                filteredList.add(data);});

        mChapters = filteredList;
        notifyDataSetChanged();
    }

    public final void removeChapter(Chapter removedChapter){
        notifyItemRemoved(mChapters.indexOf(removedChapter));
        mChapters.remove(removedChapter);
        mFullChapters.remove(removedChapter);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final View chapterView;
        private final View separatorView;
        private final TextView nameText;
        private final TextView valueText;
        private final ImageView favouriteView;
        private final RecyclerView themesRecyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterView = itemView;
            separatorView = itemView.findViewById(R.id.separatorView);
            nameText = itemView.findViewById(R.id.chapterName);
            valueText = itemView.findViewById(R.id.chapterValue);
            favouriteView = itemView.findViewById(R.id.favouriteView);
            themesRecyclerView = itemView.findViewById(R.id.themes);
        }

        public void bindDataView(int position){
            Chapter chapter = mChapters.get(position);
            if(chapter.getSerializedThemes().size() != 0)
                themesRecyclerView.setAdapter(new ThemesDataAdapter(ChaptersDataAdapter.this, mIsFavourites, new ArrayList<>(chapter.getSerializedThemes()), mOnClickListener, mRemoveOnClickListener, mSearchedText, mTurnOnDelete));
            else {
                themesRecyclerView.setVisibility(View.INVISIBLE);
                themesRecyclerView.setAdapter(null);
            }

            chapterView.setOnClickListener(view -> mOnClickListener.OnClick(chapter.getFullThemes().get(0)));
            separatorView.setVisibility(chapter.getValue().equals("") ? View.GONE : View.VISIBLE);
            nameText.setText(chapter.getName());
            valueText.setText(chapter.getValue());

            if(mIsFavourites) {
                favouriteView.setVisibility(mTurnOnDelete ? View.VISIBLE : View.GONE);
                favouriteView.setOnClickListener(view -> {
                    notifyItemRemoved(mChapters.indexOf(chapter));
                    mChapters.remove(chapter);
                    mFullChapters.remove(chapter);
                    mRemoveOnClickListener.OnClick(chapter.getFullThemes().get(0), true);
                });
            } else favouriteView.setVisibility(View.GONE);

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(valueText, mSearchedText);
        }
    }
}
