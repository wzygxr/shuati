import heapq

class Solution:
    """
    相关题目23: LeetCode 313. 超级丑数
    题目链接: https://leetcode.cn/problems/super-ugly-number/
    题目描述: 超级丑数 是一个正整数，并满足其所有质因数都出现在质数数组 primes 中。
    给你一个整数 n 和一个整数数组 primes，返回第 n 个 超级丑数 。
    解题思路1: 使用最小堆生成超级丑数序列
    解题思路2: 使用动态规划，为每个质数维护一个指针
    时间复杂度: 最小堆O(n log k)，动态规划O(nk)，其中k是primes数组的长度
    空间复杂度: 最小堆O(n)，动态规划O(n + k)
    是否最优解: 根据具体输入，两种解法各有优劣
    
    本题属于堆的应用场景：生成有特定质因数集合的有序数序列
    """
    
    def nthSuperUglyNumberHeap(self, n, primes):
        """
        使用最小堆生成超级丑数序列
        
        Args:
            n: 第n个超级丑数
            primes: 质因数数组
            
        Returns:
            int: 第n个超级丑数
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查n和primes是否有效
        if not isinstance(n, int) or n <= 0:
            raise ValueError("n必须是正整数")
        if not primes or len(primes) == 0:
            raise ValueError("primes数组不能为空")
        
        # 特殊情况：第1个超级丑数是1
        if n == 1:
            return 1
        
        # 使用集合来记录已经生成的超级丑数，避免重复
        seen = {1}
        # 创建最小堆
        heap = [1]
        
        # 用于记录当前找到的超级丑数
        current = 1
        
        # 循环n次，找到第n个超级丑数
        for _ in range(n):
            # 取出堆顶元素，即当前最小的超级丑数
            current = heapq.heappop(heap)
            
            # 生成新的超级丑数
            for prime in primes:
                next_ugly = current * prime
                # 如果新超级丑数未被生成过，则加入堆和集合
                if next_ugly not in seen:
                    seen.add(next_ugly)
                    heapq.heappush(heap, next_ugly)
        
        # 返回第n个超级丑数
        return current
    
    def nthSuperUglyNumberDP(self, n, primes):
        """
        使用动态规划生成超级丑数序列
        
        Args:
            n: 第n个超级丑数
            primes: 质因数数组
            
        Returns:
            int: 第n个超级丑数
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查n和primes是否有效
        if not isinstance(n, int) or n <= 0:
            raise ValueError("n必须是正整数")
        if not primes or len(primes) == 0:
            raise ValueError("primes数组不能为空")
        
        # 特殊情况：第1个超级丑数是1
        if n == 1:
            return 1
        
        # 创建一个数组来存储前n个超级丑数
        super_ugly = [0] * n
        # 第1个超级丑数是1
        super_ugly[0] = 1
        
        # 为每个质数维护一个指针
        # 指针i表示primes[i]应该与super_ugly[pointers[i]]相乘
        pointers = [0] * len(primes)
        
        # 生成前n个超级丑数
        for i in range(1, n):
            # 计算所有可能的下一个超级丑数
            next_uglies = [super_ugly[pointers[j]] * primes[j] for j in range(len(primes))]
            
            # 取最小值作为当前超级丑数
            min_ugly = min(next_uglies)
            super_ugly[i] = min_ugly
            
            # 更新对应的指针
            for j in range(len(primes)):
                if next_uglies[j] == min_ugly:
                    pointers[j] += 1
        
        # 返回第n个超级丑数
        return super_ugly[n - 1]

class AlternativeApproach:
    """
    超级丑数的其他实现方式
    这个类提供了不同的实现方法，用于对比和教学目的
    """
    
    def nthSuperUglyNumberOptimized(self, n, primes):
        """
        一种优化的动态规划实现，减少一些重复计算
        
        Args:
            n: 第n个超级丑数
            primes: 质因数数组
            
        Returns:
            int: 第n个超级丑数
        """
        if n <= 0:
            raise ValueError("n必须是正整数")
        if not primes:
            raise ValueError("primes数组不能为空")
        
        # 初始化结果数组
        dp = [0] * n
        dp[0] = 1
        
        # 初始化指针
        k = len(primes)
        pointers = [0] * k
        
        # 缓存当前每个质数对应的下一个可能的超级丑数
        # 避免重复计算
        next_uglies = primes.copy()
        
        for i in range(1, n):
            # 找到最小的下一个超级丑数
            dp[i] = min(next_uglies)
            
            # 更新指针和对应的下一个可能值
            for j in range(k):
                if dp[i] == next_uglies[j]:
                    pointers[j] += 1
                    next_uglies[j] = dp[pointers[j]] * primes[j]
        
        return dp[-1]

# 测试函数，验证算法在不同输入情况下的正确性
def test_super_ugly_number():
    print("=== 测试超级丑数算法 ===")
    solution = Solution()
    alternative = AlternativeApproach()
    
    # 测试用例1：基本用例
    print("\n测试用例1：基本用例")
    n1 = 12
    primes1 = [2, 7, 13, 19]
    expected1 = 32
    
    result_heap1 = solution.nthSuperUglyNumberHeap(n1, primes1)
    result_dp1 = solution.nthSuperUglyNumberDP(n1, primes1)
    result_opt1 = alternative.nthSuperUglyNumberOptimized(n1, primes1)
    
    print(f"最小堆实现: {result_heap1}, 期望: {expected1}, {'✓' if result_heap1 == expected1 else '✗'}")
    print(f"动态规划实现: {result_dp1}, 期望: {expected1}, {'✓' if result_dp1 == expected1 else '✗'}")
    print(f"优化动态规划实现: {result_opt1}, 期望: {expected1}, {'✓' if result_opt1 == expected1 else '✗'}")
    
    # 测试用例2：简单质数数组
    print("\n测试用例2：简单质数数组")
    n2 = 10
    primes2 = [2, 3, 5]
    expected2 = 12  # 等同于普通丑数的第10个
    
    result_heap2 = solution.nthSuperUglyNumberHeap(n2, primes2)
    result_dp2 = solution.nthSuperUglyNumberDP(n2, primes2)
    result_opt2 = alternative.nthSuperUglyNumberOptimized(n2, primes2)
    
    print(f"最小堆实现: {result_heap2}, 期望: {expected2}, {'✓' if result_heap2 == expected2 else '✗'}")
    print(f"动态规划实现: {result_dp2}, 期望: {expected2}, {'✓' if result_dp2 == expected2 else '✗'}")
    print(f"优化动态规划实现: {result_opt2}, 期望: {expected2}, {'✓' if result_opt2 == expected2 else '✗'}")
    
    # 测试用例3：只有一个质数
    print("\n测试用例3：只有一个质数")
    n3 = 5
    primes3 = [2]
    expected3 = 16  # 2^4
    
    result_heap3 = solution.nthSuperUglyNumberHeap(n3, primes3)
    result_dp3 = solution.nthSuperUglyNumberDP(n3, primes3)
    result_opt3 = alternative.nthSuperUglyNumberOptimized(n3, primes3)
    
    print(f"最小堆实现: {result_heap3}, 期望: {expected3}, {'✓' if result_heap3 == expected3 else '✗'}")
    print(f"动态规划实现: {result_dp3}, 期望: {expected3}, {'✓' if result_dp3 == expected3 else '✗'}")
    print(f"优化动态规划实现: {result_opt3}, 期望: {expected3}, {'✓' if result_opt3 == expected3 else '✗'}")
    
    # 测试异常情况
    print("\n=== 测试异常情况 ===")
    try:
        solution.nthSuperUglyNumberHeap(0, [2, 3])
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    try:
        solution.nthSuperUglyNumberDP(5, [])
        print("异常测试失败：未抛出预期的异常")
    except ValueError as e:
        print(f"异常测试通过: {e}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 测试中等规模输入
    n4 = 1000
    primes4 = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
    
    start_time = time.time()
    result_heap = solution.nthSuperUglyNumberHeap(n4, primes4)
    heap_time = time.time() - start_time
    print(f"最小堆实现在n={n4}时的结果: {result_heap}, 用时: {heap_time:.6f}秒")
    
    start_time = time.time()
    result_dp = solution.nthSuperUglyNumberDP(n4, primes4)
    dp_time = time.time() - start_time
    print(f"动态规划实现在n={n4}时的结果: {result_dp}, 用时: {dp_time:.6f}秒")
    
    start_time = time.time()
    result_opt = alternative.nthSuperUglyNumberOptimized(n4, primes4)
    opt_time = time.time() - start_time
    print(f"优化动态规划实现在n={n4}时的结果: {result_opt}, 用时: {opt_time:.6f}秒")
    
    print(f"\n性能比较:")
    if dp_time > 0:
        print(f"最小堆比动态规划 {'慢' if heap_time > dp_time else '快'} {abs(heap_time/dp_time - 1):.2f}倍")
    if opt_time > 0:
        print(f"原始动态规划比优化动态规划 {'慢' if dp_time > opt_time else '快'} {abs(dp_time/opt_time - 1):.2f}倍")

# 运行测试
if __name__ == "__main__":
    test_super_ugly_number()

# 解题思路总结：
# 1. 最小堆方法：
#    - 使用最小堆来维护待处理的超级丑数候选
#    - 每次取出最小的超级丑数，然后生成新的超级丑数
#    - 使用集合避免重复
#    - 时间复杂度O(n log k)，空间复杂度O(n)
#    - 当primes数组长度较大时，这种方法可能会更高效
# 
# 2. 动态规划方法：
#    - 维护primes数组长度个指针，分别指向每个质数需要乘的下一个位置
#    - 每次选择所有可能的下一个超级丑数中的最小值
#    - 更新对应的指针
#    - 时间复杂度O(nk)，空间复杂度O(n + k)
#    - 当primes数组长度较小时，这种方法通常比堆方法更高效
# 
# 3. 优化技巧：
#    - 对于动态规划，可以缓存每个质数的下一个可能值，避免重复计算
#    - 注意处理重复元素，特别是当多个质数生成相同的超级丑数时
#    - 在Python中使用列表推导式可以简化代码
# 
# 4. 应用场景：
#    - 当需要生成具有特定质因数集合的有序序列时，超级丑数算法是一个很好的模型
#    - 这种算法可以应用于各种生成满足特定条件的有序序列的问题