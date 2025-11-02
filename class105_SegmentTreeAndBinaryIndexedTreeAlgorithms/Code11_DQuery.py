# SPOJ DQUERY - D-query
# 给定一个长度为n的数组，每次查询一个区间[l,r]内不同元素的个数
# 测试链接: https://www.spoj.com/problems/DQUERY/

from typing import List
import bisect

class Solution:
    """
    使用树状数组解决DQUERY问题
    
    解题思路:
    1. 这是一个经典的区间不同元素个数查询问题
    2. 可以使用离线处理+树状数组来解决
    3. 首先将所有查询按照右端点排序
    4. 从左到右扫描数组，维护每个元素最后出现的位置
    5. 当处理到位置i时，如果元素a[i]之前出现过，就将之前位置的贡献删除，
       然后在当前位置添加贡献
    6. 使用树状数组维护前缀和，支持单点更新和前缀和查询
    
    时间复杂度分析:
    - 离散化: O(n log n)
    - 排序查询: O(q log q)
    - 处理数组: O(n log n)
    - 处理查询: O(q log n)
    - 总时间复杂度: O((n+q) log n + q log q)
    
    空间复杂度分析:
    - 树状数组: O(n)
    - 查询存储: O(q)
    - 位置记录: O(n)
    - 总空间复杂度: O(n+q)
    
    工程化考量:
    1. 离线处理优化查询效率
    2. 边界条件处理
    3. 异常输入检查
    4. 详细注释和变量命名
    """

    class FenwickTree:
        def __init__(self, size: int):
            """
            初始化树状数组
            :param size: 数组大小
            """
            self.n = size
            self.tree = [0] * (size + 1)

        def lowbit(self, x: int) -> int:
            """
            lowbit操作，获取x的二进制表示中最右边的1所代表的值
            :param x: 输入整数
            :return: x & (-x)
            """
            return x & (-x)

        def add(self, index: int, delta: int) -> None:
            """
            在树状数组中更新指定位置的值（增加delta）
            :param index: 要更新的位置（从1开始）
            :param delta: 增加的值
            """
            # 沿路径向上更新所有相关节点
            while index <= self.n:
                self.tree[index] += delta
                index += self.lowbit(index)

        def query(self, index: int) -> int:
            """
            查询前缀和[1, index]的和
            :param index: 查询的右边界（从1开始）
            :return: 前缀和
            """
            sum_val = 0
            # 沿路径向下累加所有相关节点的值
            while index > 0:
                sum_val += self.tree[index]
                index -= self.lowbit(index)
            return sum_val

    def dquery(self, arr: List[int], queries: List[List[int]]) -> List[int]:
        """
        计算每个查询区间内不同元素的个数
        :param arr: 输入数组
        :param queries: 查询列表，每个查询是[l, r]的形式
        :return: 每个查询的结果列表
        """
        n = len(arr)
        q = len(queries)
        
        if n == 0 or q == 0:
            return [0] * q
        
        # 查询类
        class Query:
            def __init__(self, l: int, r: int, id: int):
                self.l = l
                self.r = r
                self.id = id
        
        # 将查询存储到Query对象中并按右端点排序
        query_list = []
        for i in range(q):
            # 转换为0-indexed到1-indexed
            query_list.append(Query(queries[i][0], queries[i][1], i))
        
        # 按右端点排序
        query_list.sort(key=lambda x: x.r)
        
        # 记录每个元素最后出现的位置
        last_position = {}
        
        # 创建树状数组
        fenwick_tree = self.FenwickTree(n)
        
        # 结果数组
        result = [0] * q
        
        # 当前处理到的位置 (0-indexed)
        current_pos = 0
        
        # 处理每个查询
        for query in query_list:
            # 将数组处理到查询的右端点
            while current_pos < query.r:
                pos = current_pos + 1  # 1-indexed position
                value = arr[current_pos]
                
                # 如果这个元素之前出现过，需要删除之前的贡献
                if value in last_position:
                    prev_pos = last_position[value]
                    fenwick_tree.add(prev_pos, -1)
                
                # 在当前位置添加贡献
                fenwick_tree.add(pos, 1)
                
                # 更新最后出现位置
                last_position[value] = pos
                
                current_pos += 1
            
            # 查询区间[l,r]内不同元素个数
            # 这等于查询[1,r]的前缀和减去[1,l-1]的前缀和
            result[query.id] = fenwick_tree.query(query.r) - fenwick_tree.query(query.l - 1)
        
        return result


# 测试函数
def test():
    solution = Solution()
    
    # 测试用例1
    arr1 = [1, 1, 2, 1, 3]
    queries1 = [[1, 5], [2, 4], [3, 5]]
    result1 = solution.dquery(arr1, queries1)
    
    print("Array: [1, 1, 2, 1, 3]")
    print("Queries:")
    for i in range(len(queries1)):
        print(f"Query [{queries1[i][0]}, {queries1[i][1]}]: {result1[i]}")
    # 期望输出: 3, 2, 3
    
    # 测试用例2
    arr2 = [1, 2, 3, 4, 5]
    queries2 = [[1, 5], [2, 3], [1, 3]]
    result2 = solution.dquery(arr2, queries2)
    
    print("\nArray: [1, 2, 3, 4, 5]")
    print("Queries:")
    for i in range(len(queries2)):
        print(f"Query [{queries2[i][0]}, {queries2[i][1]}]: {result2[i]}")
    # 期望输出: 5, 2, 3


if __name__ == "__main__":
    test()