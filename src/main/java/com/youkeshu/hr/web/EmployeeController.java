package com.youkeshu.hr.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youkeshu.hr.dto.EmployeeDto;
import com.youkeshu.hr.dto.ResCode;
import com.youkeshu.hr.dto.Result;
import com.youkeshu.hr.service.IEmployeeService;
import com.youkeshu.hr.util.DownloadFieldAnnotationParse;
import com.youkeshu.hr.util.ExcelUtils;
import com.youkeshu.hr.util.ImportExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @Author: liuxing
 * @Date: 2020/1/16 18:36
 * @Description:
 */
@Slf4j
@RestController
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @RequestMapping(value = "/api/employee/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result upload(@RequestParam("file") MultipartFile multipartFile) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        ImportExcel importExcel = new ImportExcel(multipartFile, 1, 0, 1);
        List<EmployeeDto> dataList = importExcel.getDataList(EmployeeDto.class, 0);
        StringBuilder errorMessage = importExcel.getErrorMessage();
        if(errorMessage == null || !StringUtils.isEmpty(errorMessage.toString())){
            return new Result(400, errorMessage.toString());
        }
        Result result = employeeService.insertBatch(dataList);
        return result;
    }

    @RequestMapping(value = "/api/employee/export", method = RequestMethod.POST)
    public void export(@RequestBody EmployeeDto employeeDto, HttpServletResponse response) throws IOException {
        employeeDto.setPage(-1);
        Result list = employeeService.list(employeeDto);
        if(list == null || list.getData() == null){
            return;
        }
        String fileName = "员工数据" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmsss");
        JSONArray jsonArray = (JSONArray)list.getData();
        if(CollectionUtils.isEmpty(jsonArray)){
            log.info("导出数据为空");
            return;
        }
        Workbook workbook = ExcelUtils.writeExcel(jsonArray, EmployeeDto.class, DownloadFieldAnnotationParse.DEFAULT_GROUP, ".xlsx");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
        log.info("数据导出成功：{}", fileName);
    }

    @RequestMapping(value = "/api/employee/list", method = RequestMethod.POST)
    public Result list(@RequestBody EmployeeDto employeeDto){
        Result result = employeeService.list(employeeDto);
        return result;
    }

    @RequestMapping(value = "/api/employee/update", method = RequestMethod.POST)
    public Result update(@RequestBody EmployeeDto employeeDto){
        Result result = employeeService.update(employeeDto);
        return result;
    }

    @RequestMapping(value = "/api/employee/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody EmployeeDto employeeDto){
        Result result = employeeService.insert(employeeDto);
        return result;
    }

    @RequestMapping(value = "/api/employee/insertBatch", method = RequestMethod.POST)
    public Result insertBatch(@RequestBody List<EmployeeDto> employeeDto){
        Result result = employeeService.insertBatch(employeeDto);
        return result;
    }

}