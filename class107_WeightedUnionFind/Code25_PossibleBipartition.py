/**
 * LeetCode 886 - 可能的二分法
 * https://leetcode-cn.com/problems/possible-bipartition/
 * 
 * 题目描述：
 * 给定一组 n 个人（编号为 1, 2, ..., n），我们想把每个人分进任意大小的两组。每个人都可能不喜欢其他人，那么他们不应该属于同一组。
 * 
 * 给定不喜欢的人对列表 dislikes，其中 dislikes[i] = [a, b]，表示不允许将编号为 a 和 b 的人归入同一组。当可以用这种方法将所有人分进两组时，返回 true；否则返回 false。
 * 
 * 解题思路：
 * 这是一个典型的二分图判定问题，可以使用并查集或者DFS/BFS来解决。
 * 这里我们使用并查集的方法：
 * 1. 对于每个人来说，如果他不喜欢某个人，那么他应该和这个人的所有不喜欢的人属于同一组
 * 2. 我们可以使用一个邻接表来记录每个人的不喜欢列表
 * 3. 对于每个人，我们将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
 * 4. 最后，我们检查是否存在任何人与其不喜欢的人在同一个集合中
 * 
 * 时间复杂度分析：
 * - 构建不喜欢列表：O(m)，其中m是dislikes数组的长度
 * - 并查集操作：O(m * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 检查冲突：O(m)
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 不喜欢列表：O(m)
 * - 总体空间复杂度：O(n + m)
 */

class PossibleBipartition:
    def __init__(self):
        # 并查集的父节点数组
        self.parent = []
        # 不喜欢列表，记录每个人不喜欢的所有人
        self.dislike_list = []
    
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
        合并两个元素所在的集合
        
        参数:
            x (int): 第一个元素
            y (int): 第二个元素
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x != root_y:
            self.parent[root_y] = root_x
    
    def init_dislike_list(self, n, dislikes):
        """
        初始化不喜欢列表
        
        参数:
            n (int): 人数
            dislikes (List[List[int]]): 不喜欢的人对列表
        """
        # 初始化不喜欢列表，索引0不使用，从1到n
        self.dislike_list = [[] for _ in range(n + 1)]
        
        for a, b in dislikes:
            self.dislike_list[a].append(b)
            self.dislike_list[b].append(a)
    
    def possible_bipartition(self, n, dislikes):
        """
        判断是否可以将所有人分成两组
        
        参数:
            n (int): 人数
            dislikes (List[List[int]]): 不喜欢的人对列表
            
        返回:
            bool: 是否可以分成两组
        """
        # 初始化并查集
        self.parent = list(range(n + 1))  # 编号从1开始
        
        # 初始化不喜欢列表
        self.init_dislike_list(n, dislikes)
        
        # 对于每个人，将他不喜欢的人的不喜欢列表中的所有人合并到同一个集合中
        for i in range(1, n + 1):
            dislikes_of_i = self.dislike_list[i]
            if not dislikes_of_i:
                continue
            
            # 第一个不喜欢的人
            first_dislike = dislikes_of_i[0]
            
            # 将i的所有不喜欢的人合并到同一个集合
            for j in range(1, len(dislikes_of_i)):
                self.union(first_dislike, dislikes_of_i[j])
        
        # 检查是否存在冲突：如果一个人与其不喜欢的人在同一个集合中，则无法二分
        for i in range(1, n + 1):
            for dislike in self.dislike_list[i]:
                if self.find(i) == self.find(dislike):
                    return False
        
        return True

# 测试代码
def test_possible_bipartition():
    solution = PossibleBipartition()
    
    # 测试用例1
    n1 = 4
    dislikes1 = [[1, 2], [1, 3], [2, 4]]
    print("测试用例1结果：", solution.possible_bipartition(n1, dislikes1))
    # 预期输出：True
    
    # 测试用例2
    n2 = 3
    dislikes2 = [[1, 2], [1, 3], [2, 3]]
    print("测试用例2结果：", solution.possible_bipartition(n2, dislikes2))
    # 预期输出：False
    
    # 测试用例3
    n3 = 5
    dislikes3 = [[1, 2], [2, 3], [3, 4], [4, 5], [1, 5]]
    print("测试用例3结果：", solution.possible_bipartition(n3, dislikes3))
    # 预期输出：False
    
    # 测试用例4：空的不喜欢列表
    n4 = 5
    dislikes4 = []
    print("测试用例4结果：", solution.possible_bipartition(n4, dislikes4))
    # 预期输出：True
    
    # 测试用例5：只有一个人
    n5 = 1
    dislikes5 = []
    print("测试用例5结果：", solution.possible_bipartition(n5, dislikes5))
    # 预期输出：True
    
    # 测试用例6：大型测试用例
    n6 = 10
    dislikes6 = [
        [1, 2], [1, 3], [2, 4], [2, 5], 
        [3, 6], [3, 7], [8, 9], [9, 10]
    ]
    print("测试用例6结果：", solution.possible_bipartition(n6, dislikes6))
    # 预期输出：True

if __name__ == "__main__":
    test_possible_bipartition()

'''
Python特定优化：
1. 使用列表推导式初始化数据结构，提高代码简洁性
2. 利用Python的for循环特性，简化迭代操作
3. 使用空列表检查快速跳过没有不喜欢的人的情况
4. 将所有方法封装在类中，提高代码的组织性和可复用性

二分图判定的另一种方法：DFS/BFS染色法
除了并查集方法外，还可以使用染色法来判断是否是二分图：
1. 使用一个颜色数组，0表示未染色，1和-1表示两种不同的颜色
2. 对每个未染色的节点，使用DFS或BFS将其染成1，然后将其所有相邻节点染成-1
3. 如果在染色过程中发现冲突（即一个节点需要被染成与其当前颜色相同的颜色），则不是二分图

算法比较：
- 并查集方法：实现简单，代码量少，适合处理动态连通性问题
- 染色法：更直观地表达二分图的概念，在某些情况下可能更高效

工程化考量：
1. 对于大规模数据，可以考虑使用更高效的并查集实现（路径压缩+按秩合并）
2. 在实际应用中，需要考虑输入数据的验证和异常处理
3. 可以将并查集抽象成一个独立的类，提高代码的复用性
'''