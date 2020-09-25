package com.neosao.truedates.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neosao.truedates.R;
import com.neosao.truedates.model.FeatureSliderModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private ArrayList<FeatureSliderModel> list;

    public SliderAdapter(Context context, ArrayList<FeatureSliderModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        FeatureSliderModel model = list.get(position);
        Glide.with(viewHolder.itemView)
                .load(model.getFeatureLogo())
                .into(viewHolder.imageViewSlider);
        viewHolder.textViewTitle.setText(model.getFeatureName());
        viewHolder.textViewDescription.setText(model.getFeatureDescription());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return list.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewSlider;
        TextView textViewDescription, textViewTitle;

        SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewSlider = itemView.findViewById(R.id.image_slider);
            textViewTitle = itemView.findViewById(R.id.text_slider_title);
            textViewDescription = itemView.findViewById(R.id.text_slider_description);
            this.itemView = itemView;
        }
    }
}