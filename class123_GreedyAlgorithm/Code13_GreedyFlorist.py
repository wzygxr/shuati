# 贪心花匠
# 一个花店有n朵花，每朵花都有一个基本价格。有k个顾客去买花。
# 花店老板为了最大化收入，决定采用以下策略：
# 一个顾客买下第i朵花的价格 = (这个顾客之前买的花的数量 + 1) * 这朵花的基本价格
# 例如，如果一个顾客之前买了2朵花，现在要买一朵价格为5的花，那么他需要支付(2+1)*5=15。
# 你的任务是计算k个顾客买下所有n朵花所需的最少总费用。
# 测试链接 : https://www.hackerrank.com/challenges/greedy-florist/problem

'''
贪心算法解法

核心思想：
1. 为了最小化总费用，我们应该让价格高的花被购买的次数尽可能少
2. 这可以通过让每个顾客轮流购买最贵的花来实现
3. 具体来说，我们应该先将花按价格降序排序
4. 然后按顺序分配给顾客，每个顾客轮流购买

算法步骤：
1. 将花的价格按降序排序
2. 遍历排序后的价格数组
3. 对于第i朵花，它会被分配给第(i % k)个顾客
4. 该顾客购买这朵花的价格为(该顾客已购买的花数 + 1) * 花的价格
5. 累计总费用

时间复杂度：O(n log n) - 主要是排序的时间复杂度
空间复杂度：O(k) - 需要额外数组记录每个顾客已购买的花数

为什么这是最优解？
1. 贪心策略保证了每一步都做出了当前看起来最好的选择
2. 通过交换论证法可以证明这种策略能得到全局最优解
3. 无法在更少的时间内完成，因为至少需要排序一遍数组

工程化考虑：
1. 边界条件处理：空数组、k为0或1的情况
2. 异常处理：输入参数验证
3. 可读性：变量命名清晰，注释详细

算法调试技巧：
1. 可以通过打印每一步的分配情况来观察购买过程
2. 用断言验证中间结果是否符合预期

与机器学习的联系：
1. 这种资源分配的思想在机器学习中也有应用，如多任务学习中的资源分配
2. 在强化学习中，如何分配有限的计算资源也是一个重要问题
'''

def getMinimumCost(k, prices):
    """
    计算k个顾客买下所有n朵花所需的最少总费用
    
    Args:
        k: int - 顾客数量
        prices: List[int] - 每朵花的价格列表
    
    Returns:
        int - 最少总费用
    """
    # 边界条件：如果花的数量为0，返回0
    if not prices:
        return 0

    # 边界条件：如果顾客数量为0，无法购买任何花
    if k <= 0:
        return 0

    # 将花的价格按降序排序
    prices.sort(reverse=True)

    # 记录每个顾客已购买的花数
    purchases = [0] * k
    # 记录总费用
    totalCost = 0

    # 遍历所有花
    for i in range(len(prices)):
        # 确定这朵花分配给哪个顾客
        customer = i % k

        # 计算该顾客购买这朵花的价格
        cost = (purchases[customer] + 1) * prices[i]

        # 累计总费用
        totalCost += cost

        # 更新该顾客已购买的花数
        purchases[customer] += 1

    return totalCost

# 测试方法
if __name__ == "__main__":
    # 测试用例1: k=3, prices=[2,5,6] -> 13
    k1 = 3
    prices1 = [2, 5, 6]
    print("测试用例1: k={}, prices={}".format(k1, prices1))
    print("预期结果: 13, 实际结果:", getMinimumCost(k1, prices1))
    print()
    
    # 测试用例2: k=2, prices=[2,5,6] -> 15
    k2 = 2
    prices2 = [2, 5, 6]
    print("测试用例2: k={}, prices={}".format(k2, prices2))
    print("预期结果: 15, 实际结果:", getMinimumCost(k2, prices2))
    print()
    
    # 测试用例3: k=3, prices=[1,3,5,7,9] -> 29
    k3 = 3
    prices3 = [1, 3, 5, 7, 9]
    print("测试用例3: k={}, prices={}".format(k3, prices3))
    print("预期结果: 29, 实际结果:", getMinimumCost(k3, prices3))
    print()
    
    # 测试用例4: k=1, prices=[1,2,3,4] -> 20
    k4 = 1
    prices4 = [1, 2, 3, 4]
    print("测试用例4: k={}, prices={}".format(k4, prices4))
    print("预期结果: 20, 实际结果:", getMinimumCost(k4, prices4))
    print()
    
    # 测试用例5: k=5, prices=[1,2,3,4] -> 10
    k5 = 5
    prices5 = [1, 2, 3, 4]
    print("测试用例5: k={}, prices={}".format(k5, prices5))
    print("预期结果: 10, 实际结果:", getMinimumCost(k5, prices5))
    print()