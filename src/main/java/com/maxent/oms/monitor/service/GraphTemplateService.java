package com.maxent.oms.monitor.service;

import com.maxent.oms.monitor.model.GraphTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by haiquanli on 16/6/20.
 */
@Service
public class GraphTemplateService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void addGraphTemplate(GraphTemplate graphTemplate) {
        String sql = "insert into graph_template (name,type) values (?,?) ";
        jdbcTemplate.update(sql, graphTemplate.getName(), graphTemplate.getType());
    }

    @Transactional
    public void deleteGraphTemplate(Long id) {
        String sql = "delete from graph_template where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void updateGraphTemplate(Long id, GraphTemplate graphTemplate) {
        String sql = "update  graph_template set name = ? ,type = ? where id = ?  ";
        jdbcTemplate.update(sql, graphTemplate.getName(), graphTemplate.getType(), id);
    }

    public GraphTemplate findById(Long id) {
        String sql = "select * from graph_template where id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(GraphTemplate.class), id);
    }

    public List<GraphTemplate> graphTemplates(String type) {
        String sql = "select * from graph_template where type = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GraphTemplate.class), type);
    }
}
