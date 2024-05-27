package com.appdev.gadgetsgalaxy.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.gadgetsgalaxy.R;
import com.appdev.gadgetsgalaxy.databinding.ImageSampleBinding;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.function.Consumer;

public class Banner_image_adapter extends RecyclerView.Adapter<Banner_image_adapter.myViewHolder> {

    private List<String> imageUrls;
    final Consumer<String> imageClicked;

    public Banner_image_adapter(List<String> imageUrls, Consumer<String> onImageRemoved) {
        this.imageClicked = onImageRemoved;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using View Binding
        ImageSampleBinding binding = ImageSampleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Bind data to the views
        holder.bind(imageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        private final ImageSampleBinding binding;

        public myViewHolder(ImageSampleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    notifyDataSetChanged();
                    imageClicked.accept(imageUrls.get(position));
                }
            });

        }

        public void bind(String imageUrl) {
            binding.removeBtn.setOnClickListener(v -> {
                imageClicked.accept(imageUrl);
            });
            Glide.with(binding.getRoot().getContext()).load(imageUrl).placeholder(R.drawable.placeholder).into(binding.imageView);
        }
    }
}

