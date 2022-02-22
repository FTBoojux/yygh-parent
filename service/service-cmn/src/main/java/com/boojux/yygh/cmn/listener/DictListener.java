package com.boojux.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.boojux.yygh.cmn.mapper.DictMapper;
import com.example.yygh.model.cmn.Dict;
import com.example.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DictListener extends AnalysisEventListener<DictEeVo> {
    DictMapper dictMapper;
    public DictListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
