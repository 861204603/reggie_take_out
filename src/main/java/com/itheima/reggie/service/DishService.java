package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入对应口味数据，操作两张表dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，并更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
