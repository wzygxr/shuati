# P3157 [CQOI2011]动态逆序对
# 平台: 洛谷
# 难度: 省选/NOI-
# 标签: CDQ分治, 动态逆序对
# 链接: https://www.luogu.com.cn/problem/P3157
# 
# 题目描述:
# 给定一个长度为n的排列，1~n所有数字都出现一次
# 如果，前面的数 > 后面的数，那么这两个数就组成一个逆序对
# 给定一个长度为m的数组，表示依次删除的数字
# 打印每次删除数字前，排列中一共有多少逆序对，一共m条打印
# 
# 示例:
# 输入:
# 5 4
# 1 5 3 4 2
# 3 5 4 2
# 
# 输出:
# 5
# 2
# 1
# 0
# 
# 解题思路:
# 使用CDQ分治解决动态逆序对问题。将问题转化为四维偏序问题：
# 1. 第一维：时间，表示删除操作的时间
# 2. 第二维：数值，表示元素的值
# 3. 第三维：位置，表示元素在原数组中的位置
# 4. 第四维：操作类型，用于区分插入和删除操作
# 
# 我们将每个元素看作两种操作：
# 1. 初始操作：在时间0时，所有元素都存在
# 2. 删除操作：在时间t时，删除某个元素
# 
# 对于每个删除操作，我们需要计算它对逆序对数量的影响：
# 1. 作为较大元素，统计在其位置之后、值更小的元素个数
# 2. 作为较小元素，统计在其位置之前、值更大的元素个数
# 
# 时间复杂度：O((n+m) log^2 (n+m))
# 空间复杂度：O(n+m)

class OperationP3157:
    def __init__(self, time, value, position, type, id):
        self.time = time
        self.value = value
        self.position = position
        self.type = type  # 1表示初始元素，-1表示删除操作
        self.id = id
    
    def __lt__(self, other):
        if self.value != other.value:
            return self.value < other.value
        # 删除操作优先于插入操作
        return other.type < self.type

class P3157Solution:
    def __init__(self):
        self.bit = []  # 树状数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, x, v, n):
        i = x
        while i <= n:
            self.bit[i] += v
            i += self.lowbit(i)
    
    def query(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def solve(self, n, m, nums, del_list):
        # 初始化
        pos = [0] * (n + 1)
        for i in range(1, n + 1):
            pos[nums[i]] = i
        
        # 构造操作序列
        ops = []
        ans = [0] * (m + 1)
        
        # 初始操作
        for i in range(1, n + 1):
            ops.append(OperationP3157(0, nums[i], i, 1, 0))
        
        # 删除操作
        for i in range(1, m + 1):
            ops.append(OperationP3157(i, del_list[i], pos[del_list[i]], -1, i))
        
        self.bit = [0] * (n + 1)
        
        # 按值排序
        ops.sort()
        
        # 执行CDQ分治
        self.cdq(ops, ans, 0, len(ops) - 1, n)
        
        # 计算前缀和
        for i in range(1, m + 1):
            ans[i] += ans[i - 1]
        
        # 输出结果
        result = []
        total = ans[0]
        result.append(str(total))
        for i in range(1, m):
            total -= ans[i]
            result.append(str(total))
        
        return result
    
    # CDQ分治主函数
    def cdq(self, ops, ans, l, r, n):
        if l >= r:
            return
        
        mid = (l + r) >> 1
        self.cdq(ops, ans, l, mid, n)
        self.cdq(ops, ans, mid + 1, r, n)
        
        # 合并过程，计算左半部分对右半部分的贡献
        tmp = [None] * (r - l + 1)
        i, j, k = l, mid + 1, 0
        
        # 从左到右统计左侧值大的数量
        while i <= mid and j <= r:
            if ops[i].position < ops[j].position:
                if ops[i].type == 1:
                    self.add(ops[i].value, ops[i].type, n)
                tmp[k] = ops[i]
                i += 1
                k += 1
            else:
                if ops[j].type == -1:
                    ans[ops[j].id] += ops[j].type * (self.query(n) - self.query(ops[j].value))
                tmp[k] = ops[j]
                j += 1
                k += 1
        
        # 处理剩余元素
        while i <= mid:
            tmp[k] = ops[i]
            i += 1
            k += 1
        while j <= r:
            if ops[j].type == -1:
                ans[ops[j].id] += ops[j].type * (self.query(n) - self.query(ops[j].value))
            tmp[k] = ops[j]
            j += 1
            k += 1
        
        # 清除树状数组
        for t in range(l, mid + 1):
            if ops[t].type == 1:
                self.add(ops[t].value, -ops[t].type, n)
        
        # 从右到左统计右侧值小的数量
        i, j, k = mid, r, 0
        tmp2 = [None] * (r - l + 1)
        while i >= l and j > mid:
            if ops[i].position > ops[j].position:
                if ops[i].type == 1:
                    self.add(ops[i].value, ops[i].type, n)
                tmp2[k] = ops[i]
                i -= 1
                k += 1
            else:
                if ops[j].type == -1:
                    ans[ops[j].id] += ops[j].type * self.query(ops[j].value - 1)
                tmp2[k] = ops[j]
                j -= 1
                k += 1
        
        # 处理剩余元素
        while i >= l:
            tmp2[k] = ops[i]
            i -= 1
            k += 1
        while j > mid:
            if ops[j].type == -1:
                ans[ops[j].id] += ops[j].type * self.query(ops[j].value - 1)
            tmp2[k] = ops[j]
            j -= 1
            k += 1
        
        # 清除树状数组
        for t in range(l, mid + 1):
            if ops[t].type == 1:
                self.add(ops[t].value, -ops[t].type, n)
        
        # 按位置排序
        tmp2.sort(key=lambda x: x.position)
        
        # 将临时数组内容复制回原数组
        for t in range(len(tmp2)):
            ops[l + t] = tmp2[t]

def main():
    # 读取输入
    n, m = map(int, input().split())
    nums = [0] + list(map(int, input().split()))
    del_list = [0] + list(map(int, input().split()))
    
    # 求解
    solution = P3157Solution()
    result = solution.solve(n, m, nums, del_list)
    
    # 输出结果
    for line in result:
        print(line)

if __name__ == "__main__":
    main()