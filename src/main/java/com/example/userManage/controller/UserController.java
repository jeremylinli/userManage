package com.example.userManage.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.userManage.entity.User;
import com.example.userManage.service.UserService;
import com.example.userManage.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author linlihao
 * @since 2023-02-28
 */

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation("条件分页查询")
    @PostMapping("findPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           @RequestBody User user) {
        Page<User> pageParam = new Page<>(current, limit);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(user.getUserName())) {
            queryWrapper.like("user_name", user.getUserName());
        }
        if (StringUtils.isNotEmpty(user.getSex())) {
            queryWrapper.eq("sex", user.getSex());
        }
        IPage<User> userPage = userService.page(pageParam, queryWrapper);
        return Result.ok(userPage);
    }

    @ApiOperation("新建一个用户")
    @PostMapping("addUser")
    public Result addUser(@RequestBody User user) {
        boolean res = userService.save(user);
        if (res) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation("删除一个用户")
    @DeleteMapping("delUser/{id}")
    public Result delUser(@PathVariable String id) {
        boolean res = userService.removeById(id);
        if (res) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation("批量删除用户")
    @PutMapping("delBatch")
    public Result delBatch(@RequestBody List<String> idList) {
        boolean res = userService.removeByIds(idList);
        if (res) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation("修改一个用户")
    @PutMapping("updateUser")
    public Result updateUser(@RequestBody User user) {
        boolean res = userService.updateById(user);
        if (res) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

}

