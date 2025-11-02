# 分发饼干（Assign Cookies）
# 题目来源：LeetCode 455
# 题目链接：https://leetcode.cn/problems/assign-cookies/

"""
问题描述：
假设你是一位很棒的家长，想要给你的孩子们一些小饼干。每个孩子最多只能给一块饼干。
对每个孩子i，都有一个胃口值g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
对每个饼干j，都有一个尺寸s[j]。如果s[j] >= g[i]，我们可以将这个饼干j分配给孩子i，
这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。

算法思路：
使用贪心策略，将胃口最小的孩子分配给能满足他的最小饼干，这样可以最大化满足的孩子数量。
具体步骤：
1. 将孩子的胃口数组g和饼干尺寸数组s进行排序
2. 使用两个指针分别遍历g和s数组
3. 如果当前饼干能满足当前孩子，两个指针都向后移动
4. 否则，只移动饼干指针，寻找更大的饼干

时间复杂度：O(n log n + m log m)，其中n是孩子数量，m是饼干数量，主要是排序的时间复杂度
空间复杂度：O(log n + log m)，排序所需的额外空间

是否最优解：是。贪心策略在此问题中能得到最优解。

适用场景：
1. 资源分配问题，将有限资源分配给多个需求者
2. 匹配问题，需要最大化匹配数量

异常处理：
1. 处理空数组情况
2. 处理数组长度为0的边界情况

工程化考量：
1. 输入验证：检查数组是否为空
2. 边界条件：当没有孩子或没有饼干时，直接返回0
3. 性能优化：利用Python的内置排序函数提高效率
"""

class Solution:
    def findContentChildren(self, g, s):
        """
        计算最多能满足的孩子数量
        
        Args:
            g: List[int] - 孩子的胃口数组
            s: List[int] - 饼干尺寸数组
            
        Returns:
            int - 最多能满足的孩子数量
        """
        # 边界条件检查
        if not g or not s:
            return 0
        
        # 对胃口和饼干尺寸进行排序
        g.sort()
        s.sort()
        
        child_index = 0  # 指向当前需要满足的孩子
        cookie_index = 0  # 指向当前尝试分配的饼干
        satisfied_children = 0  # 已满足的孩子数量
        
        # 遍历所有饼干和孩子
        while child_index < len(g) and cookie_index < len(s):
            # 如果当前饼干能满足当前孩子的胃口
            if s[cookie_index] >= g[child_index]:
                satisfied_children += 1  # 满足的孩子数量加1
                child_index += 1  # 移动到下一个孩子
            # 无论是否满足，都需要尝试下一个饼干
            cookie_index += 1
        
        return satisfied_children

# 测试函数，验证算法正确性
def test_find_content_children():
    solution = Solution()
    
    # 测试用例1: 基本情况
    g1 = [1, 2, 3]
    s1 = [1, 1]
    result1 = solution.findContentChildren(g1, s1)
    print("测试用例1:")
    print(f"孩子胃口: {g1}")
    print(f"饼干尺寸: {s1}")
    print(f"最多能满足的孩子数量: {result1}")
    print(f"期望输出: 1")
    print()
    
    # 测试用例2: 所有孩子都能被满足
    g2 = [1, 2]
    s2 = [1, 2, 3]
    result2 = solution.findContentChildren(g2, s2)
    print("测试用例2:")
    print(f"孩子胃口: {g2}")
    print(f"饼干尺寸: {s2}")
    print(f"最多能满足的孩子数量: {result2}")
    print(f"期望输出: 2")
    print()
    
    # 测试用例3: 边界情况 - 没有饼干
    g3 = [1, 2, 3]
    s3 = []
    result3 = solution.findContentChildren(g3, s3)
    print("测试用例3:")
    print(f"孩子胃口: {g3}")
    print(f"饼干尺寸: {s3}")
    print(f"最多能满足的孩子数量: {result3}")
    print(f"期望输出: 0")
    print()
    
    # 测试用例4: 边界情况 - 没有孩子
    g4 = []
    s4 = [1, 2, 3]
    result4 = solution.findContentChildren(g4, s4)
    print("测试用例4:")
    print(f"孩子胃口: {g4}")
    print(f"饼干尺寸: {s4}")
    print(f"最多能满足的孩子数量: {result4}")
    print(f"期望输出: 0")
    print()
    
    # 测试用例5: 胃口数组和饼干数组长度不同
    g5 = [1, 2, 3, 4, 5]
    s5 = [1, 2, 3]
    result5 = solution.findContentChildren(g5, s5)
    print("测试用例5:")
    print(f"孩子胃口: {g5}")
    print(f"饼干尺寸: {s5}")
    print(f"最多能满足的孩子数量: {result5}")
    print(f"期望输出: 3")

# 运行测试
if __name__ == "__main__":
    test_find_content_children()