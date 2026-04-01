package com.smartcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcart.entity.ContactQuery;

public interface ContactQueryRepository extends JpaRepository<ContactQuery, Long> {
}