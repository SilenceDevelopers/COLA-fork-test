package com.alibaba.demo.wap;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Customer Wap Adaptor
 *
 * WAP : Wireless Application Protocol)
 *
 * @author Frank Zhang
 * @date 2020-10-27 8:03 PM
 */
public class CustomerWapAdaptor {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");

        System.out.println("初始列表: " + list);

        Iterator<String> iterator = list.iterator();

        // 在迭代期间修改列表
        list.add("Date"); // 这会导致modCount增加

        try {
            while (iterator.hasNext()) {
                String fruit = iterator.next();
                System.out.println(fruit);
                // 下一行会触发ConcurrentModificationException
            }
        } catch (Exception e) {
            System.out.println("捕获异常: " + e.getClass().getSimpleName());
        }

        // 使用迭代器自身的remove方法是安全的
        iterator = list.iterator();
        while (iterator.hasNext()) {
            String fruit = iterator.next();
            if (fruit.equals("Banana")) {
                iterator.remove(); // 这不会抛出异常
            }
        }

        System.out.println("修改后的列表: " + list);
    }

}
