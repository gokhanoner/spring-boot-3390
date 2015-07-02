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
	public void saveToBD(Notification notification, String lstnr) {
		jdbcTemplate.update("insert into notifications(id, lstnr, message) values (?,?,?)", notification.getId(), lstnr, notification.getMessage());
	}
	
	@Transactional
	public int getNotificationCount(int id,  String lstnr) {
		return jdbcTemplate.queryForObject("select count(*) from notifications where id = ? and lstnr = ?", Integer.class, id, lstnr);
	}
}
