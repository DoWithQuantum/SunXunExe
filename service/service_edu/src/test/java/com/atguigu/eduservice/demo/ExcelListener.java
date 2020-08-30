package com.atguigu.eduservice.demo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoData> {
    List<DemoData> list = new ArrayList<>();

    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("333333333" + demoData);
        list.add(demoData);
    }

    //用于读取表头内容
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息：" + headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
