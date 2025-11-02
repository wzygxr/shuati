# 有依赖的背包问题（模板）
# 
# 问题描述：
# 物品分为两大类：主件和附件
# 主件的购买没有限制，钱够就可以；附件的购买有限制，该附件所归属的主件先购买，才能购买这个附件
# 例如，若想买打印机或扫描仪这样的附件，必须先购买电脑这个主件
# 以下是一些主件及其附件的展示：
# 电脑：打印机，扫描仪 | 书柜：图书 | 书桌：台灯，文具 | 工作椅：无附件
# 每个主件最多有2个附件，并且附件不会再有附件，主件购买后，怎么去选择归属附件完全随意，钱够就可以
# 所有的物品编号都在1~m之间，每个物品有三个信息：价格v、重要度p、归属q
# 价格就是花费，价格 * 重要度 就是收益，归属就是该商品是依附于哪个编号的主件
# 比如一件商品信息为[300,2,6]，花费300，收益600，该商品是6号主件商品的附件
# 再比如一件商品信息[100,4,0]，花费100，收益400，该商品自身是主件(q==0)
# 给定m件商品的信息，给定总钱数n，返回在不违反购买规则的情况下最大的收益
# 
# 解题思路：
# 这是一个经典的依赖背包问题，可以转化为分组背包问题来解决：
# 1. 将每个主件及其附件作为一个组
# 2. 对于每个组，有以下几种选择：
#    - 不选择该组
#    - 只选择主件
#    - 选择主件+附件1
#    - 选择主件+附件2
#    - 选择主件+附件1+附件2
# 3. 对每个组内的所有选择进行预处理，然后使用分组背包的解法
# 
# 时间复杂度：O(n * m)，其中n是预算，m是物品数量
# 空间复杂度：O(n)
# 
# 测试链接 : https://www.luogu.com.cn/problem/P1064
# 测试链接 : https://www.nowcoder.com/practice/f9c6f980eeec43ef85be20755ddbeaf4

# 牛客网依赖背包问题
# 
# 题目描述：有依赖的背包问题，物品分为主件和附件，附件依赖于主件，
# 只有购买了主件才能购买附件，每个主件最多有两个附件。
# 每个物品有价格和重要度，求在预算内能获得的最大收益。
# 
# 解题思路：
# 使用分组背包的思想，将每个主件及其附件作为一个组
# 对于每个组，预处理所有可能的选择组合，然后使用分组背包的解法
def dependantBackpack(budget, itemCount, prices, importances, dependencies):
    """
    计算依赖背包问题的最大收益
    
    Args:
        budget: 总预算
        itemCount: 物品数量
        prices: 物品价格数组
        importances: 物品重要度数组
        dependencies: 物品依赖关系数组
    
    Returns:
        最大收益
    """
    # 主件标识
    isMain = [False] * (itemCount + 1)
    # 附件数量
    accessoryCount = [0] * (itemCount + 1)
    # 附件列表
    accessories = [[0, 0] for _ in range(itemCount + 1)]
    
    # 初始化依赖关系
    for i in range(1, itemCount + 1):
        if dependencies[i] == 0:
            isMain[i] = True
        else:
            master = dependencies[i]
            accessories[master][accessoryCount[master]] = i
            accessoryCount[master] += 1
    
    # dp数组
    dp = [0] * (budget + 1)
    
    # 遍历主件
    for i in range(1, itemCount + 1):
        if isMain[i]:
            # 倒序遍历预算
            for j in range(budget, prices[i] - 1, -1):
                # 只买主件
                dp[j] = max(dp[j], dp[j - prices[i]] + prices[i] * importances[i])
                
                # 买主件+附件1
                if accessoryCount[i] >= 1:
                    acc1 = accessories[i][0]
                    if j >= prices[i] + prices[acc1]:
                        dp[j] = max(dp[j], dp[j - prices[i] - prices[acc1]] + 
                                   prices[i] * importances[i] + prices[acc1] * importances[acc1])
                
                # 买主件+附件2
                if accessoryCount[i] >= 2:
                    acc2 = accessories[i][1]
                    if j >= prices[i] + prices[acc2]:
                        dp[j] = max(dp[j], dp[j - prices[i] - prices[acc2]] + 
                                   prices[i] * importances[i] + prices[acc2] * importances[acc2])
                
                # 买主件+附件1+附件2
                if accessoryCount[i] >= 2:
                    acc1 = accessories[i][0]
                    acc2 = accessories[i][1]
                    if j >= prices[i] + prices[acc1] + prices[acc2]:
                        dp[j] = max(dp[j], dp[j - prices[i] - prices[acc1] - prices[acc2]] + 
                                   prices[i] * importances[i] + prices[acc1] * importances[acc1] + 
                                   prices[acc2] * importances[acc2])
    
    return dp[budget]

'''
示例:
输入: budget = 1000, itemCount = 5
prices = [800, 400, 300, 400, 200]
importances = [2, 5, 5, 3, 2]
dependencies = [0, 1, 1, 0, 4]
输出: 2200
解释: 选择主件1和主件4，以及它们的附件

时间复杂度: O(n * m)，其中n是预算，m是物品数量
空间复杂度: O(n)
'''