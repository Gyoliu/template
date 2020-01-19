package com.youkeshu.hr.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: liuxing
 * @Date: 2018/8/16 9:29
 * @Description:
 */
public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static List<DownloadMetadata> isCanWrite(Class aClass, String group){
        try {
            Assert.notNull(aClass, "class 不能为空！");
            List<DownloadMetadata> parse = DownloadFieldAnnotationParse.parse(aClass, group);
            return parse;
        } catch (IllegalAccessException e) {
            logger.error("DownloadFieldAnnotationParse error!{}", e.getMessage());
        }
        return null;
    }

    public static Workbook writeExcel(JSONArray jsonArray, Class aClass, String group, String suffix) {
        List<DownloadMetadata> parse = ExcelUtils.isCanWrite(aClass, group);
        if(IDownloadMetadataExtend.class.isAssignableFrom(aClass)){
            try {
                IDownloadMetadataExtend iDownloadMetadataExtend = (IDownloadMetadataExtend) aClass.newInstance();
                parse = iDownloadMetadataExtend.doExtend(jsonArray, parse);
            } catch (InstantiationException e) {
                logger.error("doExtend InstantiationException:{}", e);
            } catch (IllegalAccessException e) {
                logger.error("doExtend IllegalAccessException:{}", e);
            }
        }
        if(CollectionUtils.isEmpty(parse) || ObjectUtils.isEmpty(jsonArray) || jsonArray.size() <= 0){
            logger.error("downloadMetadata is null, download fail");
            return null;
        }

        Workbook workbook = null;
        if(jsonArray.size() > 10000){
            workbook = new SXSSFWorkbook();
        } else if(".xlsx".equals(suffix)){
            workbook = new XSSFWorkbook();//.xlsx
        } else if(".xls".equals(suffix)) {
            workbook = new HSSFWorkbook();//.xls
        } else {
            new RuntimeException("不支持的下载格式！");
        }
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        for (int i = 0;i<parse.size();i++){
            DownloadMetadata downloadMetadata = parse.get(i);
            row.createCell(i).setCellValue(downloadMetadata.getHeaderName());
        }

        for (int i = 0;i < jsonArray.size();i++){
            Row row1 = sheet.createRow(i + 1);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (int j = 0;j<parse.size();j++){
                DownloadMetadata downloadMetadata = parse.get(j);
                Cell cell = row1.createCell(j);
                ExcelUtils.setCellValue(cell, jsonObject, downloadMetadata, aClass);
            }
        }
        return workbook;
    }

    private static void setCellValue(Cell cell, JSONObject jsonObject, DownloadMetadata downloadMetadata, Class rowClass){
        String value = "";
        Object o = jsonObject.get(downloadMetadata.getFieldName());
        value = o==null?"":o.toString();
        if(o instanceof Date){
            value = DateFormatUtils.format((Date) o, DATE_FORMAT);
        }

        String valueMethodStr = downloadMetadata.getValueMethod();
        if(StringUtils.isNotEmpty(valueMethodStr)) {
            Class<?> aClass1 = String.class;
            if(o != null){
                aClass1 = o.getClass();
            }
            boolean isStatic = valueMethodStr.startsWith("static");
            if(isStatic){
                //如果是静态方法
                valueMethodStr = valueMethodStr.replace("static", "").trim();
            }

            int i = valueMethodStr.lastIndexOf(".");
            String packageClassPath = valueMethodStr.substring(0, i == -1? 0 : i);
            String valueMethodName = valueMethodStr.substring(i + 1, valueMethodStr.length());
            Method valueMethod;
            if("this".equals(packageClassPath) || StringUtils.isEmpty(packageClassPath)){
                //非包路径，默认获取row class 里面的方法执行
                valueMethod = ReflectionUtils.findMethod(rowClass, valueMethodName, aClass1);
                value = (String)invokeMethod(rowClass, valueMethod, isStatic, o);
            } else {
                //指定包路径，就先Class.forName 获取class
                Class valueMethodClass = getValueMethodClass(packageClassPath);
                //在根据valueMethodName找到Method
                valueMethod = ReflectionUtils.findMethod(valueMethodClass, valueMethodName, aClass1);
                if(valueMethod != null){
                    value = (String)invokeMethod(valueMethodClass, valueMethod, isStatic, o);
                } else {
                    //如果没有找到匹配的方法就 找参数为map的方法 把当前行数据作为参数传入
                    valueMethod = ReflectionUtils.findMethod(valueMethodClass, valueMethodName, Map.class);
                    if(valueMethod != null){
                        value = (String)invokeMethod(valueMethodClass, valueMethod, isStatic, jsonObject.toJavaObject(Map.class));
                    } else {
                        valueMethod = ReflectionUtils.findMethod(valueMethodClass, valueMethodName, Map.class, DownloadMetadata.class);
                        if(valueMethod == null){
                            //再调用
                            value = (String)invokeMethod(valueMethodClass, valueMethod, isStatic, jsonObject.toJavaObject(Map.class), downloadMetadata);
                        }
                    }
                }
            }
        }
        cell.setCellValue(value);
    }

    private static Class getValueMethodClass(String cls){
        Class<?> aClass = null;
        try {
            aClass = Class.forName(cls);
        } catch (ClassNotFoundException e) {
            logger.error("ExcelUtils set value method class not found, please check annotation @Download param valueMethod !");
        }
        return aClass;
    }

    private static Object invokeMethod(Class aClass, Method valueMethod, boolean isStatic, Object... o){
        if(valueMethod == null){return "";}
        try {
            Object value;
            if(isStatic){
                value = ReflectionUtils.invokeMethod(valueMethod, null, o);
            } else {
                value = ReflectionUtils.invokeMethod(valueMethod, aClass.newInstance(), o);
            }
            return String.valueOf(value);
        } catch (InstantiationException e) {
            logger.error("getCellValue InstantiationException error!{}", e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("getCellValue IllegalAccessException error!{}", e.getMessage());
        }
        return "";
    }

}