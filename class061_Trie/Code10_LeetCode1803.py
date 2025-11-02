# LeetCode 1803. 统计异或值在范围内的数对有多少 - Python实现
# 
# 题目描述：
# 给定一个整数数组nums和两个整数low和high，统计有多少数对(i, j)满足i < j且low <= (nums[i] XOR nums[j]) <= high。
# 
# 测试链接：https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
# 
# 算法思路：
# 1. 使用前缀异或和与前缀树，通过两次查询（<=high和<low）得到结果
# 2. 构建二进制前缀树，支持统计异或值在特定范围内的数对数量
# 3. 利用前缀树的高效查询特性，避免暴力枚举
# 
# 时间复杂度分析：
# - 构建前缀树：O(N * 32)，其中N是数组长度，32是整数的位数
# - 查询过程：O(N * 32)
# - 总体时间复杂度：O(N * 32)
# 
# 空间复杂度分析：
# - 前缀树空间：O(N * 32)
# - 总体空间复杂度：O(N * 32)
# 
# 是否最优解：是
# 理由：使用前缀树可以在线性时间内统计异或值在范围内的数对数量
# 
# 工程化考虑：
# 1. 异常处理：处理空数组和非法范围
# 2. 边界情况：数组长度小于2或范围无效的情况
# 3. 极端输入：大量数据或大数值的情况
# 4. 内存管理：合理管理前缀树内存
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现，性能较高但空间固定
# C++：可使用指针实现，更节省空间
# 
# 调试技巧：
# 1. 使用小规模数据验证算法正确性
# 2. 打印中间结果调试查询过程
# 3. 单元测试覆盖各种边界条件

class BinaryTrieNode:
    """
    二进制前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持0和1两种路径
    包含经过该节点的数字数量统计
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        self.children = {}  # 0或1 -> BinaryTrieNode
        self.count = 0      # 经过该节点的数字数量

class BinaryTrie:
    """
    二进制前缀树类
    
    算法思路：
    支持32位整数的插入和范围查询
    使用字典实现，灵活且节省空间
    
    时间复杂度分析：
    - 插入：O(32) = O(1)
    - 范围查询：O(32) = O(1)
    """
    
    def __init__(self):
        """
        初始化二进制前缀树
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = BinaryTrieNode()
    
    def insert(self, num: int) -> None:
        """
        向前缀树中插入数字
        
        算法步骤：
        1. 从最高位（第31位）开始处理
        2. 对于每一位，获取当前位的值（0或1）
        3. 如果对应的子节点不存在则创建
        4. 移动到子节点，增加计数
        5. 处理完所有位后完成插入
        
        时间复杂度：O(32) = O(1)
        空间复杂度：O(32) = O(1)，最坏情况下需要创建32个新节点
        
        :param num: 待插入的数字
        """
        node = self.root
        node.count += 1
        
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node.children:
                node.children[bit] = BinaryTrieNode()
            node = node.children[bit]
            node.count += 1
    
    def count_less_equal(self, num: int, target: int) -> int:
        """
        统计与num异或值<=target的数字数量
        
        算法步骤：
        1. 从前缀树根节点开始，同时遍历num和target的二进制位
        2. 根据当前位的组合情况决定搜索路径
        3. 累加满足条件的数字数量
        
        时间复杂度：O(32) = O(1)
        空间复杂度：O(1)
        
        :param num: 当前数字
        :param target: 目标值
        :return: 异或值<=target的数字数量
        """
        if target < 0:
            return 0
        
        node = self.root
        count = 0
        
        for i in range(31, -1, -1):
            num_bit = (num >> i) & 1
            target_bit = (target >> i) & 1
            opposite = 1 - num_bit
            
            if target_bit == 1:
                # 如果target当前位为1，那么选择相同位的所有数字都满足条件
                if num_bit in node.children:
                    count += node.children[num_bit].count
                # 继续在相反位搜索
                if opposite in node.children:
                    node = node.children[opposite]
                else:
                    return count
            else:
                # 如果target当前位为0，只能选择相同位
                if num_bit in node.children:
                    node = node.children[num_bit]
                else:
                    return count
        
        # 处理最后一位（所有位都匹配）
        count += node.count
        return count

def count_pairs(nums, low, high):
    """
    主函数：统计异或值在[low, high]范围内的数对数量
    
    算法步骤：
    1. 构建前缀树，用于高效查询异或值
    2. 对于每个数字，查询与之前数字的异或值在[low, high]范围内的数量
    3. 使用两次查询技巧：count(<=high) - count(<low)
    
    时间复杂度：O(N * 32) = O(N)
    空间复杂度：O(N * 32) = O(N)
    
    :param nums: 整数数组
    :param low: 范围下限
    :param high: 范围上限
    :return: 满足条件的数对数量
    """
    if not nums or len(nums) < 2:
        return 0
    
    if low > high:
        return 0
    
    trie = BinaryTrie()
    count = 0
    
    for num in nums:
        # 查询与之前数字的异或值在[low, high]范围内的数量
        high_count = trie.count_less_equal(num, high)
        low_count = trie.count_less_equal(num, low - 1)
        count += (high_count - low_count)
        
        # 插入当前数字到前缀树
        trie.insert(num)
    
    return count

def count_pairs_brute_force(nums, low, high):
    """
    暴力解法（用于验证正确性）
    
    时间复杂度：O(N^2)
    空间复杂度：O(1)
    """
    if not nums or len(nums) < 2:
        return 0
    
    count = 0
    n = len(nums)
    
    for i in range(n):
        for j in range(i + 1, n):
            xor_val = nums[i] ^ nums[j]
            if low <= xor_val <= high:
                count += 1
    
    return count

def test_count_pairs():
    """
    单元测试函数
    
    测试用例设计：
    1. 基础功能测试
    2. 边界情况测试
    3. 极端值测试
    4. 性能对比测试
    """
    # 测试用例1：基础测试
    nums1 = [1, 4, 2, 7]
    low1, high1 = 2, 6
    result1 = count_pairs(nums1, low1, high1)
    expected1 = count_pairs_brute_force(nums1, low1, high1)
    assert result1 == expected1, f"测试用例1失败: {result1} != {expected1}"
    
    # 测试用例2：空数组
    nums2 = []
    result2 = count_pairs(nums2, 0, 10)
    assert result2 == 0, "测试用例2失败"
    
    # 测试用例3：单个元素
    nums3 = [5]
    result3 = count_pairs(nums3, 0, 10)
    assert result3 == 0, "测试用例3失败"
    
    # 测试用例4：无效范围
    nums4 = [1, 2, 3]
    result4 = count_pairs(nums4, 5, 1)
    assert result4 == 0, "测试用例4失败"
    
    # 测试用例5：相同数字
    nums5 = [1, 1, 1]
    result5 = count_pairs(nums5, 0, 0)  # 1^1=0
    expected5 = count_pairs_brute_force(nums5, 0, 0)
    assert result5 == expected5, f"测试用例5失败: {result5} != {expected5}"
    
    # 测试用例6：大数值
    nums6 = [2**31 - 1, 2**31 - 2]
    result6 = count_pairs(nums6, 0, 2**31 - 1)
    expected6 = count_pairs_brute_force(nums6, 0, 2**31 - 1)
    assert result6 == expected6, f"测试用例6失败: {result6} != {expected6}"
    
    print("所有单元测试通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 大规模数组
    2. 不同范围设置
    3. 与暴力解法对比
    """
    import time
    import random
    
    # 生成大规模测试数据
    n = 10000
    nums = [random.randint(0, 1000000) for _ in range(n)]
    low, high = 1000, 10000
    
    # 优化算法测试
    start_time = time.time()
    result_optimized = count_pairs(nums, low, high)
    optimized_time = time.time() - start_time
    
    print(f"优化算法结果: {result_optimized} 个数对")
    print(f"优化算法耗时: {optimized_time:.3f}秒")
    print(f"处理了 {n} 个数字")
    
    # 暴力解法测试（小规模验证）
    if n <= 1000:
        start_time = time.time()
        result_brute = count_pairs_brute_force(nums, low, high)
        brute_time = time.time() - start_time
        
        print(f"暴力解法结果: {result_brute} 个数对")
        print(f"暴力解法耗时: {brute_time:.3f}秒")
        
        # 验证结果一致性
        assert result_optimized == result_brute, "结果不一致！"
        print("结果验证通过！")
        
        # 性能对比
        speedup = brute_time / optimized_time if optimized_time > 0 else float('inf')
        print(f"性能提升: {speedup:.1f}倍")
    else:
        print("数据规模过大，跳过暴力解法验证")

def edge_case_test():
    """
    边界情况测试函数
    
    测试各种边界条件：
    1. 最小最大值
    2. 特殊数值
    3. 极端范围
    """
    print("开始边界情况测试...")
    
    # 测试最小数组
    nums_min = [1, 2]
    result_min = count_pairs(nums_min, 0, 3)
    assert result_min == 1, "最小数组测试失败"
    
    # 测试全零数组
    nums_zero = [0, 0, 0]
    result_zero = count_pairs(nums_zero, 0, 0)
    assert result_zero == 3, "全零数组测试失败"  # C(3,2)=3
    
    # 测试最大范围
    nums_max = [1, 2, 3]
    result_max = count_pairs(nums_max, 0, 2**31 - 1)
    expected_max = count_pairs_brute_force(nums_max, 0, 2**31 - 1)
    assert result_max == expected_max, "最大范围测试失败"
    
    # 测试负值处理（应该返回0）
    result_negative = count_pairs([1, 2], -1, -1)
    assert result_negative == 0, "负值处理测试失败"
    
    print("边界情况测试通过！")

if __name__ == "__main__":
    # 运行单元测试
    test_count_pairs()
    
    # 运行边界情况测试
    edge_case_test()
    
    # 运行性能测试
    performance_test()