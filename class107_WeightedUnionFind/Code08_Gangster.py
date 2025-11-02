import sys

"""
扩展域并查集解决团伙问题 (Python版本)

问题分析：
根据朋友和敌人关系，计算最终的黑帮（连通分量）数量

核心思想：
1. 使用扩展域并查集（种类并查集）处理朋友和敌人关系
2. 对于每个成员i，维护两个节点：
   - i：表示i在某个团伙中
   - i+n：表示i的敌人在某个团伙中
3. 关系处理：
   - F l r：l和r是朋友，直接合并l和r
   - E l r：l和r是敌人，合并(l,r+n)和(r,l+n)
4. 利用性质：敌人的敌人是朋友

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- 总体: O(n + m * α(n))

空间复杂度: O(n)

应用场景：
- 社交网络分析
- 敌对关系处理
- 连通分量计算
"""

class UnionFind:
    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        self.father = list(range(n))  # father[i] 表示节点i的父节点
        
    def find(self, i):
        """
        查找节点i所在集合的代表
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的节点
        :return: 节点i所在集合的根节点
        """
        # 路径压缩
        if self.father[i] != i:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, l, r):
        """
        合并两个集合
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧节点
        :param r: 右侧节点
        """
        self.father[self.find(l)] = self.find(r)

def main():
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    # 初始化并查集，大小为2*n以支持扩展域
    uf = UnionFind(2 * (n + 1))
    
    # enemy[i] 表示i的敌人所在的节点
    enemy = [0] * (n + 1)
    
    # 处理m条事实
    for _ in range(m):
        line = sys.stdin.readline().split()
        op = line[0]
        l = int(line[1])
        r = int(line[2])
        # 根据事实类型处理
        if op == "F":
            # 朋友关系，直接合并
            uf.union(l, r)
        else:
            # 敌人关系，利用"敌人的敌人是朋友"的性质
            # 如果l还没有分配敌人
            if enemy[l] == 0:
                # 将r作为l的敌人
                enemy[l] = r
            else:
                # 将l的敌人和r合并（敌人的敌人是朋友）
                uf.union(enemy[l], r)
            # 如果r还没有分配敌人
            if enemy[r] == 0:
                # 将l作为r的敌人
                enemy[r] = l
            else:
                # 将r的敌人和l合并（敌人的敌人是朋友）
                uf.union(l, enemy[r])
                
    # 统计黑帮数量
    ans = 0
    for i in range(1, n + 1):
        # 如果i是所在集合的代表，说明找到一个黑帮
        if i == uf.find(i):
            ans += 1
            
    # 输出黑帮数量
    print(ans)

if __name__ == "__main__":
    main()