package com.journaldev.sqlite.sample;

import com.journaldev.sqlite.model.DataItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleDataProvider {
    public static List<DataItem> dataItemList;
    public static Map<String, DataItem> dataItemMap;

    static {
        dataItemList = new ArrayList<>();
        dataItemMap = new HashMap<>();

    //FROM DataItem.java ******   public DataItem(String itemId, String Name(firstandlastname), String category(period), String description(notused), int sortPosition(studentid), double price(notused), String image) {
        addItem(new DataItem(null, "Jane Frost", "1", "", 12345678, 9, "bus.jpg"));
    }

    private static void addItem(DataItem item) {
        dataItemList.add(item);
        dataItemMap.put(item.getItemId(), item);
    }

}
