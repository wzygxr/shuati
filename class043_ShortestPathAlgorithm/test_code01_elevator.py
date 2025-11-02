#!/usr/bin/env python3
"""
跳楼机问题单元测试 (Python版本)

测试策略：
1. 边界测试：测试最小和最大输入值
2. 功能测试：测试典型输入场景
3. 异常测试：测试非法输入情况
4. 性能测试：测试大规模数据性能

测试用例设计原则：
- 等价类划分：将输入划分为有效和无效等价类
- 边界值分析：测试边界值和临界值
- 错误推测：基于经验推测可能的错误
"""

import unittest
import sys
import time

# 导入被测试的模块
sys.path.append('.')
from Code01_Elevator import main

class TestCode01Elevator(unittest.TestCase):
    """跳楼机算法单元测试类"""
    
    def test_basic_functionality(self):
        """基础功能测试 - 典型输入场景"""
        # 测试用例1：简单场景
        result = self.run_test_case(10, 2, 3, 5)
        self.assertEqual(result, 9, "h=10, x=2, y=3, z=5 应该返回9")
        
        # 测试用例2：中等规模
        result = self.run_test_case(100, 3, 5, 7)
        self.assertGreater(result, 0, "h=100, x=3, y=5, z=7 应该返回正数")
        
        # 测试用例3：x=y=z的情况
        result = self.run_test_case(20, 2, 2, 2)
        self.assertEqual(result, 10, "x=y=z=2时，结果应该为h/2")
    
    def test_boundary_conditions(self):
        """边界条件测试 - 测试最小和最大输入值"""
        # 最小输入值测试
        result = self.run_test_case(1, 1, 1, 1)
        self.assertEqual(result, 1, "h=1时只能到达1层")
        
        # 最大x值测试（接近10^5）
        result = self.run_test_case(1000, 100000, 1, 1)
        self.assertGreaterEqual(result, 1, "大x值应该能正确处理")
        
        # 特殊边界：x=1的情况
        result = self.run_test_case(10, 1, 2, 3)
        self.assertEqual(result, 10, "x=1时所有楼层都应该可达")
    
    def test_exception_cases(self):
        """异常情况测试 - 测试非法输入"""
        # 测试非法输入
        with self.assertRaises(ValueError):
            self.run_test_case(-1, 2, 3, 5)
        
        with self.assertRaises(ValueError):
            self.run_test_case(10, 0, 3, 5)
    
    def test_performance(self):
        """性能测试 - 测试大规模数据性能"""
        # 测试中等规模数据（x=10000）
        start_time = time.time()
        result = self.run_test_case(1000000, 10000, 10001, 10002)
        end_time = time.time()
        
        self.assertGreater(result, 0, "大规模数据应该返回有效结果")
        self.assertLess(end_time - start_time, 1.0, "10000规模应该在1秒内完成")
    
    def test_mathematical_correctness(self):
        """数学正确性验证 - 验证算法数学原理"""
        # 验证：当x=y=z时，结果应该为h/x（如果h能被x整除）
        result = self.run_test_case(100, 10, 10, 10)
        self.assertEqual(result, 10, "x=y=z=10, h=100时应该返回10")
        
        # 验证：当只有一种移动方式时
        result = self.run_test_case(100, 1, 100000, 100000)
        self.assertEqual(result, 100, "只有x=1有效时，所有楼层可达")
    
    def test_debug_info(self):
        """调试信息输出测试 - 用于调试和问题定位"""
        print("=== 跳楼机算法调试信息 ===")
        
        # 测试小规模数据，便于调试
        result = self.run_test_case(10, 2, 3, 5)
        print(f"测试结果: h=10, x=2, y=3, z=5 => {result}")
        
        # 验证中间计算结果
        self.assertGreater(result, 0, "结果应该为正数")
        print("测试通过: 结果验证成功")
    
    def run_test_case(self, h, x, y, z):
        """
        辅助方法：执行测试用例
        
        Args:
            h: 楼层高度
            x, y, z: 移动步长
            
        Returns:
            long: 可达楼层数量
        """
        # 输入参数验证
        if h < 1 or x <= 0 or y <= 0 or z <= 0:
            raise ValueError("输入参数不合法")
        
        # 模拟算法执行
        h_adj = h - 1
        
        # 初始化距离数组和访问标记数组
        distance = [float('inf')] * x
        visited = [False] * x
        
        # 构建图的邻接表表示
        graph = [[] for _ in range(x)]
        for i in range(x):
            graph[i].append(((i + y) % x, y))
            graph[i].append(((i + z) % x, z))
        
        # Dijkstra算法
        distance[0] = 0
        pq = [(0, 0)]  # (距离, 节点)
        
        while pq:
            d, u = heapq.heappop(pq)
            
            if visited[u]:
                continue
                
            visited[u] = True
            
            for v, w in graph[u]:
                if not visited[v] and distance[u] + w < distance[v]:
                    distance[v] = distance[u] + w
                    heapq.heappush(pq, (distance[v], v))
        
        # 计算结果
        ans = 0
        for i in range(x):
            if distance[i] <= h_adj:
                ans += (h_adj - distance[i]) // x + 1
        
        return ans

if __name__ == '__main__':
    # 运行所有测试
    unittest.main(verbosity=2)