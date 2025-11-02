import time
import random
from typing import List

class Code28_LemonadeChange:
    """
    柠檬水找零
    
    题目描述：
    在柠檬水摊上，每一杯柠檬水的售价为 5 美元。
    顾客排队购买你的产品，一次购买一杯。
    每位顾客只买一杯柠檬水，然后向你支付 5 美元、10 美元或 20 美元。
    你必须给每个顾客正确找零。
    注意，一开始你手头没有任何零钱。
    如果你能给每位顾客正确找零，返回 true ，否则返回 false 。
    
    来源：LeetCode 860
    链接：https://leetcode.cn/problems/lemonade-change/
    
    算法思路：
    使用贪心算法：
    1. 维护两个变量：five_count（5美元数量）和 ten_count（10美元数量）
    2. 遍历每个顾客的支付金额：
        - 如果支付5美元：直接收下，five_count++
        - 如果支付10美元：需要找零5美元，检查是否有足够的5美元
        - 如果支付20美元：优先使用10美元+5美元找零（贪心策略），如果没有10美元则使用3张5美元
    3. 如果无法找零，返回false；否则处理完所有顾客后返回true
    
    时间复杂度：O(n) - 只需要遍历一次顾客数组
    空间复杂度：O(1) - 只使用常数空间存储5美元和10美元的数量
    
    关键点分析：
    - 贪心策略：支付20美元时优先使用10美元+5美元的组合
    - 边界处理：检查零钱是否足够
    - 异常场景：大额支付无法找零的情况
    
    工程化考量：
    - 输入验证：检查账单数组是否为空或包含非法面额
    - 边界处理：处理第一个顾客支付20美元的情况
    - 性能优化：使用基本类型而非复杂数据结构
    - 异常处理：提供清晰的错误信息
    """
    
    @staticmethod
    def lemonade_change(bills: List[int]) -> bool:
        """
        判断是否能给所有顾客正确找零
        
        Args:
            bills: 顾客支付的账单列表
            
        Returns:
            bool: 是否能正确找零
            
        Raises:
            ValueError: 如果账单包含非法面额
        """
        # 输入验证
        if bills is None:
            raise ValueError("账单列表不能为None")
        
        # 特殊情况：空列表
        if not bills:
            return True
        
        five_count = 0  # 5美元数量
        ten_count = 0   # 10美元数量
        
        for i, bill in enumerate(bills):
            # 验证账单面额合法性
            if bill not in {5, 10, 20}:
                raise ValueError(f"非法账单面额: {bill}，只支持5、10、20美元")
            
            if bill == 5:
                # 支付5美元，直接收下
                five_count += 1
            elif bill == 10:
                # 支付10美元，需要找零5美元
                if five_count > 0:
                    five_count -= 1
                    ten_count += 1
                else:
                    # 没有5美元找零
                    return False
            else:  # bill == 20
                # 支付20美元，需要找零15美元
                # 贪心策略：优先使用10美元+5美元的组合
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1
                    five_count -= 1
                elif five_count >= 3:
                    # 如果没有10美元，使用3张5美元
                    five_count -= 3
                else:
                    # 无法找零
                    return False
            
            # 调试信息：打印当前零钱状态（实际工程中可移除）
            # print(f"处理账单 {bill} 后: 5美元={five_count}, 10美元={ten_count}")
        
        return True
    
    @staticmethod
    def lemonade_change_with_details(bills: List[int]) -> bool:
        """
        另一种实现方式：使用更详细的错误信息
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not bills:
            return True
        
        five, ten = 0, 0
        
        for i, bill in enumerate(bills):
            # 验证输入
            if bill not in {5, 10, 20}:
                print(f"错误：第{i+1}位顾客支付了非法面额 {bill}")
                return False
            
            if bill == 5:
                five += 1
            elif bill == 10:
                if five == 0:
                    print(f"错误：第{i+1}位顾客支付10美元，但无法找零5美元")
                    return False
                five -= 1
                ten += 1
            else:  # bill == 20
                if ten > 0 and five > 0:
                    ten -= 1
                    five -= 1
                elif five >= 3:
                    five -= 3
                else:
                    print(f"错误：第{i+1}位顾客支付20美元，但无法找零15美元")
                    return False
        
        return True
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 柠檬水找零测试 ===")
        
        # 测试用例1: [5,5,5,10,20] -> True
        bills1 = [5, 5, 5, 10, 20]
        print(f"测试用例1: {bills1}")
        result = Code28_LemonadeChange.lemonade_change(bills1)
        print(f"结果: {result}")  # 期望: True
        
        # 测试用例2: [5,5,10,10,20] -> False
        bills2 = [5, 5, 10, 10, 20]
        print(f"\n测试用例2: {bills2}")
        result = Code28_LemonadeChange.lemonade_change(bills2)
        print(f"结果: {result}")  # 期望: False
        
        # 测试用例3: [5,5,10] -> True
        bills3 = [5, 5, 10]
        print(f"\n测试用例3: {bills3}")
        result = Code28_LemonadeChange.lemonade_change(bills3)
        print(f"结果: {result}")  # 期望: True
        
        # 测试用例4: [10,10] -> False (第一个顾客支付10美元就无法找零)
        bills4 = [10, 10]
        print(f"\n测试用例4: {bills4}")
        result = Code28_LemonadeChange.lemonade_change(bills4)
        print(f"结果: {result}")  # 期望: False
        
        # 测试用例5: [5,5,10,10,5,20,5,10,5,5] -> True
        bills5 = [5,5,10,10,5,20,5,10,5,5]
        print(f"\n测试用例5: {bills5}")
        result = Code28_LemonadeChange.lemonade_change(bills5)
        print(f"结果: {result}")  # 期望: True
        
        # 测试用例6: 空列表 -> True
        bills6 = []
        print(f"\n测试用例6: {bills6}")
        result = Code28_LemonadeChange.lemonade_change(bills6)
        print(f"结果: {result}")  # 期望: True
        
        # 边界测试：单个5美元
        bills7 = [5]
        print(f"\n测试用例7: {bills7}")
        result = Code28_LemonadeChange.lemonade_change(bills7)
        print(f"结果: {result}")  # 期望: True
        
        # 异常测试：非法面额
        try:
            bills8 = [5, 15, 10]
            print(f"\n测试用例8: {bills8}")
            result = Code28_LemonadeChange.lemonade_change(bills8)
            print(f"结果: {result}")
        except ValueError as e:
            print(f"异常测试通过: {e}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        large_bills = []
        for _ in range(1000000):
            # 随机生成5、10、20美元，比例大致为6:3:1
            rand = random.randint(0, 9)
            if rand < 6:
                large_bills.append(5)
            elif rand < 9:
                large_bills.append(10)
            else:
                large_bills.append(20)
        
        start_time = time.time()
        result = Code28_LemonadeChange.lemonade_change(large_bills)
        end_time = time.time()
        
        print(f"大规模测试结果: {result}")
        print(f"执行时间: {(end_time - start_time) * 1000:.2f} 毫秒")
        print(f"数据规模: {len(large_bills)} 个顾客")
    
    @staticmethod
    def correctness_test():
        """算法正确性验证"""
        print("\n=== 算法正确性验证 ===")
        
        # 验证贪心策略的正确性
        test1 = [5, 5, 10, 20]  # 应该成功
        test2 = [5, 5, 5, 20]   # 应该成功
        test3 = [5, 10, 10, 20]  # 应该失败（只有一个5美元）
        
        print(f"测试1 [5,5,10,20]: {Code28_LemonadeChange.lemonade_change(test1)}")  # True
        print(f"测试2 [5,5,5,20]: {Code28_LemonadeChange.lemonade_change(test2)}")   # True
        print(f"测试3 [5,10,10,20]: {Code28_LemonadeChange.lemonade_change(test3)}") # False
        
        # 验证贪心策略的必要性
        test4 = [5, 5, 10, 10, 20]  # 贪心策略能成功
        print(f"测试4 [5,5,10,10,20]: {Code28_LemonadeChange.lemonade_change(test4)}") # True
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("时间复杂度: O(n)")
        print("- 只需要遍历一次顾客数组")
        print("- 每个顾客的处理时间是常数时间")
        
        print("\n空间复杂度: O(1)")
        print("- 只使用两个整数变量存储5美元和10美元的数量")
        print("- 不随输入规模增长而增长")
        
        print("\n贪心策略证明:")
        print("1. 支付20美元时，优先使用10美元+5美元是最优选择")
        print("2. 这样可以保留更多的5美元用于后续找零")
        print("3. 数学证明：10美元只能用于找零20美元，而5美元可以用于找零10美元和20美元")
        
        print("\n工程化考量:")
        print("1. 输入验证：确保账单面额合法")
        print("2. 边界处理：处理空列表和极端情况")
        print("3. 性能优化：避免不必要的操作")
        print("4. 可读性：清晰的变量命名和注释")

def main():
    """主函数"""
    Code28_LemonadeChange.run_tests()
    Code28_LemonadeChange.performance_test()
    Code28_LemonadeChange.correctness_test()
    Code28_LemonadeChange.analyze_complexity()

if __name__ == "__main__":
    main()