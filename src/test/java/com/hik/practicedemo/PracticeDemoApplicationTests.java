package com.hik.practicedemo;

import com.hik.practicedemo.conf.async.TestAsync;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PracticeDemoApplicationTests {

    @Autowired
    @Qualifier("primaryDataSource")
    private DataSource dataSource;

    @Resource
    private TestAsync async;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testDataSourceConf() {
        try {

            Connection connection = dataSource.getConnection();
            System.out.println(connection.getCatalog());
            Statement statement = connection.createStatement();
            boolean b = statement.execute("select * from tb_user where id = 9962");
            if (b) {
                ResultSet resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    System.out.println(resultSet.getRow());
                }
                System.out.println(resultSet.getObject(1));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        System.out.println("ok");
    }


    @Test
    public void testAsync() {
        async.testExecutor();
        System.out.println("----------------------");
        async.testSchedulerThreadPool();
        System.out.println("----------------------");
        async.testExecutorThreadPool();
    }
}
