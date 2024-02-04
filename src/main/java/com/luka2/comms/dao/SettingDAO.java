package com.luka2.comms.dao;

import com.luka2.comms.models.Setting;
import com.luka2.comms.repositories.SettingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class SettingDAO {
    private final SettingRepository settingRepository;

    public SettingDAO(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Setting get(){
        List<Setting> all = settingRepository.findAll();

        if(all.isEmpty()) throw new RuntimeException("No settings records in DB");
        else if(all.size() > 1) throw new RuntimeException("Multiple settings records in DB");

        return all.get(0);
    }
}
