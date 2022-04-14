package Adapters;

/*
    The creator of the ChaptersDataAdapter class is Asadjon Xusanjonov
    Created on 15:35, 23.03.2022
*/

import android.annotation.SuppressLint;
import android.view.*;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import Datas.Chapter;
import Datas.Data;

import com.cyberpanterra.book.Interactions.StaticClass;

import Interfaces.OnClickListener;

import com.cyberpanterra.book.R;
import com.cyberpanterra.book.databinding.ItemChapterBinding;

import java.util.ArrayList;
import java.util.List;

public class ChaptersDataAdapter extends RecyclerView.Adapter<ViewHolder<ItemChapterBinding>> implements Filterable {

    private List<Data> chapterList = new ArrayList<>();
    private final List<Data> fullChapters = new ArrayList<>();
    private OnClickListener<Data> onActionListener;
    private OnClickListener<Data> onClickListener;
    private String searchedText = "";
    private boolean isSelected = false;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
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
                    onActionListener.onClick(((ViewHolder<ItemChapterBinding>) viewHolder).binding.getChapter());
            }
        };
    }

    public ChaptersDataAdapter setChapterList(List<Data> list) {
        if (list != null) {
            fullChapters.clear();
            fullChapters.addAll(list);
            getFilter().filter(searchedText);
        }
        return this;
    }

    @NonNull
    @Override
    public ViewHolder<ItemChapterBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemChapterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemChapterBinding> holder, int position) {
        holder.binding.setChapter(getItem(position));
        holder.binding.setSearchText(searchedText);
        holder.binding.setClick(onClickListener);

        if (holder.binding.getChapter() instanceof Chapter) {
            holder.binding.setAdapter(new ThemesDataAdapter((Chapter) holder.binding.getChapter())
                    .setOnClickListener(onClickListener)
                    .setOnActionListener(onActionListener));
            holder.binding.getAdapter().getFilter().filter(searchedText);
        } else holder.binding.setAdapter(null);

        holder.binding.chapter.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_DOWN) isSelected = true;
            else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
                isSelected = false;
            return false;
        });
    }

    @Override
    public int getItemCount() { return chapterList.size(); }

    public Data getItem(int position) { return chapterList.get(position); }

    @Override
    public Filter getFilter() { return filter; }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Data> filteredList = new ArrayList<>();

            searchedText = charSequence.toString().toUpperCase().trim();
            if (searchedText.isEmpty()) filteredList.addAll(fullChapters);
            else filteredList.addAll(StaticClass.whereAll(fullChapters,
                    data -> (data instanceof Chapter && ((Chapter) data).isSearchResult(searchedText)) ||
                            data.getName().toUpperCase().contains(searchedText) ||
                            data.getValue().toUpperCase().contains(searchedText)));

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values != null) {
                chapterList = (List<Data>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    };

    public List<Data> getFullChapters() { return fullChapters; }

    public ChaptersDataAdapter setOnActionListener(OnClickListener<Data> Listener) {
        onActionListener = Listener;
        return this;
    }

    public ChaptersDataAdapter setOnClickListener(OnClickListener<Data> listener) {
        onClickListener = listener;
        return this;
    }
}
