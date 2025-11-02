# P2345 [USACO04OPEN] MooFest G
# 平台: 洛谷
# 难度: 省选/NOI-
# 标签: CDQ分治, 二维数点
# 链接: https://www.luogu.com.cn/problem/P2345
# 
# 题目描述:
# 约翰的n头奶牛每年都会参加"哞哞大会"。每头奶牛的坐标为x_i，听力为v_i。
# 第i头和第j头奶牛交流，会发出max{v_i,v_j} × |x_i − x_j|的音量。
# 假设每对奶牛之间同时都在说话，请计算所有奶牛产生的音量之和是多少。
# 
# 解题思路:
# 使用CDQ分治解决二维数点问题。
# 1. 将所有奶牛按x坐标排序
# 2. 对于每对奶牛(i,j)，其中i<j，贡献为max{v_i,v_j} × |x_j - x_i|
# 3. 由于x_j > x_i，所以贡献为max{v_i,v_j} × (x_j - x_i)
# 4. 将贡献拆分为两部分：
#    - max{v_i,v_j} × x_j
#    - -max{v_i,v_j} × x_i
# 5. 对于固定的j，我们需要计算所有i<j的max{v_i,v_j}的和
# 6. 这可以通过CDQ分治来解决，将问题转化为二维数点问题
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class Cow:
    def __init__(self, x, v, id):
        self.x = x
        self.v = v
        self.id = id

class Query:
    def __init__(self, type, x, v, id, idx):
        self.type = type  # 0表示插入，1表示查询
        self.x = x
        self.v = v
        self.id = id
        self.idx = idx

class Solution:
    def __init__(self):
        self.bit = []  # 树状数组
        self.sumBit = []  # 用于维护v的和的树状数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, x, v, sv, n):
        i = x
        while i <= n:
            self.bit[i] += v
            self.sumBit[i] += sv
            i += self.lowbit(i)
    
    def query(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def querySum(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.sumBit[i]
            i -= self.lowbit(i)
        return res
    
    def solveMooFest(self, x, v):
        n = len(x)
        if n == 0:
            return 0
        
        # 创建奶牛数组并按x坐标排序
        cows = []
        for i in range(n):
            cows.append(Cow(x[i], v[i], i))
        
        cows.sort(key=lambda cow: (cow.x, cow.v))
        
        # 离散化v值
        sorted_v = sorted(v)
        unique_size = self.remove_duplicates(sorted_v)
        
        queries = []
        result = [0] * n
        self.bit = [0] * (n + 1)
        self.sumBit = [0] * (n + 1)
        
        # 构造操作序列
        for i in range(n):
            # 使用二分查找找到离散化后的值
            v_id = self.binary_search(sorted_v, 0, unique_size, cows[i].v) + 1
            
            # 插入操作
            queries.append(Query(0, cows[i].x, cows[i].v, v_id, i))
            # 查询操作：查询所有v <= cows[i].v的元素个数和v的和
            queries.append(Query(1, cows[i].x, cows[i].v, v_id, i))
        
        # 按x坐标排序
        queries.sort(key=lambda q: (q.x, q.type))  # 插入操作优先于查询操作
        
        # 执行CDQ分治
        self.cdq(queries, result, 0, len(queries) - 1, n)
        
        # 计算最终结果
        total = 0
        for i in range(n):
            total += result[i]
        
        return total
    
    # CDQ分治主函数
    def cdq(self, queries, result, l, r, n):
        if l >= r:
            return
        
        mid = (l + r) >> 1
        self.cdq(queries, result, l, mid, n)
        self.cdq(queries, result, mid + 1, r, n)
        
        # 合并过程，计算左半部分对右半部分的贡献
        tmp = [None] * (r - l + 1)
        i, j, k = l, mid + 1, 0
        
        while i <= mid and j <= r:
            if queries[i].x <= queries[j].x:
                # 左半部分的元素x坐标小于等于右半部分，处理插入操作
                if queries[i].type == 0:
                    self.add(queries[i].id, 1, queries[i].v, n)  # 插入元素
                tmp[k] = queries[i]
                i += 1
                k += 1
            else:
                # 右半部分的元素x坐标更大，处理查询操作
                if queries[j].type == 1:
                    # 查询v <= queries[j].v的元素个数和v的和
                    count = self.query(queries[j].id)
                    sumV = self.querySum(queries[j].id)
                    # 贡献为：count * queries[j].x - sumV
                    result[queries[j].idx] += count * queries[j].x - sumV
                tmp[k] = queries[j]
                j += 1
                k += 1
        
        # 处理剩余元素
        while i <= mid:
            tmp[k] = queries[i]
            i += 1
            k += 1
            
        while j <= r:
            if queries[j].type == 1:
                count = self.query(queries[j].id)
                sumV = self.querySum(queries[j].id)
                result[queries[j].idx] += count * queries[j].x - sumV
            tmp[k] = queries[j]
            j += 1
            k += 1
        
        # 清理树状数组
        for t in range(l, mid + 1):
            if queries[t].type == 0:
                self.add(queries[t].id, -1, -queries[t].v, n)
        
        # 将临时数组内容复制回原数组
        for t in range(k):
            queries[l + t] = tmp[t]
    
    # 去重函数
    def remove_duplicates(self, nums):
        if len(nums) == 0:
            return 0
        unique_size = 1
        for i in range(1, len(nums)):
            if nums[i] != nums[unique_size - 1]:
                nums[unique_size] = nums[i]
                unique_size += 1
        return unique_size
    
    # 二分查找函数
    def binary_search(self, arr, l, r, target):
        left, right = l, r - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1
        return left

def main():
    solution = Solution()
    
    # 测试用例
    x1 = [1, 2, 3, 4]
    v1 = [1, 2, 3, 4]
    result1 = solution.solveMooFest(x1, v1)
    
    print("输入: x = [1,2,3,4], v = [1,2,3,4]")
    print("输出:", result1)

if __name__ == "__main__":
    main()