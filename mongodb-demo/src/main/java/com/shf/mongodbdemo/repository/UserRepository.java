package com.shf.mongodbdemo.repository;

import com.shf.mongodbdemo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
