import sys

"""
带权并查集解决Parity game问题 (Python版本)

问题分析：
给定一个01序列，每次询问一个区间内1的个数是奇数还是偶数，找出第一个错误的回答

核心思想：
1. 将区间[l,r]的奇偶性转化为前缀和sum[r]和sum[l-1]的奇偶性关系
2. 使用带权并查集维护每个点到根节点的奇偶关系
3. dist[i] = 0表示i与根节点同奇偶，dist[i] = 1表示i与根节点不同奇偶
4. 离散化处理大数据范围

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- check: O(α(n)) 近似O(1)
- 总体: O(n + m * α(n))

空间复杂度: O(n) 用于存储father和dist数组

应用场景：
- 区间奇偶性判断
- 逻辑一致性验证
- 离散化处理大数据范围
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 节点数量
        """
        self.father = list(range(n + 1))  # father[i] 表示节点i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示节点i与根节点的奇偶关系
        
    def find(self, i):
        """
        查找节点i所在集合的代表，并进行路径压缩
        同时更新dist[i]为节点i与根节点的奇偶关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的节点
        :return: 节点i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 保存父节点
            tmp = self.father[i]
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(tmp)
            # 更新奇偶关系：当前节点与根节点的关系 = 当前节点与父节点的关系 ^ 父节点与根节点的关系
            self.dist[i] ^= self.dist[tmp]
        return self.father[i]
    
    def union(self, l, r, v):
        """
        合并两个集合，建立关系
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左边界
        :param r: 右边界
        :param v: 奇偶性：0表示偶数，1表示奇数
        :return: 如果合并成功返回True，如果发现矛盾返回False
        """
        # 查找两个节点的根节点
        lf = self.find(l)
        rf = self.find(r)
        # 如果在同一集合中
        if lf == rf:
            # 检查是否与已有关系矛盾
            # l和r的奇偶关系应该等于v
            # l与根节点的关系 ^ r与根节点的关系 = l与r的关系
            if (self.dist[l] ^ self.dist[r]) != v:
                # 发现矛盾
                return False
        else:
            # 合并两个集合
            self.father[lf] = rf
            # 更新奇偶关系：
            # l与r的关系 = v
            # l与根节点lf的关系 = dist[l], r与根节点rf的关系 = dist[r]
            # 根节点lf与根节点rf的关系 = dist[l] ^ dist[r] ^ v
            self.dist[lf] = self.dist[l] ^ self.dist[r] ^ v
        return True

def main():
    # 读取输入
    lines = sys.stdin.readlines()
    line_idx = 0
    
    # 读取序列长度（虽然题目给了但实际不需要用到）
    len_seq = int(lines[line_idx].strip())
    line_idx += 1
    
    # 读取询问数量
    m = int(lines[line_idx].strip())
    line_idx += 1
    
    # 收集所有需要离散化的坐标
    ls = []
    rs = []
    parity = []
    
    for i in range(m):
        parts = lines[line_idx].strip().split()
        ls.append(int(parts[0]))
        rs.append(int(parts[1]))
        parity.append(parts[2])
        line_idx += 1
    
    # 离散化
    coord_map = {}
    index = 0
    for i in range(m):
        # 将l-1和r加入离散化
        if (ls[i] - 1) not in coord_map:
            coord_map[ls[i] - 1] = index
            index += 1
        if rs[i] not in coord_map:
            coord_map[rs[i]] = index
            index += 1
    
    n = index
    
    # 初始化带权并查集
    wuf = WeightedUnionFind(n)
    
    # 处理每个询问
    for i in range(m):
        l = coord_map[ls[i] - 1]
        r = coord_map[rs[i]]
        v = 0 if parity[i] == "even" else 1
        
        # 尝试合并
        if not wuf.union(l, r, v):
            # 发现矛盾，输出答案
            print(i)
            return
    
    # 没有发现矛盾
    print(m)

if __name__ == "__main__":
    main()