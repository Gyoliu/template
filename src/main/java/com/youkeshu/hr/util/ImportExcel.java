package com.youkeshu.hr.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: liuxing
 * @Date: 2018/11/5 9:48
 * @Description:
 */
public class ImportExcel {

    private static Logger logger = LoggerFactory.getLogger(ImportExcel.class);

    private Workbook workbook;
    private Sheet sheet;
    private int headerNum;
    private int starterCellNum = 0;
    private StringBuilder errorMessage;

    public StringBuilder getErrorMessage() {
        return errorMessage;
    }

    public ImportExcel(String fileName, int headerNum, int starterCellNum) throws IOException {
        this(new File(fileName), headerNum, starterCellNum);
    }

    public ImportExcel(File file, int headerNum, int starterCellNum) throws IOException {
        this((File)file, headerNum, 0, starterCellNum);
    }

    public ImportExcel(String fileName, int headerNum, int sheetIndex, int starterCellNum) throws IOException {
        this(new File(fileName), headerNum, sheetIndex, starterCellNum);
    }

    public ImportExcel(File file, int headerNum, int sheetIndex, int starterCellNum) throws IOException {
        this(file.getName(), new FileInputStream(file), headerNum, sheetIndex, starterCellNum);
    }

    /**
     *
     * @param multipartFile 上传文件
     * @param headerNum 标题占几行
     * @param sheetIndex 解析第几个sheet
     * @throws IOException
     */
    public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex, int starterCellNum) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex, starterCellNum);
    }
    public ImportExcel(MultipartFile multipartFile) throws IOException {
        this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), 0, 0,0);
    }

    public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex, int starterCellNum) throws IOException {
        if(StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("导入文档为空!");
        } else {
            if(fileName.toLowerCase().endsWith("xls")) {
                this.workbook = new HSSFWorkbook(is);
            } else if(fileName.toLowerCase().endsWith("xlsx")) {
                this.workbook = new XSSFWorkbook(is);
            } else {
                throw new RuntimeException("文档格式不正确!");
            }
            if(this.workbook.getNumberOfSheets() < sheetIndex) {
                throw new RuntimeException("文档中没有工作表!");
            } else {
                this.sheet = this.workbook.getSheetAt(sheetIndex);
                this.headerNum = headerNum;
                this.starterCellNum = starterCellNum;
                errorMessage = new StringBuilder();
                logger.info("ImportExcel Initialize success.");
            }
        }
    }

    private List<UploadMetaData> parseAnnotation(Class<?> aClass, int group){
        Field[] declaredFields = aClass.getDeclaredFields();
        ArrayList<UploadMetaData> annotationList = new ArrayList<>();
        Arrays.stream(declaredFields).forEach((Field field) -> {
            boolean annotationPresent = field.isAnnotationPresent(UploadField.class);
            if(annotationPresent){
                UploadField annotation = field.getAnnotation(UploadField.class);
                int order = annotation.order();
                boolean required = annotation.required();
                //过滤某个组的字段
                int[] group1 = annotation.group();
                long count = Arrays.stream(group1).filter(x -> x == group).count();
                if(count > 0){
                    UploadMetaData uploadMetaData = new UploadMetaData();
                    uploadMetaData.setOrder(order);
                    uploadMetaData.setGroup(group);
                    uploadMetaData.setRequired(required);
                    uploadMetaData.setField(field);
                    uploadMetaData.setValidate(annotation.validate());
                    annotationList.add(uploadMetaData);
                }
            }
        });
        if(CollectionUtils.isEmpty(annotationList)){
            logger.error("{} please use annotations @UploadField.", aClass.getName());
            return null;
        }
        if(this.sheet.getLastRowNum() < 1){
            throw new RuntimeException("excel中没有数据!");
        } else if(this.sheet.getLastRowNum() < headerNum){
            throw new RuntimeException("模板错误，请重新下载模板上传!");
        } else if(this.sheet.getRow(headerNum).getLastCellNum() < 1){
            throw new RuntimeException("excel中没有有效数据!");
        } else if(this.sheet.getRow(headerNum).getLastCellNum() < annotationList.size()){
            throw new RuntimeException("模板错误，请重新下载模板上传!");
        }
        Collections.sort(annotationList, (o1, o2) -> new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder())));
        return annotationList;
    }

    public <E> List<E> getDataList(Class<E> aClass, int group) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("excel parse begin.");
        List<UploadMetaData> annotationList = this.parseAnnotation(aClass, group);
        ArrayList targets = new ArrayList<E>();
        for(int i = this.getDataRowNum(); i <= this.getLastDataRowNum(); i++){
            Row row = this.getRow(i);
            if(row == null){continue;}
            Object target = aClass.newInstance();
            int z = 0;
            for(int j=this.getStarterCellNum();j < row.getLastCellNum();j++){
                if(z >= annotationList.size() ){
                    continue;
                }
                final int tmp = j;
                Optional<UploadMetaData> first = annotationList.stream().filter(x -> x.getOrder() == tmp).findFirst();
                if(!first.isPresent()){
                    continue;
                }
                UploadMetaData uploadMetaData = annotationList.get(z);
                z++;
                Field field = uploadMetaData.getField();
                field.setAccessible(true);
                try{
                    Object cellValue = this.getCellValue(row, j, field.getType());
                    if(uploadMetaData.isRequired() && ObjectUtils.isEmpty(cellValue)){
                        errorMessage.append("第").append(i+1).append("行").append(j+1).append("列不能为空. \n");
                    }
                    field.set(target, cellValue);
                } catch (ParseException ex) {
                    logger.error("upload get value error:" + ex.getMessage());
                    errorMessage.append("第").append(i+1).append("行").append(j+1).append("列:").append("时间格式错误").append("\n");
                } catch (Exception ex) {
                    logger.error("upload get value error:" + ex.getMessage());
                    errorMessage.append("第").append(i+1).append("行").append(j+1).append("列:").append(ex.getMessage()).append("\n");
                }
            }
            if(IExcelValidate.class.isAssignableFrom(aClass)){
                IExcelValidate target1 = (IExcelValidate) target;
                String validate = target1.validate(target);
                if(!StringUtils.isEmpty(validate)){
                    errorMessage.append("第").append(i+1).append("行").append(validate);
                }
            }
            targets.add(target);
        }
        stopWatch.stop();
        logger.info("excel解析耗时{}毫秒", stopWatch.getTotalTimeMillis());
        return targets;
    }

    private Row getRow(int rownum) {
        return this.sheet.getRow(rownum);
    }

    private int getDataRowNum() {
        return this.headerNum;
    }

    private int getLastDataRowNum() {
        return this.sheet.getLastRowNum();
    }

    private int getStarterCellNum() {
        return this.starterCellNum;
    }

    private <T> T getCellValue(Row row, int column, Class<T> aClass) throws ParseException {
        Object value = "";
        Cell cell = row.getCell(column);
        if(cell == null) {
            return (T)value;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    DecimalFormat df = new DecimalFormat("0");
                    value = df.format(cell.getNumericCellValue());
                } else {
                    try {
                        value = cell.getRichStringCellValue().toString();
                    } catch (IllegalStateException e) {
                        try {
                            DecimalFormat df = new DecimalFormat("0");
                            value = df.format(cell.getNumericCellValue());
                        }catch (IllegalStateException e1){
                            value = cell.toString();
                        }
                    }
                }
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            case Cell.CELL_TYPE_FORMULA:
                String cellFormula = cell.getCellFormula();
                if(cell.getClass().equals(XSSFCell.class)){
                    XSSFFormulaEvaluator xssfFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) this.workbook);
                    String stringValue = xssfFormulaEvaluator.evaluate(cell).getStringValue();
                    value = stringValue;
                } else if(cell.getClass().equals(HSSFCell.class)){
                    HSSFFormulaEvaluator hssfFormulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) this.workbook);
                    String stringValue = hssfFormulaEvaluator.evaluate(cell).getStringValue();
                    value = stringValue;
                } else {
                    value = "";
                }
                break;
            default:
                value = "";
                break;
        }
        Object ovalue = "";
        if(aClass.equals(Integer.class)){
            ovalue = Integer.parseInt(value.toString());
        } else if(aClass.equals(String.class)){
            if(value.getClass().equals(Date.class)){
                ovalue = DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:sss");
            } else {
                ovalue = value.toString().trim();
            }
        } else if(aClass.equals(Date.class)){
            if(value.getClass().equals(Date.class)){
                ovalue = value;
            } else {
                if(!StringUtils.isEmpty(value.toString())){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
                    ovalue = simpleDateFormat.parse(value.toString());
                } else {
                    ovalue = null;
                }
            }
        }
        return (T)ovalue;
    }


}