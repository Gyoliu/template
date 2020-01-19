package com.youkeshu.hr.util;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2019/9/5 15:21
 * @Description: 扩展下载的元数据
 */
@FunctionalInterface
public interface IDownloadMetadataExtend {

    /**
     *
     * @param jsonArray 下载的数据
     * @param downloadMetadatas 已经解析好的元数据
     * @param o SpringContextHolder 支持Spring Bean调用
     * @return
     */
    List<DownloadMetadata> doExtend(JSONArray jsonArray, List<DownloadMetadata> downloadMetadatas);

}