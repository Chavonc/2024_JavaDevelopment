package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.walletListData;

import java.util.ArrayList;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.WalletViewHolder> {

    private Context context;
    private int resource;
    private ArrayList<walletListData> walletList;
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){this.listener=listener;};
    public WalletListAdapter(Context context, int resource, ArrayList<walletListData> walletList) {
        this.context = context;
        this.resource = resource;
        this.walletList = walletList;
    }

    // 新增 setData 方法
    public void setData(ArrayList<walletListData> newData) {
        this.walletList = newData;
    }
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new WalletViewHolder(view);
    }
    public walletListData getSelectedItem(int position)
    {
        if (walletList != null && position >= 0 && position < walletList.size()) {
            return walletList.get(position);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        walletListData walletData = walletList.get(position);
        holder.bind(walletData);
    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }
    public walletListData getItem(int position)
    {
        if (walletList != null && position >= 0 && position < walletList.size())
        {
            return walletList.get(position);
        }
        return null;
    }
    public class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView dateTextView, contentTextView, amountTextView;

        public WalletViewHolder(@NonNull View itemView)
        {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.wallet_itemDate);
            contentTextView = itemView.findViewById(R.id.wallet_itemName);
            amountTextView = itemView.findViewById(R.id.wallet_itemCost);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view)
        {
            // 获取点击的位置
            int position = getAdapterPosition();
            // 在这里添加处理点击事件的代码
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(position);
            }
        }
        public void bind(walletListData walletData) {
            dateTextView.setText(walletData.getItemDate());
            contentTextView.setText(walletData.getItemName());
            amountTextView.setText(walletData.getItemCost());
        }
    }
}

//public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.ViewHolder> {
//    private List<walletListData> walletList;
//
//    public WalletListAdapter(List<walletListData> walletList) {
//        this.walletList = walletList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_wallet, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        walletListData data = walletList.get(position);
//
//        // 設置 CardView 中的 TextView 內容為從資料庫獲取的數據
//        holder.walletDate.setText(data.getItemDate());
//        holder.walletContent.setText(data.getItemName());
//        holder.walletAmount.setText(data.getItemCost());
//    }
//
//    @Override
//    public int getItemCount() {
//        return walletList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView walletDate;
//        public TextView walletContent;
//        public TextView walletAmount;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            walletDate = itemView.findViewById(R.id.walletDate);
//            walletContent = itemView.findViewById(R.id.walletContent);
//            walletAmount = itemView.findViewById(R.id.walletAmount);
//        }
//    }
//}

