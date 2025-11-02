/**
 * LeetCode 990 - 等式方程的可满足性
 * https://leetcode-cn.com/problems/satisfiability-of-equality-equations/
 * 
 * 题目描述：
 * 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，并采用两种不同的形式之一："a==b" 或 "a!=b"。
 * 在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
 * 
 * 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。
 * 
 * 解题思路：
 * 1. 使用并查集来处理等式关系（"a==b"）
 * 2. 首先处理所有的等式，将相等的变量合并到同一个集合中
 * 3. 然后处理所有的不等式（"a!=b"），检查a和b是否在同一个集合中
 * 4. 如果存在任何不等式的a和b在同一个集合中，则返回false
 * 5. 否则返回true
 * 
 * 时间复杂度分析：
 * - 处理所有等式：O(n * α(26))，其中n是方程的数量，α是阿克曼函数的反函数，近似为常数
 * - 处理所有不等式：O(n * α(26))
 * - 总体时间复杂度：O(n * α(26)) ≈ O(n)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(26)，因为变量名是小写字母
 * - 总体空间复杂度：O(1)
 */

class SatisfiabilityOfEqualityEquations:
    def __init__(self):
        # 并查集的父节点数组，26个小写字母
        self.parent = []
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x (int): 要查找的元素（0-25，对应a-z）
            
        返回:
            int: 根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将x的父节点直接设置为根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def equations_possible(self, equations):
        """
        判断等式方程是否可满足
        
        参数:
            equations (List[str]): 等式方程数组
            
        返回:
            bool: 是否可满足
        """
        # 初始化并查集
        self.parent = list(range(26))
        
        # 第一遍：处理所有的等式（"a==b"）
        for equation in equations:
            if equation[1] == '=':  # 等式
                var1 = equation[0]
                var2 = equation[3]
                # 将相等的变量合并到同一个集合
                root1 = self.find(ord(var1) - ord('a'))
                root2 = self.find(ord(var2) - ord('a'))
                if root1 != root2:
                    self.parent[root2] = root1
        
        # 第二遍：处理所有的不等式（"a!=b"）
        for equation in equations:
            if equation[1] == '!':  # 不等式
                var1 = equation[0]
                var2 = equation[3]
                
                # 如果两个变量在同一个集合中，则违反不等式，返回False
                if self.find(ord(var1) - ord('a')) == self.find(ord(var2) - ord('a')):
                    return False
        
        # 所有方程都满足
        return True

# 测试代码
def test_equations_possible():
    solution = SatisfiabilityOfEqualityEquations()
    
    # 测试用例1
    equations1 = ["a==b", "b!=a"]
    print("测试用例1结果：", solution.equations_possible(equations1))
    # 预期输出：False
    
    # 测试用例2
    equations2 = ["b==a", "a==b"]
    print("测试用例2结果：", solution.equations_possible(equations2))
    # 预期输出：True
    
    # 测试用例3
    equations3 = ["a==b", "b==c", "a==c"]
    print("测试用例3结果：", solution.equations_possible(equations3))
    # 预期输出：True
    
    # 测试用例4
    equations4 = ["a==b", "b!=c", "c==a"]
    print("测试用例4结果：", solution.equations_possible(equations4))
    # 预期输出：False
    
    # 测试用例5
    equations5 = ["c==c", "b==d", "x!=z"]
    print("测试用例5结果：", solution.equations_possible(equations5))
    # 预期输出：True
    
    # 测试用例6：单个变量的等式和不等式
    equations6 = ["a==a", "a!=a"]
    print("测试用例6结果：", solution.equations_possible(equations6))
    # 预期输出：False
    
    # 测试用例7：空数组
    equations7 = []
    print("测试用例7结果：", solution.equations_possible(equations7))
    # 预期输出：True

if __name__ == "__main__":
    test_equations_possible()

'''
异常处理考虑：
1. 输入参数校验：确保equations数组中的每个字符串都是有效的等式
2. 自反性处理：a==a总是成立的，a!=a总是不成立的
3. 传递性处理：a==b和b==c意味着a==c
4. 反对称性处理：a!=b意味着b!=a

Python特定优化：
1. 使用列表推导式初始化并查集父节点数组
2. 使用ord()函数高效地将字符转换为对应的数字索引
3. 将并查集操作封装在类中，提高代码的复用性和可读性
4. 异常情况下的快速返回，避免不必要的计算

算法变体与扩展：
1. 对于包含更多变量的情况，可以使用字典来实现并查集
2. 如果需要处理更复杂的关系（如偏序关系），可能需要引入更高级的数据结构
3. 对于大规模数据，可以考虑路径压缩和按秩合并的优化版本
'''