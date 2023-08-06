package cn.itcast.jd.impl;

import cn.itcast.jd.dao.ItemDao;
import cn.itcast.jd.pojo.Item;
import cn.itcast.jd.service.ItemService;
import org.springframework.data.domain.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    public void save(Item item) {
        this.itemDao.save(item);
    }

    @Override
    public List<Item> findAll(Item item) {
        // 声明查询条件
        Example<Item> example = Example.of(item);

        // 根据查询条件进行数据查询
        List<Item> list = this.itemDao.findAll(example);

        return list;
    }
}
