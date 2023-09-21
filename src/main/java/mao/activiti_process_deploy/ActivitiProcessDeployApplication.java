package mao.activiti_process_deploy;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ActivitiProcessDeployApplication
{
    private static final Logger log = LoggerFactory.getLogger(ActivitiProcessDeployApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(ActivitiProcessDeployApplication.class, args);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程定义")
                .addClasspathResource("test.bpmn20.xml")
                .deploy();
        log.info("流程id：" + deployment.getId());
        log.info("流程名称：" + deployment.getName());
    }

}
