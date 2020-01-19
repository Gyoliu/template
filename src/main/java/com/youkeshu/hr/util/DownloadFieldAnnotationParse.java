package com.youkeshu.hr.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: liuxing
 * @Date: 2018/8/15 10:40
 * @Description:
 */
public class DownloadFieldAnnotationParse {

    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    public static List<DownloadMetadata> parse(Class aClass, String group) throws IllegalAccessException {
        boolean b = group != null && group.length() > 0;
        Field[] declaredFields = aClass.getDeclaredFields();
        List<DownloadMetadata> downloadMetadatas = new ArrayList<>();
        Arrays.stream(declaredFields).forEach(field -> {
            boolean annotationPresent = field.isAnnotationPresent(DownloadField.class);
            if(annotationPresent){
                DownloadField annotation = field.getAnnotation(DownloadField.class);
                List<String> groups = Arrays.asList(annotation.group());
                if(b && groups != null && groups.size() > 0 && groups.contains(group)){
                    int index = groups.indexOf(group);
                    DownloadMetadata build = build(annotation, field, group, index);
                    downloadMetadatas.add(build);
                } else {
                    DownloadMetadata build = build(annotation, field, DEFAULT_GROUP, 0);
                    downloadMetadatas.add(build);
                }
            }
        });
        if(downloadMetadatas.size() <= 0){
            return downloadMetadatas;
        }
        List<DownloadMetadata> metadatas;
        if(b){
            metadatas = downloadMetadatas.stream().filter(x -> group.equals(x.getGroup())).collect(Collectors.toList());
        } else {
            metadatas = downloadMetadatas.stream().filter(x -> DEFAULT_GROUP.equals(x.getGroup())).collect(Collectors.toList());
        }
        metadatas = metadatas.stream().sorted(Comparator.comparing(DownloadMetadata::getOrder)).collect(Collectors.toList());
        return metadatas;
    }

    private static DownloadMetadata build(DownloadField annotation, Field field, String group, int index){
        DownloadMetadata downloadMetadata = new DownloadMetadata();
        downloadMetadata.setHeaderName(annotation.name());
        if(annotation.order().length < index){
            downloadMetadata.setOrder(annotation.order()[0]);
        } else {
            downloadMetadata.setOrder(annotation.order()[index]);
        }
        downloadMetadata.setGroup(group);
        downloadMetadata.setValueMethod(annotation.valueMethod());

        String fieldName = field.getName();
        downloadMetadata.setFieldName(fieldName);
        String methodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1,fieldName.length());
        downloadMetadata.setGetMethodName(methodName);
        return downloadMetadata;
    }
}