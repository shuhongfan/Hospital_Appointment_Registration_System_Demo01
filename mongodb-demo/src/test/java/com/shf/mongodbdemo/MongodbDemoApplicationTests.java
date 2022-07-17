package com.shf.mongodbdemo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.shf.mongodbdemo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class MongodbDemoApplicationTests {

    /**
     * 注入MongoTemplate
     */
    @Autowired
    private MongoTemplate mongoTemplate;


    //    添加操作
    @Test
    public void create() {
        User user = new User();
        user.setAge(20);
        user.setName("test");
        user.setEmail("111@ee.com");

        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

//    查询所有记录
    @Test
    public void findAll() {
        List<User> all = mongoTemplate.findAll(User.class);
        System.out.println(all);
    }

    //    通过id查询
    @Test
    public void findId() {
        User user = mongoTemplate.findById("62d37fe4b8886c558ff6e9f0", User.class);
        System.out.println(user);
    }

    //    条件查询
    @Test
    public void findUserList() {
        Query query = new Query(
                Criteria
                        .where("name").is("test")
                        .and("age").is(20));

        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    //    模糊查询
    @Test
    public void findLikeUserList() {
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(
                Criteria
                        .where("name").regex(pattern)
        );

        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }


    //    分页查询
    @Test
    public void findPageUserList() {
        int pageNo = 1;
        int pageSize = 3;

//        条件构建
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(
                Criteria
                        .where("name").regex(pattern)
        );

//        分页构建
        long count = mongoTemplate.count(query, User.class);

//        分页            开始位置 =（当前页-1）* 每页记录数
        List<User> users =
                mongoTemplate.find(query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
        System.out.println(users);
    }

    /**
     * 修改
     */
    @Test
    public void updateUser() {
//        根据id查询
        User user = mongoTemplate.findById("62d37fe4b8886c558ff6e9f0", User.class);

//        设置修改值
        user.setName("testnew");
        user.setAge(50);
        user.setEmail("dewew@ww.com");

//        调用方法实现修改
        Query query = new Query(
                Criteria.where("_id").is(user.getId())
        );

        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());

        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);

        long modifiedCount = upsert.getModifiedCount();
        System.out.println(modifiedCount);

        findId();
    }

    /**
     * 删除
     */
    @Test
    public void deleteUser() {
        Query query = new Query(Criteria.where("_id").is("62d37fe4b8886c558ff6e9f0"));
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        long deletedCount = remove.getDeletedCount();
        System.out.println(deletedCount);
    }
}
