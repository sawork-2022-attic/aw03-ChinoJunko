package com.example.webpos.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Cart {

    private double total;

    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        int quantity = item.getQuantity();
        try {
            Optional<Item> existItem = items.stream().filter(
                    item1 -> {return item.getProduct().getId().equals(item1.getProduct().getId());}).findAny();
            if(existItem.isPresent()){
                 quantity += existItem.get().getQuantity();
                existItem.get().setQuantity(quantity);
                if(quantity<=0){
                    deleteItem(item);
                }
                return true;
            }
            return items.add(item);
        }
        finally {
            updateTotal();
        }
    }

    public boolean deleteItem(Item item) {
        try {
            return items.removeIf(item1 -> {return item.getProduct().getId().equals(item1.getProduct().getId());});
        }
        finally {
            updateTotal();
        }
    }

    public double updateTotal(){
        total = 0;
        for (Item item: items
        ) {
            total += item.getQuantity()*item.getProduct().getPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        if (items.size() ==0){
            return "Empty Cart";
        }
        double total = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cart -----------------\n"  );

        for (int i = 0; i < items.size(); i++) {
            stringBuilder.append(items.get(i).toString()).append("\n");
            total += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
        }
        stringBuilder.append("----------------------\n"  );

        stringBuilder.append("Total...\t\t\t" + total );

        return stringBuilder.toString();
    }
}
