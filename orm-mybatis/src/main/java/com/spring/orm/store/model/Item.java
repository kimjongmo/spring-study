package com.spring.orm.store.model;

public class Item {
    private Integer itemId;
    private int price;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_id=" + itemId +
                ", price=" + price +
                '}';
    }
}
