# EKO (SPOJ)
# Lumberjack Mirko needs to chop down M metres of wood. It is an easy job for him since he has a nifty new woodcutting machine that can take down forests like wildfire. 
# However, Mirko is only allowed to cut a single row of trees.
# Mirko's machine works as follows: Mirko sets a height parameter H (in metres), and the machine raises a giant sawblade to that height and cuts off all tree parts higher than H (of course, trees not higher than H meters remain intact). 
# Mirko then takes the parts that were cut off. For example, if the tree row contains trees with heights of 20, 15, 10, and 17 metres, and Mirko raises his sawblade to 15 metres, the remaining tree heights after cutting will be 15, 15, 10, and 15 metres, respectively, while Mirko will take 5 metres off the first tree and 2 metres off the fourth tree (7 metres of wood in total).
# Mirko is ecologically minded, so he doesn't want to cut off more wood than necessary. That's why he wants to set his sawblade at the height that will allow him to cut off at least M metres of wood, but with as little waste as possible.
# What is the maximum integer height of the sawblade that still allows him to cut off at least M metres of wood?
# Problem Link: https://www.spoj.com/problems/EKO/

def eko(trees, required_wood):
    """
    使用二分答案解决EKO问题
    
    Args:
        trees: 树木高度列表
        required_wood: 需要的木材总量
    
    Returns:
        最高的锯片高度
    
    时间复杂度: O(n * log(max))
    空间复杂度: O(1)
    """
    # 确定二分搜索的上下界
    # 下界：0（不切割任何树木）
    # 上界：树木中的最大高度
    left = 0
    right = max(trees) if trees else 0
    
    result = 0
    
    # 二分搜索最高的锯片高度
    while left <= right:
        mid = left + ((right - left) >> 1)
        # 判断以mid为锯片高度是否能获得至少required_wood的木材
        if get_wood(trees, mid) >= required_wood:
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result

def get_wood(trees, saw_height):
    """
    计算以saw_height为锯片高度能获得的木材总量
    
    Args:
        trees: 树木高度列表
        saw_height: 锯片高度
    
    Returns:
        获得的木材总量
    """
    total_wood = 0
    
    for tree in trees:
        # 如果树木高度大于锯片高度，则可以获得木材
        if tree > saw_height:
            total_wood += tree - saw_height
    
    return total_wood

"""
补充说明：

问题解析：
这是一个经典的二分答案问题，目标是找到最高的锯片高度，使得切下的木材总量至少为M米。
这是一个"最大化满足条件的值"问题。

解题思路：
1. 确定答案范围：
   - 下界：0（不切割任何树木）
   - 上界：树木中的最大高度
2. 二分搜索：在[left, right]范围内二分搜索最高的锯片高度
3. 判断函数：get_wood(trees, saw_height)计算以saw_height为锯片高度能获得的木材总量
4. 贪心策略：对于每棵树，切掉高于锯片高度的部分

时间复杂度分析：
1. 二分搜索范围是[0, max]，二分次数是O(log(max))
2. 每次二分需要调用get_wood函数，该函数遍历数组一次，时间复杂度是O(n)
3. 总时间复杂度：O(n * log(max))

空间复杂度分析：
只使用了常数个额外变量，空间复杂度是O(1)

工程化考虑：
1. 数据类型选择：Python自动处理大整数
2. 边界条件处理：注意锯片高度为0或等于最大树高的情况
3. 位运算优化：(right - left) >> 1 等价于 (right - left) // 2，但效率略高

相关题目扩展：
1. SPOJ EKO - https://www.spoj.com/problems/EKO/
2. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
3. LeetCode 875. 爱吃香蕉的珂珂 - https://leetcode.cn/problems/koko-eating-bananas/
4. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
5. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
6. SPOJ AGGRCOW - Aggressive Cows - https://www.spoj.com/problems/AGGRCOW/
7. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
8. HackerRank - Fair Rations - https://www.hackerrank.com/challenges/fair-rations/problem
9. Codeforces 460C - Present - https://codeforces.com/problemset/problem/460/C
10. AtCoder ABC146 - C - Buy an Integer - https://atcoder.jp/contests/abc146/tasks/abc146_c
"""

# 测试代码
if __name__ == "__main__":
    trees = [20, 15, 10, 17]
    required_wood = 7
    result = eko(trees, required_wood)
    print(f"EKO Result: {result}")