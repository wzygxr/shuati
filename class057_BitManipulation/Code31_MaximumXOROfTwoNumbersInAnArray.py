"""
数组中两个数的最大异或值
测试链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/

题目描述：
给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < nums.length 。

解题思路：
1. 暴力法：双重循环计算所有异或值
2. 字典树法：使用Trie树存储二进制表示
3. 哈希集合法：利用异或性质进行优化
4. 位运算技巧：逐位确定最大值

时间复杂度：O(n) - 使用字典树或哈希集合
空间复杂度：O(n) - 需要存储字典树或哈希集合
"""

class TrieNode:
    """字典树节点"""
    def __init__(self):
        self.children = [None, None]  # 0和1两个分支

class Code31_MaximumXOROfTwoNumbersInAnArray:
    """
    数组中两个数的最大异或值解决方案
    """
    
    @staticmethod
    def find_maximum_xor1(nums: list[int]) -> int:
        """
        方法1：暴力法（不推荐，会超时）
        时间复杂度：O(n²)
        空间复杂度：O(1)
        
        Args:
            nums: 整数数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        max_val = 0
        n = len(nums)
        for i in range(n):
            for j in range(i + 1, n):
                max_val = max(max_val, nums[i] ^ nums[j])
        return max_val
    
    @staticmethod
    def find_maximum_xor2(nums: list[int]) -> int:
        """
        方法2：字典树法（最优解）
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            nums: 整数数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        # 创建字典树根节点
        root = TrieNode()
        
        # 构建字典树
        for num in nums:
            node = root
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                if node.children[bit] is None:
                    node.children[bit] = TrieNode()
                node = node.children[bit]
        
        max_val = 0
        # 对每个数字，在字典树中寻找最大异或值
        for num in nums:
            node = root
            current_max = 0
            for i in range(31, -1, -1):
                bit = (num >> i) & 1
                desired_bit = 1 - bit  # 希望找到相反的位
                
                if node.children[desired_bit] is not None:
                    current_max |= (1 << i)
                    node = node.children[desired_bit]
                else:
                    node = node.children[bit]
            max_val = max(max_val, current_max)
        
        return max_val
    
    @staticmethod
    def find_maximum_xor3(nums: list[int]) -> int:
        """
        方法3：哈希集合法
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            nums: 整数数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        max_val = 0
        mask = 0
        
        for i in range(31, -1, -1):
            mask |= (1 << i)
            prefix_set = set()
            
            # 提取前缀
            for num in nums:
                prefix_set.add(num & mask)
            
            # 尝试设置当前位为1
            temp = max_val | (1 << i)
            for prefix in prefix_set:
                if (temp ^ prefix) in prefix_set:
                    max_val = temp
                    break
        
        return max_val
    
    @staticmethod
    def find_maximum_xor4(nums: list[int]) -> int:
        """
        方法4：位运算优化版
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            nums: 整数数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        max_result = 0
        mask = 0
        
        # 从最高位开始尝试
        for i in range(31, -1, -1):
            # 设置掩码，只保留前i位
            mask = mask | (1 << i)
            
            left_bits = set()
            for num in nums:
                left_bits.add(num & mask)
            
            # 尝试设置当前位为1
            greedy_try = max_result | (1 << i)
            for left_bit in left_bits:
                # 如果存在两个数使得它们的异或等于greedy_try
                if (greedy_try ^ left_bit) in left_bits:
                    max_result = greedy_try
                    break
        
        return max_result
    
    @staticmethod
    def test():
        """测试方法"""
        # 测试用例1：正常情况
        nums1 = [3, 10, 5, 25, 2, 8]
        result1 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor1(nums1)
        result2 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor2(nums1)
        result3 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor3(nums1)
        result4 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor4(nums1)
        print(f"测试用例1 - 输入: {nums1}")
        print(f"方法1结果: {result1} (预期: 28)")
        print(f"方法2结果: {result2} (预期: 28)")
        print(f"方法3结果: {result3} (预期: 28)")
        print(f"方法4结果: {result4} (预期: 28)")
        
        # 测试用例2：边界情况（两个元素）
        nums2 = [1, 2]
        result5 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor2(nums2)
        print(f"测试用例2 - 输入: {nums2}")
        print(f"方法2结果: {result5} (预期: 3)")
        
        # 测试用例3：边界情况（一个元素）
        nums3 = [5]
        result6 = Code31_MaximumXOROfTwoNumbersInAnArray.find_maximum_xor2(nums3)
        print(f"测试用例3 - 输入: {nums3}")
        print(f"方法2结果: {result6} (预期: 0)")
        
        # 复杂度分析
        print("\n=== 复杂度分析 ===")
        print("方法1 - 暴力法:")
        print("  时间复杂度: O(n²)")
        print("  空间复杂度: O(1)")
        
        print("方法2 - 字典树法:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        print("方法3 - 哈希集合法:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        print("方法4 - 位运算优化版:")
        print("  时间复杂度: O(n)")
        print("  空间复杂度: O(n)")
        
        # 工程化考量
        print("\n=== 工程化考量 ===")
        print("1. 方法选择：")
        print("   - 实际工程：方法2（字典树法）最优")
        print("   - 面试场景：方法2（字典树法）最优")
        print("2. 性能优化：避免暴力法，使用高效数据结构")
        print("3. 边界处理：处理空数组和单元素数组")
        print("4. 可读性：添加详细注释说明算法原理")
        
        # Python特性考量
        print("\n=== Python特性考量 ===")
        print("1. 集合操作：使用set进行高效查找")
        print("2. 位运算：Python支持标准的位运算符")
        print("3. 内存管理：Python自动管理字典树内存")
        print("4. 类型注解：使用类型提示提高代码可读性")
        
        # 算法技巧总结
        print("\n=== 算法技巧总结 ===")
        print("1. 字典树应用：高效处理二进制前缀")
        print("2. 贪心策略：从高位到低位确定最大值")
        print("3. 哈希集合：利用集合特性快速查找")
        print("4. 位掩码：逐位构建最大异或值")

if __name__ == "__main__":
    Code31_MaximumXOROfTwoNumbersInAnArray.test()