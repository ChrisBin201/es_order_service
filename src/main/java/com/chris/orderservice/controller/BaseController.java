package com.chris.orderservice.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class BaseController {

    public PageRequest pageRequest(List<String> sortOptions,String sort, Integer page, Integer size) {
//        if(CollectionUtils.isEmpty(sort)) {
        if(sort.isBlank() || sort.isEmpty() ) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, sort(sortOptions ,sort));
    }

//    @SuppressWarnings("java:S3776")
    public Sort sort(List<String> sortOptions, String sort) {
        sort = sort.toLowerCase();
        if(sort.isBlank() || sort.isEmpty() ) return null;
//        if (CollectionUtils.isEmpty(sort)) {
//            return null;
//        }

        List<Sort.Order> orderList = new ArrayList<>();
        String[] sortArray = sort.split(",");
        for(String str : sortArray) {
            if(!sortOptions.contains(str.toUpperCase())) return null;
            String field = str.split("_")[0];
            String order = str.split("_")[1];
            if("asc".equalsIgnoreCase(order))
                orderList.add(Sort.Order.asc(field));
            else
                orderList.add(Sort.Order.desc(field));
        }
        return Sort.by(orderList);

    }

}
