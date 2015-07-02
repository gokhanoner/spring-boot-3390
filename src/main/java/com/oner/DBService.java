package com.oner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DBService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public void saveToBD(Notification notification) {
		jdbcTemplate.update("insert into notifications(id, message) values (?,?)", notification.getId(), notification.getMessage());
	}
	
	@Transactional
	public int getNotificationCount(int id) {
		return jdbcTemplate.queryForObject("select count(*) from notifications where id = ?", Integer.class, id);
	}
}
