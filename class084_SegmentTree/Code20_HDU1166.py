# 敌兵布阵 (HDU 1166)
# 题目来源: HDU 1166. 敌兵布阵
# 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1166
# 
# 题目描述:
# C国在海岸线沿直线布置了N个工兵营地，Derek和Tidy的任务就是要监视这些工兵营地的活动情况。
# 每个工兵营地的人数C国都时刻掌握着。现在Tidy要向Derek汇报某一段连续的工兵营地一共有多少人，
# 例如Derek问："Tidy,马上汇报第3个营地到第10个营地共有多少人！"Tidy就要马上开始计算这一段的总人数并汇报。
# 但敌兵营地的人数经常变动，而Derek每次询问的段都不一样，所以Tidy要编写一个程序，支持以下两种操作：
# 1. Add i j: i和j为正整数，表示第i个营地增加j个人（j不超过30）
# 2. Query i j: i和j为正整数，表示询问第i个营地到第j个营地的总人数
#
# 解题思路:
# 1. 使用线段树实现单点更新和区间查询
# 2. 线段树的每个节点存储对应区间的元素和
# 3. 更新操作时，从根节点开始，找到对应的叶子节点并更新，然后逐层向上更新父节点
# 4. 查询操作时，从根节点开始，根据查询区间与当前节点区间的关系进行递归查询
#
# 时间复杂度分析:
# - 构建线段树: O(n)
# - 单点更新: O(log n)
# - 区间查询: O(log n)
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, nums):
        self.n = len(nums)
        self.data = nums[:]
        self.tree = [0] * (4 * self.n)
        self._build_tree(0, 0, self.n - 1)
    
    # 构建线段树
    def _build_tree(self, tree_index, l, r):
        if l == r:
            self.tree[tree_index] = self.data[l]
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        # 构建左子树
        self._build_tree(left_tree_index, l, mid)
        # 构建右子树
        self._build_tree(right_tree_index, mid + 1, r)
        
        # 当前节点的值等于左右子树值的和
        self.tree[tree_index] = self.tree[left_tree_index] + self.tree[right_tree_index]
    
    # 单点更新
    def add(self, index, val):
        self.data[index] += val
        self._update_tree(0, 0, self.n - 1, index, self.data[index])
    
    # 更新线段树
    def _update_tree(self, tree_index, l, r, index, val):
        if l == r:
            self.tree[tree_index] = val
            return
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        if index >= l and index <= mid:
            # 要更新的索引在左子树
            self._update_tree(left_tree_index, l, mid, index, val)
        else:
            # 要更新的索引在右子树
            self._update_tree(right_tree_index, mid + 1, r, index, val)
        
        # 更新当前节点的值
        self.tree[tree_index] = self.tree[left_tree_index] + self.tree[right_tree_index]
    
    # 查询区间和
    def query(self, query_l, query_r):
        if self.n == 0:
            return 0
        return self._query_tree(0, 0, self.n - 1, query_l, query_r)
    
    def _query_tree(self, tree_index, l, r, query_l, query_r):
        if l == query_l and r == query_r:
            return self.tree[tree_index]
        
        mid = l + (r - l) // 2
        left_tree_index = 2 * tree_index + 1
        right_tree_index = 2 * tree_index + 2
        
        if query_r <= mid:
            # 查询区间完全在左子树
            return self._query_tree(left_tree_index, l, mid, query_l, query_r)
        elif query_l > mid:
            # 查询区间完全在右子树
            return self._query_tree(right_tree_index, mid + 1, r, query_l, query_r)
        else:
            # 查询区间跨越左右子树
            left_result = self._query_tree(left_tree_index, l, mid, query_l, mid)
            right_result = self._query_tree(right_tree_index, mid + 1, r, mid + 1, query_r)
            return left_result + right_result

# 测试代码
if __name__ == "__main__":
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    T = int(data[idx])  # 测试用例数量
    idx += 1
    
    for case_num in range(1, T + 1):
        print(f"Case {case_num}:")
        
        n = int(data[idx])  # 营地数量
        idx += 1
        
        # 读取每个营地的初始人数
        nums = [int(data[idx + i]) for i in range(n)]
        idx += n
        
        # 构建线段树
        segment_tree = SegmentTree(nums)
        
        # 处理操作
        while True:
            operation = data[idx]
            idx += 1
            
            if operation == "End":
                break
            elif operation == "Add":
                i = int(data[idx]) - 1  # 转换为0索引
                j = int(data[idx + 1])
                idx += 2
                segment_tree.add(i, j)
            elif operation == "Sub":
                i = int(data[idx]) - 1  # 转换为0索引
                j = int(data[idx + 1])
                idx += 2
                segment_tree.add(i, -j)
            elif operation == "Query":
                i = int(data[idx]) - 1  # 转换为0索引
                j = int(data[idx + 1]) - 1  # 转换为0索引
                idx += 2
                print(segment_tree.query(i, j))