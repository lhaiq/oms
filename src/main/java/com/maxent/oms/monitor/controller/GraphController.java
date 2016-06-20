package com.maxent.oms.monitor.controller;

import com.maxent.oms.monitor.model.Graph;
import com.maxent.oms.monitor.model.GraphTemplate;
import com.maxent.oms.monitor.service.GraphService;
import com.maxent.oms.monitor.service.GraphTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by haiquanli on 16/6/3.
 */
@RestController
@RequestMapping("/oms")
public class GraphController {

    @Autowired
    private GraphTemplateService graphTemplateService;

    @Autowired
    private GraphService graphService;

    /**
     * 添加图表模版
     * @param graphTemplate
     */
    @RequestMapping(value = "/graphTemplate", method = RequestMethod.POST)
    public void addGraphTemplate(@RequestBody GraphTemplate graphTemplate) {
        graphTemplateService.addGraphTemplate(graphTemplate);
    }

    /**
     * 删除图表模板
     * @param id
     */
    @RequestMapping(value = "/graphTemplate/{id}", method = RequestMethod.DELETE)
    public void deleteGraphTemplate(@PathVariable Long id) {
        graphTemplateService.deleteGraphTemplate(id);
    }

    /**
     * 修改图表模板
     * @param graphTemplate
     * @param id
     */
    @RequestMapping(value = "/graphTemplate/{id}", method = RequestMethod.PUT)
    public void updateGraphTemplate(@RequestBody GraphTemplate graphTemplate,
                                    @PathVariable Long id) {
        graphTemplateService.updateGraphTemplate(id, graphTemplate);
    }

    /**
     * 图表模版详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/graphTemplate/{id}", method = RequestMethod.GET)
    public Object findGraphTemplate(@PathVariable Long id) {
        return graphTemplateService.findById(id);
    }

    /**
     * 图表模板列表
     * @param type
     * @return
     */
    @RequestMapping(value = "/graphTemplates", method = RequestMethod.GET)
    public Object listGraphTemplates(@RequestParam String type) {
        return graphTemplateService.graphTemplates(type);
    }


    /**
     * 添加图表
     * @param graph
     */
    @RequestMapping(value = "/graph", method = RequestMethod.POST)
    public void addGraph(@RequestBody Graph graph) {
        graphService.addGraph(graph);
    }

    /**
     * 删除图表
     * @param id
     */
    @RequestMapping(value = "/graph/{id}", method = RequestMethod.DELETE)
    public void deleteGraph(@PathVariable Long id) {
        graphService.deleteGraph(id);
    }

    /**
     * 修改图表
     * @param graph
     * @param id
     */
    @RequestMapping(value = "/graph/{id}", method = RequestMethod.PUT)
    public void updateGraph(@RequestBody Graph graph,
                            @PathVariable Long id) {
        graphService.updateGraph(id, graph);
    }

    /**
     * 图表详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/graph/{id}", method = RequestMethod.GET)
    public Object findGraph(@PathVariable Long id) {
        return graphService.findById(id);
    }

    /**
     * 图表列表
     * @param templateId
     * @return
     */
    @RequestMapping(value = "/{templateId}/graphs", method = RequestMethod.GET)
    public Object listGraphs(@PathVariable Long templateId) {
        return graphService.graphs(templateId);
    }


}
