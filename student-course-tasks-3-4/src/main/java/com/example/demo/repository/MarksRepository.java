package com.example.demo.repository;

import com.example.demo.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarksRepository extends JpaRepository<Marks, Long> {
}
