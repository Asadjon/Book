package com.cyberpanterra.book.Adapters;

/*
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Chapter;
import com.cyberpanterra.book.Datas.Theme;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.ForAdapters;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

import java.util.ArrayList;
import java.util.List;

public class ChaptersDataAdapter extends ListAdapter<Chapter, ChaptersDataAdapter.MyViewHolder> {

    private final List<Chapter> mFullChapters = new ArrayList<>();
    private final ForAdapters forAdapters;
    private final OnClickListener<Theme> mOnClickListener;
    private String mSearchedText = "";
    private boolean isSelected = false;

    public ChaptersDataAdapter(ForAdapters forAdapters, OnClickListener<Theme> listener) {
        super(DIFF_CALLBACK);

        this.forAdapters = forAdapters;
        mOnClickListener = listener;
//        setHasStableIds(false);
    }

    private static final DiffUtil.ItemCallback<Chapter> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chapter>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chapter oldItem, @NonNull Chapter newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setChapters(List<Chapter> list) {
        submitList(list);
        if (list != null) mFullChapters.addAll(list);
        else mFullChapters.clear();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) { holder.bindDataView(); }

    public boolean filter(String searchText) {
        mSearchedText = searchText.toUpperCase().trim();

        List<Chapter> filteredList = new ArrayList<>();

        if (mSearchedText == null || mSearchedText.isEmpty()) filteredList.addAll(mFullChapters);
        else filteredList.addAll(StaticClass.whereAll(mFullChapters,
                data -> data.isSearchResult(mSearchedText) || data.getName().toUpperCase().contains(mSearchedText) || data.getValue().toUpperCase().contains(mSearchedText)));

        submitList(filteredList);
        notifyDataSetChanged();

        return filteredList.isEmpty();
    }

    public Chapter getChapterAt(int position) { return getItem(position); }

    public boolean isSelected() { return isSelected; }

    public void setSelected(boolean selected) { isSelected = selected; }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final View separatorView;
        private final TextView nameText;
        private final TextView valueText;
        private final RecyclerView themesRecyclerView;


        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            separatorView = itemView.findViewById(R.id.separatorView);
            nameText = itemView.findViewById(R.id.chapterName);
            valueText = itemView.findViewById(R.id.chapterValue);
            themesRecyclerView = itemView.findViewById(R.id.themes);
            themesRecyclerView.setHasFixedSize(true);

            itemView.setOnTouchListener((view, motionEvent) -> { isSelected = true; return false; });
        }

        public void bindDataView() {
            Chapter chapter = getItem(getAdapterPosition());
            if (chapter.getSerializedThemes().size() != 0) {
                ThemesDataAdapter adapter = new ThemesDataAdapter(mOnClickListener, mSearchedText);
                themesRecyclerView.setAdapter(adapter);
                adapter.submitList(chapter.getSerializedThemes());
                if (forAdapters != null)
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            Theme removedTheme = adapter.getThemeAt(viewHolder.getAdapterPosition());
                            forAdapters.getFavouriteViewModel().removeFavourite(removedTheme);
                            forAdapters.checkFavourites();
                            adapter.notifyDataSetChanged();
                        }
                    }).attachToRecyclerView(themesRecyclerView);
            } else {
                themesRecyclerView.setVisibility(View.INVISIBLE);
                themesRecyclerView.setAdapter(null);
            }

            itemView.setOnClickListener(view -> mOnClickListener.OnClick(chapter.getFullThemes().get(0)));
            separatorView.setVisibility(chapter.getValue().equals("") ? View.GONE : View.VISIBLE);
            nameText.setText(chapter.getName());
            valueText.setText(chapter.getValue());

            StaticClass.setHighLightedText(nameText, mSearchedText);
            StaticClass.setHighLightedText(valueText, mSearchedText);
        }
    }
}
