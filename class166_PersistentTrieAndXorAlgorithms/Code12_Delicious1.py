# 美味
# 一家餐厅有n道菜，编号1...n，大家对第i道菜的评价值为ai。
# 有m位顾客，第i位顾客的期望值为bi，而他的偏好值为xi。
# 因此，第i位顾客认为第j道菜的美味度为bi⊕(aj+xi)，⊕表示异或运算。
# 第i位顾客希望从这些菜中挑出他认为最美味的菜，即美味值最大的菜，
# 但由于价格等因素，他只能从第li道到第ri道中选择。
# 请你帮助他们找出最美味的菜。
# 测试链接 : https://www.luogu.com.cn/problem/P3293

# 补充题目链接：
# 1. 美味 - 洛谷 P3293
#    来源：洛谷
#    内容：给定n道菜的评价值，m个顾客查询，每个顾客在指定区间内找与(b+x)异或最大的评价值
#    网址：https://www.luogu.com.cn/problem/P3293
#
# 2. 最大异或对 - 洛谷 P4551
#    来源：洛谷
#    内容：给定n个数，找出两个数异或的最大值
#    网址：https://www.luogu.com.cn/problem/P4551
#
# 3. 区间异或最大值 - HDU 4825
#    来源：HDU
#    内容：给定n个数，m次查询，每次查询与给定数异或的最大值
#    网址：http://acm.hdu.edu.cn/showproblem.php?pid=4825

class PersistentTrie:
    """
    可持久化Trie类
    可持久化Trie是一种可以保存历史版本的数据结构，每次更新只创建需要改变的节点
    """
    
    def __init__(self):
        """
        初始化可持久化Trie
        """
        # tree[node][0/1]表示节点node的左右子节点编号
        self.tree = [[0, 0]]
        # pass_count[node]表示经过节点node的数字个数（用于区间查询）
        self.pass_count = [0]
        # root[i]表示前i个数字构成的Trie树的根节点编号
        self.root = [0]
        # cnt表示当前已使用的节点编号（节点计数器）
        self.cnt = 0
    
    def insert(self, num, version):
        """
        在版本version的基础上插入数字num，返回新版本的根节点
        可持久化Trie的核心思想是：每次插入只创建需要改变的节点，其余节点继承历史版本
        时间复杂度：O(log M)，其中M是数字的最大值
        空间复杂度：O(log M)
        :param num: 要插入的数字
        :param version: 基于版本version创建新版本（即前version个数字构成的Trie）
        :return: 新版本的根节点编号
        """
        # 创建新根节点
        self.cnt += 1
        new_root = self.cnt
        # 复制历史版本的根节点信息
        self.tree.append([self.tree[version][0], self.tree[version][1]])
        self.pass_count.append(self.pass_count[version] + 1)
        
        # 从根节点开始插入
        cur = new_root
        # 从高位到低位处理数字的每一位
        for i in range(17, -1, -1):
            # 获取num的第i位（0或1）
            bit = (num >> i) & 1
            # 获取历史版本中对应子节点的编号
            old_child = self.tree[cur][bit]
            
            # 创建新节点
            self.cnt += 1
            new_child = self.cnt
            # 复制历史版本中对应子节点的信息
            self.tree.append([self.tree[old_child][0], self.tree[old_child][1]])
            self.pass_count.append(self.pass_count[old_child] + 1)
            
            # 更新当前节点的子节点
            self.tree[cur][bit] = new_child
            cur = new_child
        
        return new_root
    
    def query(self, num, version_l, version_r):
        """
        在版本version_l到version_r之间查询与num异或的最大值
        利用可持久化Trie实现区间查询，通过比较两个版本中节点pass值的差来判断区间内是否存在该路径
        时间复杂度：O(log M)，其中M是数字的最大值
        :param num: 查询数字
        :param version_l: 区间左端点版本（前version_l个数字构成的Trie）
        :param version_r: 区间右端点版本（前version_r个数字构成的Trie）
        :return: 与num异或的最大值
        """
        ans = 0  # 最终结果
        u, v = version_l, version_r
        
        # 从高位到低位贪心选择，尽量使异或结果为1
        for i in range(17, -1, -1):
            # 获取num的第i位
            bit = (num >> i) & 1
            # 期望的最优选择（与bit相反）
            best = bit ^ 1
            
            # 判断在区间[u,v]中是否存在best路径
            # self.pass_count[self.tree[v][best]] - self.pass_count[self.tree[u][best]]
            # 表示区间内经过self.tree[v][best]但不经过self.tree[u][best]的数字个数
            if self.pass_count[self.tree[v][best]] > self.pass_count[self.tree[u][best]]:
                # 存在best路径，选择该路径
                ans += (1 << i)  # 将第i位设为1
                u = self.tree[u][best]
                v = self.tree[v][best]
            else:
                # 不存在best路径，只能选择bit路径
                u = self.tree[u][bit]
                v = self.tree[v][bit]
        
        return ans

def main():
    # 读取输入
    n, m = map(int, input().split())  # n: 菜品数量, m: 顾客数量
    arr = list(map(int, input().split()))  # 菜品评价值数组
    
    # 构建可持久化Trie
    trie = PersistentTrie()
    trie.root = [0] * (n + 1)
    
    # 插入所有数字
    # trie.root[i]表示前i道菜构成的Trie树的根节点编号
    for i in range(1, n + 1):
        trie.root[i] = trie.insert(arr[i - 1], trie.root[i - 1])
    
    # 处理查询
    for _ in range(m):
        b, x, l, r = map(int, input().split())
        # b: 顾客期望值, x: 顾客偏好值
        # l: 可选菜品左端点, r: 可选菜品右端点
        # 查询区间[l,r]中与(b+x)异或的最大值
        # trie.root[l-1]表示前l-1道菜构成的Trie（不包含第l道菜）
        # trie.root[r]表示前r道菜构成的Trie（包含第r道菜）
        result = trie.query(b + x, trie.root[l - 1], trie.root[r])
        print(result)

if __name__ == "__main__":
    main()

'''
算法分析:
时间复杂度: O((n + m) * log M)
  - n是菜品数，m是顾客数
  - log M是数字的位数（这里M=10^5，所以log M≈17）
  - 每次插入和查询操作都需要遍历数字的所有位
空间复杂度: O(n * log M)
  - 每个版本的Trie最多有log M个节点
  - 总共有n个版本

算法思路:
1. 使用可持久化Trie维护所有菜品评价值的历史版本
   可持久化Trie是一种可以保存历史版本的数据结构，每次更新只创建需要改变的节点
2. 对于每个查询，在指定区间版本中查找与(b+x)异或的最大值
   通过pass_count数组记录每个节点的出现次数，实现区间查询
3. 通过pass_count数组记录每个节点的出现次数，实现区间查询
   区间[u,v]中经过某节点的数字个数 = pass_count[v] - pass_count[u]

关键点:
1. 可持久化Trie的实现：每次只创建需要改变的节点，其余继承历史版本
   这样可以大大节省空间，避免为每个版本都创建完整的Trie树
2. 区间查询的实现：通过比较两个版本中节点pass_count值的差来判断区间内是否存在该路径
   这是可持久化数据结构的经典应用
3. 异或最大值的贪心策略：从高位到低位，尽量选择与当前位相反的路径
   异或运算的性质：相同为0，不同为1，要使结果最大应尽量使高位为1

数学原理:
1. 异或运算性质：
   - a ⊕ a = 0
   - a ⊕ 0 = a
   - a ⊕ b = b ⊕ a
   - (a ⊕ b) ⊕ c = a ⊕ (b ⊕ c)
2. 贪心策略正确性：
   从高位到低位贪心选择，可以保证最终结果是最大的
   因为高位的1比低位的所有1加起来都大

工程化考量:
1. 数据结构设计：
   - 使用二维列表tree[node][0/1]表示Trie树，动态扩展
   - 使用pass_count列表记录节点访问次数，实现区间查询
2. 边界条件处理：
   - 注意列表下标从0开始，但题目中数组下标从1开始
   - 注意版本控制，root[0]表示空版本
3. 性能优化：
   - 使用位运算提高计算效率
   - 动态扩展列表，避免预分配过多空间
'''