/**
 * LeetCode 547 - 省份数量
 * https://leetcode-cn.com/problems/number-of-provinces/
 * 
 * 题目描述：
 * 有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连。
 * 
 * 省份是一组直接或间接相连的城市，组内不含其他没有相连的城市。
 * 
 * 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，而 isConnected[i][j] = 0 表示二者不直接相连。
 * 
 * 返回矩阵中省份的数量。
 * 
 * 解题思路：
 * 1. 使用并查集来管理城市之间的连通关系
 * 2. 遍历矩阵，将相连的城市合并到同一个集合中
 * 3. 最后统计集合的数量，即为省份的数量
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 遍历矩阵并合并相连的城市：O(n² * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计集合数量：O(n)
 * - 总体时间复杂度：O(n² * α(n)) ≈ O(n²)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

class NumberOfProvinces:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 并查集的秩数组，用于按秩合并优化
        self.rank = []
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的城市
            
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
            n (int): 城市数量
        """
        # 初始化，每个城市的父节点是自己，秩为0
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find_circle_num(self, isConnected):
        """
        计算省份的数量
        
        参数:
            isConnected (List[List[int]]): 连通矩阵
            
        返回:
            int: 省份数量
        """
        n = len(isConnected)
        
        # 初始化并查集
        self.init_union_find(n)
        
        # 遍历矩阵，合并相连的城市
        for i in range(n):
            for j in range(i + 1, n):
                if isConnected[i][j] == 1:
                    root_i = self.find(i)
                    root_j = self.find(j)
                    
                    if root_i != root_j:
                        # 按秩合并：将秩小的树连接到秩大的树下
                        if self.rank[root_i] < self.rank[root_j]:
                            self.parent[root_i] = root_j
                        elif self.rank[root_i] > self.rank[root_j]:
                            self.parent[root_j] = root_i
                        else:
                            # 秩相同时，任选一个作为根，并增加其秩
                            self.parent[root_j] = root_i
                            self.rank[root_i] += 1
        
        # 统计省份数量（即集合的数量）
        count = 0
        for i in range(n):
            if self.parent[i] == i:
                count += 1
        
        return count

# 测试代码
def test_number_of_provinces():
    solution = NumberOfProvinces()
    
    # 测试用例1
    isConnected1 = [
        [1, 1, 0],
        [1, 1, 0],
        [0, 0, 1]
    ]
    print("测试用例1结果：", solution.find_circle_num(isConnected1))
    # 预期输出：2
    
    # 测试用例2
    isConnected2 = [
        [1, 0, 0],
        [0, 1, 0],
        [0, 0, 1]
    ]
    print("测试用例2结果：", solution.find_circle_num(isConnected2))
    # 预期输出：3
    
    # 测试用例3：所有城市都相连
    isConnected3 = [
        [1, 1, 1],
        [1, 1, 1],
        [1, 1, 1]
    ]
    print("测试用例3结果：", solution.find_circle_num(isConnected3))
    # 预期输出：1
    
    # 测试用例4：单个城市
    isConnected4 = [[1]]
    print("测试用例4结果：", solution.find_circle_num(isConnected4))
    # 预期输出：1
    
    # 测试用例5：四个城市，形成两个省份
    isConnected5 = [
        [1, 1, 0, 0],
        [1, 1, 0, 0],
        [0, 0, 1, 1],
        [0, 0, 1, 1]
    ]
    print("测试用例5结果：", solution.find_circle_num(isConnected5))
    # 预期输出：2

if __name__ == "__main__":
    test_number_of_provinces()

'''
Python特定优化：
1. 使用列表推导式初始化parent数组，提高代码简洁性
2. 只遍历矩阵的上三角部分，避免重复处理
3. 直接在父节点数组中统计集合数量，不需要额外的数据结构

算法思路详解：
1. 问题本质：找出图中的连通分量数量
2. 并查集应用：并查集是处理连通分量问题的高效数据结构
3. 贪心策略：通过合并所有相连的节点，最终统计集合的数量

工程化考量：
1. 输入验证：在实际应用中，需要验证输入矩阵的有效性
2. 性能优化：对于大规模数据，可以考虑使用路径压缩和按秩合并优化
3. 可扩展性：可以将并查集抽象成一个独立的类，以便在其他问题中复用
4. 边界情况：需要处理空矩阵、单个城市等情况

时间复杂度分析深入：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 对于n个城市和O(n²)个可能的连接，总体时间复杂度为O(n² * α(n))
- 在实际应用中，α(n)增长极其缓慢，对于任何可能的n值，α(n)不超过4，因此可以近似认为是O(n²)

空间复杂度分析深入：
- 并查集需要两个长度为n的数组，因此空间复杂度为O(n)
- 总体空间复杂度为O(n)
'''