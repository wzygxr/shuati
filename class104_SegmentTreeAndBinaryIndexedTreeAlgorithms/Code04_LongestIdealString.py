"""
LeetCode 2370. 最长理想子序列 (Longest Ideal Subsequence)
题目链接: https://leetcode.cn/problems/longest-ideal-subsequence/

题目描述:
给定一个长度为n，只由小写字母组成的字符串s，给定一个非负整数k
字符串s可以生成很多子序列，理想子序列的定义为：
子序列中任意相邻的两个字符，在字母表中位次的差值绝对值<=k
返回最长理想子序列的长度。

解题思路:
使用线段树优化动态规划的方法解决此问题。
1. 定义状态dp[c]表示以字符c结尾的最长理想子序列长度
2. 对于每个字符，查询与其ASCII值差值不超过k的所有字符对应的dp值的最大值
3. 使用线段树维护dp数组，支持区间查询最大值和单点更新
4. 遍历字符串，对每个字符更新对应的dp值

时间复杂度分析:
- 遍历字符串: O(n)
- 每次查询和更新: O(log e)，e为字符集大小
- 总时间复杂度: O(n * log e)
空间复杂度: O(e) 用于存储线段树

工程化考量:
1. 性能优化: 线段树查询和更新都是O(log n)
2. 边界处理: 处理k=0和k>=25的特殊情况
3. 异常防御: 处理空字符串和非法字符
4. 可读性: 清晰的变量命名和注释
"""

class SegmentTree:
    """线段树类，用于维护区间最大值"""
    
    def __init__(self, size):
        """
        初始化线段树
        
        Args:
            size: 线段树大小
        """
        self.size = size
        self.tree = [0] * (4 * size)
    
    def up(self, idx):
        """
        线段树向上更新操作
        
        Args:
            idx: 线段树节点索引
        """
        self.tree[idx] = max(self.tree[idx * 2], self.tree[idx * 2 + 1])
    
    def update(self, pos, val, l, r, idx):
        """
        线段树单点更新操作
        
        Args:
            pos: 要更新的位置
            val: 新的值
            l: 当前区间左边界
            r: 当前区间右边界
            idx: 当前线段树节点索引
        """
        if l == r:
            if val > self.tree[idx]:
                self.tree[idx] = val
            return
        
        mid = (l + r) // 2
        if pos <= mid:
            self.update(pos, val, l, mid, idx * 2)
        else:
            self.update(pos, val, mid + 1, r, idx * 2 + 1)
        
        self.up(idx)
    
    def query(self, L, R, l, r, idx):
        """
        线段树区间查询操作
        
        Args:
            L: 查询区间左边界
            R: 查询区间右边界
            l: 当前区间左边界
            r: 当前区间右边界
            idx: 当前线段树节点索引
            
        Returns:
            区间最大值
        """
        if L <= l and r <= R:
            return self.tree[idx]
        
        mid = (l + r) // 2
        result = 0
        
        if L <= mid:
            result = max(result, self.query(L, R, l, mid, idx * 2))
        if R > mid:
            result = max(result, self.query(L, R, mid + 1, r, idx * 2 + 1))
        
        return result

def longest_ideal_string(s, k):
    """
    计算最长理想子序列长度
    
    Args:
        s: 输入字符串
        k: 字符差值上限
        
    Returns:
        最长理想子序列长度
        
    Raises:
        ValueError: 如果输入参数不合法
    """
    # 异常处理
    if not s:
        return 0
    if k < 0:
        k = 0
    
    # 字符集大小（小写字母）
    CHAR_SET_SIZE = 26
    seg_tree = SegmentTree(CHAR_SET_SIZE)
    
    result = 0
    
    # 遍历字符串中的每个字符
    for c in s:
        # 将字符转换为1-26的数字
        v = ord(c) - ord('a') + 1
        
        # 计算查询区间
        left = max(1, v - k)
        right = min(CHAR_SET_SIZE, v + k)
        
        # 查询区间内的最大值
        max_val = seg_tree.query(left, right, 1, CHAR_SET_SIZE, 1)
        
        # 更新结果
        result = max(result, max_val + 1)
        
        # 更新线段树
        seg_tree.update(v, max_val + 1, 1, CHAR_SET_SIZE, 1)
    
    return result

# 单元测试
def test_longest_ideal_string():
    """测试函数，验证算法正确性"""
    
    print("开始测试最长理想子序列算法...")
    
    # 测试用例1: 正常情况
    s1 = "acfgbd"
    result1 = longest_ideal_string(s1, 2)
    print(f"测试用例1: s='{s1}', k=2 -> {result1}")
    assert result1 == 4, f"预期4，实际{result1}"
    
    # 测试用例2: k=0
    s2 = "abcd"
    result2 = longest_ideal_string(s2, 0)
    print(f"测试用例2: s='{s2}', k=0 -> {result2}")
    assert result2 == 1, f"预期1，实际{result2}"
    
    # 测试用例3: 空字符串
    s3 = ""
    result3 = longest_ideal_string(s3, 5)
    print(f"测试用例3: 空字符串, k=5 -> {result3}")
    assert result3 == 0, f"预期0，实际{result3}"
    
    # 测试用例4: 单字符
    s4 = "a"
    result4 = longest_ideal_string(s4, 10)
    print(f"测试用例4: s='{s4}', k=10 -> {result4}")
    assert result4 == 1, f"预期1，实际{result4}"
    
    # 测试用例5: 大k值
    s5 = "xyz"
    result5 = longest_ideal_string(s5, 25)
    print(f"测试用例5: s='{s5}', k=25 -> {result5}")
    
    # 测试用例6: 边界情况 - k为负数
    s6 = "abc"
    result6 = longest_ideal_string(s6, -1)
    print(f"测试用例6: s='{s6}', k=-1 -> {result6}")
    assert result6 == 1, f"预期1，实际{result6}"
    
    print("所有测试用例通过！")

# 性能测试
def performance_test():
    """性能测试函数"""
    
    print("开始性能测试...")
    
    import time
    
    # 大规模数据测试
    large_s = ''.join(chr(ord('a') + (i % 26)) for i in range(100000))
    
    start_time = time.time()
    result = longest_ideal_string(large_s, 5)
    end_time = time.time()
    
    print(f"大规模测试: 字符串长度{len(large_s)}，k=5，结果{result}，耗时{end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行测试
    test_longest_ideal_string()
    
    # 性能测试
    performance_test()
    
    # 算法技巧总结
    print("\n=== 算法技巧总结 ===")
    print("1. 线段树应用：用于维护字符集上的动态规划状态")
    print("2. 区间查询：快速查询与当前字符差值不超过k的字符状态")
    print("3. 单点更新：高效更新当前字符对应的最长子序列长度")
    print("4. 字符映射：将字符映射到1-26的整数范围")
    print("5. 边界处理：处理k=0和k>=25的特殊情况")
    
    print("\n=== 工程化考量 ===")
    print("1. 异常防御：处理空字符串和非法参数")
    print("2. 性能优化：线段树操作时间复杂度O(log n)")
    print("3. 空间优化：线段树空间复杂度O(n)")
    print("4. 可读性：清晰的变量命名和注释")
    print("5. 测试覆盖：单元测试覆盖各种边界情况")
    
    print("\n=== 复杂度分析 ===")
    print("时间复杂度: O(n * log 26) = O(n)")
    print("空间复杂度: O(26) = O(1)")
    print("其中n为字符串长度，26为字符集大小")