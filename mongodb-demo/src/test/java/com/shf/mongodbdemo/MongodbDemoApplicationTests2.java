package com.shf.mongodbdemo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.shf.mongodbdemo.entity.User;
import com.shf.mongodbdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class MongodbDemoApplicationTests2 {

    /**
     * 注入UserRepository
     */
    @Autowired
    private UserRepository userRepository;


    //    添加操作
    @Test
    public void create() {
        User user = new User();
        user.setAge(18);
        user.setName("lucy");
        user.setEmail("lucy@fre.com");

        User user1 = userRepository.save(user);
        System.out.println(user1);
    }

//    查询所有记录
    @Test
    public void findAll() {
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }

    //    通过id查询
    @Test
    public void findId() {
        User user = userRepository.findById("62d37fe4b8886c558ff6e9f0").get();
        System.out.println(user);
    }

    //    条件查询
    @Test
    public void findUserList() {
        User user = new User();
        user.setAge(30);
        user.setName("mary");

        Example<User> userExample = Example.of(user);

        List<User> all = userRepository.findAll(userExample);
        System.out.println(all);
    }

    //    模糊查询
    @Test
    public void findLikeUserList() {
//        设置模糊匹配的查询条件
        ExampleMatcher matcher =
                ExampleMatcher
                        .matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase(true);

        User user = new User();
        user.setName("m");

        Example<User> userExample = Example.of(user,matcher);

        List<User> all = userRepository.findAll(userExample);
        System.out.println(all);
    }


    //    分页查询
    @Test
    public void findPageUserList() {
//        设置分页参数
//        0 代表第一页
        PageRequest pageRequest = PageRequest.of(0, 3);

        User user = new User();
        user.setAge(30);
        user.setName("mary");

        Example<User> userExample = Example.of(user);

        Page<User> page = userRepository.findAll(userExample, pageRequest);
        System.out.println(page);
    }

    /**
     * 修改
     */
    @Test
    public void updateUser() {
        User user = userRepository.findById("62d38766c029e67cd7da5dc4").get();
        user.setAge(100);
        user.setName("lucy_111");
        user.setEmail("223@e.co");
        User save = userRepository.save(user);
        System.out.println(save);
    }

    /**
     * 删除
     */
    @Test
    public void deleteUser() {
        userRepository.deleteById("62d38766c029e67cd7da5dc4");
    }
}
