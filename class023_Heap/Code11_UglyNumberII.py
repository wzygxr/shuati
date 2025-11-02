import heapq

class Solution:
    """
    相关题目3: LeetCode 264. 丑数 II
    题目链接: https://leetcode.cn/problems/ugly-number-ii/
    题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
    丑数 就是只包含质因数 2、3 和 5 的正整数。
    解题思路: 使用最小堆维护候选丑数，确保每次取出的是当前最小的丑数
    时间复杂度: O(n log n)，每次堆操作需要O(log n)时间
    空间复杂度: O(n)，堆和集合都需要O(n)空间
    是否最优解: 是，另一种更优的动态规划解法可以达到O(n)时间复杂度，但堆解法更直观
    
    本题属于堆的典型应用场景：需要频繁获取最小值并生成新的候选值
    """
    
    def nthUglyNumber(self, n):
        """
        查找第n个丑数
        
        Args:
            n: 需要查找的丑数的位置（从1开始计数）
            
        Returns:
            int: 第n个丑数
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查n是否为正整数
        if n <= 0:
            raise ValueError("n必须是正整数")
        
        # 特殊情况处理
        if n == 1:
            return 1  # 第一个丑数是1
        
        # 定义质因数
        factors = [2, 3, 5]
        
        # 使用最小堆维护候选丑数
        min_heap = []
        # 使用集合去重
        seen = set()
        
        # 初始化堆和集合，第一个丑数是1
        heapq.heappush(min_heap, 1)
        seen.add(1)
        
        ugly = 0
        # 执行n次操作，每次取出最小的丑数并生成新的候选丑数
        for i in range(n):
            # 取出当前最小的丑数
            ugly = heapq.heappop(min_heap)
            
            # 调试信息：打印当前处理的丑数
            # print(f"当前丑数: {ugly}，是第{i + 1}个丑数")
            
            # 生成新的候选丑数：将当前丑数分别乘以2、3、5
            for factor in factors:
                next_ugly = ugly * factor
                # 如果新生成的数没有出现过，则加入堆和集合
                if next_ugly not in seen:
                    seen.add(next_ugly)
                    heapq.heappush(min_heap, next_ugly)
                    # 调试信息：打印新加入的候选丑数
                    # print(f"新加入候选丑数: {next_ugly}")
        
        # 第n次取出的就是第n个丑数
        return ugly

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    n1 = 10
    result1 = solution.nthUglyNumber(n1)
    print(f"示例1输出: {result1}")
    assert result1 == 12, f"测试用例1失败，期望12，实际得到{result1}"
    
    # 测试用例2：边界情况 - n=1
    n2 = 1
    result2 = solution.nthUglyNumber(n2)
    print(f"示例2输出: {result2}")
    assert result2 == 1, f"测试用例2失败，期望1，实际得到{result2}"
    
    # 测试用例3：较大的n
    n3 = 1500
    result3 = solution.nthUglyNumber(n3)
    print(f"示例3输出: {result3}")
    assert result3 == 859963392, f"测试用例3失败，期望859963392，实际得到{result3}"
    
    # 测试用例4：中等大小的n
    n4 = 1690
    result4 = solution.nthUglyNumber(n4)
    print(f"示例4输出: {result4}")
    assert result4 == 2123366400, f"测试用例4失败，期望2123366400，实际得到{result4}"
    
    # 测试异常情况
    try:
        # 测试用例5：异常测试 - n=0
        solution.nthUglyNumber(0)
        print("测试用例5失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例5成功捕获异常: {e}")
    
    try:
        # 测试用例6：异常测试 - n为负数
        solution.nthUglyNumber(-5)
        print("测试用例6失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例6成功捕获异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")