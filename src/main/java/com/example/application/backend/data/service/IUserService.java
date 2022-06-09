package com.example.application.backend.data.service;

import com.example.application.backend.data.entity.Tracking;
import com.example.application.backend.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findById(Long id);

    void deleteById(Long id);

    User update(User entity);

    List<User> findAll();

    List<User> orderByUserId();

    LocalDateTime getLastAttendance(User user);

    byte[] generatePDF(User user);
}