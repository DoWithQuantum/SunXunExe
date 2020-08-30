package com.atguigu.msmservice.service;

import java.util.HashMap;
import java.util.Map;

public interface MsmService {
    boolean send(HashMap<String, Object> param, String phone);
}
