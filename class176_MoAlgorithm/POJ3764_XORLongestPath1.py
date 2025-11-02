import sys
from sys import stdin

"""
POJ 3764 The xor-longest Path
题目链接：http://poj.org/problem?id=3764

题目描述：
给定一棵树，每条边都有一个权值，求树中最长的异或路径。
异或路径的权值定义为路径上所有边的权值的异或和。

输入格式：
第一行一个整数n，表示树的节点数。
接下来n-1行，每行三个整数u, v, w，表示节点u和v之间有一条权值为w的边。

输出格式：
一个整数，表示最长的异或路径的权值。

数据范围：
1 <= n <= 100000
0 <= u, v < n
0 <= w < 2^31

解题思路：
1. 首先进行DFS遍历，计算每个节点到根节点的异或和xor_sum[u]
2. 两个节点u和v之间的异或路径的权值等于xor_sum[u] ^ xor_sum[v]
3. 问题转化为：在数组xor_sum中找到两个元素，它们的异或值最大
4. 使用字典树（Trie）来高效地解决最大异或对问题

时间复杂度：O(n * 32)，其中32是二进制位数
空间复杂度：O(n * 32)
"""

class TrieNode:
    """
    字典树节点类，用于存储二进制位
    """
    def __init__(self):
        self.children = [None, None]  # 0和1两个子节点

def main():
    # 读取输入
    sys.setrecursionlimit(1 << 25)  # 增加递归深度限制
    
    def dfs(u, current_xor, tree, visited, xor_sum):
        """
        DFS遍历树，计算每个节点到根节点的异或和
        """
        visited[u] = True
        xor_sum[u] = current_xor
        
        # 遍历所有邻接边
        for v, w in tree[u]:
            if not visited[v]:
                # 递归访问子节点，更新异或和
                dfs(v, current_xor ^ w, tree, visited, xor_sum)
    
    def insert(root, num):
        """
        将一个数插入字典树
        """
        current = root
        # 从最高位到最低位插入
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 取出当前位
            if current.children[bit] is None:
                current.children[bit] = TrieNode()
            current = current.children[bit]
    
    def query(root, num):
        """
        查询与给定数异或最大的值
        """
        current = root
        max_xor = 0
        # 从最高位到最低位查询
        for i in range(31, -1, -1):
            bit = (num >> i) & 1  # 取出当前位
            target_bit = 1 - bit  # 期望的异或位
            
            # 如果可以选择不同的位，则选择它
            if current.children[target_bit] is not None:
                max_xor |= (1 << i)  # 这一位可以得到1
                current = current.children[target_bit]
            else:
                # 否则只能选择相同的位
                if current.children[bit] is None:
                    break  # 处理意外情况
                current = current.children[bit]
        return max_xor
    
    # 读取输入数据
    input = stdin.read().split()
    ptr = 0
    while ptr < len(input):
        n = int(input[ptr])
        ptr += 1
        
        # 构建树结构
        tree = [[] for _ in range(n)]
        for _ in range(n-1):
            u = int(input[ptr])
            ptr += 1
            v = int(input[ptr])
            ptr += 1
            w = int(input[ptr])
            ptr += 1
            # 添加双向边
            tree[u].append( (v, w) )
            tree[v].append( (u, w) )
        
        # 初始化访问数组和异或和数组
        visited = [False] * n
        xor_sum = [0] * n
        
        # 从节点0开始DFS，计算异或和
        dfs(0, 0, tree, visited, xor_sum)
        
        # 构建字典树并查找最大异或值
        root = TrieNode()
        insert(root, 0)  # 插入0，表示根节点到自身的异或和
        
        max_xor = 0
        for i in range(n):
            current_max = query(root, xor_sum[i])
            if current_max > max_xor:
                max_xor = current_max
            insert(root, xor_sum[i])
        
        # 输出结果
        print(max_xor)

if __name__ == "__main__":
    main()

'''
算法分析：
时间复杂度：O(n * 32)
- DFS遍历树的时间复杂度：O(n)
- 构建字典树和查询的时间复杂度：每个数最多处理32位（二进制），总时间复杂度O(n * 32)
- 整体时间复杂度：O(n * 32)

空间复杂度：O(n * 32)
- 树的邻接表表示：O(n)
- 异或和数组：O(n)
- 字典树存储：最坏情况下O(n * 32)，但实际空间使用会小于此值

优化点：
1. 使用邻接表高效存储树结构
2. 使用位运算高效处理二进制位
3. 字典树的使用使得查找最大异或对的时间复杂度大大降低
4. 使用sys.setrecursionlimit增加递归深度，避免在树很深时出现递归错误
5. 使用stdin.read()一次性读取所有输入，提高输入效率

边界情况处理：
1. 树只有一个节点的情况，最长异或路径为0
2. 边权值为0的情况，异或不改变当前值
3. 大数值的处理，Python自动支持大整数

工程化考量：
1. 函数化设计，提高代码复用性和可读性
2. 使用类来表示字典树节点，代码结构更清晰
3. 一次性读取所有输入并使用指针访问，提高输入效率

调试技巧：
1. 可以在DFS函数中输出每个节点的异或和，检查是否计算正确
2. 测试用例：如n=3，边为0-1 1，0-2 2，预期结果为3（路径1-2的异或和为1^2=3）
3. 注意处理节点编号从0开始的情况
4. 在Python中处理大数据时，可能需要注意递归深度和执行效率问题

注意事项：
1. Python的递归深度默认较小，对于较大的树需要增加递归深度限制
2. 对于非常大的数据集，可能需要考虑使用迭代版的DFS来避免递归栈溢出
3. 在Python中，字典树的实现可能会比C++或Java稍慢，但对于题目给定的数据范围仍然可以通过
'''