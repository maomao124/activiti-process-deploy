package mao.activiti_process_deploy;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        String assignee = "张三";
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

    /**
     * 完成任务
     */
    @Test
    void completeTask()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("test")
                .taskAssignee("张三")
                .list().get(0);
        //完成任务
        taskService.complete(task.getId());
    }

    @Test
    void testFindPersonalTaskList2()
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

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询出当前所有的流程定义
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.processDefinitionKey("test")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //输出
        processDefinitionList.forEach(processDefinition ->
        {
            log.info("流程定义 id=" + processDefinition.getId());
            log.info("流程定义 name=" + processDefinition.getName());
            log.info("流程定义 key=" + processDefinition.getKey());
            log.info("流程定义 Version=" + processDefinition.getVersion());
            log.info("流程部署ID =" + processDefinition.getDeploymentId());
        });
    }

    /**
     * 流程删除
     */
    @Test
    void deleteDeployment()
    {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式
        repositoryService.deleteDeployment("2503",false);
    }

}
