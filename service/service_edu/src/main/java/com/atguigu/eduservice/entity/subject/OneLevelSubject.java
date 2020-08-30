package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneLevelSubject {
    private String id;
    private String title;
    private List<TwoLevelSubject> children = new ArrayList<>();
}
