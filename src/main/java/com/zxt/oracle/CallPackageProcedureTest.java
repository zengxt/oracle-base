package com.zxt.oracle;

import com.zxt.utils.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

@Slf4j
public class CallPackageProcedureTest {
    /**
     * CREATE OR REPLACE PACKAGE BODY mypackage AS
     *   PROCEDURE queryEmpList(dno in number, empList out empcursor) AS
     */
    @Test
    public void testCallProcedure() {
        String sql = "{call mypackage.queryEmpList(?, ?)}";
        Connection conn = null;
        CallableStatement call = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);

            // 对于in参数，需要赋值
            call.setInt(1, 20);

            // 对于out参数，声明
            call.registerOutParameter(2, OracleTypes.CURSOR);

            // 执行调用
            call.execute();

            /**
             * 取出结果
             * 这里使用的是Oracle的光标，因此需要将CallableStatement接口类型强制装换为Oracle的实现类的类型
             * 而光标对应在Java程序里面实际上就是结果集，因此：
             */
            resultSet = ((OracleCallableStatement) call).getCursor(2);
            while (resultSet.next()) {
                int empno = resultSet.getInt("empno");
                String ename = resultSet.getString("ename");
                double sal = resultSet.getDouble("sal");
                String job = resultSet.getString("job");
                System.out.println("[empno = " + empno + ", ename = " +
                        ename + ", sal = " + sal + ", job = " + job + "]");
            }
        } catch (Exception ex) {
            log.error("调用存储过程失败：{}", ex.getStackTrace());
        } finally {
            JDBCUtils.release(conn, call, null);
        }
    }
}
