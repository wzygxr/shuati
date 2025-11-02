"""
柠檬水找零 - 贪心算法 + 计数策略解决方案（Python实现，LeetCode版本）

题目描述：
在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯
每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元
注意，一开始你手头没有任何零钱
给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。如果你能给每位顾客正确找零，返回 true ，否则返回 false

测试链接：https://leetcode.cn/problems/lemonade-change/

算法思想：
贪心算法 + 计数策略
1. 维护5美元和10美元纸币的数量
2. 遇到5美元：直接收下，5美元数量加1
3. 遇到10美元：需要找零5美元，检查是否有5美元纸币，如果有则5美元数量减1，10美元数量加1，否则返回false
4. 遇到20美元：需要找零15美元，优先使用一张10美元和一张5美元的组合，如果没有10美元则使用三张5美元，如果都不满足则返回false

贪心策略解释：
在找零15美元时，优先使用10+5的组合而不是5+5+5，因为5美元更万能，可以用于10美元和20美元的找零，
而10美元只能用于20美元的找零，所以要尽可能保留5美元纸币

时间复杂度分析：
O(n) - 其中n是数组bills的长度，只需要遍历一次数组

空间复杂度分析：
O(1) - 只使用了常数级别的额外空间

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：空数组等特殊情况
2. 输入验证：检查输入是否为有效数组
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名
"""

from typing import List
import time
import random

class Solution:
    """
    柠檬水找零问题的解决方案类
    """
    
    def lemonadeChange(self, bills: List[int]) -> bool:
        """
        判断是否能正确找零
        
        Args:
            bills: 顾客付款数组
            
        Returns:
            bool: 是否能正确找零
            
        Raises:
            TypeError: 如果输入不是列表类型
            ValueError: 如果付款面额不是5、10或20美元
            
        算法步骤：
        1. 维护5美元和10美元纸币的数量
        2. 遍历付款数组，根据面额进行相应处理
        3. 如果无法找零，立即返回false
        4. 所有顾客都能正确找零则返回true
        """
        # 输入验证
        if not isinstance(bills, list):
            raise TypeError("输入必须是列表类型")
        
        if len(bills) == 0:
            return True  # 空数组表示没有顾客，可以正确找零
        
        five_count = 0  # 5美元纸币数量
        ten_count = 0   # 10美元纸币数量
        
        # 遍历顾客付款数组
        for bill in bills:
            # 验证付款面额
            if bill not in [5, 10, 20]:
                raise ValueError("付款面额必须是5、10或20美元")
            
            if bill == 5:
                # 顾客支付5美元，无需找零，直接收下
                five_count += 1
            elif bill == 10:
                # 顾客支付10美元，需要找零5美元
                if five_count > 0:
                    five_count -= 1  # 找零一张5美元
                    ten_count += 1   # 收下一张10美元
                else:
                    # 没有5美元纸币找零，返回false
                    return False
            elif bill == 20:
                # 顾客支付20美元，需要找零15美元
                # 贪心策略：优先使用一张10美元和一张5美元的组合
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1   # 找零一张10美元
                    five_count -= 1  # 找零一张5美元
                # 如果没有10美元，则尝试使用三张5美元
                elif five_count >= 3:
                    five_count -= 3  # 找零三张5美元
                # 如果两种方式都不满足，则无法正确找零
                else:
                    return False
        
        # 所有顾客都能正确找零
        return True
    
    def debug_lemonadeChange(self, bills: List[int]) -> bool:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            bills: 顾客付款数组
            
        Returns:
            bool: 是否能正确找零
        """
        if len(bills) == 0:
            print("没有顾客，可以正确找零")
            return True
        
        print("顾客付款序列:")
        for i, bill in enumerate(bills):
            print(f"第{i+1}位顾客支付: {bill}美元")
        
        five_count = 0
        ten_count = 0
        
        print("\n找零过程:")
        for i, bill in enumerate(bills):
            print(f"第{i+1}位顾客支付{bill}美元: ", end="")
            
            if bill == 5:
                five_count += 1
                print(f"收下5美元，无需找零。当前5美元数量: {five_count}, 10美元数量: {ten_count}")
            elif bill == 10:
                if five_count > 0:
                    five_count -= 1
                    ten_count += 1
                    print(f"找零5美元，收下10美元。当前5美元数量: {five_count}, 10美元数量: {ten_count}")
                else:
                    print("无法找零5美元，交易失败")
                    return False
            elif bill == 20:
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1
                    five_count -= 1
                    print(f"找零10+5美元组合。当前5美元数量: {five_count}, 10美元数量: {ten_count}")
                elif five_count >= 3:
                    five_count -= 3
                    print(f"找零5+5+5美元组合。当前5美元数量: {five_count}, 10美元数量: {ten_count}")
                else:
                    print("无法找零15美元，交易失败")
                    return False
        
        print("\n所有顾客都能正确找零")
        return True

def test_lemonadeChange():
    """
    测试函数：验证柠檬水找零算法的正确性
    """
    solution = Solution()
    
    print("柠檬水找零算法测试开始")
    print("=" * 50)
    
    # 测试用例1: [5,5,5,10,20]
    bills1 = [5, 5, 5, 10, 20]
    result1 = solution.lemonadeChange(bills1)
    print("输入: [5,5,5,10,20]")
    print("输出:", result1)
    print("预期: True")
    print("✓ 通过" if result1 else "✗ 失败")
    print()
    
    # 测试用例2: [5,5,10,10,20]
    bills2 = [5, 5, 10, 10, 20]
    result2 = solution.lemonadeChange(bills2)
    print("输入: [5,5,10,10,20]")
    print("输出:", result2)
    print("预期: False")
    print("✓ 通过" if not result2 else "✗ 失败")
    print()
    
    # 测试用例3: [10,10]
    bills3 = [10, 10]
    result3 = solution.lemonadeChange(bills3)
    print("输入: [10,10]")
    print("输出:", result3)
    print("预期: False")
    print("✓ 通过" if not result3 else "✗ 失败")
    print()
    
    # 测试用例4: [5,5,10]
    bills4 = [5, 5, 10]
    result4 = solution.lemonadeChange(bills4)
    print("输入: [5,5,10]")
    print("输出:", result4)
    print("预期: True")
    print("✓ 通过" if result4 else "✗ 失败")
    print()
    
    # 测试用例5: 空数组
    bills5 = []
    result5 = solution.lemonadeChange(bills5)
    print("输入: []")
    print("输出:", result5)
    print("预期: True")
    print("✓ 通过" if result5 else "✗ 失败")
    print()
    
    # 测试用例6: 复杂情况
    bills6 = [5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20]
    result6 = solution.lemonadeChange(bills6)
    print("输入: [5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20]")
    print("输出:", result6)
    print("预期: False")
    print("✓ 通过" if not result6 else "✗ 失败")
    print()

def performance_test():
    """
    性能测试：测试算法在大规模数据下的表现
    """
    solution = Solution()
    
    print("性能测试开始")
    print("=" * 30)
    
    # 生成大规模测试数据
    n = 100000
    bills = []
    
    # 生成随机付款序列（5、10、20美元）
    for _ in range(n):
        random_value = random.randint(0, 2)
        if random_value == 0:
            bills.append(5)
        elif random_value == 1:
            bills.append(10)
        else:
            bills.append(20)
    
    start_time = time.time()
    result = solution.lemonadeChange(bills)
    end_time = time.time()
    
    print(f"数据规模: {n} 位顾客")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} 毫秒")
    print(f"找零结果: {'成功' if result else '失败'}")
    print("性能测试结束")

if __name__ == "__main__":
    print("柠檬水找零 - 贪心算法 + 计数策略解决方案")
    print("=" * 50)
    
    # 运行基础测试
    test_lemonadeChange()
    
    print("\n调试模式示例:")
    solution = Solution()
    debug_bills = [5, 5, 5, 10, 20]
    print("对测试用例 [5,5,5,10,20] 进行调试跟踪:")
    debug_result = solution.debug_lemonadeChange(debug_bills)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n) - 只需要遍历一次付款数组")
    print("- 空间复杂度: O(1) - 只使用常数级别额外空间")
    print("- 贪心策略: 优先使用10+5的组合找零，保留更多5美元纸币")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- 关键点: 5美元纸币的通用性比10美元更强")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # performance_test()
    
    # 测试异常处理
    print("\n异常处理测试:")
    try:
        # 创建一个无效的输入来测试类型检查
        invalid_bills = "invalid"  # 字符串而不是列表
        solution.lemonadeChange(invalid_bills)
    except TypeError as e:
        print(f"类型错误测试通过: {e}")
    
    try:
        # 测试包含非法面额的付款序列
        solution.lemonadeChange([5, 15, 10])  # 包含非法面额15
    except ValueError as e:
        print(f"数值错误测试通过: {e}")