package com.zxt.oracle;

import com.zxt.utils.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.internal.OracleTypes;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.Connection;

@Slf4j
public class CallFunctionTest {
    /**
     * CREATE OR REPLACE FUNCTION queryEmpIncome(eno IN NUMBER)
     * RETURN NUMBER
     */
    @Test
    public void testCallFunction() {
        String sql = "{?=call queryEmpIncome(?)}";
        Connection conn = null;
        CallableStatement call = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);

            // 对于out参数，声明
            call.registerOutParameter(1, OracleTypes.NUMBER);
            // 对于in参数，需要赋值
            call.setInt(2, 7839);
            // 执行调用
            call.execute();

            // 取出结果
            double income = call.getDouble(1);
            System.out.println("该员工的年收入为：" + income);
        } catch (Exception ex) {
            log.error("调用存储函数失败：{}", ex.getStackTrace());
        } finally {
            JDBCUtils.release(conn, call, null);
        }
    }
}
