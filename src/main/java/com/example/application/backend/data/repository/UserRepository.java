package com.example.application.backend.data.repository;

import com.example.application.backend.data.entity.Status;
import com.example.application.backend.data.entity.Tracking;
import com.example.application.backend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    @Query(value = "select * from user u join tracking pd on pd.user_user_id = u.user_id",
            nativeQuery = true)
    List<Tracking> findTrackingByUser(User user);


    @Query(value = "select * from user u order by u.id",
            nativeQuery = true)
    List<User> orderByUserId();

    List<User> findAllByName(String firstName);

    List<User> findAllByNameAndSurname(String firstName, String surname);

    List<User> findAllByEmail(String email);

    List<User> findAllByStatus(Status status);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query(value="select * FROM tracking where user_user_id = :userId ORDER BY user_user_id DESC LIMIT 1",
    nativeQuery = true)
    String findLatestUserTracking(@Param("userId") Long userId);

}