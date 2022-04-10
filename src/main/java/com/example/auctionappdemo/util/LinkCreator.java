package com.example.auctionappdemo.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.List;

public class LinkCreator {

    private static final String QUERY_PARAM_SORT = "sort";

    public static String replaceParam(String name, String value) {
        ServletUriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        if (QUERY_PARAM_SORT.equals(name)) {
            adjustSortOrder(urlBuilder, value);
        } else {
            urlBuilder.replaceQueryParam(name, value);
        }
        return urlBuilder.toUriString();
    }

    private static void adjustSortOrder(ServletUriComponentsBuilder urlBuilder, String targetSortBy) {
        LinkedMultiValueMap<String, String> queryParams = getQueryParams(urlBuilder);
        List<String> sortList = queryParams.get(QUERY_PARAM_SORT);
        if (sortList != null && !sortList.isEmpty()) {
            String currentSortBy = sortList.get(0);
            if (currentSortBy.equals(targetSortBy)) {
                urlBuilder.replaceQueryParam(QUERY_PARAM_SORT, currentSortBy + ",desc");
            } else {
                urlBuilder.replaceQueryParam(QUERY_PARAM_SORT, targetSortBy);
            }
        } else {
            urlBuilder.replaceQueryParam(QUERY_PARAM_SORT, targetSortBy);
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static LinkedMultiValueMap<String, String> getQueryParams(ServletUriComponentsBuilder urlBuilder) {
        Field field = ReflectionUtils.findField(ServletUriComponentsBuilder.class, "queryParams");
        ReflectionUtils.makeAccessible(field);
        return (LinkedMultiValueMap<String, String>) ReflectionUtils.getField(field, urlBuilder);
    }
}