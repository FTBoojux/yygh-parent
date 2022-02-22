package com.boojux.yygh.cmn.controller;

import com.boojux.yygh.cmn.service.DictService;
import com.example.yygh.common.result.Result;
import com.example.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(description = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
public class CmnController {
    @Autowired
    private DictService dictService;
    @ApiOperation(value = "根据数据id查询子数据列表")

    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> childData = dictService.findChildData(id);
        return Result.ok(childData);
    }

    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);
    }

    @PostMapping("importData")
    public Result importData(MultipartFile file){
        dictService.importData(file);
        return Result.ok();
    }

    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value
    ){
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value
    ){
        String dictName = dictService.getDictName(null,value);
        return dictName;
    }

    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

}
