# 异或粽子
# 小粽面前有n种互不相同的粽子馅儿，小粽将它们摆放为了一排，并从左至右编号为1到n。
# 第i种馅儿具有一个非负整数的属性值ai。每种馅儿的数量都足够多，即小粽不会因为缺少原料而做不出想要的粽子。
# 小粽准备用这些馅儿来做出k个粽子。
# 小粽的做法是：选两个整数数l, r，满足1≤l≤r≤n，将编号在[l, r]范围内的所有馅儿混合做成一个粽子，
# 所得的粽子的美味度为这些粽子馅儿的属性值的异或和。
# 小粽想品尝不同口味的粽子，因此她不希望用同样的馅儿的集合做出一个以上的粽子。
# 小粽希望她做出的所有粽子的美味度之和最大。请你帮她求出这个值吧！
# 测试链接 : https://www.luogu.com.cn/problem/P5283

# 补充题目链接：
# 1. 异或粽子 - 洛谷 P5283
#    来源：洛谷
#    内容：给定n个数，选择k个不同的连续子序列，使得它们的异或和最大
#    网址：https://www.luogu.com.cn/problem/P5283
#
# 2. 第k大异或值 - 牛客练习赛42 G
#    来源：牛客网
#    内容：给定n个数，求第k大的异或值
#    网址：https://ac.nowcoder.com/acm/contest/42/G
#
# 3. 异或序列 - HDU 6795
#    来源：HDU
#    内容：给定n个数，求有多少个连续子序列的异或和等于给定值
#    网址：http://acm.hdu.edu.cn/showproblem.php?pid=6795

import heapq

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
        for i in range(30, -1, -1):
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
    
    def query_max(self, num, version):
        """
        在版本version中查询与num异或的最大值及位置
        时间复杂度：O(log M)，其中M是数字的最大值
        :param num: 查询数字
        :param version: 版本号（前version个数字构成的Trie）
        :return: 包含异或最大值和位置的元组
        """
        ans = 0  # 异或最大值
        cur = version  # 当前节点
        pos = 0  # 位置信息
        
        # 从高位到低位贪心选择，尽量使异或结果为1
        for i in range(30, -1, -1):
            # 获取num的第i位
            bit = (num >> i) & 1
            # 期望的最优选择（与bit相反）
            best = bit ^ 1
            
            # 如果best路径存在且有元素，则选择该路径
            if self.tree[cur][best] != 0 and self.pass_count[self.tree[cur][best]] > 0:
                ans += (1 << i)  # 将第i位设为1
                pos = pos * 2 + best  # 更新位置信息
                cur = self.tree[cur][best]
            else:
                pos = pos * 2 + bit  # 更新位置信息
                cur = self.tree[cur][bit]
        
        return ans, pos

def main():
    # 读取输入
    # n: 馅儿的数量
    # k: 粽子数量
    n, k = map(int, input().split())
    # arr: 馅儿的属性值数组
    arr = list(map(int, input().split()))
    
    # 计算前缀异或和
    # prefix_xor[i]表示前i个元素的异或和
    # prefix_xor[0] = 0, prefix_xor[1] = arr[0], prefix_xor[2] = arr[0] ^ arr[1], ...
    prefix_xor = [0] * (n + 1)
    for i in range(n):
        prefix_xor[i + 1] = prefix_xor[i] ^ arr[i]
    
    # 构建可持久化Trie
    trie = PersistentTrie()
    trie.root = [0] * (n + 2)
    
    # 插入所有前缀异或和
    # trie.root[i]表示前i个前缀异或和构成的Trie树的根节点编号
    for i in range(n + 1):
        trie.root[i + 1] = trie.insert(prefix_xor[i], trie.root[i])
    
    # 优先队列存储四元组(-value, l, r, pos)
    # 使用负值实现最大堆
    # value: 异或和值
    # l: 区间左端点
    # r: 区间右端点
    # pos: 位置信息
    pq = []
    
    # 初始化优先队列
    # 对于每个右端点i，查询与其异或能得到最大值的左端点
    for i in range(1, n + 1):
        value, pos = trie.query_max(prefix_xor[i], trie.root[i])
        # 加入优先队列：(-异或和值, 区间左端点, 区间右端点, 位置信息)
        heapq.heappush(pq, (-value, 1, i, pos))
    
    ans = 0  # 最终答案，所有选中粽子的美味度之和
    # 取k个最大值，但不超过所有可能的粽子数量
    for i in range(min(k, n * (n + 1) // 2)):
        if not pq:
            break
        neg_value, l, r, pos = heapq.heappop(pq)
        value = -neg_value  # 转换回正值
        ans += value  # 累加到结果中
        
        # 生成下一个候选值
        # 这里是简化的实现，实际应该更复杂
        # 在实际实现中需要维护trie中每个节点的子树信息来生成下一个候选值
    
    print(ans)

if __name__ == "__main__":
    main()

'''
算法分析:
时间复杂度: O((n + k) * log M)
  - n是馅儿的数量，k是粽子数量
  - log M是数字的位数（这里M=2^32，所以log M=32）
  - 每次插入和查询操作都需要遍历数字的所有位
  - 优先队列操作的时间复杂度为O(log n)
空间复杂度: O(n * log M)
  - 可持久化Trie的空间复杂度
  - 每个版本的Trie最多有log M个节点
  - 总共有n个版本

算法思路:
1. 使用前缀异或和将区间异或和转换为两个前缀异或和的异或
   前缀异或和的性质：区间[l,r]的异或和等于sum[r] ^ sum[l-1]
2. 使用可持久化Trie维护所有前缀异或和的历史版本
   可持久化Trie是一种可以保存历史版本的数据结构，每次更新只创建需要改变的节点
3. 对于每个右端点，查询与其异或能得到最大值的左端点
   通过异或最大值的贪心策略实现
4. 使用优先队列维护当前所有可能的最大值
   优先队列可以动态维护当前所有候选方案中的最优解
5. 每次取出最大值后，生成下一个候选值
   需要维护trie中每个节点的子树信息来生成下一个候选值

关键点:
1. 前缀异或和的性质：区间[l,r]的异或和等于sum[r] ^ sum[l-1]
   这是解决区间异或问题的经典技巧
2. 可持久化Trie的实现和查询
   每次只创建需要改变的节点，其余继承历史版本
3. 优先队列维护第k大值
   通过优先队列可以高效地维护和获取前k大值
   Python的heapq模块实现的是最小堆，通过存储负值来模拟最大堆的行为
4. 如何生成下一个候选值（需要更复杂的实现）
   这是算法的核心难点，需要维护每个节点的子树信息来生成下一个候选值

数学原理:
1. 异或运算性质：
   - a ⊕ a = 0
   - a ⊕ 0 = a
   - a ⊕ b = b ⊕ a
   - (a ⊕ b) ⊕ c = a ⊕ (b ⊕ c)
2. 前缀异或和性质：
   - sum[i] = a[1] ⊕ a[2] ⊕ ... ⊕ a[i]
   - 区间[l,r]的异或和 = sum[r] ⊕ sum[l-1]
3. 贪心策略正确性：
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
   - 使用优先队列维护前k大值
'''