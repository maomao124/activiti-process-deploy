package mao.activiti_process_deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ActivitiProcessDeployApplicationTests
{
    private static final Logger log = LoggerFactory.getLogger(ActivitiProcessDeployApplicationTests.class);

    @Test
    void testStartProcess()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> map = new HashMap<>();
        map.put("assignee0", "张三");
        map.put("assignee1", "王经理");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("test", map);
        log.info("流程定义id:" + instance.getProcessDefinitionId());
        log.info("流程实例 id:" + instance.getId());
        log.info("当前活动的id:" + instance.getActivityId());
    }

    @Test
    void testFindPersonalTaskList()
    {
        //任务负责人
        String assignee = "王经理";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();
        for (Task task : taskList)
        {
            log.info("流程定义id:" + task.getProcessDefinitionId());
            log.info("流程实例 id:" + task.getId());
            log.info("任务负责人：" + task.getAssignee());
            log.info("任务名称：" + task.getName());
        }

    }

}
