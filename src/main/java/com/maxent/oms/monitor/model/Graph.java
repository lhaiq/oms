package com.maxent.oms.monitor.model;

/**
 * Created by haiquanli on 16/6/20.
 */
public class Graph {

    private Long id;
    private Long templateId;
    private String name;
    private String items;
    private Integer graphType;
    private Integer dataType;

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getGraphType() {
        return graphType;
    }

    public void setGraphType(Integer graphType) {
        this.graphType = graphType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getTemplateId() {
        return templateId;
    }
}
