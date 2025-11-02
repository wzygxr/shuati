import time
import random
from typing import List

class Code34_GasStation:
    """
    加油站
    
    题目描述：
    在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
    你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
    你从其中的一个加油站出发，开始时油箱为空。
    如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
    
    来源：LeetCode 134
    链接：https://leetcode.cn/problems/gas-station/
    
    算法思路：
    使用贪心算法：
    1. 计算每个加油站的净收益（gas[i] - cost[i]）
    2. 如果总净收益小于0，则无法绕行一周
    3. 否则，从起点开始遍历，维护当前油量和总油量
    4. 如果当前油量小于0，说明从当前起点无法完成，重置起点为下一个加油站
    
    时间复杂度：O(n) - 只需要遍历一次数组
    空间复杂度：O(1) - 只使用常数空间
    
    关键点分析：
    - 贪心策略：选择净收益最大的起点
    - 数学证明：总油量必须大于等于总消耗
    - 边界处理：环形数组的处理
    
    工程化考量：
    - 输入验证：检查数组是否为空
    - 性能优化：避免不必要的计算
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def can_complete_circuit(gas: List[int], cost: List[int]) -> int:
        """
        找到可以绕行一周的加油站起点
        
        Args:
            gas: 汽油数组
            cost: 消耗数组
            
        Returns:
            int: 起点索引，如果无法完成返回-1
        """
        # 输入验证
        if not gas or not cost or len(gas) != len(cost):
            raise ValueError("输入数组不能为空且长度必须相等")
        if len(gas) == 0:
            return -1
        
        n = len(gas)
        total_gas = 0    # 总汽油量
        total_cost = 0   # 总消耗量
        current_gas = 0  # 当前油量
        start = 0        # 起点索引
        
        for i in range(n):
            total_gas += gas[i]
            total_cost += cost[i]
            current_gas += gas[i] - cost[i]
            
            # 如果当前油量小于0，说明从当前起点无法完成
            if current_gas < 0:
                start = i + 1  # 重置起点为下一个加油站
                current_gas = 0  # 重置当前油量
        
        # 如果总汽油量小于总消耗量，无法完成
        if total_gas < total_cost:
            return -1
        
        return start
    
    @staticmethod
    def can_complete_circuit_two_pass(gas: List[int], cost: List[int]) -> int:
        """
        另一种实现：两次遍历法
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not gas or not cost or len(gas) != len(cost):
            raise ValueError("输入数组不能为空且长度必须相等")
        if len(gas) == 0:
            return -1
        
        n = len(gas)
        total = 0
        current = 0
        start = 0
        
        # 第一次遍历：检查总油量是否足够
        for i in range(n):
            total += gas[i] - cost[i]
        
        if total < 0:
            return -1
        
        # 第二次遍历：找到起点
        for i in range(n):
            current += gas[i] - cost[i]
            if current < 0:
                start = i + 1
                current = 0
        
        return start
    
    @staticmethod
    def can_complete_circuit_brute_force(gas: List[int], cost: List[int]) -> int:
        """
        暴力解法：检查每个起点
        时间复杂度：O(n^2)
        空间复杂度：O(1)
        """
        if not gas or not cost or len(gas) != len(cost):
            raise ValueError("输入数组不能为空且长度必须相等")
        if len(gas) == 0:
            return -1
        
        n = len(gas)
        
        for start in range(n):
            current_gas = 0
            can_complete = True
            
            for i in range(n):
                index = (start + i) % n
                current_gas += gas[index] - cost[index]
                
                if current_gas < 0:
                    can_complete = False
                    break
            
            if can_complete:
                return start
        
        return -1
    
    @staticmethod
    def validate_circuit(gas: List[int], cost: List[int], start: int) -> bool:
        """
        验证函数：检查起点是否正确
        
        Args:
            gas: 汽油数组
            cost: 消耗数组
            start: 起点索引
            
        Returns:
            bool: 起点是否正确
        """
        if start == -1:
            # 检查是否真的无法完成
            total = 0
            for i in range(len(gas)):
                total += gas[i] - cost[i]
            return total < 0
        
        n = len(gas)
        current_gas = 0
        
        for i in range(n):
            index = (start + i) % n
            current_gas += gas[index] - cost[index]
            if current_gas < 0:
                return False
        
        return True
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 加油站测试 ===")
        
        # 测试用例1: gas = [1,2,3,4,5], cost = [3,4,5,1,2] -> 3
        gas1 = [1,2,3,4,5]
        cost1 = [3,4,5,1,2]
        print(f"测试用例1:")
        print(f"Gas: {gas1}")
        print(f"Cost: {cost1}")
        result1 = Code34_GasStation.can_complete_circuit(gas1, cost1)
        result2 = Code34_GasStation.can_complete_circuit_two_pass(gas1, cost1)
        result3 = Code34_GasStation.can_complete_circuit_brute_force(gas1, cost1)
        print(f"方法1结果: {result1}")  # 3
        print(f"方法2结果: {result2}")  # 3
        print(f"方法3结果: {result3}")  # 3
        print(f"验证: {Code34_GasStation.validate_circuit(gas1, cost1, result1)}")
        
        # 测试用例2: gas = [2,3,4], cost = [3,4,3] -> -1
        gas2 = [2,3,4]
        cost2 = [3,4,3]
        print(f"\n测试用例2:")
        print(f"Gas: {gas2}")
        print(f"Cost: {cost2}")
        result1 = Code34_GasStation.can_complete_circuit(gas2, cost2)
        result2 = Code34_GasStation.can_complete_circuit_two_pass(gas2, cost2)
        result3 = Code34_GasStation.can_complete_circuit_brute_force(gas2, cost2)
        print(f"方法1结果: {result1}")  # -1
        print(f"方法2结果: {result2}")  # -1
        print(f"方法3结果: {result3}")  # -1
        print(f"验证: {Code34_GasStation.validate_circuit(gas2, cost2, result1)}")
        
        # 测试用例3: gas = [5,1,2,3,4], cost = [4,4,1,5,1] -> 4
        gas3 = [5,1,2,3,4]
        cost3 = [4,4,1,5,1]
        print(f"\n测试用例3:")
        print(f"Gas: {gas3}")
        print(f"Cost: {cost3}")
        result1 = Code34_GasStation.can_complete_circuit(gas3, cost3)
        result2 = Code34_GasStation.can_complete_circuit_two_pass(gas3, cost3)
        result3 = Code34_GasStation.can_complete_circuit_brute_force(gas3, cost3)
        print(f"方法1结果: {result1}")  # 4
        print(f"方法2结果: {result2}")  # 4
        print(f"方法3结果: {result3}")  # 4
        print(f"验证: {Code34_GasStation.validate_circuit(gas3, cost3, result1)}")
        
        # 测试用例4: gas = [5], cost = [4] -> 0
        gas4 = [5]
        cost4 = [4]
        print(f"\n测试用例4:")
        print(f"Gas: {gas4}")
        print(f"Cost: {cost4}")
        result1 = Code34_GasStation.can_complete_circuit(gas4, cost4)
        result2 = Code34_GasStation.can_complete_circuit_two_pass(gas4, cost4)
        result3 = Code34_GasStation.can_complete_circuit_brute_force(gas4, cost4)
        print(f"方法1结果: {result1}")  # 0
        print(f"方法2结果: {result2}")  # 0
        print(f"方法3结果: {result3}")  # 0
        print(f"验证: {Code34_GasStation.validate_circuit(gas4, cost4, result1)}")
        
        # 边界测试：空数组
        try:
            gas5 = []
            cost5 = []
            print(f"\n测试用例5:")
            print(f"Gas: {gas5}")
            print(f"Cost: {cost5}")
            result1 = Code34_GasStation.can_complete_circuit(gas5, cost5)
            result2 = Code34_GasStation.can_complete_circuit_two_pass(gas5, cost5)
            result3 = Code34_GasStation.can_complete_circuit_brute_force(gas5, cost5)
            print(f"方法1结果: {result1}")  # -1
            print(f"方法2结果: {result2}")  # -1
            print(f"方法3结果: {result3}")  # -1
            print(f"验证: {Code34_GasStation.validate_circuit(gas5, cost5, result1)}")
        except ValueError as e:
            print(f"测试用例5异常（预期行为）: {e}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        n = 10000
        gas = [random.randint(1, 10) for _ in range(n)]  # 1-10
        cost = [random.randint(1, 10) for _ in range(n)]  # 1-10
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code34_GasStation.can_complete_circuit(gas, cost)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        print(f"验证: {Code34_GasStation.validate_circuit(gas, cost, result1)}")
        
        start_time2 = time.time()
        result2 = Code34_GasStation.can_complete_circuit_two_pass(gas, cost)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        print(f"验证: {Code34_GasStation.validate_circuit(gas, cost, result2)}")
        
        start_time3 = time.time()
        result3 = Code34_GasStation.can_complete_circuit_brute_force(gas, cost)
        end_time3 = time.time()
        print(f"方法3执行时间: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
        print(f"验证: {Code34_GasStation.validate_circuit(gas, cost, result3)}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（贪心算法）:")
        print("- 时间复杂度: O(n)")
        print("  - 只需要遍历一次数组")
        print("  - 每个加油站处理一次")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法2（两次遍历）:")
        print("- 时间复杂度: O(n)")
        print("  - 两次遍历数组")
        print("  - 总体线性时间复杂度")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n方法3（暴力解法）:")
        print("- 时间复杂度: O(n^2)")
        print("  - 外层循环: O(n)")
        print("  - 内层循环: O(n)")
        print("  - 总体平方时间复杂度")
        print("- 空间复杂度: O(1)")
        print("  - 只使用常数空间")
        
        print("\n贪心策略证明:")
        print("1. 总油量必须大于等于总消耗是必要条件")
        print("2. 如果从起点i无法到达j，那么i到j之间的任何点都无法到达j")
        print("3. 数学归纳法证明贪心选择性质")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理空数组和边界情况")
        print("2. 性能优化：避免不必要的计算")
        print("3. 可读性：清晰的算法逻辑和注释")
        print("4. 测试覆盖：全面的测试用例设计")

def main():
    """主函数"""
    Code34_GasStation.run_tests()
    Code34_GasStation.performance_test()
    Code34_GasStation.analyze_complexity()

if __name__ == "__main__":
    main()