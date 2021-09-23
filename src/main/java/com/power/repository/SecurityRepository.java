package com.power.repository;

import com.power.domain.SecurityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityRepository extends JpaRepository<SecurityData, Long> {
}
