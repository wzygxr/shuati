"""
两地调度 - 贪心算法解决方案（Python实现）

题目描述：
公司计划面试2n个人，给定一个数组 costs，其中costs[i]=[aCosti, bCosti]
表示第i人飞往a市的费用为aCosti，飞往b市的费用为bCosti
返回将每个人都飞到a、b中某座城市的最低费用，要求每个城市都有n人抵达

测试链接：https://leetcode.cn/problems/two-city-scheduling/

算法思想：
贪心算法 + 差值排序
1. 假设所有人都先去A市，总费用为所有aCosti之和
2. 计算每个人从A市改去B市的费用变化：bCosti - aCosti
3. 选择费用变化最小的n个人改去B市
4. 总费用 = 所有aCosti之和 + 最小的n个费用变化值之和

时间复杂度分析：
O(n*logn) - 主要时间消耗在排序上

空间复杂度分析：
O(n) - 需要额外的数组存储费用变化值

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：处理空数组、奇数人数等特殊情况
2. 输入验证：检查输入参数的有效性
3. 异常处理：对非法输入进行检查
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
对于每个人，选择去A市还是B市取决于bCosti - aCosti的值
如果这个值很小（负数），说明去B市比去A市便宜很多，应该优先选择去B市
通过排序选择最小的n个差值，可以保证总费用最小
"""

from typing import List

class Code02_TwoCityScheduling:
    
    @staticmethod
    def two_city_sched_cost(costs: List[List[int]]) -> int:
        """
        计算两地调度的最低费用
        
        Args:
            costs: 费用数组，每个元素是[aCosti, bCosti]
            
        Returns:
            最低总费用
            
        算法步骤：
        1. 假设所有人都先去A市，计算总费用
        2. 计算每个人从A市改去B市的费用变化
        3. 对费用变化进行排序
        4. 选择最小的n个费用变化值加到总费用中
        
        数学原理：
        总费用 = ΣaCosti + Σ(bCosti - aCosti) = ΣaCosti + ΣbCosti - ΣaCosti = ΣbCosti
        但这里只选择最小的n个差值，所以实际上是部分人去B市
        """
        # 输入验证
        if not costs:
            return 0
            
        n = len(costs)
        # 检查人数是否为偶数
        if n % 2 != 0:
            raise ValueError("人数必须为偶数")
        
        diff = [0] * n  # 存储每个人从A市改去B市的费用变化
        sum_a = 0       # 所有人都去A市的总费用
        
        # 计算费用变化和A市总费用
        for i in range(n):
            sum_a += costs[i][0]              # 累加A市费用
            diff[i] = costs[i][1] - costs[i][0]  # 计算费用变化
        
        # 对费用变化进行排序（升序）
        diff.sort()
        
        m = n // 2  # 每个城市需要的人数
        # 选择最小的m个费用变化值
        for i in range(m):
            sum_a += diff[i]
        
        return sum_a
    
    @staticmethod
    def debug_two_city_sched_cost(costs: List[List[int]]) -> int:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            costs: 费用数组
            
        Returns:
            最低总费用
        """
        if not costs:
            return 0
            
        n = len(costs)
        if n % 2 != 0:
            raise ValueError("人数必须为偶数")
        
        diff = [0] * n
        sum_a = 0
        
        print("原始费用数据:")
        print("序号\tA市费用\tB市费用\t费用变化(B-A)")
        for i in range(n):
            a_cost = costs[i][0]
            b_cost = costs[i][1]
            change = b_cost - a_cost
            diff[i] = change
            sum_a += a_cost
            print(f"{i}\t{a_cost}\t{b_cost}\t{change}")
        
        print(f"\n所有人都去A市的总费用: {sum_a}")
        
        # 打印排序前的费用变化
        print(f"排序前的费用变化数组: {diff}")
        
        diff.sort()
        print(f"排序后的费用变化数组: {diff}")
        
        m = n // 2
        print(f"每个城市需要的人数: {m}")
        print(f"选择最小的{m}个费用变化值:")
        
        change_sum = 0
        for i in range(m):
            change_sum += diff[i]
            print(f"选择第{i+1}个变化值: {diff[i]}, 累计变化: {change_sum}")
        
        total_cost = sum_a + change_sum
        print(f"最终总费用: {total_cost}")
        
        return total_cost
    
    @staticmethod
    def test_two_city_sched_cost():
        """
        测试函数：验证两地调度算法的正确性
        """
        print("两地调度算法测试开始")
        print("====================")
        
        # 测试用例1: [[10,20],[30,200],[400,50],[30,20]]
        costs1 = [[10,20], [30,200], [400,50], [30,20]]
        result1 = Code02_TwoCityScheduling.two_city_sched_cost(costs1)
        print("输入: [[10,20],[30,200],[400,50],[30,20]]")
        print("输出:", result1)
        print("预期: 110")
        print("✓ 通过" if result1 == 110 else "✗ 失败")
        print()
        
        # 测试用例2: [[259,770],[448,54],[926,667],[184,139],[840,118],[577,469]]
        costs2 = [[259,770], [448,54], [926,667], [184,139], [840,118], [577,469]]
        result2 = Code02_TwoCityScheduling.two_city_sched_cost(costs2)
        print("输入: 6个人的测试用例")
        print("输出:", result2)
        print("预期: 1859")
        print("✓ 通过" if result2 == 1859 else "✗ 失败")
        print()
        
        # 测试用例3: [[515,563],[451,713],[537,709],[343,819],[855,779],[457,60],[650,359],[631,42]]
        costs3 = [[515,563], [451,713], [537,709], [343,819], [855,779], [457,60], [650,359], [631,42]]
        result3 = Code02_TwoCityScheduling.two_city_sched_cost(costs3)
        print("输入: 8个人的复杂测试用例")
        print("输出:", result3)
        print("预期: 3086")
        print("✓ 通过" if result3 == 3086 else "✗ 失败")
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
        n = 10000  # 5000对人
        costs = []
        for i in range(n):
            a_cost = random.randint(1, 1000)
            b_cost = random.randint(1, 1000)
            costs.append([a_cost, b_cost])
        
        start_time = time.time()
        result = Code02_TwoCityScheduling.two_city_sched_cost(costs)
        end_time = time.time()
        
        print(f"数据规模: {n} 对人")
        print(f"执行时间: {end_time - start_time:.4f} 秒")
        print(f"计算结果: {result}")
        print("性能测试结束")


def main():
    """
    主函数：运行测试
    """
    print("两地调度 - 贪心算法解决方案（Python实现）")
    print("===================================")
    
    # 运行基础测试
    Code02_TwoCityScheduling.test_two_city_sched_cost()
    
    print("\n调试模式示例:")
    debug_costs = [[10,20], [30,200], [400,50], [30,20]]
    print("对测试用例 [[10,20],[30,200],[400,50],[30,20]] 进行调试跟踪:")
    debug_result = Code02_TwoCityScheduling.debug_two_city_sched_cost(debug_costs)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n*logn) - 主要时间消耗在排序上")
    print("- 空间复杂度: O(n) - 需要额外的数组存储费用变化值")
    print("- 贪心策略: 假设所有人都去A市，然后选择费用变化最小的n个人改去B市")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- 数学原理: 总费用 = ΣaCosti + min_n(bCosti - aCosti)")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # Code02_TwoCityScheduling.performance_test()


if __name__ == "__main__":
    main()