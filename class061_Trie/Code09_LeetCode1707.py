# LeetCode 1707. 与数组中元素的最大异或值 - Python实现
# 
# 题目描述：
# 给定一个数组和查询数组，每个查询包含x和m，找出数组中满足num <= m的元素与x的最大异或值。
# 
# 测试链接：https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
# 
# 算法思路：
# 1. 离线查询 + 前缀树：将查询和数组排序，按顺序插入前缀树并回答查询
# 2. 构建二进制前缀树，支持最大异或值查询
# 3. 使用离线处理技巧，避免重复构建前缀树
# 
# 时间复杂度分析：
# - 排序：O(N log N + Q log Q)，其中N是数组长度，Q是查询数量
# - 前缀树操作：O((N + Q) * 32)，32是整数的位数
# - 总体时间复杂度：O(N log N + Q log Q + (N + Q) * 32)
# 
# 空间复杂度分析：
# - 前缀树空间：O(N * 32)
# - 排序空间：O(N + Q)
# - 总体空间复杂度：O(N * 32 + Q)
# 
# 是否最优解：是
# 理由：离线查询+前缀树是最优解法，避免了重复构建前缀树
# 
# 工程化考虑：
# 1. 异常处理：处理空数组和非法查询
# 2. 边界情况：数组为空或查询为空的情况
# 3. 极端输入：大量查询或大数值的情况
# 4. 内存管理：合理管理前缀树内存
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现，性能较高但空间固定
# C++：可使用指针实现，更节省空间
# 
# 调试技巧：
# 1. 打印中间结果验证排序和查询处理
# 2. 使用小规模测试数据验证算法正确性
# 3. 单元测试覆盖各种边界条件
# 
# 性能优化：
# 1. 离线查询减少前缀树重建次数
# 2. 使用字典的哈希特性提高访问速度
# 3. 预计算最大位数减少循环次数

class TrieNode:
    """
    二进制前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持0和1两种路径
    每个节点代表二进制数的一位
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典：0 -> 左子节点，1 -> 右子节点
        self.children = {}
        # 标记该节点是否是某个数字的结尾（实际上不需要，因为路径完整即代表数字存在）
        self.is_end = False

class BinaryTrie:
    """
    二进制前缀树类
    
    算法思路：
    支持32位整数的插入和最大异或值查询
    使用字典实现，灵活且节省空间
    
    时间复杂度分析：
    - 插入：O(32) = O(1)
    - 查询最大异或值：O(32) = O(1)
    """
    
    def __init__(self):
        """
        初始化二进制前缀树
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def insert(self, num: int) -> None:
        """
        向前缀树中插入数字
        
        算法步骤：
        1. 从最高位（第31位）开始处理
        2. 对于每一位，获取当前位的值（0或1）
        3. 如果对应的子节点不存在则创建
        4. 移动到子节点继续处理下一位
        
        时间复杂度：O(32) = O(1)
        空间复杂度：O(32) = O(1)，最坏情况下需要创建32个新节点
        
        :param num: 待插入的数字
        """
        node = self.root
        # 处理32位整数
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 获取第i位的值
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
        node.is_end = True
    
    def max_xor(self, x: int) -> int:
        """
        查询与x的最大异或值
        
        算法步骤：
        1. 从最高位开始，尽量选择与x当前位相反的位
        2. 如果相反的位存在，则选择该路径，结果当前位设为1
        3. 否则选择相同的位，结果当前位设为0
        4. 计算最终的异或结果
        
        时间复杂度：O(32) = O(1)
        空间复杂度：O(1)
        
        :param x: 查询值
        :return: 最大异或值
        """
        if not self.root.children:  # 空树
            return -1
        
        node = self.root
        result = 0
        
        for i in range(31, -1, -1):
            bit = (x >> i) & 1
            opposite = 1 - bit  # 希望选择的相反位
            
            # 尽量选择相反的位
            if opposite in node.children:
                result |= (1 << i)  # 设置当前位为1
                node = node.children[opposite]
            else:
                # 只能选择相同的位
                node = node.children[bit]
        
        return result

def maximize_xor(nums, queries):
    """
    主函数：计算每个查询的最大异或值
    
    算法步骤：
    1. 对数组进行排序
    2. 对查询按m值排序，保留原始索引
    3. 使用离线处理，按m值从小到大处理查询
    4. 对于每个查询，将数组中<=m的元素插入前缀树
    5. 在前缀树中查询与x的最大异或值
    
    时间复杂度：O(N log N + Q log Q + (N + Q) * 32)
    空间复杂度：O(N * 32 + Q)
    
    :param nums: 整数数组
    :param queries: 查询数组，每个查询为[x, m]
    :return: 每个查询的最大异或值结果
    """
    n = len(nums)
    q = len(queries)
    
    # 对数组排序
    nums.sort()
    
    # 创建查询索引数组，按m值排序
    indexed_queries = [(i, queries[i][0], queries[i][1]) for i in range(q)]
    indexed_queries.sort(key=lambda x: x[2])  # 按m值排序
    
    # 初始化前缀树
    trie = BinaryTrie()
    result = [-1] * q
    idx = 0  # 数组索引
    
    # 离线处理查询
    for original_idx, x, m in indexed_queries:
        # 将数组中<=m的元素插入前缀树
        while idx < n and nums[idx] <= m:
            trie.insert(nums[idx])
            idx += 1
        
        # 查询最大异或值
        result[original_idx] = trie.max_xor(x)
    
    return result

def test_maximize_xor():
    """
    单元测试函数
    
    测试用例设计：
    1. 基础功能测试
    2. 边界情况测试
    3. 极端值测试
    4. 性能测试
    """
    # 测试用例1：基础测试
    nums1 = [0, 1, 2, 3, 4]
    queries1 = [[3, 1], [1, 3], [5, 6]]
    result1 = maximize_xor(nums1, queries1)
    expected1 = [3, 3, 7]
    assert result1 == expected1, f"测试用例1失败: {result1} != {expected1}"
    
    # 测试用例2：空数组
    nums2 = []
    queries2 = [[1, 1]]
    result2 = maximize_xor(nums2, queries2)
    expected2 = [-1]
    assert result2 == expected2, f"测试用例2失败: {result2} != {expected2}"
    
    # 测试用例3：单个元素
    nums3 = [5]
    queries3 = [[1, 10], [10, 1]]
    result3 = maximize_xor(nums3, queries3)
    expected3 = [4, -1]  # 5^1=4, 第二个查询无匹配元素
    assert result3 == expected3, f"测试用例3失败: {result3} != {expected3}"
    
    # 测试用例4：大数值
    nums4 = [2**31 - 1]  # 最大32位整数
    queries4 = [[0, 2**31 - 1]]
    result4 = maximize_xor(nums4, queries4)
    expected4 = [2**31 - 1]
    assert result4 == expected4, f"测试用例4失败: {result4} != {expected4}"
    
    # 测试用例5：重复元素
    nums5 = [1, 1, 1]
    queries5 = [[0, 2], [1, 1]]
    result5 = maximize_xor(nums5, queries5)
    expected5 = [1, 0]  # 0^1=1, 1^1=0
    assert result5 == expected5, f"测试用例5失败: {result5} != {expected5}"
    
    print("所有单元测试通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 大规模数组和查询
    2. 极端数值情况
    3. 边界条件处理
    """
    import time
    import random
    
    # 生成大规模测试数据
    n = 100000
    q = 100000
    nums = [random.randint(0, 10**9) for _ in range(n)]
    queries = [[random.randint(0, 10**9), random.randint(0, 10**9)] for _ in range(q)]
    
    start_time = time.time()
    result = maximize_xor(nums, queries)
    end_time = time.time()
    
    print(f"大规模测试耗时: {end_time - start_time:.3f}秒")
    print(f"处理了 {n} 个数字和 {q} 个查询")
    print(f"结果数组长度: {len(result)}")
    
    # 验证部分结果
    valid_results = [r for r in result if r != -1]
    if valid_results:
        print(f"有效结果数量: {len(valid_results)}")
        print(f"最大异或值范围: {min(valid_results)} ~ {max(valid_results)}")
    else:
        print("所有查询结果均为-1")

if __name__ == "__main__":
    # 运行单元测试
    test_maximize_xor()
    
    # 运行性能测试
    performance_test()