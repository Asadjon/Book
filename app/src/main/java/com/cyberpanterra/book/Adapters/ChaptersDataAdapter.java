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
import com.cyberpanterra.book.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChaptersDataAdapter extends RecyclerView.Adapter<ChaptersDataAdapter.MyViewHolder> implements Filterable {

    private List<Chapter> mChapters;
    private List<Chapter> mFullChapters;
    private final OnClickListener<Theme> mOnClickListener;
    private final OnClickListener<Theme> mThemeOnClickListener;
    private String mSearchedText = "";

    public ChaptersDataAdapter(List<Chapter> chapters, OnClickListener<Theme> listener, OnClickListener<Theme> themeListener) {
        mChapters = chapters;
        mFullChapters = new ArrayList<>();
        if(mChapters != null) mFullChapters.addAll(mChapters);
        mOnClickListener = listener;
        mThemeOnClickListener = themeListener;

        setHasStableIds(false);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindDataView(mChapters.get(position));
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() { return mChapters.size(); }

    public void setChapters(List<Chapter> chapters) {
        mChapters = chapters;
        mFullChapters = new ArrayList<>(mChapters);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() { return exampleFilter; }

    private final Filter exampleFilter =  new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mSearchedText = constraint.toString().toUpperCase().trim();

            List<Chapter> filteredList = new ArrayList<>();

            if (mSearchedText == null || mSearchedText.length() == 0) {
                filteredList.addAll(mFullChapters);
                for (Chapter data : mFullChapters) data.resetData();
            }
            else for (Chapter data : mFullChapters)
                    if (data.isSearchResult(mSearchedText) || data.getName().toUpperCase().contains(mSearchedText) || data.getValue().toUpperCase().contains(mSearchedText))
                        filteredList.add(data);

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.values != null) {
                mChapters = (List<Chapter>) results.values;
                notifyDataSetChanged();
            }
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final View chapterView;
        private final View separatorView;
        private final TextView nameText;
        private final TextView valueText;
        private final RecyclerView themesRecyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterView = itemView;
            separatorView = itemView.findViewById(R.id.separatorView);
            nameText = itemView.findViewById(R.id.chapterName);
            valueText = itemView.findViewById(R.id.chapterValue);
            themesRecyclerView = itemView.findViewById(R.id.themes);
        }

        public void bindDataView(Chapter chapter){
            if(chapter.getSerializedThemes().size() != 0) {
                themesRecyclerView.setAdapter(new ThemesDataAdapter(chapter.getSerializedThemes(), mThemeOnClickListener, mSearchedText));
            } else {
                themesRecyclerView.setVisibility(View.INVISIBLE);
                themesRecyclerView.setAdapter(null);
            }

            chapterView.setOnClickListener(view -> mOnClickListener.OnClick(chapter.getFullThemes().get(0)));
            separatorView.setVisibility(chapter.getValue().equals("") ? View.GONE : View.VISIBLE);
            nameText.setText(chapter.getName());
            valueText.setText(chapter.getValue());

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(valueText, mSearchedText);
        }
    }
}
