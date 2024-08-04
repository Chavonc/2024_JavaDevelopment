package com.example.test;

public class walletListData
{
    private String walletDate;
    private String walletContent;
    private String WalletAmount;
    public walletListData(String date,String name,String cost)
    {
        this.walletDate=date;
        this.walletContent=name;
        this.WalletAmount=cost;
    }
    public String getItemDate()
    {
        return walletDate;
    }
    public String getItemName()
    {
        return walletContent;
    }
    public String getItemCost()
    {
        return WalletAmount;
    }
//    public void setItemDate(String date)
//    {
//        this.walletDate=date;
//    }
//    public void setItemName(String name)
//    {
//        this.walletContent=name;
//    }
//    public void setItemCost(String cost)
//    {
//        this.WalletAmount=cost;
//    }

}
