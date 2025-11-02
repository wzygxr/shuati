/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 
 * 你可以多次交换在 pairs 中任意一对索引处的字符。
 * 
 * 返回在经过若干次交换后，该字符串可以变成的按字典序最小的字符串。
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的字符的索引归为一个连通分量
 * 2. 对于每个连通分量，将其对应的字符收集起来并排序
 * 3. 按照排序后的字符顺序重新填充原字符串
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有索引对：O(m * α(n))，其中m是pairs数组的长度
 * - 收集字符并排序：O(n * log n)
 * - 重建字符串：O(n)
 * - 总体时间复杂度：O(n * log n + m * α(n)) ≈ O(n * log n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储连通分量的映射：O(n)
 * - 总体空间复杂度：O(n)
 */

class SmallestStringWithSwaps:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 并查集的秩数组，用于按秩合并优化
        self.rank = []
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的元素
            
        返回:
            int: 根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将x的父节点直接设置为根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def init_union_find(self, n):
        """
        初始化并查集
        
        参数:
            n (int): 字符串长度
        """
        # 初始化，每个元素的父节点是自己，秩为0
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def smallest_string_with_swaps(self, s, pairs):
        """
        交换字符串中的元素，使得结果字典序最小
        
        参数:
            s (str): 原始字符串
            pairs (List[List[int]]): 索引对数组
            
        返回:
            str: 字典序最小的字符串
        """
        n = len(s)
        
        # 初始化并查集
        self.init_union_find(n)
        
        # 处理所有索引对，将可以互相交换的索引归为一个连通分量
        for a, b in pairs:
            root_a = self.find(a)
            root_b = self.find(b)
            
            if root_a != root_b:
                # 按秩合并：将秩小的树连接到秩大的树下
                if self.rank[root_a] < self.rank[root_b]:
                    self.parent[root_a] = root_b
                elif self.rank[root_a] > self.rank[root_b]:
                    self.parent[root_b] = root_a
                else:
                    # 秩相同时，任选一个作为根，并增加其秩
                    self.parent[root_b] = root_a
                    self.rank[root_a] += 1
        
        # 使用字典将每个连通分量的根节点映射到对应的字符列表
        component_map = {}
        for i in range(n):
            root = self.find(i)
            if root not in component_map:
                component_map[root] = []
            component_map[root].append(s[i])
        
        # 对每个连通分量的字符列表进行排序
        for root in component_map:
            component_map[root].sort()
        
        # 为每个连通分量创建一个指针，用于依次取出排序后的字符
        pointers = {root: 0 for root in component_map}
        
        # 重建字符串
        result = []
        for i in range(n):
            root = self.find(i)
            # 取出该连通分量中当前指针指向的字符
            result.append(component_map[root][pointers[root]])
            pointers[root] += 1
        
        return ''.join(result)

# 测试代码
def test_smallest_string_with_swaps():
    solution = SmallestStringWithSwaps()
    
    # 测试用例1
    s1 = "dcab"
    pairs1 = [[0, 3], [1, 2]]
    print("测试用例1结果：", solution.smallest_string_with_swaps(s1, pairs1))
    # 预期输出：bacd
    
    # 测试用例2
    s2 = "dcab"
    pairs2 = [[0, 3], [1, 2], [0, 2]]
    print("测试用例2结果：", solution.smallest_string_with_swaps(s2, pairs2))
    # 预期输出：abcd
    
    # 测试用例3：没有交换对的情况
    s3 = "dcba"
    pairs3 = []
    print("测试用例3结果：", solution.smallest_string_with_swaps(s3, pairs3))
    # 预期输出：dcba
    
    # 测试用例4：所有字符都可以交换的情况
    s4 = "dcba"
    pairs4 = [[0, 1], [1, 2], [2, 3]]
    print("测试用例4结果：", solution.smallest_string_with_swaps(s4, pairs4))
    # 预期输出：abcd
    
    # 测试用例5：较大的字符串
    s5 = "abcdefgh"
    pairs5 = [[0, 4], [1, 5], [2, 6], [3, 7], [0, 1], [2, 3]]
    print("测试用例5结果：", solution.smallest_string_with_swaps(s5, pairs5))
    # 预期输出：应该是将可以交换的字符排序后的结果

if __name__ == "__main__":
    test_smallest_string_with_swaps()

'''
Python特定优化：
1. 使用列表推导式初始化parent数组，提高代码简洁性
2. 使用字典和列表而不是优先队列，避免了导入额外的模块
3. 利用Python的字符串join方法高效地构建结果字符串
4. 使用字典的get方法和默认值简化代码逻辑

算法思路详解：
1. 问题转化：将问题转化为找出可以互相交换的字符的连通分量
2. 贪心策略：在每个连通分量内部，按字典序排序字符，以得到最小的可能字符串
3. 并查集应用：并查集是处理连通分量问题的高效数据结构

工程化考量：
1. 输入验证：在实际应用中，需要验证输入参数的有效性
2. 性能优化：对于大规模数据，可以考虑使用更高效的排序算法
3. 可扩展性：可以将并查集抽象成一个独立的类，以便在其他问题中复用
4. 边界情况：需要处理空字符串、没有交换对的情况等

时间复杂度分析深入：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 对于n个元素和m次操作，总体时间复杂度为O(n * log n + m * α(n))
- 在实际应用中，α(n)增长极其缓慢，对于任何可能的n值，α(n)不超过4，因此可以近似认为是O(n * log n + m)

空间复杂度分析深入：
- 并查集需要两个长度为n的数组，因此空间复杂度为O(n)
- 字符映射和指针映射的空间复杂度也为O(n)
- 总体空间复杂度为O(n)
'''