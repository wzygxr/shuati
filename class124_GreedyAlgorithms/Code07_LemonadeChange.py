# 柠檬水找零（Lemonade Change）
# 题目来源：LeetCode 860
# 题目链接：https://leetcode.cn/problems/lemonade-change/

"""
问题描述：
在柠檬水摊上，每杯柠檬水的售价为5美元。
顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。
你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
注意，一开始你手头没有任何零钱。
给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。
如果你能给每位顾客正确找零，返回 true ，否则返回 false 。

算法思路：
使用贪心策略，优先使用大面额的钞票来找零，这样可以保留更多小面额的钞票用于未来可能的找零。
具体步骤：
1. 维护两个变量，分别记录当前拥有的5美元和10美元的数量
2. 遍历账单数组：
   - 如果顾客支付5美元，直接增加5美元的数量
   - 如果顾客支付10美元，需要找零5美元，减少5美元数量，增加10美元数量
   - 如果顾客支付20美元，优先找零10+5美元，然后再考虑三个5美元
3. 如果在任何时候无法满足找零需求，返回false

时间复杂度：O(n)，其中n是账单数量，只需遍历数组一次
空间复杂度：O(1)，只使用了常数额外空间

是否最优解：是。贪心策略在此问题中能得到最优解。

适用场景：
1. 现金交易找零问题
2. 资源分配问题，其中不同面额的钞票代表不同类型的资源

异常处理：
1. 处理空数组情况
2. 处理无效支付情况（如支付不是5、10、20的情况）

工程化考量：
1. 输入验证：检查数组是否为空，检查支付金额是否合法
2. 边界条件：处理第一个顾客不是支付5美元的情况
3. 性能优化：使用变量而不是字典来跟踪钞票数量，提高效率
"""

class Solution:
    def lemonadeChange(self, bills):
        """
        判断是否能给每位顾客正确找零
        
        Args:
            bills: List[int] - 顾客支付的账单数组
            
        Returns:
            bool - 如果能正确找零返回True，否则返回False
        """
        # 边界条件检查
        if not bills:
            return True  # 没有顾客，自然能正确找零
        
        five_count = 0  # 5美元钞票的数量
        ten_count = 0   # 10美元钞票的数量
        
        for bill in bills:
            if bill == 5:
                # 顾客支付5美元，不需要找零，直接收入
                five_count += 1
            elif bill == 10:
                # 顾客支付10美元，需要找零5美元
                if five_count == 0:
                    return False  # 没有5美元钞票找零
                five_count -= 1
                ten_count += 1
            elif bill == 20:
                # 顾客支付20美元，优先找零10+5美元，因为5美元更常用
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1
                    five_count -= 1
                elif five_count >= 3:
                    # 如果没有10美元钞票，使用三个5美元钞票找零
                    five_count -= 3
                else:
                    return False  # 无法找零
            else:
                # 无效的支付金额
                return False
        
        # 所有顾客都能正确找零
        return True

# 测试函数，验证算法正确性
def test_lemonade_change():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 能正确找零
    bills1 = [5, 5, 5, 10, 20]
    result1 = solution.lemonadeChange(bills1)
    print("测试用例1:")
    print(f"账单顺序: {bills1}")
    print(f"能否正确找零: {result1}")
    print(f"期望输出: True")
    print()
    
    # 测试用例2: 基本情况 - 不能正确找零
    bills2 = [5, 5, 10, 10, 20]
    result2 = solution.lemonadeChange(bills2)
    print("测试用例2:")
    print(f"账单顺序: {bills2}")
    print(f"能否正确找零: {result2}")
    print(f"期望输出: False")
    print()
    
    # 测试用例3: 边界情况 - 空数组
    bills3 = []
    result3 = solution.lemonadeChange(bills3)
    print("测试用例3:")
    print(f"账单顺序: {bills3}")
    print(f"能否正确找零: {result3}")
    print(f"期望输出: True")
    print()
    
    # 测试用例4: 边界情况 - 第一个顾客支付10美元
    bills4 = [10, 5, 5, 5]
    result4 = solution.lemonadeChange(bills4)
    print("测试用例4:")
    print(f"账单顺序: {bills4}")
    print(f"能否正确找零: {result4}")
    print(f"期望输出: False")
    print()
    
    # 测试用例5: 复杂情况 - 多个20美元的处理
    bills5 = [5, 5, 10, 5, 20, 5, 10, 5, 20]
    result5 = solution.lemonadeChange(bills5)
    print("测试用例5:")
    print(f"账单顺序: {bills5}")
    print(f"能否正确找零: {result5}")
    print(f"期望输出: True")

# 运行测试
if __name__ == "__main__":
    test_lemonade_change()