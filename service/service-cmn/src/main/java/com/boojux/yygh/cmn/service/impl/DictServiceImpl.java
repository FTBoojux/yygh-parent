package com.boojux.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boojux.yygh.cmn.listener.DictListener;
import com.boojux.yygh.cmn.mapper.DictMapper;
import com.boojux.yygh.cmn.service.DictService;
import com.example.yygh.model.cmn.Dict;
import com.example.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    @Cacheable(value = "dict",keyGenerator="keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        for (Dict dict : dicts) {
            dict.setHasChildren(this.ifChildren(dict.getId()));
        }
        return dicts;
    }

    @Override
    @CacheEvict(value = "dict",allEntries = true)
    public void exportData(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictEeVos = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo);
                dictEeVos.add(dictEeVo);
            }
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVos);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if(StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return null == dict?null:dict.getName();
        }else{
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long id = codeDict.getId();
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", id).eq("value", value));
            return dict.getName();
        }
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = this.getDictByDictCode(dictCode);
        return this.findChildData(dict.getId());
    }

    //    判断有无子节点
    private boolean ifChildren(Long id){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id",id);
        Integer selectCount = baseMapper.selectCount(wrapper);
        return selectCount>0;
    }

    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }
}
