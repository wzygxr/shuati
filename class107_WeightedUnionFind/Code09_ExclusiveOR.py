import sys

"""
带权并查集解决异或关系问题 (Python版本)

问题分析：
维护变量之间的异或关系，支持声明和查询操作，并检测矛盾

核心思想：
1. 将异或关系转化为带权并查集
2. 对于每个变量i，维护其到根节点的异或值dist[i]
3. 如果i ^ j = v，则i和j在同一集合中，且dist[i] ^ dist[j] = v
4. 为了处理单个变量的赋值，引入一个虚拟根节点n

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- opi: O(α(n)) 近似O(1)
- opq: O(k * α(n) + k * log(k))
- 总体: O(n + m * α(n) + Σk * log(k))

空间复杂度: O(n + k)

应用场景：
- 异或关系维护
- 逻辑一致性验证
- 位运算问题
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 节点数量
        """
        self.n = n
        self.father = list(range(n + 1))  # father[i] 表示节点i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示节点i到根节点的异或值
        
    def prepare(self):
        """
        初始化并查集
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 重置状态
        self.father = list(range(self.n + 1))
        # 初始时每个节点到根节点的异或值为0
        self.dist = [0] * (self.n + 1)
        
    def find(self, i):
        """
        查找节点i所在集合的代表，并进行路径压缩
        同时更新dist[i]为节点i到根节点的异或值
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
            # 更新异或值：当前节点到根节点的异或值 = 当前节点到父节点的异或值 ^ 父节点到根节点的异或值
            self.dist[i] ^= self.dist[tmp]
        return self.father[i]
    
    def union(self, l, r, v):
        """
        处理声明操作
        时间复杂度: O(α(n)) 近似O(1)
        
        :param l: 左侧变量编号
        :param r: 右侧变量编号
        :param v: 异或值
        :return: 如果操作成功返回True，如果发现矛盾返回False
        """
        # 查找两个变量的根节点
        lf = self.find(l)
        rf = self.find(r)
        # 如果在同一集合中
        if lf == rf:
            # 检查是否与已有关系矛盾
            # l ^ r = (l ^ root) ^ (r ^ root) = dist[l] ^ dist[r]
            if (self.dist[l] ^ self.dist[r]) != v:
                # 发现矛盾
                return False
        else:
            # 如果l所在集合的根节点是虚拟根节点
            if lf == self.n:
                # 交换，确保l所在集合不是虚拟根节点
                lf, rf = rf, self.n
            # 合并两个集合
            self.father[lf] = rf
            # 更新异或关系：
            # l ^ r = v
            # l ^ root_l = dist[l], r ^ root_r = dist[r]
            # root_l ^ root_r = dist[l] ^ dist[r] ^ v
            # 因此 dist[lf] = dist[r] ^ dist[l] ^ v
            self.dist[lf] = self.dist[r] ^ self.dist[l] ^ v
        return True
    
    def query(self, nums):
        """
        处理查询操作
        时间复杂度: O(k * α(n) + k * log(k))
        
        :param nums: 查询变量列表
        :return: 查询结果，如果无法确定返回-1
        """
        k = len(nums)
        ans = 0
        fas = []
        
        # 处理所有查询变量
        for i in range(k):
            # 查找根节点
            fa = self.find(nums[i])
            # 累计异或值
            ans ^= self.dist[nums[i]]
            # 记录根节点
            fas.append(fa)
            
        # 排序根节点，便于检查是否所有变量在同一集合中
        fas.sort()
        
        # 检查连通性
        l = 0
        while l < k:
            r = l
            # 找到相同根节点的连续段
            while r + 1 < k and fas[r + 1] == fas[l]:
                r += 1
            # 如果这一段的长度是奇数且根节点不是虚拟根节点
            if (r - l + 1) % 2 != 0 and fas[l] != self.n:
                # 无法确定结果
                return -1
            l = r + 1
            
        return ans

def main():
    lines = sys.stdin.readlines()
    i = 0
    
    t = 0
    while i < len(lines):
        line = lines[i].split()
        n = int(line[0])
        m = int(line[1])
        
        # 如果输入为0 0，结束程序
        if n == 0 and m == 0:
            break
            
        t += 1
        print(f"Case {t}:")
        
        # 初始化带权并查集
        wuf = WeightedUnionFind(n)
        
        # 是否发现矛盾
        conflict = False
        
        # 声明操作计数器
        cnti = 0
        
        # 处理m个操作
        for j in range(1, m + 1):
            line = lines[i + j].split()
            op = line[0]
            
            if op == "I":
                # 如果尚未发现矛盾
                if not conflict:
                    # 根据参数数量处理不同类型的声明
                    if len(line) == 3:
                        # 单变量赋值：I x v
                        l = int(line[1])
                        r = n  # 使用虚拟根节点
                        v = int(line[2])
                    else:
                        # 双变量异或：I x y v
                        l = int(line[1])
                        r = int(line[2])
                        v = int(line[3])
                        
                    # 声明操作计数器加1
                    cnti += 1
                    
                    # 处理声明
                    if not wuf.union(l, r, v):
                        # 发现矛盾，输出提示信息
                        conflict = True
                        print(f"The first {cnti} facts are conflicting.")
            else:
                # 查询操作：Q k a1 .. ak
                k = int(line[1])
                nums = []
                for idx in range(k):
                    nums.append(int(line[2 + idx]))
                    
                # 如果尚未发现矛盾
                if not conflict:
                    # 处理查询
                    ans = wuf.query(nums)
                    if ans == -1:
                        # 无法确定结果
                        print("I don't know.")
                    else:
                        # 输出查询结果
                        print(ans)
                        
        print()
        i += m + 1

if __name__ == "__main__":
    main()