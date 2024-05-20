package com.appdev.gadgetsgalaxy.recyclerview;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.databinding.FilterChipLayoutBinding;

import java.util.List;
import java.util.function.Consumer;

public class Chip_adapter extends RecyclerView.Adapter<Chip_adapter.ChipViewHolder> {

    private List<String> chipList;
    public int selectedPosition = 0;
    final Consumer<String> onFilterClick;

    public Chip_adapter(List<String> chipList, Consumer<String> onFilterClick) {
        this.onFilterClick = onFilterClick;
        this.chipList = chipList;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using View Binding
        FilterChipLayoutBinding binding = FilterChipLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChipViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        // Bind data to the views
        holder.bind(chipList.get(position), position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return chipList.size();
    }

    public class ChipViewHolder extends RecyclerView.ViewHolder {

        private final FilterChipLayoutBinding binding;

        public ChipViewHolder(FilterChipLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    onFilterClick.accept(chipList.get(position));
                }
            });

        }

        public void bind(String chipText, boolean isSelected) {
            // Bind data to the views
            binding.chipName.setText(chipText);

            int currentNightMode = binding.getRoot().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;


            if (isSelected) {
                binding.getRoot().setCardBackgroundColor(binding.getRoot().getResources().getColor(R.color.btnColor, null));
                binding.chipName.setTextColor(Color.WHITE);
            } else {
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    binding.chipName.setTextColor(Color.WHITE);
                    binding.getRoot().setCardBackgroundColor(Color.TRANSPARENT);
                } else {
                    binding.getRoot().setCardBackgroundColor(Color.TRANSPARENT);
                    binding.chipName.setTextColor(Color.BLACK);
                }
            }
        }
    }
}
