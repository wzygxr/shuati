"""
连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（Python实现，LeetCode版本）

题目描述：
你有一些长度为正整数的棍子，这些长度以数组sticks的形式给出
sticks[i]是第i个木棍的长度
你可以通过支付x+y的成本将任意两个长度为x和y的棍子连接成一个棍子
你必须连接所有的棍子，直到剩下一个棍子
返回以这种方式将所有给定的棍子连接成一个棍子的最小成本

测试链接：https://leetcode.cn/problems/minimum-cost-to-connect-sticks/

算法思想：
贪心算法 + 最小堆（优先队列）
1. 使用最小堆存储所有棍子的长度
2. 每次从堆中取出两根最短的棍子进行连接
3. 将连接后的新棍子放回堆中
4. 重复直到只剩下一根棍子

时间复杂度分析：
O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作

空间复杂度分析：
O(n) - 堆的大小为n

是否为最优解：
是，这是解决该问题的最优解（哈夫曼编码思想）

工程化考量：
1. 边界条件处理：处理空数组、单个棍子等特殊情况
2. 输入验证：检查棍子长度是否为正整数
3. 异常处理：对非法输入进行检查
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
这是经典的哈夫曼编码问题，每次选择最小的两个元素合并可以保证总成本最小
这种策略满足贪心选择性质和最优子结构性质
"""

import heapq
from typing import List

class Code06_MinimumCostToConnectSticks1:
    
    @staticmethod
    def connect_sticks(sticks: List[int]) -> int:
        """
        计算连接所有棍子的最小成本
        
        Args:
            sticks: 棍子长度数组
            
        Returns:
            最小连接成本
            
        算法步骤：
        1. 将棍子长度加入最小堆
        2. 当堆中棍子数量大于1时：
           - 取出两根最短的棍子
           - 计算连接成本并累加
           - 将连接后的新棍子放回堆中
        3. 返回总成本
        """
        # 输入验证
        if not sticks:
            return 0
        
        # 单个棍子不需要连接
        if len(sticks) == 1:
            return 0
        
        # 最小堆，存储棍子长度
        heap = []
        for stick in sticks:
            # 验证棍子长度有效性
            if stick <= 0:
                raise ValueError("棍子长度必须为正整数")
            heapq.heappush(heap, stick)
        
        total_cost = 0  # 总连接成本
        
        # 当堆中还有多于一根棍子时继续连接
        while len(heap) > 1:
            # 取出两根最短的棍子
            first = heapq.heappop(heap)
            second = heapq.heappop(heap)
            
            # 计算连接成本
            cost = first + second
            total_cost += cost
            
            # 将连接后的新棍子放回堆中
            heapq.heappush(heap, cost)
        
        return total_cost
    
    @staticmethod
    def debug_connect_sticks(sticks: List[int]) -> int:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            sticks: 棍子长度数组
            
        Returns:
            最小连接成本
        """
        if not sticks:
            print("空数组，不需要连接")
            return 0
        
        if len(sticks) == 1:
            print("单个棍子，不需要连接")
            return 0
        
        print("原始棍子长度:", sticks)
        
        heap = []
        for stick in sticks:
            heapq.heappush(heap, stick)
        
        total_cost = 0
        step = 1
        
        print("\n连接过程:")
        while len(heap) > 1:
            print(f"步骤 {step}:")
            
            # 取出两根最短的棍子
            first = heapq.heappop(heap)
            second = heapq.heappop(heap)
            print(f"  取出两根最短的棍子: {first} 和 {second}")
            
            # 计算连接成本
            cost = first + second
            total_cost += cost
            print(f"  连接成本: {first} + {second} = {cost}")
            print(f"  累计总成本: {total_cost}")
            
            # 将连接后的新棍子放回堆中
            heapq.heappush(heap, cost)
            print(f"  将新棍子({cost})放回堆中")
            
            # 打印当前堆的状态
            print(f"  当前堆中棍子: {heap}")
            
            step += 1
        
        print(f"\n最终结果: 最小连接成本 = {total_cost}")
        return total_cost
    
    @staticmethod
    def test_connect_sticks():
        """
        测试函数：验证连接棒材算法的正确性
        """
        print("连接棒材的最低费用算法测试开始")
        print("============================")
        
        # 测试用例1: [2,4,3]
        sticks1 = [2, 4, 3]
        result1 = Code06_MinimumCostToConnectSticks1.connect_sticks(sticks1)
        print("输入: [2,4,3]")
        print("输出:", result1)
        print("预期: 14")
        print("✓ 通过" if result1 == 14 else "✗ 失败")
        print()
        
        # 测试用例2: [1,8,3,5]
        sticks2 = [1, 8, 3, 5]
        result2 = Code06_MinimumCostToConnectSticks1.connect_sticks(sticks2)
        print("输入: [1,8,3,5]")
        print("输出:", result2)
        print("预期: 30")
        print("✓ 通过" if result2 == 30 else "✗ 失败")
        print()
        
        # 测试用例3: 空数组
        sticks3 = []
        result3 = Code06_MinimumCostToConnectSticks1.connect_sticks(sticks3)
        print("输入: []")
        print("输出:", result3)
        print("预期: 0")
        print("✓ 通过" if result3 == 0 else "✗ 失败")
        print()
        
        # 测试用例4: 单个棍子
        sticks4 = [5]
        result4 = Code06_MinimumCostToConnectSticks1.connect_sticks(sticks4)
        print("输入: [5]")
        print("输出:", result4)
        print("预期: 0")
        print("✓ 通过" if result4 == 0 else "✗ 失败")
        print()
        
        print("测试结束")
    
    @staticmethod
    def performance_test():
        """
        性能测试：测试算法在大规模数据下的表现
        """
        import time
        import random
        
        print("性能测试开始")
        print("============")
        
        # 生成大规模测试数据
        n = 10000
        sticks = [random.randint(1, 1000) for _ in range(n)]  # 1-1000的随机数
        
        start_time = time.time()
        result = Code06_MinimumCostToConnectSticks1.connect_sticks(sticks)
        end_time = time.time()
        
        print(f"数据规模: {n} 根棍子")
        print(f"执行时间: {end_time - start_time:.4f} 秒")
        print(f"最小连接成本: {result}")
        print("性能测试结束")
    
    @staticmethod
    def compare_with_naive(sticks: List[int]) -> None:
        """
        与暴力解法对比，展示贪心算法的优势
        
        Args:
            sticks: 棍子长度数组
        """
        print("贪心算法结果:", Code06_MinimumCostToConnectSticks1.connect_sticks(sticks))
        
        # 暴力解法：尝试所有可能的连接顺序（仅用于小规模对比）
        def brute_force(sticks: List[int]) -> int:
            if len(sticks) <= 1:
                return 0
            
            min_cost = 10**9  # 使用大数代替无穷大
            # 尝试所有可能的连接顺序
            for i in range(len(sticks)):
                for j in range(i + 1, len(sticks)):
                    # 连接第i和第j根棍子
                    new_sticks = sticks.copy()
                    cost = new_sticks[i] + new_sticks[j]
                    new_sticks[i] = cost
                    new_sticks.pop(j)
                    
                    # 递归计算剩余棍子的最小成本
                    total_cost = cost + brute_force(new_sticks)
                    min_cost = min(min_cost, total_cost)
            
            return min_cost
        
        if len(sticks) <= 5:  # 只对小规模数据进行暴力计算
            print("暴力解法结果:", brute_force(sticks))
        else:
            print("暴力解法: 数据规模太大，无法计算")


def main():
    """
    主函数：运行测试
    """
    print("连接棒材的最低费用 - 贪心算法 + 最小堆解决方案（Python实现，LeetCode版本）")
    print("==================================================================")
    
    # 运行基础测试
    Code06_MinimumCostToConnectSticks1.test_connect_sticks()
    
    print("\n调试模式示例:")
    debug_sticks = [2, 4, 3]
    print("对测试用例 [2,4,3] 进行调试跟踪:")
    debug_result = Code06_MinimumCostToConnectSticks1.debug_connect_sticks(debug_sticks)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n*logn) - 每个棍子进出堆一次需要O(logn)，总共需要n-1次连接操作")
    print("- 空间复杂度: O(n) - 堆的大小为n")
    print("- 贪心策略: 每次选择最短的两根棍子进行连接（哈夫曼编码思想）")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- 数学原理: 这是经典的哈夫曼编码问题")
    print("- Python特性: 使用heapq模块实现最小堆")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # Code06_MinimumCostToConnectSticks1.performance_test()
    
    # 可选：与暴力解法对比
    # print("\n算法对比:")
    # test_sticks = [2, 4, 3]
    # Code06_MinimumCostToConnectSticks1.compare_with_naive(test_sticks)


if __name__ == "__main__":
    main()