package com.cyberpanterra.book.UI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cyberpanterra.book.Datas.Theme;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Theme> mData = new MutableLiveData<>();

    public LiveData<Theme> getData() { return mData; }

    public void setData(Theme data) { mData.setValue(data); }
}