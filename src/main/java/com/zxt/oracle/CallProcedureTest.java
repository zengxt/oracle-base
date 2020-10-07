package com.zxt.oracle;

import com.zxt.utils.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.internal.OracleTypes;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.Connection;

@Slf4j
public class CallProcedureTest {
    /**
     * CREATE OR REPLACE PROCEDURE queryEmpInform(eno IN NUMBER,
     *        pname OUT VARCHAR2, psal OUT NUMBER, pjob OUT VARCHAR2)
     */
    @Test
    public void testCallProcedure() {
        String sql = "{call queryEmpInform(?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement call = null;
        try {
            conn = JDBCUtils.getConnection();
            call = conn.prepareCall(sql);

            // 对于in参数，需要赋值
            call.setInt(1, 7839);

            // 对于out参数，声明
            call.registerOutParameter(2, OracleTypes.VARCHAR);
            call.registerOutParameter(3, OracleTypes.NUMBER);
            call.registerOutParameter(4, OracleTypes.VARCHAR);

            // 执行调用
            call.execute();

            // 取出结果
            String pname = call.getString(2);
            double psal = call.getDouble(3);
            String pjob = call.getString(4);
            System.out.println("[pname = " + pname + ", psal = " + psal + ", pjob = " + pjob + "]");
        } catch (Exception ex) {
            log.error("调用存储过程失败：{}", ex.getStackTrace());
        } finally {
            JDBCUtils.release(conn, call, null);
        }
    }
}
