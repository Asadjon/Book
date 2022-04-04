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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

import java.util.List;

public class ThemesDataAdapter extends ListAdapter<Theme, ThemesDataAdapter.MyViewHolder> {

    private final OnClickListener<Theme> mOnClickListener;
    private final String mSearchedText;

    public ThemesDataAdapter(OnClickListener<Theme> listener, String searchedText) {
        super(DIFF_CALLBACK);

        mOnClickListener = listener;
        mSearchedText = searchedText;
    }

    private static final DiffUtil.ItemCallback<Theme> DIFF_CALLBACK = new DiffUtil.ItemCallback<Theme>() {
        @Override
        public boolean areItemsTheSame(@NonNull Theme oldItem, @NonNull Theme newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Theme oldItem, @NonNull Theme newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { holder.bindDataView(position); }

    public Theme getThemeAt(int position) { return getItem(position); }

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
            Theme theme = getItem(position);

            chapterView.setOnClickListener(view -> mOnClickListener.OnClick(theme));
            nameText.setText(theme.getName());
            indexText.setText(theme.getIndex());

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(indexText, mSearchedText);
        }
    }
}
