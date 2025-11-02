# 分发糖果（Candy）
# 题目来源：LeetCode 135
# 题目链接：https://leetcode.cn/problems/candy/
# 
# 问题描述：
# 老师想给孩子们分发糖果，有N个孩子站成了一条直线，每个孩子至少分配到1个糖果。
# 相邻的孩子中，评分高的孩子必须获得更多的糖果。
# 计算最少需要准备多少糖果。
# 
# 算法思路：
# 使用贪心策略，两次遍历：
# 1. 从左到右遍历，如果当前孩子评分比左边高，糖果数比左边多1
# 2. 从右到左遍历，如果当前孩子评分比右边高，糖果数取当前值和右边值+1的最大值
# 3. 最后统计所有糖果数之和
# 
# 时间复杂度：O(n) - 两次遍历数组
# 空间复杂度：O(n) - 需要额外的糖果数组
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 分配问题，需要满足相邻约束条件
# 2. 双向约束的最优化问题
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理单元素数组
# 
# 工程化考量：
# 1. 输入验证：检查数组是否为空
# 2. 边界条件：处理单元素和双元素数组
# 3. 性能优化：使用列表推导式提高效率
# 
# 相关题目：
# 1. LeetCode 42. 接雨水 - 双向遍历的经典问题
# 2. LeetCode 84. 柱状图中最大的矩形 - 单调栈应用
# 3. LeetCode 406. 根据身高重建队列 - 贪心排序问题
# 4. 牛客网 NC140 排序 - 各种排序算法实现
# 5. LintCode 391. 数飞机 - 区间调度相关
# 6. HackerRank - Jim and the Orders - 贪心调度问题
# 7. CodeChef - TACHSTCK - 区间配对问题
# 8. AtCoder ABC104C - All Green - 动态规划相关
# 9. Codeforces 1363C - Game On Leaves - 博弈论相关
# 10. POJ 3169 - Layout - 差分约束系统

class Solution:
    """
    计算最少需要准备的糖果数量
    
    Args:
        ratings: List[int] - 孩子的评分数组
    
    Returns:
        int - 最少需要的糖果数量
    """
    def candy(self, ratings):
        # 边界条件检查
        if not ratings:
            return 0
        
        n = len(ratings)
        if n == 1:
            return 1  # 只有一个孩子，最少需要1个糖果
        
        # 初始化糖果数组，每个孩子至少1个糖果
        candies = [1] * n
        
        # 从左到右遍历，处理递增序列
        for i in range(1, n):
            if ratings[i] > ratings[i - 1]:
                candies[i] = candies[i - 1] + 1
        
        # 从右到左遍历，处理递减序列
        for i in range(n - 2, -1, -1):
            if ratings[i] > ratings[i + 1]:
                candies[i] = max(candies[i], candies[i + 1] + 1)
        
        # 统计总糖果数
        return sum(candies)


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 递增序列
    ratings1 = [1, 0, 2]
    result1 = solution.candy(ratings1)
    print("测试用例1:")
    print(f"评分数组: {ratings1}")
    print(f"最少糖果数: {result1}")
    print("期望输出: 5")
    print()
    
    # 测试用例2: 基本情况 - 递减序列
    ratings2 = [1, 2, 2]
    result2 = solution.candy(ratings2)
    print("测试用例2:")
    print(f"评分数组: {ratings2}")
    print(f"最少糖果数: {result2}")
    print("期望输出: 4")
    print()
    
    # 测试用例3: 复杂情况 - 山峰形状
    ratings3 = [1, 3, 2, 2, 1]
    result3 = solution.candy(ratings3)
    print("测试用例3:")
    print(f"评分数组: {ratings3}")
    print(f"最少糖果数: {result3}")
    print("期望输出: 7")
    print()
    
    # 测试用例4: 边界情况 - 单元素数组
    ratings4 = [5]
    result4 = solution.candy(ratings4)
    print("测试用例4:")
    print(f"评分数组: {ratings4}")
    print(f"最少糖果数: {result4}")
    print("期望输出: 1")
    print()
    
    # 测试用例5: 边界情况 - 两个相同评分
    ratings5 = [2, 2]
    result5 = solution.candy(ratings5)
    print("测试用例5:")
    print(f"评分数组: {ratings5}")
    print(f"最少糖果数: {result5}")
    print("期望输出: 2")
    print()
    
    # 测试用例6: 复杂情况 - 长序列
    ratings6 = [1, 2, 87, 87, 87, 2, 1]
    result6 = solution.candy(ratings6)
    print("测试用例6:")
    print(f"评分数组: {ratings6}")
    print(f"最少糖果数: {result6}")
    print("期望输出: 13")


if __name__ == "__main__":
    main()