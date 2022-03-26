package com.cyberpanterra.book.Adapters;

/* 
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

import java.util.List;

public class ThemesDataAdapter extends RecyclerView.Adapter<ThemesDataAdapter.MyViewHolder> {

    private final List<Theme> mThemes;
    private final OnClickListener<Theme> mOnClickListener;
    private String mSearchedText = "";

    public ThemesDataAdapter(List<Theme> themes, OnClickListener<Theme> listener) {
        mThemes = themes;
        mOnClickListener = listener;
    }

    public ThemesDataAdapter(List<Theme> themes, OnClickListener<Theme> listener, String searchedText) {
        this(themes, listener);
        mSearchedText = searchedText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { holder.bindDataView(position); }

    @Override
    public int getItemCount() { return mThemes.size(); }

    public void setSearchedText(String searchedText) { mSearchedText = searchedText; }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final View chapterView;
        private final TextView nameText;
        private final TextView indexText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterView = itemView;
            nameText = itemView.findViewById(R.id.themeName);
            indexText = itemView.findViewById(R.id.themeIndex);
        }

        public void bindDataView(int position){
            Theme theme = mThemes.get(position);

            chapterView.setOnClickListener(view ->
                    mOnClickListener.OnClick(theme));

            nameText.setText(theme.getName());
            indexText.setText(theme.getIndex());

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(indexText, mSearchedText);
        }
    }
}
