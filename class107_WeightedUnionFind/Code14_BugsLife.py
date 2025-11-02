import sys

"""
种类并查集解决A Bug's Life问题 (Python版本)

问题分析：
判断虫子交互关系是否满足性别假设

核心思想：
1. 使用种类并查集（扩展域并查集）
2. 对于每只虫子i，维护两个节点：i（雄性）和i+n（雌性）
3. 如果i和j是异性，则合并i和j+n，以及i+n和j
4. 如果发现矛盾（i和i+n在同一集合中），则假设不成立

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- check: O(α(n)) 近似O(1)
- 总体: O(n + m * α(n))

空间复杂度: O(n) 用于存储father数组

应用场景：
- 种类关系维护
- 逻辑一致性验证
- 扩展域并查集
"""

class UnionFind:
    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        self.father = list(range(n + 1))  # father[i] 表示节点i的父节点
        
    def find(self, i):
        """
        查找节点i所在集合的代表，并进行路径压缩
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的节点
        :return: 节点i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, x, y):
        """
        合并两个节点所在的集合
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 节点x
        :param y: 节点y
        """
        # 查找两个节点的根节点
        xf = self.find(x)
        yf = self.find(y)
        # 如果不在同一集合中
        if xf != yf:
            # 合并两个集合
            self.father[xf] = yf

def main():
    lines = sys.stdin.readlines()
    line_idx = 0
    
    t = int(lines[line_idx].strip())
    line_idx += 1
    
    for case_num in range(1, t + 1):
        print(f"Scenario #{case_num}:")
        
        parts = lines[line_idx].strip().split()
        line_idx += 1
        n = int(parts[0])
        m = int(parts[1])
        
        # 初始化并查集
        # 对于每只虫子i，节点i表示雄性，节点i+n表示雌性
        uf = UnionFind(2 * n)
        
        suspicious = False
        
        # 处理每个交互
        for i in range(m):
            parts = lines[line_idx].strip().split()
            line_idx += 1
            x = int(parts[0])
            y = int(parts[1])
            
            # 如果已经发现矛盾，跳过后续处理
            if suspicious:
                continue
            
            # 检查是否矛盾
            # 如果x和y在同一集合中，说明它们必须是同性，与交互矛盾
            if uf.find(x) == uf.find(y):
                suspicious = True
            else:
                # 合并：x和y是异性
                # x的雄性与y的雌性合并，x的雌性与y的雄性合并
                uf.union(x, y + n)
                uf.union(x + n, y)
        
        # 输出结果
        if suspicious:
            print("Suspicious bugs found!")
        else:
            print("No suspicious bugs found!")
        print()  # 空行

if __name__ == "__main__":
    main()