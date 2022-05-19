package com.itheima.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.itheima.stock.common.domain.SysRoleDomain;
import com.itheima.stock.mapper.SysRoleMapper;
import com.itheima.stock.mapper.SysUserMapper;
import com.itheima.stock.pojo.SysPermission;
import com.itheima.stock.pojo.SysUser;
import com.itheima.stock.pojo.UserPage;
import com.itheima.stock.service.PermissionService;
import com.itheima.stock.service.UserService;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.vo.req.*;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.PermissionRespNodeVo;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
//          1.判断传入的参数的合法性
        if (vo == null || Strings.isNullOrEmpty(vo.getUsername()) || Strings.isNullOrEmpty(vo.getPassword())
                || Strings.isNullOrEmpty(vo.getRkey())) {
            //请求参数异常
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //2.根据用户名查询用户信息（包含了加密后的密文）
        SysUser user = sysUserMapper.getPasswordByname(vo.getUsername());
//        获取Redis缓存的验证码
        String code = (String) redisTemplate.opsForValue().get(vo.getRkey());
//        判断验证码是否为空或者验证码错误
        if (Strings.isNullOrEmpty(code) || !code.equals(vo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        //3.判断对应用户是否存在，以及密码是否匹配
        if (user == null || !passwordEncoder.matches(vo.getPassword(), user.getPassword())) {
            return R.error(ResponseCode.SYSTEM_PASSWORD_ERROR.getMessage());
        }
        redisTemplate.delete(vo.getRkey());
        LoginRespVo loginRespVo = new LoginRespVo();
        BeanUtils.copyProperties(user, loginRespVo);
        //获取指定用户的权限集合
        List<SysPermission> permissions = permissionService.getPermissionByUserId(user.getId());
        //获取树状权限菜单数据
        List<PermissionRespNodeVo> tree = permissionService.getTree(permissions, "0", true);
        //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        loginRespVo.setMenus(tree);
        loginRespVo.setPermissions(authBtnPerms);

        return R.ok(loginRespVo);
    }

    @Override
    public R<Map> captcha() {
//        1.生成四位数字验证码
        String code = RandomStringUtils.randomNumeric(4);
//        2.生成唯一ID
        String id = String.valueOf(idWorker.nextId());
//        存redis中
        redisTemplate.opsForValue().set(id, code, 60, TimeUnit.SECONDS);
        Map<String, String> map = new HashMap<>();
        map.put("rkey", id);
        map.put("code", code);
        return R.ok(map);
    }

    /**
     * 分页查看用户信息并搜索
     *
     * @param userReqVo
     * @return
     */
    @Override
    public R<PageResult<UserPage>> selectByUser(UserReqVo userReqVo) {
        PageHelper.startPage(userReqVo.getPageNum(), userReqVo.getPageSize());
        List<UserPage> users = sysUserMapper.selectByPageAllUser(userReqVo.getUsername(), userReqVo.getNickname(),
                userReqVo.getStartTime(), userReqVo.getEndTime());
        PageInfo<UserPage> PageInfo = new PageInfo<>(users);
        PageResult<UserPage> PageResult = new PageResult<>(PageInfo);
        return R.ok(PageResult);
    }

    /**
     * 添加用户
     *
     * @param userAddReqVo
     * @return
     */

    @Override
    public R<String> addUser(UserAddReqVo userAddReqVo) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userAddReqVo, user);
        user.setId(idWorker.nextId() + "");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPhone(user.getPhone());
        user.setEmail(user.getEmail());
        user.setNickName(user.getNickName());
        user.setRealName(user.getRealName());
        user.setSex(user.getSex());
        user.setCreateWhere(user.getCreateWhere());
        user.setStatus(user.getStatus());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int count = sysUserMapper.insert(user);
        if (count == 0) {
            return R.error(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    @Override
    public R<PageResult> selectUser(RolePageReqVo rolePageReqVo) {
        PageHelper.startPage(rolePageReqVo.getPageNum(), rolePageReqVo.getPageSize());
        List<SysRoleDomain> roles = sysRoleMapper.selectUser();
        PageInfo<SysRoleDomain> PageInfo = new PageInfo<>(roles);
        PageResult<SysRoleDomain> PageResult = new PageResult<>(PageInfo);
        return R.ok(PageResult);
    }


}
