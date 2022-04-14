package Adapters;

/* 
    The creator of the ViewHolder class is Asadjon Xusanjonov
    Created on 17:43, 11.04.2022
*/

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    final T binding;

    public ViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
