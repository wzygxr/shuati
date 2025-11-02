/**
 * LeetCode 399 - 除法求值
 * https://leetcode-cn.com/problems/evaluate-division/
 * 
 * 题目描述：
 * 给你一个变量对数组 equations 和一个实数值数组 values 作为已知条件，其中 equations[i] = [Ai, Bi] 和 values[i] 共同表示等式 Ai / Bi = values[i]。
 * 每个 Ai 或 Bi 是一个表示单个变量的字符串。
 * 
 * 另有一些以数组 queries 表示的问题，其中 queries[j] = [Cj, Dj] 表示第 j 个问题，请你根据已知条件，返回 Cj / Dj = ? 的结果作为答案。
 * 如果无法确定结果，请返回 -1.0。
 * 
 * 注意：输入总是有效的。你可以假设除法运算中不会出现除数为0的情况，且不存在任何矛盾的结果。
 * 
 * 解题思路：
 * 1. 使用带权并查集来解决这个问题
 * 2. 权值表示从当前节点到父节点的商（即父节点 / 当前节点的值）
 * 3. find 操作时进行路径压缩，并同时更新权值
 * 4. union 操作时合并两个节点，并维护权值关系
 * 5. 查询时，如果两个节点不在同一集合，返回 -1.0；否则返回它们的权值比
 * 
 * 时间复杂度分析：
 * - 构建并查集：O(n * α(m))，其中n是equations的长度，m是不同变量的数量，α是阿克曼函数的反函数，近似为常数
 * - 处理查询：O(q * α(m))，其中q是queries的长度
 * - 总体时间复杂度：O((n+q) * α(m))
 * 
 * 空间复杂度分析：
 * - 存储并查集：O(m)
 * - 总体空间复杂度：O(m)
 */

class DivisionEvaluation:
    def __init__(self):
        # 并查集的父节点映射
        self.parent = {}
        # 并查集的权值映射，表示从当前节点到父节点的商（parent / current）
        self.weight = {}
    
    def find(self, x):
        """
        查找节点的根节点，并进行路径压缩，同时更新权值
        
        参数:
            x (str): 要查找的节点
            
        返回:
            str: 根节点
        """
        # 如果节点不存在于并查集中，将其加入并查集
        if x not in self.parent:
            self.parent[x] = x
            self.weight[x] = 1.0  # 自己到自己的商为1
            return x
        
        # 如果x不是根节点，需要进行路径压缩
        if self.parent[x] != x:
            origin_parent = self.parent[x]
            # 递归查找父节点的根节点，同时更新父节点的权值
            root = self.find(origin_parent)
            # 更新x的父节点为根节点（路径压缩）
            self.parent[x] = root
            # 更新x的权值：x到根节点的权值 = x到原父节点的权值 * 原父节点到根节点的权值
            self.weight[x] *= self.weight[origin_parent]
        
        return self.parent[x]
    
    def unite(self, x, y, value):
        """
        合并两个节点，并维护权值关系
        
        参数:
            x (str): 第一个节点
            y (str): 第二个节点
            value (float): x / y 的值
        """
        # 查找x和y的根节点
        root_x = self.find(x)
        root_y = self.find(y)
        
        # 如果x和y已经在同一个集合中，不需要合并
        if root_x == root_y:
            return
        
        # 合并x的集合到y的集合
        self.parent[root_x] = root_y
        # 维护权值关系：
        # 已知x / y = value
        # 需要确定 root_x / root_y 的值
        # x到root_x的权值是 weight[x]，即 root_x / x
        # y到root_y的权值是 weight[y]，即 root_y / y
        # 所以 root_x / root_y = (root_x / x) * (x / y) * (y / root_y) = weight[x] * value * (1 / weight[y])
        self.weight[root_x] = self.weight[x] * value / self.weight[y]
    
    def calc_equation(self, equations, values, queries):
        """
        计算除法求值问题
        
        参数:
            equations (List[List[str]]): 等式数组
            values (List[float]): 等式结果数组
            queries (List[List[str]]): 查询数组
            
        返回:
            List[float]: 查询结果数组
        """
        # 清空并查集
        self.parent.clear()
        self.weight.clear()
        
        # 构建并查集
        for i in range(len(equations)):
            x, y = equations[i]
            val = values[i]  # x / y = value
            self.unite(x, y, val)
        
        # 处理查询
        results = []
        for query in queries:
            x, y = query
            
            # 如果x或y不存在于并查集中，无法计算
            if x not in self.parent or y not in self.parent:
                results.append(-1.0)
                continue
            
            root_x = self.find(x)
            root_y = self.find(y)
            
            # 如果x和y不在同一个集合中，无法计算
            if root_x != root_y:
                results.append(-1.0)
            else:
                # x / y = (x到根节点的权值倒数) / (y到根节点的权值倒数) = weight[y] / weight[x]
                # 因为weight存储的是 root / node，所以 node = root / weight[node]
                results.append(self.weight[y] / self.weight[x])
        
        return results

# 测试代码
def test_division_evaluation():
    solution = DivisionEvaluation()
    
    # 测试用例1
    equations1 = [
        ["a", "b"],
        ["b", "c"]
    ]
    values1 = [2.0, 3.0]
    queries1 = [
        ["a", "c"],
        ["b", "a"],
        ["a", "e"],
        ["a", "a"],
        ["x", "x"]
    ]
    
    results1 = solution.calc_equation(equations1, values1, queries1)
    print("测试用例1结果：")
    print(results1)
    # 预期输出：[6.0, 0.5, -1.0, 1.0, -1.0]
    
    # 测试用例2
    equations2 = [
        ["a", "b"],
        ["b", "c"],
        ["bc", "cd"]
    ]
    values2 = [1.5, 2.5, 5.0]
    queries2 = [
        ["a", "c"],
        ["c", "b"],
        ["bc", "cd"],
        ["cd", "bc"]
    ]
    
    results2 = solution.calc_equation(equations2, values2, queries2)
    print("测试用例2结果：")
    print(results2)
    # 预期输出：[3.75, 0.4, 5.0, 0.2]

if __name__ == "__main__":
    test_division_evaluation()

'''
异常处理考虑：
1. 输入参数校验：equations和values长度是否一致，queries是否合法
2. 处理不存在的变量：当查询中包含未在equations中出现的变量时，返回-1.0
3. 处理自环查询：如a/a返回1.0
4. 精度问题：浮点数计算可能存在精度误差，Python的浮点数精度通常足够

Python特定优化：
1. 使用字典实现并查集，动态添加节点
2. 递归实现路径压缩，代码更简洁
3. 注意Python中的浮点运算精度问题
4. 使用清晰的变量命名增强代码可读性

扩展与变体：
1. 如果需要处理更多的数学运算，可以扩展权值的表示方式
2. 对于大规模数据，可以考虑使用更高效的路径压缩和按秩合并策略
3. 可以添加对异常输入的更严格校验
'''