package com.luka2.comms.repositories;

import com.luka2.comms.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    Optional<Setting> findById(Long id);

}
