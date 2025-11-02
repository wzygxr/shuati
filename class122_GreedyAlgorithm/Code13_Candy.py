# 分发糖果
# n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
# 你需要按照以下要求，给这些孩子分发糖果：
# 每个孩子至少分配到 1 个糖果。
# 相邻两个孩子评分更高的孩子会获得更多的糖果。
# 请你给每个孩子分发糖果，计算并返回需要准备的 最少糖果数目 。
# 测试链接 : https://leetcode.cn/problems/candy/

def candy(ratings):
    """
    分发糖果问题
    
    算法思路：
    使用贪心策略：
    1. 从左到右遍历，确保右边评分高的孩子比左边的糖果多
    2. 从右到左遍历，确保左边评分高的孩子比右边的糖果多
    3. 取两次遍历结果的最大值
    
    正确性分析：
    1. 问题有两个约束条件：左规则和右规则
    2. 左规则：ratings[i] > ratings[i-1] 时，candies[i] > candies[i-1]
    3. 右规则：ratings[i] > ratings[i+1] 时，candies[i] > candies[i+1]
    4. 两次遍历分别满足左右规则，取最大值可以同时满足两个规则
    
    时间复杂度：O(n) - 需要遍历数组两次
    空间复杂度：O(n) - 需要额外的数组存储糖果数量
    
    :param ratings: 孩子评分数组
    :return: 最少糖果数目
    """
    n = len(ratings)
    # 边界情况处理
    if n == 0:
        return 0
    if n == 1:
        return 1
    
    # 初始化每个孩子至少1个糖果
    candies = [1] * n
    
    # 从左到右遍历，满足右规则
    for i in range(1, n):
        if ratings[i] > ratings[i - 1]:
            candies[i] = candies[i - 1] + 1
    
    # 从右到左遍历，满足左规则
    for i in range(n - 2, -1, -1):
        if ratings[i] > ratings[i + 1]:
            candies[i] = max(candies[i], candies[i + 1] + 1)
    
    # 计算总糖果数
    return sum(candies)

# 测试用例
if __name__ == "__main__":
    # 测试用例1: ratings = [1,0,2] -> 输出: 5
    ratings1 = [1, 0, 2]
    print("测试用例1:")
    print("评分数组:", ratings1)
    print("最少糖果数目:", candy(ratings1))  # 期望输出: 5 (2+1+2)
    
    # 测试用例2: ratings = [1,2,2] -> 输出: 4
    ratings2 = [1, 2, 2]
    print("\n测试用例2:")
    print("评分数组:", ratings2)
    print("最少糖果数目:", candy(ratings2))  # 期望输出: 4 (1+2+1)
    
    # 测试用例3: ratings = [1,3,2,2,1] -> 输出: 7
    ratings3 = [1, 3, 2, 2, 1]
    print("\n测试用例3:")
    print("评分数组:", ratings3)
    print("最少糖果数目:", candy(ratings3))  # 期望输出: 7 (1+2+1+2+1)
    
    # 测试用例4: ratings = [1] -> 输出: 1
    ratings4 = [1]
    print("\n测试用例4:")
    print("评分数组:", ratings4)
    print("最少糖果数目:", candy(ratings4))  # 期望输出: 1