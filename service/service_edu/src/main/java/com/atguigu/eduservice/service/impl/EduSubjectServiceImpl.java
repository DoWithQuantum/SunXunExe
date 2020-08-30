package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneLevelSubject;
import com.atguigu.eduservice.entity.subject.TwoLevelSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-07-31
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    //通过easyExcel读取上传文件保存到数据库中
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectExcelListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {

        }

    }

    @Override
    public List<OneLevelSubject> getAllLevelSubject() {
        //parent_Id为0 查出所有一级分类对象 EduSubject
        QueryWrapper<EduSubject> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", 0);
        List<EduSubject> oneLevelEduSubjectList = baseMapper.selectList(wrapper1);

        //parent_Id不为0 查出所有二级分类对象 EduSubject
        QueryWrapper<EduSubject> wrapper2 = new QueryWrapper<>();
        wrapper2.ne("parent_id", 0);
        List<EduSubject> twoLevelEduSubjectList = baseMapper.selectList(wrapper2);

        //创建一个一份分类的集合用于封装最终返回的List<OneLevelSubject>集合
        ArrayList<OneLevelSubject> finalSubjectList = new ArrayList<>();

        //遍历一级数据库对象列表 转换成OneSubjectSubject格式后添加进最终的List中
        for (int i = 0; i < oneLevelEduSubjectList.size(); i++) {
            EduSubject oneLevelEduSubject = oneLevelEduSubjectList.get(i);
            OneLevelSubject oneLevelSubject = new OneLevelSubject();
            //重点记住 @BeanUtils的copyProperties方法
            BeanUtils.copyProperties(oneLevelEduSubject, oneLevelSubject);

            //用于封装二级分类
            ArrayList<TwoLevelSubject> twoLevelSubjectList = new ArrayList<>();

            for (int j = 0; j < twoLevelEduSubjectList.size(); j++) {
                EduSubject twoLevelEduSubject = twoLevelEduSubjectList.get(j);
                //判断二级目录pid与一级目录的id是否相同
                if (twoLevelEduSubject.getParentId().equals(oneLevelEduSubject.getId())) {
                    //建立一个临时二级目录类，方便将二级数据库类与它进行赋值后添加到 twoLevelEduSubjectList 中
                    TwoLevelSubject twoLevelSubject = new TwoLevelSubject();
                    BeanUtils.copyProperties(twoLevelEduSubject, twoLevelSubject);
                    twoLevelSubjectList.add(twoLevelSubject);
                }
            }
            //将所有二级分类的封装集合添加到一级分类对象中
            oneLevelSubject.setChildren(twoLevelSubjectList);
            //将封装了二级分类的一级分类添加到最终的集合中
            finalSubjectList.add(oneLevelSubject);
        }
        return finalSubjectList;
    }
}
