package com.cyberpanterra.book.Adapters;

/* 
    The creator of the ViewHolder class is Asadjon Xusanjonov
    Created on 17:43, 11.04.2022
*/

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberpanterra.book.Datas.Data;
import com.cyberpanterra.book.Interactions.StaticClass;
import com.cyberpanterra.book.Interfaces.OnClickListener;
import com.cyberpanterra.book.R;

class ViewHolder extends RecyclerView.ViewHolder {
    Data data;
    private final View separatorView;
    private final TextView valueText;
    private final TextView nameText;

    private OnClickListener<Data> onClickListener;
    private String searchText = "";

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        separatorView = itemView.findViewById(R.id.separatorView);
        valueText = itemView.findViewById(R.id.valueText);
        nameText = itemView.findViewById(R.id.nameText);
    }

    public ViewHolder bindDataView(Data data) {
        this.data = data;

        itemView.setOnClickListener(view -> {
            if (onClickListener != null) onClickListener.onClick(data);
        });
        nameText.setText(data.getName());
        valueText.setText(data.getValue());
        separatorView.setVisibility(data.getName().isEmpty() || data.getValue().isEmpty() ? View.GONE : View.VISIBLE);

        changeHighlightedText(searchText);

        return this;
    }

    public void changeHighlightedText(String text) {
        StaticClass.setHighLightedText(valueText, text);
        StaticClass.setHighLightedText(nameText, text);
    }

    public ViewHolder setOnClickListener(OnClickListener<Data> onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public ViewHolder setSearchText(String searchText) {
        this.searchText = searchText;
        return this;
    }
}
