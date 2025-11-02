import heapq

class Solution:
    """
    相关题目22: LeetCode 264. 丑数 II
    题目链接: https://leetcode.cn/problems/ugly-number-ii/
    题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
    丑数 就是只包含质因数 2、3 和 5 的正整数。
    解题思路1: 使用最小堆生成丑数序列
    解题思路2: 使用动态规划，维护三个指针分别指向2、3、5的下一个乘数
    时间复杂度: 最小堆O(n log n)，动态规划O(n)
    空间复杂度: 最小堆O(n)，动态规划O(n)
    是否最优解: 动态规划解法是最优的，时间复杂度为O(n)
    
    本题属于堆的应用场景：生成排序序列中的第n个元素
    """
    
    def nthUglyNumberHeap(self, n):
        """
        使用最小堆生成丑数序列
        
        Args:
            n: 第n个丑数
            
        Returns:
            int: 第n个丑数
            
        Raises:
            ValueError: 当n参数无效时抛出异常
        """
        # 异常处理：检查n是否有效
        if not isinstance(n, int) or n <= 0:
            raise ValueError("n必须是正整数")
        
        # 特殊情况：第1个丑数是1
        if n == 1:
            return 1
        
        # 使用集合来记录已经生成的丑数，避免重复
        seen = {1}
        # 创建最小堆
        heap = [1]
        
        # 质因数列表
        factors = [2, 3, 5]
        
        # 用于记录当前找到的丑数
        current = 1
        
        # 循环n次，找到第n个丑数
        for _ in range(n):
            # 取出堆顶元素，即当前最小的丑数
            current = heapq.heappop(heap)
            
            # 生成新的丑数
            for factor in factors:
                next_ugly = current * factor
                # 如果新丑数未被生成过，则加入堆和集合
                if next_ugly not in seen:
                    seen.add(next_ugly)
                    heapq.heappush(heap, next_ugly)
        
        # 返回第n个丑数
        return current
    
    def nthUglyNumberDP(self, n):
        """
        使用动态规划生成丑数序列
        
        Args:
            n: 第n个丑数
            
        Returns:
            int: 第n个丑数
            
        Raises:
            ValueError: 当n参数无效时抛出异常
        """
        # 异常处理：检查n是否有效
        if not isinstance(n, int) or n <= 0:
            raise ValueError("n必须是正整数")
        
        # 特殊情况：第1个丑数是1
        if n == 1:
            return 1
        
        # 创建一个数组来存储前n个丑数
        ugly_numbers = [0] * n
        # 第1个丑数是1
        ugly_numbers[0] = 1
        
        # 初始化三个指针，分别指向2、3、5的下一个乘数
        # 每个指针表示对应质因数与当前位置的丑数相乘
        p2 = p3 = p5 = 0
        
        # 生成前n个丑数
        for i in range(1, n):
            # 计算下一个可能的丑数
            next_ugly_2 = ugly_numbers[p2] * 2
            next_ugly_3 = ugly_numbers[p3] * 3
            next_ugly_5 = ugly_numbers[p5] * 5
            
            # 取三个可能的丑数中的最小值作为当前丑数
            min_ugly = min(next_ugly_2, next_ugly_3, next_ugly_5)
            ugly_numbers[i] = min_ugly
            
            # 更新对应的指针
            # 注意这里不能使用if-elif，因为可能有多个指针生成相同的丑数
            # 例如，ugly_numbers[1] = 2, ugly_numbers[2] = 3, ugly_numbers[3] = 4
            # 当生成6时，可能是2*3或3*2，需要同时更新p2和p3
            if min_ugly == next_ugly_2:
                p2 += 1
            if min_ugly == next_ugly_3:
                p3 += 1
            if min_ugly == next_ugly_5:
                p5 += 1
        
        # 返回第n个丑数
        return ugly_numbers[n - 1]

class AlternativeApproach:
    """
    丑数II的其他实现方式
    这个类提供了不同的实现方法，用于对比和教学目的
    """
    
    def nthUglyNumberEfficient(self, n):
        """
        一种优化的动态规划实现，代码更简洁
        
        Args:
            n: 第n个丑数
            
        Returns:
            int: 第n个丑数
        """
        # 处理边界情况
        if n <= 0:
            raise ValueError("n必须是正整数")
        
        # 初始化结果数组
        res = [1] * n
        # 初始化三个指针
        i2 = i3 = i5 = 0
        
        for i in range(1, n):
            # 计算下一个可能的最小值
            res[i] = min(res[i2] * 2, res[i3] * 3, res[i5] * 5)
            # 更新指针
            if res[i] == res[i2] * 2:
                i2 += 1
            if res[i] == res[i3] * 3:
                i3 += 1
            if res[i] == res[i5] * 5:
                i5 += 1
        
        return res[-1]

# 测试函数，验证算法在不同输入情况下的正确性
def test_nth_ugly_number():
    print("=== 测试丑数II算法 ===")
    solution = Solution()
    alternative = AlternativeApproach()
    
    # 测试用例
    test_cases = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
    expected_results = [1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24]
    
    print("\n=== 测试最小堆实现 ===")
    for i, n in enumerate(test_cases):
        result = solution.nthUglyNumberHeap(n)
        expected = expected_results[i]
        print(f"第{n}个丑数 = {result}, 期望结果 = {expected}, {'✓' if result == expected else '✗'}")
    
    print("\n=== 测试动态规划实现 ===")
    for i, n in enumerate(test_cases):
        result = solution.nthUglyNumberDP(n)
        expected = expected_results[i]
        print(f"第{n}个丑数 = {result}, 期望结果 = {expected}, {'✓' if result == expected else '✗'}")
    
    print("\n=== 测试优化的动态规划实现 ===")
    for i, n in enumerate(test_cases):
        result = alternative.nthUglyNumberEfficient(n)
        expected = expected_results[i]
        print(f"第{n}个丑数 = {result}, 期望结果 = {expected}, {'✓' if result == expected else '✗'}")
    
    # 测试异常情况
    print("\n=== 测试异常情况 ===")
    try:
        solution.nthUglyNumberHeap(0)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    try:
        solution.nthUglyNumberDP(-5)
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 测试大输入
    n = 1690  # 最大的第1690个丑数在题目约束范围内
    
    start_time = time.time()
    result_heap = solution.nthUglyNumberHeap(n)
    heap_time = time.time() - start_time
    print(f"最小堆实现在n={n}时的结果: {result_heap}, 用时: {heap_time:.6f}秒")
    
    start_time = time.time()
    result_dp = solution.nthUglyNumberDP(n)
    dp_time = time.time() - start_time
    print(f"动态规划实现在n={n}时的结果: {result_dp}, 用时: {dp_time:.6f}秒")
    
    print(f"\n性能比较: 动态规划比最小堆快 {heap_time/dp_time:.2f}倍")

# 运行测试
if __name__ == "__main__":
    test_nth_ugly_number()

# 解题思路总结：
# 1. 最小堆方法：
#    - 使用最小堆来维护待处理的丑数候选
#    - 每次取出最小的丑数，然后生成新的丑数
#    - 使用集合避免重复
#    - 时间复杂度O(n log n)，空间复杂度O(n)
# 
# 2. 动态规划方法（最优解）：
#    - 维护三个指针，分别指向2、3、5需要乘的下一个位置
#    - 每次选择三个指针生成的最小值作为下一个丑数
#    - 更新对应的指针
#    - 时间复杂度O(n)，空间复杂度O(n)
# 
# 3. 应用技巧：
#    - 当需要按顺序生成有特定性质的数时，堆是一个很好的选择
#    - 对于有多个生成规则的序列，可以考虑使用多指针的动态规划方法
#    - 注意处理重复元素，避免无效计算