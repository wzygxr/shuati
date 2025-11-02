import sys

"""
带权并查集解决Rochambeau问题 (Python版本)

问题分析：
判断谁是裁判以及最早在第几轮可以确定

核心思想：
1. 枚举每个人作为裁判
2. 使用带权并查集维护三人组的关系
3. dist[i]表示玩家i与根节点的关系（0:相同, 1:胜, 2:负）
4. 判断是否存在矛盾

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- check: O(α(n)) 近似O(1)
- 总体: O(n * m * α(n))

空间复杂度: O(n) 用于存储father和dist数组

应用场景：
- 逻辑推理
- 枚举验证
- 关系维护
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 玩家数量
        """
        self.father = list(range(n))  # father[i] 表示玩家i的父节点
        self.dist = [0] * n           # dist[i] 表示玩家i与根节点的关系
        
    def find(self, i):
        """
        查找玩家i所在集合的代表，并进行路径压缩
        同时更新dist[i]为玩家i与根节点的关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的玩家编号
        :return: 玩家i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新关系：当前玩家与根节点的关系 = 当前玩家与父节点的关系 + 父节点与根节点的关系
            # 使用模3运算处理关系
            self.dist[i] = (self.dist[i] + self.dist[tmp]) % 3
        return self.father[i]
    
    def union(self, x, y, r):
        """
        合并两个玩家所在的集合，建立关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 玩家x编号
        :param y: 玩家y编号
        :param r: 关系：0表示平局，1表示x胜，2表示y胜
        :return: 如果合并成功返回True，如果发现矛盾返回False
        """
        # 查找两个玩家的根节点
        xf = self.find(x)
        yf = self.find(y)
        # 如果在同一集合中
        if xf == yf:
            # 检查是否与已有关系矛盾
            # x和y的关系应该等于r
            # x与根节点的关系 - y与根节点的关系 = x与y的关系
            relation = (self.dist[x] - self.dist[y] + 3) % 3
            if relation != r:
                # 发现矛盾
                return False
        else:
            # 合并两个集合
            self.father[xf] = yf
            # 更新关系：
            # x与y的关系 = r
            # x与根节点xf的关系 = dist[x], y与根节点yf的关系 = dist[y]
            # 根节点xf与根节点yf的关系 = (dist[y] - dist[x] + r + 3) % 3
            self.dist[xf] = (self.dist[y] - self.dist[x] + r + 3) % 3
        return True

def check(player, limit, n, a, b, c):
    """
    检查假设player是裁判的情况下是否存在矛盾
    
    :param player: 假设的裁判编号
    :param limit: 检查前limit轮游戏
    :param n: 玩家数量
    :param a: 玩家a数组
    :param b: 玩家b数组
    :param c: 结果数组
    :return: 如果存在矛盾返回False，否则返回True
    """
    # 初始化带权并查集
    wuf = WeightedUnionFind(n)
    
    # 检查前limit轮游戏
    for i in range(limit):
        # 如果涉及裁判则跳过
        if a[i] == player or b[i] == player:
            continue
        
        # 尝试合并
        if not wuf.union(a[i], b[i], c[i]):
            # 发现矛盾
            return False
    return True

def main():
    lines = sys.stdin.readlines()
    line_idx = 0
    
    while line_idx < len(lines):
        line = lines[line_idx].strip()
        if not line:
            break
            
        parts = line.split()
        n = int(parts[0])
        m = int(parts[1])
        line_idx += 1
        
        # 存储游戏结果
        a = [0] * m
        b = [0] * m
        c = [0] * m  # 0:平局, 1:a胜, 2:b胜
        
        # 读取游戏结果
        for i in range(m):
            line = lines[line_idx].strip()
            parts = line.split()
            a[i] = int(parts[0])
            
            # 解析结果符号
            if "=" in parts[1]:
                c[i] = 0  # 平局
            elif ">" in parts[1]:
                c[i] = 1  # a胜
            else:
                c[i] = 2  # b胜
                
            # 提取玩家b编号
            if "=" in parts[1]:
                b[i] = int(parts[1].replace("=", ""))
            elif ">" in parts[1]:
                b[i] = int(parts[1].replace(">", ""))
            else:
                b[i] = int(parts[1].replace("<", ""))
                
            line_idx += 1
        
        # 枚举每个人作为裁判
        judge = -1
        rounds = m
        count = 0
        
        for i in range(n):
            # 检查假设i是裁判是否可能
            # 二分查找最早发现矛盾的轮数
            left, right = 0, m
            while left < right:
                mid = (left + right) // 2
                if not check(i, mid, n, a, b, c):
                    right = mid
                else:
                    left = mid + 1
            
            # 如果在所有轮次中都没有矛盾，则i可能是裁判
            if check(i, m, n, a, b, c):
                judge = i
                rounds = left
                count += 1
        
        # 输出结果
        if count == 0:
            print("Impossible")
        elif count > 1:
            print("Can not determine")
        else:
            print(f"Player {judge} can be determined to be the judge after {rounds} lines")

if __name__ == "__main__":
    main()