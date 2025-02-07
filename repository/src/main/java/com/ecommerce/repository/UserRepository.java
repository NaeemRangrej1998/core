package com.ecommerce.repository;

import com.ecommerce.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

//    @Query(value = "select * from user where`user`.status = 'true' and `user`.deactivate = 'false' and (user.first_name like %:searchValue% or `user`.last_name like %:searchValue% or `user`.email like %:searchValue% or `user`.role_name like %:searchValue%)",
//            nativeQuery = true);
//    Page<UserEntity> getUserByStatusAndDeactivate(Boolean status, Boolean deactivate, Pageable pageable,String Search);
    Optional<UserEntity> getUserByEmail(String email);

    @Query(value = "SELECT * FROM user u WHERE u.status = :status AND u.deactivate = :deactivate " +
            "AND (u.first_name LIKE %:search% OR u.last_name LIKE %:search% OR u.email LIKE %:search% OR u.role_name LIKE %:search%)",
            countQuery = "SELECT COUNT(*) FROM user u WHERE u.status = :status AND u.deactivate = :deactivate " +
                    "AND (u.first_name LIKE %:search% OR u.last_name LIKE %:search% OR u.email LIKE %:search% OR u.role_name LIKE %:search%)",
            nativeQuery = true)
    Page<UserEntity> getUserByStatusAndDeactivate(@Param("status") Boolean status,
                                                  @Param("deactivate") Boolean deactivate,
                                                  @Param("search") String search,
                                                  Pageable pageable);

}
