package com.yww.nexus.moudles.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.nexus.moudles.sys.entity.Menu;
import com.yww.nexus.moudles.sys.mapper.MenuMapper;
import com.yww.nexus.moudles.sys.service.IUserService;
import com.yww.nexus.moudles.sys.service.IMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *      菜单权限实体类 服务实现类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final IUserService userService;

}

