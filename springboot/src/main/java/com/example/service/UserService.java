package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.common.Constants.USER_DEFAULT_PASSWORD;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void add(User user) {
        User dbuser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbuser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    public PageInfo<User> selcetPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list =  userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    //登录
    public User login(Account account) {
        User user = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!user.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        //生成token
        String token = TokenUtils.createToken(user.getId() + "-" + user.getRole(), user.getPassword());
        user.setToken(token);
        return user;
    }

    public void updatePassword(Account account) {
        User dbuser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbuser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbuser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbuser.setPassword(account.getNewPassword());
        userMapper.updateById(dbuser);
    }
}
