package com.maxent.oms.monitor.service;

import com.maxent.oms.monitor.model.TriggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TriggerConfigService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public TriggerConfig getTriggerConfig(String tplTriggerId) {

		return jdbcTemplate.queryForObject("select  * from zag_trigger_config where tpl_trigger_id = ? ",
				new BeanPropertyRowMapper<>(TriggerConfig.class), tplTriggerId);
	}

}
