#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
异或运算高级题目实现 (Python版本)

本文件包含各种复杂的异或运算题目，展示异或在算法中的广泛应用
涵盖LeetCode、LintCode、HackerRank、Codeforces、AtCoder、SPOJ、POJ等平台的经典题目
"""


class TrieNode:
    """
    前缀树节点类
    """
    def __init__(self):
        self.children = {}  # 子节点字典，键为0或1，值为TrieNode
        self.count = 0      # 通过该节点的路径数


class Code10_XorAdvancedProblems:
    """
    异或运算高级题目实现类
    """
    
    @staticmethod
    def maximizeXor(nums, queries):
        """
        题目1: 与数组中元素的最大异或值
        
        题目来源: LeetCode 1707. Maximum XOR With an Element From Array
        链接: https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
        
        题目描述:
        给你一个由非负整数组成的数组 nums 和一个查询数组 queries，
        其中 queries[i] = [xi, mi]。
        第 i 个查询的答案是 xi 与 nums 中所有小于等于 mi 的元素异或的最大值。
        如果 nums 中的所有元素都大于 mi，最终答案就是 -1。
        返回一个数组 answer 作为查询的答案，其中 answer.length == queries.length 且 answer[i] 是第 i 个查询的答案。
        
        解题思路:
        使用前缀树(Trie)配合离线处理：
        1. 将查询按照 mi 排序，将数组按照值排序
        2. 对于每个查询，将所有小于等于 mi 的数插入前缀树
        3. 在前缀树中查找与 xi 异或的最大值
        
        时间复杂度: O((n + q) * log(max)) - n为数组长度，q为查询次数，max为最大值
        空间复杂度: O(n * log(max)) - 前缀树的空间
        
        Args:
            nums: 输入数组
            queries: 查询数组
            
        Returns:
            查询结果数组
        """
        # 将数组排序
        nums.sort()
        
        # 构建查询索引并排序
        indexed_queries = []
        for i, query in enumerate(queries):
            indexed_queries.append([query[0], query[1], i])  # [xi, mi, 原始索引]
        
        # 按照mi排序查询
        indexed_queries.sort(key=lambda x: x[1])
        
        result = [0] * len(queries)
        num_index = 0
        
        # 构建前缀树
        root = TrieNode()
        
        # 处理每个查询
        for xi, mi, index in indexed_queries:
            # 将所有小于等于mi的数插入前缀树
            while num_index < len(nums) and nums[num_index] <= mi:
                Code10_XorAdvancedProblems._insert(root, nums[num_index])
                num_index += 1
            
            # 如果前缀树为空，说明没有符合条件的数
            if not root.children:
                result[index] = -1
            else:
                # 在前缀树中查找与xi异或的最大值
                result[index] = Code10_XorAdvancedProblems._find_max_xor(root, xi)
        
        return result
    
    @staticmethod
    def _insert(root, num):
        """
        向前缀树中插入数字
        
        Args:
            root: 前缀树根节点
            num: 待插入的数字
        """
        node = root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
    
    @staticmethod
    def _find_max_xor(root, num):
        """
        在前缀树中查找与num异或能得到最大值的数字
        
        Args:
            root: 前缀树根节点
            num: 待查询的数字
            
        Returns:
            最大异或值
        """
        node = root
        max_xor = 0
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            # 贪心策略：尽量走相反的位
            desired_bit = 1 - bit
            if desired_bit in node.children:
                # 能走相反的位，该位异或结果为1
                max_xor |= (1 << i)
                node = node.children[desired_bit]
            else:
                # 只能走相同的位，该位异或结果为0
                node = node.children[bit] if bit in node.children else None
                if node is None:
                    break
        return max_xor

    @staticmethod
    def countPairs(nums, low, high):
        """
        题目2: 统计异或值在范围内的数对有多少
        
        题目来源: LeetCode 1803. Count Pairs With XOR in a Range
        链接: https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
        
        题目描述:
        给你一个整数数组 nums（下标从 0 开始）和一个整数 low、high，
        返回满足以下条件的数对 (i, j) 的数目：
        - 0 <= i < j < nums.length
        - low <= (nums[i] XOR nums[j]) <= high
        
        解题思路:
        使用前缀树配合数学技巧：
        1. 利用容斥原理：count(low, high) = count(0, high) - count(0, low-1)
        2. 对于每个数，在前缀树中查找与其异或结果小于等于某个值的数的个数
        3. 使用前缀树存储已处理的数，并在树中进行计数查询
        
        时间复杂度: O(n * log(max)) - n为数组长度，max为最大值
        空间复杂度: O(n * log(max)) - 前缀树的空间
        
        Args:
            nums: 输入数组
            low: 范围下界
            high: 范围上界
            
        Returns:
            满足条件的数对数目
        """
        # 利用容斥原理
        return Code10_XorAdvancedProblems._count_pairs_less_than(nums, high + 1) - \
               Code10_XorAdvancedProblems._count_pairs_less_than(nums, low)
    
    @staticmethod
    def _count_pairs_less_than(nums, limit):
        """
        计算异或结果小于指定值的数对数目
        
        Args:
            nums: 输入数组
            limit: 限制值
            
        Returns:
            数对数目
        """
        root = TrieNode()
        count = 0
        
        for num in nums:
            # 查找与当前数异或结果小于limit的数的个数
            count += Code10_XorAdvancedProblems._count_less_than(root, num, limit)
            # 将当前数插入前缀树
            Code10_XorAdvancedProblems._insert_with_count(root, num)
        
        return count
    
    @staticmethod
    def _insert_with_count(root, num):
        """
        向前缀树中插入数字并维护计数
        
        Args:
            root: 前缀树根节点
            num: 待插入的数字
        """
        node = root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
            # 增加通过该节点的路径数
            node.count += 1
    
    @staticmethod
    def _count_less_than(root, num, limit):
        """
        在前缀树中查找与num异或结果小于limit的数的个数
        
        Args:
            root: 前缀树根节点
            num: 待查询的数字
            limit: 限制值
            
        Returns:
            满足条件的数的个数
        """
        node = root
        count = 0
        
        # 从最高位开始处理
        for i in range(31, -1, -1):
            if node is None:
                break
                
            num_bit = (num >> i) & 1
            limit_bit = (limit >> i) & 1
            
            if limit_bit == 1:
                # 如果limit的当前位是1，我们可以选择与num相同位的路径（异或结果为0）
                if num_bit in node.children:
                    count += node.children[num_bit].count
                # 继续走相反位的路径（异或结果为1）
                node = node.children.get(1 - num_bit)
            else:
                # 如果limit的当前位是0，只能走与num相同位的路径（异或结果为0）
                node = node.children.get(num_bit)
        
        return count

    @staticmethod
    def findMaximumXOR(nums):
        """
        题目3: 数组中两个数的最大异或值 II
        
        题目来源: LintCode 1490. Maximum XOR
        链接: https://www.lintcode.com/problem/1490/
        
        题目描述:
        给定一个非负整数数组，找到数组中任意两个数异或的最大值。
        
        解题思路:
        使用前缀树(Trie)：
        1. 将所有数字的二进制表示插入前缀树
        2. 对于每个数字，在前缀树中查找能产生最大异或值的数字
        3. 贪心策略：在前缀树中尽量走相反的位（0走1，1走0）
        
        时间复杂度: O(n * log(max)) - n为数组长度，max为最大值
        空间复杂度: O(n * log(max)) - 前缀树的空间
        
        Args:
            nums: 输入数组
            
        Returns:
            最大异或值
        """
        if len(nums) < 2:
            return 0
        
        # 构建前缀树
        root = TrieNode()
        
        # 将所有数字插入前缀树
        for num in nums:
            Code10_XorAdvancedProblems._insert(root, num)
        
        max_xor = 0
        # 对于每个数字，在前缀树中寻找能产生最大异或值的数字
        for num in nums:
            current_xor = Code10_XorAdvancedProblems._find_max_xor(root, num)
            max_xor = max(max_xor, current_xor)
        
        return max_xor

    @staticmethod
    def nc152MaxXor(nums):
        """
        题目4: 牛客网 NC152. 数组中两个数的最大异或值
        
        题目来源: 牛客网 NC152
        链接: https://www.nowcoder.com/practice/363b9cab5ab142459f757c79c0b540be
        
        题目描述:
        给定一个非负整数数组，找到数组中任意两个数异或的最大值。
        
        解题思路:
        与LeetCode 421相同，使用前缀树(Trie)方法。
        
        时间复杂度: O(n * log(max)) - n为数组长度，max为最大值
        空间复杂度: O(n * log(max)) - 前缀树的空间
        
        Args:
            nums: 输入数组
            
        Returns:
            最大异或值
        """
        return Code10_XorAdvancedProblems.findMaximumXOR(nums)

    @staticmethod
    def maxXOR(nums):
        """
        题目5: 数组中两个数的最大异或值
        
        题目来源: LeetCode 421. Maximum XOR of Two Numbers in an Array
        链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
        
        题目描述:
        给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
        
        解题思路:
        使用前缀树(Trie)结构：
        1. 将数组中每个数字的二进制表示插入前缀树中
        2. 对于每个数字，在前缀树中查找能与之产生最大异或值的路径
        3. 贪心策略：对于每一位，尽量寻找相反的位以最大化异或结果
        
        时间复杂度: O(n * 32) = O(n) - 每个数字处理32位
        空间复杂度: O(n * 32) = O(n) - 前缀树存储
        
        Args:
            nums: 输入数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        # 构建前缀树
        root = Code10_XorAdvancedProblems.TrieNode()
        Code10_XorAdvancedProblems._insert(root, nums[0])  # 先插入第一个数
        
        max_result = 0
        
        # 对于每个后续的数，先查询再插入
        for i in range(1, len(nums)):
            # 查询当前数与前缀树中已有数的最大异或值
            current_max = Code10_XorAdvancedProblems._find_max_xor(root, nums[i])
            max_result = max(max_result, current_max)
            
            # 将当前数插入前缀树
            Code10_XorAdvancedProblems._insert(root, nums[i])
        
        return max_result

    @staticmethod
    def singleNumberII(nums):
        """
        题目6: 数组中唯一出现一次的元素 II
        
        题目来源: LeetCode 137. Single Number II
        链接: https://leetcode.cn/problems/single-number-ii/
        
        题目描述:
        给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。
        请你找出并返回那个只出现了一次的元素。
        
        解题思路:
        使用位运算统计每一位出现的次数：
        1. 对于每一位（0-31），统计数组中所有数字在该位上出现1的次数
        2. 如果该位出现的次数对3取余等于1，说明只出现一次的数字在该位上是1
        3. 否则，只出现一次的数字在该位上是0
        
        时间复杂度: O(n * 32) = O(n)
        空间复杂度: O(1)
        
        Args:
            nums: 输入数组
            
        Returns:
            只出现一次的元素
        """
        result = 0
        
        # 遍历每一位
        for i in range(32):
            count = 0
            # 统计数组中所有数字在第i位上出现1的次数
            for num in nums:
                count += (num >> i) & 1
            # 如果该位出现的次数对3取余等于1，则只出现一次的数字在该位上是1
            if count % 3 == 1:
                result |= (1 << i)
        
        return result

    @staticmethod
    def singleNumberIII(nums):
        """
        题目7: 数组中两个只出现一次的元素
        
        题目来源: LeetCode 260. Single Number III
        链接: https://leetcode.cn/problems/single-number-iii/
        
        题目描述:
        给你一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。
        找出只出现一次的那两个元素。你可以按 任意顺序 返回答案。
        
        解题思路:
        利用异或运算的性质：
        1. 首先，对数组中所有元素进行异或操作，得到两个只出现一次的元素的异或结果
        2. 找出异或结果中最右边的1位，根据该位将数组分为两组
        3. 对每组分别进行异或操作，得到两个只出现一次的元素
        
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        Args:
            nums: 输入数组
            
        Returns:
            只出现一次的两个元素
        """
        # 对数组中所有元素进行异或操作
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 找出异或结果中最右边的1位
        # 注意Python中处理负数的情况
        rightmost_one = xor_result & (-xor_result)
        
        # 根据该位将数组分为两组，并分别异或
        result = [0, 0]
        for num in nums:
            if (num & rightmost_one) == 0:
                result[0] ^= num
            else:
                result[1] ^= num
        
        return result

    @staticmethod
    def sumXor(n):
        """
        题目8: Sum vs XOR
        
        题目来源: HackerRank - Sum vs XOR
        链接: https://www.hackerrank.com/challenges/sum-vs-xor/problem
        
        题目描述:
        给定一个整数 n，找出非负整数 x 的个数，使得 x + n == x ^ n。
        
        解题思路:
        数学分析：x + n = x ^ n 当且仅当 x & n = 0
        即x和n在二进制表示中没有重叠的1位。
        1. 计算n的二进制表示中0的个数count
        2. 答案就是2^count
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            n: 输入整数
            
        Returns:
            满足条件的x的个数
        """
        # 计算n的二进制表示中0的个数
        count_zeros = 0
        temp = n
        
        # 特殊情况：当n=0时，任何x都满足条件
        if n == 0:
            return 1
        
        while temp > 0:
            # 如果当前位是0，计数加1
            if (temp & 1) == 0:
                count_zeros += 1
            temp >>= 1
        
        # 答案是2的count_zeros次方
        return 1 << count_zeros

    @staticmethod
    def xorFavoriteNumber(nums, k):
        """
        题目9: XOR and Favorite Number
        
        题目来源: Codeforces - Round #400 (Div. 2) - C
        链接: https://codeforces.com/contest/776/problem/C
        
        题目描述:
        给定一个数组a和一个数k，计算有多少个子数组满足子数组元素的异或值等于k。
        
        解题思路:
        利用前缀异或和哈希表：
        1. 计算前缀异或数组prefixXor
        2. 对于每个i，我们需要找到有多少个j < i使得prefixXor[i] ^ prefixXor[j] = k
        3. 这等价于查找prefixXor[j] = prefixXor[i] ^ k的次数
        4. 使用哈希表记录每个prefixXor值出现的次数
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        Args:
            nums: 输入数组
            k: 目标异或值
            
        Returns:
            满足条件的子数组个数
        """
        from collections import defaultdict
        
        xor_count = defaultdict(int)
        # 初始状态：空前缀的异或值为0
        xor_count[0] = 1
        
        prefix_xor = 0
        result = 0
        
        for num in nums:
            # 计算当前前缀异或
            prefix_xor ^= num
            
            # 查找prefix_xor ^ k在哈希表中出现的次数
            result += xor_count.get(prefix_xor ^ k, 0)
            
            # 将当前前缀异或值加入哈希表
            xor_count[prefix_xor] += 1
        
        return result

    @staticmethod
    def jianzhiOffer56(nums):
        """
        题目10: 剑指Offer - 数组中数字出现的次数
        
        题目来源: 剑指Offer 56 - I
        链接: https://leetcode.cn/problems/shu-zu-zhong-shu-zi-chu-xian-de-ci-shu-lcof/
        
        题目描述:
        一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。请写程序找出这两个只出现一次的数字。
        要求时间复杂度是O(n)，空间复杂度是O(1)。
        
        解题思路:
        与LeetCode 260相同，利用异或运算的性质：
        1. 首先，对数组中所有元素进行异或操作，得到两个只出现一次的元素的异或结果
        2. 找出异或结果中最右边的1位，根据该位将数组分为两组
        3. 对每组分别进行异或操作，得到两个只出现一次的元素
        
        时间复杂度: O(n)
        空间复杂度: O(1)
        
        Args:
            nums: 输入数组
            
        Returns:
            只出现一次的两个元素
        """
        return Code10_XorAdvancedProblems.singleNumberIII(nums)

    @staticmethod
    def xorLongestPath(xorPath):
        """
        题目11: The XOR-longest Path
        
        题目来源: POJ 3764
        链接: http://poj.org/problem?id=3764
        
        题目描述:
        给定一棵树，每条边有一个权值。找出树中最长的一条路径，使得路径上的边权异或值最大。
        
        解题思路:
        树的异或路径问题：
        1. 树中任意两点之间的路径是唯一的
        2. 计算每个节点到根节点的路径异或值xorPath[u]
        3. 任意两点u和v之间的路径异或值等于xorPath[u] ^ xorPath[v]
        4. 问题转化为：在xorPath数组中找出两个数，使得它们的异或值最大
        5. 使用前缀树(Trie)解决最大异或对问题
        
        时间复杂度: O(n * 32)
        空间复杂度: O(n * 32)
        
        注意：这里只提供xorPath数组的处理方法，完整解法需要先进行树的遍历计算xorPath数组
        
        Args:
            xorPath: 各节点到根节点的异或路径值
            
        Returns:
            最长异或路径的异或值
        """
        return Code10_XorAdvancedProblems.findMaximumXOR(xorPath)

    @staticmethod
    def maximumSubarrayXOR(nums):
        """
        题目12: SPOJ XOR
        
        题目来源: SPOJ XOR - XOR
        链接: https://www.spoj.com/problems/XOR/
        
        题目描述:
        给定一个数组，找出一个子数组，使得子数组的异或值最大。
        
        解题思路:
        利用前缀异或和前缀树：
        1. 计算前缀异或数组prefixXor
        2. 对于每个i，我们需要找到j < i使得prefixXor[i] ^ prefixXor[j]最大
        3. 使用前缀树(Trie)来快速查找最大异或对
        
        时间复杂度: O(n * 32)
        空间复杂度: O(n * 32)
        
        Args:
            nums: 输入数组
            
        Returns:
            最大异或子数组的异或值
        """
        if not nums:
            return 0
        
        n = len(nums)
        prefix_xor = [0] * (n + 1)
        
        # 计算前缀异或数组
        for i in range(n):
            prefix_xor[i + 1] = prefix_xor[i] ^ nums[i]
        
        # 使用前缀树查找最大异或对
        root = Code10_XorAdvancedProblems.TrieNode()
        Code10_XorAdvancedProblems._insert(root, prefix_xor[0])
        
        max_result = 0
        for i in range(1, n + 1):
            # 查找当前前缀异或值与之前前缀异或值的最大异或结果
            current_max = Code10_XorAdvancedProblems._find_max_xor(root, prefix_xor[i])
            max_result = max(max_result, current_max)
            
            # 将当前前缀异或值插入前缀树
            Code10_XorAdvancedProblems._insert(root, prefix_xor[i])
        
        return max_result


# 测试代码
if __name__ == "__main__":
    # 测试 maximizeXor 方法
    nums1 = [0, 1, 2, 3, 4]
    queries1 = [[3, 1], [1, 3], [5, 6]]
    result1 = Code10_XorAdvancedProblems.maximizeXor(nums1, queries1)
    print("maximizeXor测试结果:", result1)  # 应该输出 [3, 3, 7]
    
    # 测试 countPairs 方法
    nums2 = [1, 4, 2, 7]
    low, high = 2, 6
    result2 = Code10_XorAdvancedProblems.countPairs(nums2, low, high)
    print("countPairs测试结果:", result2)  # 应该输出 6
    
    # 测试 findMaximumXOR 方法
    nums3 = [3, 10, 5, 25, 2, 8]
    result3 = Code10_XorAdvancedProblems.findMaximumXOR(nums3)
    print("findMaximumXOR测试结果:", result3)  # 应该输出 28 (5^25)
    
    # 测试 nc152MaxXor 方法
    nums4 = [3, 10, 5, 25, 2, 8]
    result4 = Code10_XorAdvancedProblems.nc152MaxXor(nums4)
    print(f"nc152MaxXor测试结果: {result4}")  # 应该输出 28 (5^25)
    
    # 测试 maxXOR 方法
    nums5 = [3, 10, 5, 25, 2, 8]
    result5 = Code10_XorAdvancedProblems.maxXOR(nums5)
    print(f"maxXOR测试结果: {result5}")  # 应该输出 28 (5^25)
    
    # 测试 singleNumberII 方法
    nums6 = [2, 2, 3, 2]
    result6 = Code10_XorAdvancedProblems.singleNumberII(nums6)
    print(f"singleNumberII测试结果: {result6}")  # 应该输出 3
    
    # 测试 singleNumberIII 方法
    nums7 = [1, 2, 1, 3, 2, 5]
    result7 = Code10_XorAdvancedProblems.singleNumberIII(nums7)
    print(f"singleNumberIII测试结果: {result7}")  # 应该输出 [3, 5] 或 [5, 3]
    
    # 测试 sumXor 方法
    n = 5
    result8 = Code10_XorAdvancedProblems.sumXor(n)
    print(f"sumXor测试结果: {result8}")  # 应该输出 2
    
    # 测试 xorFavoriteNumber 方法
    nums8 = [4, 2, 2, 6, 4]
    k = 6
    result9 = Code10_XorAdvancedProblems.xorFavoriteNumber(nums8, k)
    print(f"xorFavoriteNumber测试结果: {result9}")  # 应该输出 4
    
    # 测试 jianzhiOffer56 方法
    nums9 = [1, 2, 1, 3, 2, 5]
    result10 = Code10_XorAdvancedProblems.jianzhiOffer56(nums9)
    print(f"jianzhiOffer56测试结果: {result10}")  # 应该输出 [3, 5] 或 [5, 3]
    
    # 测试 xorLongestPath 方法 (模拟数据)
    xorPath = [0, 3, 1, 5, 2, 8]
    result11 = Code10_XorAdvancedProblems.xorLongestPath(xorPath)
    print(f"xorLongestPath测试结果: {result11}")  # 应该输出 13 (5^8)
    
    # 测试 maximumSubarrayXOR 方法
    nums10 = [8, 1, 2, 12, 7, 6]
    result12 = Code10_XorAdvancedProblems.maximumSubarrayXOR(nums10)
    print(f"maximumSubarrayXOR测试结果: {result12}")  # 应该输出 15