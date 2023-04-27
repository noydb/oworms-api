package com.oworms.auth.repository;

import com.oworms.auth.domain.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<AppSettings, String> {
}
