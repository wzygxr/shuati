package class143;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 跳楼机问题单元测试类
 * 
 * 测试策略：
 * 1. 边界测试：测试最小和最大输入值
 * 2. 功能测试：测试典型输入场景
 * 3. 异常测试：测试非法输入情况
 * 4. 性能测试：测试大规模数据性能
 * 
 * 测试用例设计原则：
 * - 等价类划分：将输入划分为有效和无效等价类
 * - 边界值分析：测试边界值和临界值
 * - 错误推测：基于经验推测可能的错误
 */
public class TestCode01_Elevator {

    /**
     * 基础功能测试 - 典型输入场景
     */
    @Test
    public void testBasicFunctionality() {
        // 测试用例1：简单场景
        long result1 = testCase(10, 2, 3, 5);
        assertEquals(9, result1, "h=10, x=2, y=3, z=5 应该返回9");
        
        // 测试用例2：中等规模
        long result2 = testCase(100, 3, 5, 7);
        assertTrue(result2 > 0, "h=100, x=3, y=5, z=7 应该返回正数");
        
        // 测试用例3：x=y=z的情况
        long result3 = testCase(20, 2, 2, 2);
        assertEquals(10, result3, "x=y=z=2时，结果应该为h/2");
    }

    /**
     * 边界条件测试 - 测试最小和最大输入值
     */
    @Test
    public void testBoundaryConditions() {
        // 最小输入值测试
        long result1 = testCase(1, 1, 1, 1);
        assertEquals(1, result1, "h=1时只能到达1层");
        
        // 最大x值测试（接近10^5）
        long result2 = testCase(1000, 100000, 1, 1);
        assertTrue(result2 >= 1, "大x值应该能正确处理");
        
        // 特殊边界：x=1的情况
        long result3 = testCase(10, 1, 2, 3);
        assertEquals(10, result3, "x=1时所有楼层都应该可达");
    }

    /**
     * 异常情况测试 - 测试非法输入
     */
    @Test
    public void testExceptionCases() {
        // 测试非法输入（虽然题目有约束，但工程上应该处理）
        assertThrows(IllegalArgumentException.class, () -> {
            testCase(-1, 2, 3, 5);
        }, "负数h应该抛出异常");
        
        assertThrows(IllegalArgumentException.class, () -> {
            testCase(10, 0, 3, 5);
        }, "x=0应该抛出异常");
    }

    /**
     * 性能测试 - 测试大规模数据性能
     */
    @Test
    public void testPerformance() {
        // 测试中等规模数据（x=10000）
        long startTime = System.currentTimeMillis();
        long result = testCase(1000000L, 10000, 10001, 10002);
        long endTime = System.currentTimeMillis();
        
        assertTrue(result > 0, "大规模数据应该返回有效结果");
        assertTrue((endTime - startTime) < 1000, "10000规模应该在1秒内完成");
    }

    /**
     * 数学正确性验证 - 验证算法数学原理
     */
    @Test
    public void testMathematicalCorrectness() {
        // 验证：当x=y=z时，结果应该为h/x（如果h能被x整除）
        long result = testCase(100, 10, 10, 10);
        assertEquals(10, result, "x=y=z=10, h=100时应该返回10");
        
        // 验证：当只有一种移动方式时
        long result2 = testCase(100, 1, 100000, 100000);
        assertEquals(100, result2, "只有x=1有效时，所有楼层可达");
    }

    /**
     * 辅助方法：执行测试用例
     */
    private long testCase(long h, int x, int y, int z) {
        // 保存原始静态变量值
        long originalH = Code01_Elevator.h;
        int originalX = Code01_Elevator.x;
        int originalY = Code01_Elevator.y;
        int originalZ = Code01_Elevator.z;
        
        try {
            // 设置测试参数
            Code01_Elevator.h = h - 1;
            Code01_Elevator.x = x;
            Code01_Elevator.y = y;
            Code01_Elevator.z = z;
            
            // 执行算法
            Code01_Elevator.prepare();
            for (int i = 0; i < x; i++) {
                Code01_Elevator.addEdge(i, (i + y) % x, y);
                Code01_Elevator.addEdge(i, (i + z) % x, z);
            }
            return Code01_Elevator.compute();
        } finally {
            // 恢复原始值
            Code01_Elevator.h = originalH;
            Code01_Elevator.x = originalX;
            Code01_Elevator.y = originalY;
            Code01_Elevator.z = originalZ;
        }
    }

    /**
     * 调试信息输出测试 - 用于调试和问题定位
     */
    @Test
    public void testDebugInfo() {
        System.out.println("=== 跳楼机算法调试信息 ===");
        
        // 测试小规模数据，便于调试
        long result = testCase(10, 2, 3, 5);
        System.out.println("测试结果: h=10, x=2, y=3, z=5 => " + result);
        
        // 验证中间计算结果
        assertTrue(result > 0, "结果应该为正数");
        System.out.println("测试通过: 结果验证成功");
    }
}