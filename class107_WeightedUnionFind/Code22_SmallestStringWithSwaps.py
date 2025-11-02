/**
 * LeetCode 1202 - 交换字符串中的元素
 * https://leetcode-cn.com/problems/smallest-string-with-swaps/
 * 
 * 题目描述：
 * 给你一个字符串 s，以及该字符串中的一些「索引对」数组 pairs，其中 pairs[i] = [a, b] 表示字符串中的两个索引（编号从 0 开始）。
 * 你可以 任意多次交换 在 pairs 中任意一对索引处的字符。
 * 返回在经过若干次交换后，s 可以变成的按字典序最小的字符串。
 * 
 * 解题思路：
 * 1. 使用并查集将可以互相交换的索引合并到同一个集合中
 * 2. 对于每个集合，将其中的字符按照字典序排序
 * 3. 按照原始索引的顺序，依次从对应的集合中取出最小的可用字符
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n + m * α(n))，其中n是字符串长度，m是pairs数组长度，α是阿克曼函数的反函数，近似为常数
 * - 收集每个集合中的字符：O(n)
 * - 对每个集合中的字符排序：O(n log k)，其中k是集合的最大大小
 * - 重组字符串：O(n)
 * - 总体时间复杂度：O(n log n + m * α(n))
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 存储每个集合的字符：O(n)
 * - 总体空间复杂度：O(n)
 */

class SmallestStringWithSwaps:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 并查集的秩数组，用于按秩合并
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
    
    def union(self, x, y):
        """
        合并两个元素所在的集合，使用按秩合并优化
        
        参数:
            x (int): 第一个元素
            y (int): 第二个元素
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return  # 已经在同一集合中
        
        # 按秩合并：将较小秩的树连接到较大秩的树上
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            # 秩相等时，任选一个作为根，并增加其秩
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
    
    def smallest_string_with_swaps(self, s, pairs):
        """
        计算字典序最小的字符串
        
        参数:
            s (str): 原始字符串
            pairs (List[List[int]]): 索引对数组
            
        返回:
            str: 字典序最小的字符串
        """
        n = len(s)
        
        # 初始化并查集
        self.parent = list(range(n))
        self.rank = [1] * n
        
        # 将可以交换的索引合并到同一个集合中
        for a, b in pairs:
            self.union(a, b)
        
        # 收集每个集合中的字符和它们的索引
        # key: 集合的根节点
        # value: (字符列表, 索引列表)
        groups = {}
        for i in range(n):
            root = self.find(i)
            if root not in groups:
                groups[root] = ([], [])
            groups[root][0].append(s[i])  # 字符列表
            groups[root][1].append(i)     # 索引列表
        
        # 创建结果数组（使用列表以便修改）
        result = list(s)
        
        # 对每个集合进行处理
        for root, (chars, indices) in groups.items():
            # 对字符排序
            chars.sort()
            # 对索引排序
            indices.sort()
            
            # 将排序后的字符放置到对应的索引位置
            for char, idx in zip(chars, indices):
                result[idx] = char
        
        # 将结果列表转换为字符串
        return ''.join(result)

# 测试代码
def test_smallest_string_with_swaps():
    solution = SmallestStringWithSwaps()
    
    # 测试用例1
    s1 = "dcab"
    pairs1 = [[0, 3], [1, 2]]
    print("测试用例1结果：", solution.smallest_string_with_swaps(s1, pairs1))
    # 预期输出："bacd"
    
    # 测试用例2
    s2 = "dcab"
    pairs2 = [[0, 3], [1, 2], [0, 2]]
    print("测试用例2结果：", solution.smallest_string_with_swaps(s2, pairs2))
    # 预期输出："abcd"
    
    # 测试用例3
    s3 = "cba"
    pairs3 = [[0, 1], [1, 2]]
    print("测试用例3结果：", solution.smallest_string_with_swaps(s3, pairs3))
    # 预期输出："abc"
    
    # 测试用例4：空字符串
    s4 = ""
    pairs4 = []
    print("测试用例4结果：", solution.smallest_string_with_swaps(s4, pairs4))
    # 预期输出：""
    
    # 测试用例5：无交换对
    s5 = "hello"
    pairs5 = []
    print("测试用例5结果：", solution.smallest_string_with_swaps(s5, pairs5))
    # 预期输出："hello"

if __name__ == "__main__":
    test_smallest_string_with_swaps()

'''
异常处理考虑：
1. 空字符串处理：当s为空时，直接返回空字符串
2. 空pairs数组处理：当pairs为空时，无法进行任何交换，直接返回原字符串
3. 索引越界检查：确保pairs中的索引在有效范围内
4. 大规模数据处理：通过路径压缩和按秩合并确保并查集操作的高效性

Python特定优化：
1. 使用列表实现并查集，提高访问效率
2. 使用字典直接存储每个集合的字符和索引
3. 使用zip函数高效地配对排序后的字符和索引
4. 使用列表来构建结果字符串，方便字符修改操作

算法变体与扩展：
1. 如果要求字典序最大的字符串，只需将字符降序排序即可
2. 如果要求最小交换次数，可以引入更复杂的算法（如图论中的最短路径）
3. 对于大规模数据，可以使用路径压缩和按秩合并的优化版本
'''