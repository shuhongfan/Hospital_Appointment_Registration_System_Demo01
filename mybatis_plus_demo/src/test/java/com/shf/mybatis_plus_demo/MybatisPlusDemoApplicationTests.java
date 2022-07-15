package com.shf.mybatis_plus_demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shf.mybatis_plus_demo.entity.User;
import com.shf.mybatis_plus_demo.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisPlusDemoApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void findAll(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testAdd() {
        User user = new User();
        user.setName("lucy");
        user.setAge(20);
        user.setEmail("123@ss.com");

        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1547771425893679105L);
        user.setName("lucymary");
        int res = userMapper.updateById(user);
        System.out.println(res);
    }

    /**
     * 测试乐观锁
     */
    @Test
    public void  OptimisticLocker() {
//        根据id查询
        User user = userMapper.selectById(1547778904669044737L);
//        修改
        user.setName("张三");

        userMapper.updateById(user);
    }

    /**
     * 多个id批量查询
     */
    @Test
    public void testSelect1() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println(users);
    }

    /**
     * 通过map封装查询条件 不常用
     */
    @Test
    public void testSelect2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "jack");
        map.put("age", 20);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }


    /**
     * 分页查询
     */
    @Test
    public void testSelectPage() {
        /**
         * 创建page对象，传入两个参数，当前页、每页记录数
         */
        Page<User> page = new Page<>(1, 3);

        /**
         * 调用mp selectPage方法实现分页
         */
        Page<User> userPage = userMapper.selectPage(page, null);

//        返回对象得到分页的所有数据
//        总页数
        long pages = userPage.getPages();

//        当前页
        long current = userPage.getCurrent();

//        查询数据集合
        List<User> records = userPage.getRecords();

//        总记录数
        long total = userPage.getTotal();

//        下一页
        boolean hasNext = userPage.hasNext();

//        上一页
        boolean hasPrevious = userPage.hasPrevious();

        System.out.println("总页数:"+pages);
        System.out.println("当前页:"+current);
        System.out.println("查询数据集合:"+records);
        System.out.println("总记录数:"+total);
        System.out.println("下一页:"+hasNext);
        System.out.println("上一页:"+hasPrevious);
    }

    /**
     * 当指定了特定的查询列时，希望分页结果列表只返回被查询的列，而不是很多null值
     * 测试selectMapsPage分页：结果集是Map
     */
    @Test
    public void testSelectMapsPage() {
//Page不需要泛型
        Page<Map<String, Object>> page = new Page<>(1, 5);
        Page<Map<String, Object>> pageParam = userMapper.selectMapsPage(page, null);
        List<Map<String, Object>> records = pageParam.getRecords();
        records.forEach(System.out::println);

        System.out.println(pageParam.getCurrent());
        System.out.println(pageParam.getPages());
        System.out.println(pageParam.getSize());
        System.out.println(pageParam.getTotal());
        System.out.println(pageParam.hasNext());
        System.out.println(pageParam.hasPrevious());
    }


    /**
     * 根据id删除记录
     */
    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(5L);
        System.out.println(result);
    }

    /**
     * 批量删除
     */
    @Test
    public void testDeleteBatchIds() {
        int res = userMapper.deleteBatchIds(Arrays.asList(1, 32, 3));
        System.out.println(res);
    }

    /**
     * 简单条件删除
     */
    @Test
    public void testDeleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Helen");
        map.put("age", 18);
        int res = userMapper.deleteByMap(map);
        System.out.println(res);
    }

    /**
     * mp复杂查询操作
     */
    @Test
    public void testSelect(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 21); //大于等于 >=
        queryWrapper.gt("age", 21); //大于 >
        queryWrapper.le("age", 21); //小于 <
        queryWrapper.lt("age", 21); //小于等于 <=

        queryWrapper.eq("name", "tom");  //等于 =
        queryWrapper.ne("name", "tom");  //不等于 <>

        queryWrapper.between("age", 24, 28); // BETWEEN 值1 AND 值2
        queryWrapper.notBetween("age", 24, 28); // NOT BETWEEN 值1 AND 值2

        queryWrapper.like("name", "张"); //LIKE '%值%'
        queryWrapper.notLike("name", "张"); //NOT LIKE '%值%'
        queryWrapper.likeLeft("name", "张"); //LIKE '%值'
        queryWrapper.likeRight("name", "张"); //LIKE '值%'

        queryWrapper.isNull("name"); // 字段 IS NULL
        queryWrapper.isNotNull("name"); // 字段 IS NOT NULL

        queryWrapper.orderByDesc("age", "id"); //排序：ORDER BY 字段, ... DESC
        queryWrapper.orderByAsc("age", "id"); //排序：ORDER BY 字段, ... ASC
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }
}
