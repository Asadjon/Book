package com.cyberpanterra.book.Adapters;

/* 
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class ThemesDataAdapter extends RecyclerView.Adapter<ThemesDataAdapter.MyViewHolder> {

    private final ChaptersDataAdapter mChaptersAdapter;
    private final List<Theme> mThemes;
    private final OnClickListener<Theme> mOnClickListener;
    private final RemoveOnClickListener<Theme, Boolean> mRemoveOnClickListener;
    private final String mSearchedText;
    private final boolean mIsFavourites;
    private final boolean mTurnOnDelete;

    public ThemesDataAdapter(
            ChaptersDataAdapter chaptersAdapter,
            boolean isFavourites,
            List<Theme> themes,
            OnClickListener<Theme> listener,
            RemoveOnClickListener<Theme, Boolean> removeListener,
            String searchedText,
            boolean turnOnDelete) {
        mChaptersAdapter = chaptersAdapter;
        mIsFavourites = isFavourites;
        mThemes = themes;
        mOnClickListener = listener;
        mRemoveOnClickListener = removeListener;
        mSearchedText = searchedText;
        mTurnOnDelete = turnOnDelete;
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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final View chapterView;
        private final TextView nameText;
        private final TextView indexText;
        private final ImageView favouriteView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterView = itemView;
            nameText = itemView.findViewById(R.id.themeName);
            indexText = itemView.findViewById(R.id.themeIndex);
            favouriteView = itemView.findViewById(R.id.favouriteView);
        }

        public void bindDataView(int position){
            Theme theme = mThemes.get(position);

            chapterView.setOnClickListener(view -> mOnClickListener.OnClick(theme));
            nameText.setText(theme.getName());
            indexText.setText(theme.getIndex());

            if(mIsFavourites) {
                favouriteView.setVisibility(mTurnOnDelete ? View.VISIBLE : View.GONE);
                favouriteView.setOnClickListener(view -> {
                    int index = mThemes.indexOf(theme);
                    mThemes.remove(theme);
                    if (mThemes.isEmpty()) mChaptersAdapter.removeChapter(theme.getChapter());
                    notifyItemRemoved(index);
                    mRemoveOnClickListener.OnClick(theme, false);
                });
            }else favouriteView.setVisibility(View.GONE);

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(indexText, mSearchedText);
        }
    }
}
