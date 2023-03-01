package com.example.userManage.service.impl;

import com.example.userManage.entity.User;
import com.example.userManage.mapper.UserMapper;
import com.example.userManage.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linlihao
 * @since 2023-02-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
