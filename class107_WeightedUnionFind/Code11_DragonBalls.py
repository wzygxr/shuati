import sys

"""
带权并查集解决Dragon Balls问题 (Python版本)

问题分析：
维护龙珠的转移次数和城市龙珠数量

核心思想：
1. 使用带权并查集维护每个龙珠被转移的次数
2. 维护每个城市的龙珠数量
3. 在合并操作中正确更新转移次数和数量

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O(n + m * α(n))

空间复杂度: O(n) 用于存储father、dist和count数组

应用场景：
- 资源转移追踪
- 数量统计维护
- 操作次数记录
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 城市和龙珠数量
        """
        self.father = list(range(n + 1))  # father[i] 表示龙珠i所在城市的代表
        self.dist = [0] * (n + 1)         # dist[i] 表示龙珠i被转移的次数
        self.count = [1] * (n + 1)        # count[i] 表示城市i中的龙珠数量
        
    def find(self, i):
        """
        查找龙珠i所在城市的代表，并进行路径压缩
        同时更新dist[i]为龙珠i被转移的次数
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的龙珠编号
        :return: 龙珠i所在城市的代表
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新转移次数：当前龙珠的转移次数 += 父节点的转移次数
            self.dist[i] += self.dist[tmp]
        return self.father[i]
    
    def union(self, a, b):
        """
        合并两个城市，将A所在城市的所有龙珠转移到B所在城市
        时间复杂度: O(α(n)) 近似O(1)
        
        :param a: 龙珠A编号
        :param b: 龙珠B编号
        """
        # 查找两个龙珠所在城市的代表
        af = self.find(a)
        bf = self.find(b)
        # 如果不在同一城市
        if af != bf:
            # 将A所在城市的所有龙珠转移到B所在城市
            self.father[af] = bf
            # A所在城市的所有龙珠转移次数加1
            self.dist[af] += 1
            # 更新B所在城市的龙珠数量
            self.count[bf] += self.count[af]
            # A所在城市的龙珠数量清零
            self.count[af] = 0
    
    def query(self, a):
        """
        查询龙珠信息
        时间复杂度: O(α(n)) 近似O(1)
        
        :param a: 龙珠编号
        :return: (城市编号, 城市龙珠数量, 龙珠转移次数)
        """
        # 查找龙珠所在城市的代表
        city = self.find(a)
        return (city, self.count[city], self.dist[a])

def main():
    lines = sys.stdin.readlines()
    line_idx = 0
    
    t = int(lines[line_idx].strip())
    line_idx += 1
    
    for case_num in range(1, t + 1):
        print(f"Case {case_num}:")
        
        parts = lines[line_idx].strip().split()
        line_idx += 1
        n = int(parts[0])
        m = int(parts[1])
        
        # 初始化带权并查集
        wuf = WeightedUnionFind(n)
        
        # 处理每个操作
        for i in range(m):
            parts = lines[line_idx].strip().split()
            line_idx += 1
            
            if parts[0] == "T":
                a = int(parts[1])
                b = int(parts[2])
                wuf.union(a, b)
            else:
                a = int(parts[1])
                city, count, dist = wuf.query(a)
                print(f"{city} {count} {dist}")

if __name__ == "__main__":
    main()