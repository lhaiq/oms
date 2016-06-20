package com.maxent.oms.monitor.service;

import com.maxent.oms.monitor.model.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haiquanli on 16/6/20.
 */
@Service
public class GraphService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public void addGraph(Graph graph) {
        String sql = "insert into z_graph (name,items,graphType,dataType,templateId) values (?,?,?,?,?) ";
        jdbcTemplate.update(sql, graph.getName(), graph.getItems(), graph.getGraphType(), graph.getDataType(), graph.getTemplateId());
    }

    @Transactional
    public void deleteGraph(Long id) {
        String sql = "delete from z_graph where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void updateGraph(Long id, Graph graph) {
        StringBuffer sb = new StringBuffer();
        Map<String, Object> param = new HashMap<>();
        sb.append("update  z_graph set ");

        if (!StringUtils.isEmpty(graph.getName())) {
            sb.append(",name =:name ");
            param.put("name", graph.getName());
        }

        if (!StringUtils.isEmpty(graph.getItems())) {
            sb.append(",items =:items ");
            param.put("items", graph.getItems());
        }

        if (null != graph.getDataType()) {
            sb.append(",dataType =:dataType ");
            param.put("dataType", graph.getDataType());
        }

        if (null != graph.getGraphType()) {
            sb.append(",graphType =:graphType ");
            param.put("graphType", graph.getGraphType());
        }

        if (null != graph.getTemplateId()) {
            sb.append(",templateId =:templateId ");
            param.put("templateId", graph.getTemplateId());
        }

        sb.append("where id=:id");
        param.put("id", id);
        String sql = sb.toString();
        sql = sql.replaceFirst(",", "");
        namedParameterJdbcTemplate.update(sql, param);
    }

    public Graph findById(Long id) {
        String sql = "select * from z_graph where id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Graph.class), id);
    }

    public List<Graph> graphs(Long templateId) {
        String sql = "select * from z_graph where templateId = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Graph.class), templateId);
    }
}
