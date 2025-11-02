# FHQ-Treap 详解与实战指南

## 算法原理与实现

FHQ-Treap（又称无旋 Treap）是由范浩强提出的一种平衡二叉搜索树数据结构。它通过两个核心操作——分裂（Split）和合并（Merge）来维护树的平衡，避免了传统平衡树中的旋转操作，代码实现简洁而强大。

### 核心思想

FHQ-Treap 结合了二叉搜索树和堆的特性：
1. 满足二叉搜索树性质：左子树所有节点的值 ≤ 根节点值 ≤ 右子树所有节点的值
2. 满足堆性质：每个节点有一个随机优先级，父节点的优先级 ≥ 子节点优先级

通过随机优先级来维持树的平衡，使得树的期望高度为 O(log n)，从而保证所有操作的期望时间复杂度为 O(log n)。

### 核心操作详解

#### 1. 分裂（Split）

将一棵树按照某个值分裂成两棵树：
- **按值分裂**：将树分裂为左树（所有节点值 ≤ k）和右树（所有节点值 > k）

实现思路：
```python
# 伪代码实现
function split(node, k):
    if node is null:
        return null, null
    
    if node.value <= k:
        # 当前节点及其左子树属于左树，递归分裂右子树
        left_tree = node
        left_tree.right, right_tree = split(node.right, k)
    else:
        # 当前节点及其右子树属于右树，递归分裂左子树
        right_tree = node
        left_tree, right_tree.left = split(node.left, k)
    
    # 更新当前节点的子树大小信息
    update_size(node)
    return left_tree, right_tree
```

时间复杂度：O(log n)，其中n为树中节点数量
空间复杂度：O(log n)，递归调用栈深度

#### 2. 合并（Merge）

将两棵满足条件的树合并成一棵树：
- 前提条件：左树的所有节点值 ≤ 右树的所有节点值
- 根据堆性质（优先级）决定合并方向

实现思路：
```python
# 伪代码实现
function merge(left_tree, right_tree):
    if left_tree is null:
        return right_tree
    if right_tree is null:
        return left_tree
    
    if left_tree.priority >= right_tree.priority:
        # 左树根节点优先级更高，作为新根
        left_tree.right = merge(left_tree.right, right_tree)
        update_size(left_tree)
        return left_tree
    else:
        # 右树根节点优先级更高，作为新根
        right_tree.left = merge(left_tree, right_tree.left)
        update_size(right_tree)
        return right_tree
```

时间复杂度：O(log n)
空间复杂度：O(log n)，递归调用栈深度

### 基于分裂合并的扩展操作

所有FHQ-Treap的基本操作都可以通过分裂和合并组合实现：

1. **插入操作**：先分裂，再创建新节点，最后合并
2. **删除操作**：通过两次分裂隔离目标节点，然后合并剩余部分
3. **排名查询**：统计左子树大小+1
4. **第k小查询**：递归查找第k个元素
5. **前驱查询**：查找小于目标值的最大元素
6. **后继查询**：查找大于目标值的最小元素

## 应用场景与实战题目

FHQ-Treap在各种算法竞赛和工程应用中都有广泛的用途，下面介绍一些典型应用场景和相应的实战题目，涵盖各大算法平台。

### 一、普通平衡树应用

#### 题目1：洛谷 P3369 普通平衡树

**题目描述**：实现一个平衡树，支持插入、删除、查询排名、查询第k小数、前驱、后继等操作。

**题解**：使用FHQ-Treap实现所有操作，代码如下（Python版本）：

```python
import random

class FHQTreapWithCount:
    def __init__(self, max_n=100001):
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号（根节点）
        self.cnt = 0   # 空间使用计数，记录当前已分配的节点数量
        
        # 节点信息数组 - 使用预分配数组而非链表，提高性能
        self.key = [0] * self.MAXN      # 节点的键值
        self.count = [0] * self.MAXN    # 词频计数
        self.left = [0] * self.MAXN     # 左子节点索引
        self.right = [0] * self.MAXN    # 右子节点索引
        self.size = [0] * self.MAXN     # 子树大小
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
        # 初始化随机数种子
        random.seed(42)
    
    def update_size(self, i):
        """更新节点的子树大小"""
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + self.count[i]
    
    def split(self, l, r, i, num):
        """分裂操作：按值分裂"""
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            self.update_size(i)
    
    def merge(self, l, r):
        """合并操作"""
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.update_size(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.update_size(r)
            return r
    
    def find(self, i, num):
        """查找指定值的节点"""
        if i == 0:
            return 0
        if self.key[i] == num:
            return i
        elif self.key[i] > num:
            return self.find(self.left[i], num)
        else:
            return self.find(self.right[i], num)
    
    def change_count(self, i, num, delta):
        """修改指定值的节点计数"""
        if self.key[i] == num:
            self.count[i] += delta
        elif self.key[i] > num:
            self.change_count(self.left[i], num, delta)
        else:
            self.change_count(self.right[i], num, delta)
        self.update_size(i)
    
    def add(self, num):
        """添加元素操作"""
        if self.find(self.head, num) != 0:
            self.change_count(self.head, num, 1)
        else:
            self.split(0, 0, self.head, num)
            self.cnt += 1
            self.key[self.cnt] = num
            self.count[self.cnt] = self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def remove(self, num):
        """删除元素操作"""
        i = self.find(self.head, num)
        if i != 0:
            if self.count[i] > 1:
                self.change_count(self.head, num, -1)
            else:
                self.split(0, 0, self.head, num)
                lm = self.right[0]
                r = self.left[0]
                self.split(0, 0, lm, num - 1)
                l = self.right[0]
                self.head = self.merge(l, r)
    
    def small(self, i, num):
        """统计小于num的元素个数"""
        if i == 0:
            return 0
        if self.key[i] >= num:
            return self.small(self.left[i], num)
        else:
            return self.size[self.left[i]] + self.count[i] + self.small(self.right[i], num)
    
    def rank(self, num):
        """查询元素的排名"""
        return self.small(self.head, num) + 1
    
    def index(self, i, x):
        """查询第k小的元素"""
        if self.size[self.left[i]] >= x:
            return self.index(self.left[i], x)
        elif self.size[self.left[i]] + self.count[i] < x:
            return self.index(self.right[i], x - self.size[self.left[i]] - self.count[i])
        return self.key[i]
    
    def get_kth(self, x):
        """查询第k小的元素（对外接口）"""
        return self.index(self.head, x)
    
    def pre(self, i, num):
        """查询前驱"""
        if i == 0:
            return -float('inf')
        if self.key[i] >= num:
            return self.pre(self.left[i], num)
        else:
            return max(self.key[i], self.pre(self.right[i], num))
    
    def get_predecessor(self, num):
        """查询前驱（对外接口）"""
        return self.pre(self.head, num)
    
    def post(self, i, num):
        """查询后继"""
        if i == 0:
            return float('inf')
        if self.key[i] <= num:
            return self.post(self.right[i], num)
        else:
            return min(self.key[i], self.post(self.left[i], num))
    
    def get_successor(self, num):
        """查询后继（对外接口）"""
        return self.post(self.head, num)

# 主函数用于处理输入输出
class Main:
    def __init__(self):
        import sys
        input = sys.stdin.read().split()
        ptr = 0
        n = int(input[ptr])
        ptr += 1
        
        treap = FHQTreapWithCount()
        results = []
        
        for _ in range(n):
            op = int(input[ptr])
            x = int(input[ptr + 1])
            ptr += 2
            
            try:
                if op == 1:
                    treap.add(x)
                elif op == 2:
                    treap.remove(x)
                elif op == 3:
                    results.append(str(treap.rank(x)))
                elif op == 4:
                    results.append(str(treap.get_kth(x)))
                elif op == 5:
                    results.append(str(treap.get_predecessor(x)))
                elif op == 6:
                    results.append(str(treap.get_successor(x)))
            except Exception as e:
                results.append(f"Error: {e}")
        
        print('\n'.join(results))

if __name__ == "__main__":
    Main()
```

**时间复杂度**：每个操作的期望时间复杂度为 O(log n)，其中n为元素总数
**空间复杂度**：O(n)，预分配数组的大小

#### 题目2：LeetCode 456. 132模式

**题目链接**：https://leetcode.cn/problems/132-pattern/

**题目描述**：给你一个整数数组 nums，判断这个数组中是否存在长度为 3 的递增子序列，且满足 i < j < k 和 nums[i] < nums[k] < nums[j]。

**题解**：使用FHQ-Treap维护元素，对于每个j，查询前面的最小值和后面的小于nums[j]的最大值

```python
# FHQ-Treap实现132模式检测
import random

class FHQTreap:
    def __init__(self):
        self.MAXN = 20001  # 题目约束n<=20000
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
        random.seed(42)
    
    def update_size(self, i):
        if i != 0:
            self.size[i] = self.size[self.left[i]] + 1 + self.size[self.right[i]]
    
    def split(self, l, r, i, num):
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            self.update_size(i)
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.update_size(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.update_size(r)
            return r
    
    def insert(self, num):
        self.split(0, 0, self.head, num)
        self.cnt += 1
        self.key[self.cnt] = num
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def find_largest_smaller(self, num):
        # 找到小于num的最大元素
        res = -float('inf')
        i = self.head
        while i != 0:
            if self.key[i] < num:
                res = max(res, self.key[i])
                i = self.right[i]  # 尝试找更大的但仍小于num的值
            else:
                i = self.left[i]
        return res

class Solution:
    def find132pattern(self, nums):
        n = len(nums)
        if n < 3:
            return False
        
        # 维护前缀最小值数组
        min_left = [float('inf')] * n
        min_left[0] = nums[0]
        for i in range(1, n):
            min_left[i] = min(min_left[i-1], nums[i])
        
        # 使用FHQ-Treap维护后缀元素
        treap = FHQTreap()
        treap.insert(nums[-1])
        
        # 从后往前遍历
        for j in range(n-2, 0, -1):
            # 检查是否存在nums[k]满足min_left[j-1] < nums[k] < nums[j]
            # min_left[j-1]是nums[i]的可能值（i<j）
            # nums[k]是nums[j]之后的值（k>j）
            if min_left[j-1] < nums[j]:
                # 在Treap中查找小于nums[j]的最大元素
                candidate = treap.find_largest_smaller(nums[j])
                if candidate > min_left[j-1]:
                    return True
            # 将nums[j]插入Treap，供前面的j'使用
            treap.insert(nums[j])
        
        return False
```

**时间复杂度**：O(n log n)，其中n为数组长度，每个插入和查询操作的时间复杂度为O(log n)
**空间复杂度**：O(n)，存储Treap节点和前缀最小值数组

#### 题目3：LeetCode 2336. 无限集中的最小数字

**题目链接**：https://leetcode.cn/problems/smallest-number-in-infinite-set/

**题目描述**：实现一个无限集合，支持插入、删除和查询最小数字操作。

**题解**：使用FHQ-Treap维护可用的数字，实现高效的插入、删除和查询最小值操作

**Python实现**：
```python
import random

class FHQTreap:
    def __init__(self):
        self.MAXN = 2001  # 题目约束操作数不超过1000
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
        random.seed(42)
    
    def update_size(self, i):
        if i != 0:
            self.size[i] = self.size[self.left[i]] + 1 + self.size[self.right[i]]
    
    def split(self, l, r, i, num):
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            self.update_size(i)
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.update_size(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.update_size(r)
            return r
    
    def insert(self, num):
        # 先检查是否已存在
        if not self.exists(self.head, num):
            self.split(0, 0, self.head, num)
            self.cnt += 1
            self.key[self.cnt] = num
            self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def exists(self, i, num):
        if i == 0:
            return False
        if self.key[i] == num:
            return True
        elif self.key[i] > num:
            return self.exists(self.left[i], num)
        else:
            return self.exists(self.right[i], num)
    
    def remove(self, num):
        # 通过两次分裂删除节点
        self.split(0, 0, self.head, num)
        a = self.right[0]  # 小于等于num的部分
        b = self.left[0]   # 大于num的部分
        
        self.split(0, 0, a, num - 1)
        c = self.right[0]  # 小于num的部分
        # 中间的部分（等于num）被丢弃
        
        self.head = self.merge(c, b)
    
    def find_min(self):
        # 找最左边的节点
        if self.head == 0:
            return -1
        i = self.head
        while self.left[i] != 0:
            i = self.left[i]
        return self.key[i]

class SmallestInfiniteSet:
    def __init__(self):
        self.treap = FHQTreap()
        self.current_min = 1  # 当前最小的未被移除的自然数
    
    def popSmallest(self):
        # 如果Treap不为空，说明有更小的被add回来的数
        if self.treap.head != 0:
            min_val = self.treap.find_min()
            self.treap.remove(min_val)
            return min_val
        else:
            # 否则返回current_min并递增
            res = self.current_min
            self.current_min += 1
            return res
    
    def addBack(self, num):
        # 只有当num小于current_min且未在Treap中时才添加
        if num < self.current_min:
            self.treap.insert(num)
```

**Java实现**：
```java
import java.util.Random;

class FHQTreap {
    private int MAXN = 2001;
    private int head = 0;
    private int cnt = 0;
    private int[] key = new int[MAXN];
    private int[] size = new int[MAXN];
    private int[] left = new int[MAXN];
    private int[] right = new int[MAXN];
    private double[] priority = new double[MAXN];
    private Random random;
    
    public FHQTreap() {
        random = new Random(42);
    }
    
    private void updateSize(int i) {
        if (i != 0) {
            size[i] = size[left[i]] + 1 + size[right[i]];
        }
    }
    
    private void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
            }
            updateSize(i);
        }
    }
    
    private int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            updateSize(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            updateSize(r);
            return r;
        }
    }
    
    public void insert(int num) {
        if (!exists(head, num)) {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            size[cnt] = 1;
            priority[cnt] = random.nextDouble();
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    private boolean exists(int i, int num) {
        if (i == 0) return false;
        if (key[i] == num) return true;
        if (key[i] > num) return exists(left[i], num);
        return exists(right[i], num);
    }
    
    public void remove(int num) {
        split(0, 0, head, num);
        int a = right[0];
        int b = left[0];
        
        split(0, 0, a, num - 1);
        int c = right[0];
        
        head = merge(c, b);
    }
    
    public int findMin() {
        if (head == 0) return -1;
        int i = head;
        while (left[i] != 0) {
            i = left[i];
        }
        return key[i];
    }
}

class SmallestInfiniteSet {
    private FHQTreap treap;
    private int currentMin;
    
    public SmallestInfiniteSet() {
        treap = new FHQTreap();
        currentMin = 1;
    }
    
    public int popSmallest() {
        if (treap.findMin() != -1) {
            int minVal = treap.findMin();
            treap.remove(minVal);
            return minVal;
        } else {
            return currentMin++;
        }
    }
    
    public void addBack(int num) {
        if (num < currentMin) {
            treap.insert(num);
        }
    }
}
```

**C++实现**：
```cpp
#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

class FHQTreap {
private:
    static const int MAXN = 2001;
    int head = 0;
    int cnt = 0;
    int key[MAXN];
    int size[MAXN];
    int left[MAXN];
    int right[MAXN];
    double priority[MAXN];
    
    void updateSize(int i) {
        if (i != 0) {
            size[i] = size[left[i]] + 1 + size[right[i]];
        }
    }
    
    void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
            }
            updateSize(i);
        }
    }
    
    int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            updateSize(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            updateSize(r);
            return r;
        }
    }
    
    bool exists(int i, int num) {
        if (i == 0) return false;
        if (key[i] == num) return true;
        if (key[i] > num) return exists(left[i], num);
        return exists(right[i], num);
    }
    
public:
    FHQTreap() {
        srand(42);
    }
    
    void insert(int num) {
        if (!exists(head, num)) {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            size[cnt] = 1;
            priority[cnt] = (double)rand() / RAND_MAX;
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    void remove(int num) {
        split(0, 0, head, num);
        int a = right[0];
        int b = left[0];
        
        split(0, 0, a, num - 1);
        int c = right[0];
        
        head = merge(c, b);
    }
    
    int findMin() {
        if (head == 0) return -1;
        int i = head;
        while (left[i] != 0) {
            i = left[i];
        }
        return key[i];
    }
};

class SmallestInfiniteSet {
private:
    FHQTreap treap;
    int currentMin;
    
public:
    SmallestInfiniteSet() {
        currentMin = 1;
    }
    
    int popSmallest() {
        int minVal = treap.findMin();
        if (minVal != -1) {
            treap.remove(minVal);
            return minVal;
        } else {
            return currentMin++;
        }
    }
    
    void addBack(int num) {
        if (num < currentMin) {
            treap.insert(num);
        }
    }
};
```

**时间复杂度**：每个操作的期望时间复杂度为 O(log n)，其中n为当前在Treap中的元素数量
**空间复杂度**：O(n)，存储Treap节点

#### 题目4：LeetCode 1845. 座位预约管理系统

**题目链接**：https://leetcode.cn/problems/seat-reservation-manager/

**题目描述**：实现一个座位预约管理系统，支持预订和取消预订操作，每次预订时返回可用的最小座位号。

**题解**：这是一个典型的优先队列/平衡树应用场景，FHQ-Treap非常适合这种需要高效查找最小值和维护动态集合的情况。

**Python实现**：
```python
import random

class FHQTreap:
    def __init__(self):
        self.MAXN = 100001  # 题目约束n<=1e5
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
        random.seed(42)
    
    def update_size(self, i):
        if i != 0:
            self.size[i] = self.size[self.left[i]] + 1 + self.size[self.right[i]]
    
    def split(self, l, r, i, num):
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            self.update_size(i)
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.update_size(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.update_size(r)
            return r
    
    def insert(self, num):
        # 先检查是否已存在
        if not self.exists(self.head, num):
            self.split(0, 0, self.head, num)
            self.cnt += 1
            self.key[self.cnt] = num
            self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def exists(self, i, num):
        if i == 0:
            return False
        if self.key[i] == num:
            return True
        elif self.key[i] > num:
            return self.exists(self.left[i], num)
        else:
            return self.exists(self.right[i], num)
    
    def remove(self, num):
        # 通过两次分裂删除节点
        self.split(0, 0, self.head, num)
        a = self.right[0]  # 小于等于num的部分
        b = self.left[0]   # 大于num的部分
        
        self.split(0, 0, a, num - 1)
        c = self.right[0]  # 小于num的部分
        # 中间的部分（等于num）被丢弃
        
        self.head = self.merge(c, b)
    
    def find_min(self):
        # 找最左边的节点
        if self.head == 0:
            return -1
        i = self.head
        while self.left[i] != 0:
            i = self.left[i]
        return self.key[i]

class SeatManager:
    def __init__(self, n):
        self.treap = FHQTreap()
        self.current_min = 1  # 当前最小的未被预订的座位
        self.max_seat = n     # 最大座位号
    
    def reserve(self):
        # 如果Treap不为空，说明有被取消的座位
        if self.treap.head != 0:
            min_seat = self.treap.find_min()
            self.treap.remove(min_seat)
            return min_seat
        else:
            # 否则返回current_min并递增
            res = self.current_min
            self.current_min += 1
            return res
    
    def unreserve(self, seatNumber):
        # 只有当座位号在有效范围内且已被预订时才取消
        if 1 <= seatNumber <= self.max_seat and seatNumber < self.current_min:
            self.treap.insert(seatNumber)
```

**Java实现**：
```java
import java.util.Random;

class FHQTreap {
    private int MAXN = 100001;
    private int head = 0;
    private int cnt = 0;
    private int[] key = new int[MAXN];
    private int[] size = new int[MAXN];
    private int[] left = new int[MAXN];
    private int[] right = new int[MAXN];
    private double[] priority = new double[MAXN];
    private Random random;
    
    public FHQTreap() {
        random = new Random(42);
    }
    
    private void updateSize(int i) {
        if (i != 0) {
            size[i] = size[left[i]] + 1 + size[right[i]];
        }
    }
    
    private void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
            }
            updateSize(i);
        }
    }
    
    private int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            updateSize(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            updateSize(r);
            return r;
        }
    }
    
    public void insert(int num) {
        if (!exists(head, num)) {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            size[cnt] = 1;
            priority[cnt] = random.nextDouble();
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    private boolean exists(int i, int num) {
        if (i == 0) return false;
        if (key[i] == num) return true;
        if (key[i] > num) return exists(left[i], num);
        return exists(right[i], num);
    }
    
    public void remove(int num) {
        split(0, 0, head, num);
        int a = right[0];
        int b = left[0];
        
        split(0, 0, a, num - 1);
        int c = right[0];
        
        head = merge(c, b);
    }
    
    public int findMin() {
        if (head == 0) return -1;
        int i = head;
        while (left[i] != 0) {
            i = left[i];
        }
        return key[i];
    }
}

class SeatManager {
    private FHQTreap treap;
    private int currentMin;
    private int maxSeat;
    
    public SeatManager(int n) {
        treap = new FHQTreap();
        currentMin = 1;
        maxSeat = n;
    }
    
    public int reserve() {
        if (treap.findMin() != -1) {
            int minSeat = treap.findMin();
            treap.remove(minSeat);
            return minSeat;
        } else {
            return currentMin++;
        }
    }
    
    public void unreserve(int seatNumber) {
        if (1 <= seatNumber && seatNumber <= maxSeat && seatNumber < currentMin) {
            treap.insert(seatNumber);
        }
    }
}
```

**C++实现**：
```cpp
#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

class FHQTreap {
private:
    static const int MAXN = 100001;
    int head = 0;
    int cnt = 0;
    int key[MAXN];
    int size[MAXN];
    int left[MAXN];
    int right[MAXN];
    double priority[MAXN];
    
    void updateSize(int i) {
        if (i != 0) {
            size[i] = size[left[i]] + 1 + size[right[i]];
        }
    }
    
    void split(int l, int r, int i, int num) {
        if (i == 0) {
            right[l] = left[r] = 0;
        } else {
            if (key[i] <= num) {
                right[l] = i;
                split(i, r, right[i], num);
            } else {
                left[r] = i;
                split(l, i, left[i], num);
            }
            updateSize(i);
        }
    }
    
    int merge(int l, int r) {
        if (l == 0 || r == 0) {
            return l + r;
        }
        if (priority[l] >= priority[r]) {
            right[l] = merge(right[l], r);
            updateSize(l);
            return l;
        } else {
            left[r] = merge(l, left[r]);
            updateSize(r);
            return r;
        }
    }
    
    bool exists(int i, int num) {
        if (i == 0) return false;
        if (key[i] == num) return true;
        if (key[i] > num) return exists(left[i], num);
        return exists(right[i], num);
    }
    
public:
    FHQTreap() {
        srand(42);
    }
    
    void insert(int num) {
        if (!exists(head, num)) {
            split(0, 0, head, num);
            cnt++;
            key[cnt] = num;
            size[cnt] = 1;
            priority[cnt] = (double)rand() / RAND_MAX;
            head = merge(merge(right[0], cnt), left[0]);
        }
    }
    
    void remove(int num) {
        split(0, 0, head, num);
        int a = right[0];
        int b = left[0];
        
        split(0, 0, a, num - 1);
        int c = right[0];
        
        head = merge(c, b);
    }
    
    int findMin() {
        if (head == 0) return -1;
        int i = head;
        while (left[i] != 0) {
            i = left[i];
        }
        return key[i];
    }
};

class SeatManager {
private:
    FHQTreap treap;
    int currentMin;
    int maxSeat;
    
public:
    SeatManager(int n) {
        currentMin = 1;
        maxSeat = n;
    }
    
    int reserve() {
        int minSeat = treap.findMin();
        if (minSeat != -1) {
            treap.remove(minSeat);
            return minSeat;
        } else {
            return currentMin++;
        }
    }
    
    void unreserve(int seatNumber) {
        if (1 <= seatNumber && seatNumber <= maxSeat && seatNumber < currentMin) {
            treap.insert(seatNumber);
        }
    }
};
```

**时间复杂度**：每个操作的期望时间复杂度为 O(log n)，其中n为座位总数
**空间复杂度**：O(k)，其中k为被取消预订的座位数量

### 二、文艺平衡树应用（区间翻转）

#### 题目1：洛谷 P3391 文艺平衡树

**题目链接**：https://www.luogu.com.cn/problem/P3391

**题目描述**：实现一个支持区间翻转的数据结构，能够处理对数组的区间反转操作。

**题解**：这是典型的文艺平衡树问题，通过FHQ-Treap的分裂和合并操作，结合懒惰标记实现区间翻转。

**Python实现**：
```python
import random

class FHQTreap:
    def __init__(self, n):
        self.MAXN = n + 10
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN        # 节点值
        self.size = [0] * self.MAXN       # 子树大小
        self.left = [0] * self.MAXN       # 左子节点
        self.right = [0] * self.MAXN      # 右子节点
        self.priority = [0.0] * self.MAXN # 优先级
        self.reverse = [False] * self.MAXN # 翻转标记
        random.seed(42)
        
        # 构建初始的有序树
        self.build(1, n)
    
    def update_size(self, i):
        if i != 0:
            self.size[i] = self.size[self.left[i]] + 1 + self.size[self.right[i]]
    
    def push_down(self, i):
        # 下传翻转标记
        if i != 0 and self.reverse[i]:
            # 交换左右子树
            self.left[i], self.right[i] = self.right[i], self.left[i]
            # 下传标记
            if self.left[i] != 0:
                self.reverse[self.left[i]] ^= True
            if self.right[i] != 0:
                self.reverse[self.right[i]] ^= True
            # 清除当前节点标记
            self.reverse[i] = False
    
    def build(self, l, r):
        # 递归构建平衡树
        if l > r:
            return 0
        mid = (l + r) // 2
        self.cnt += 1
        i = self.cnt
        self.key[i] = mid
        self.size[i] = r - l + 1
        self.priority[i] = random.random()
        self.left[i] = self.build(l, mid - 1)
        self.right[i] = self.build(mid + 1, r)
        return i
    
    def split(self, l, r, i, k):
        # 按大小分裂，将前k个元素分到左树
        self.push_down(i)
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            left_size = self.size[self.left[i]] + 1
            if left_size <= k:
                self.right[l] = i
                self.split(i, r, self.right[i], k - left_size)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], k)
            self.update_size(i)
    
    def merge(self, l, r):
        # 合并两个树
        if l == 0 or r == 0:
            return l + r
        self.push_down(l)
        self.push_down(r)
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.update_size(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.update_size(r)
            return r
    
    def reverse_range(self, l, r):
        # 翻转区间[l, r]
        self.split(0, 0, self.head, r)
        a = self.right[0]  # 前r个元素
        b = self.left[0]   # 后面的元素
        
        self.split(0, 0, a, l - 1)
        c = self.right[0]  # 中间需要翻转的部分
        d = self.left[0]   # 前l-1个元素
        
        # 打上翻转标记
        self.reverse[c] ^= True
        
        # 合并回去
        self.head = self.merge(self.merge(d, c), b)
    
    def inorder_traversal(self):
        # 中序遍历获取结果
        result = []
        self._inorder(self.head, result)
        return result
    
    def _inorder(self, i, result):
        if i == 0:
            return
        self.push_down(i)
        self._inorder(self.left[i], result)
        result.append(self.key[i])
        self._inorder(self.right[i], result)

# 主函数处理输入输出
class Main:
    def __init__(self):
        import sys
        input = sys.stdin.read().split()
        ptr = 0
        n = int(input[ptr])
        ptr += 1
        m = int(input[ptr])
        ptr += 1
        
        treap = FHQTreap(n)
        
        for _ in range(m):
            l = int(input[ptr])
            ptr += 1
            r = int(input[ptr])
            ptr += 1
            treap.reverse_range(l, r)
        
        # 输出结果
        print(' '.join(map(str, treap.inorder_traversal())))

if __name__ == "__main__":
    Main()
```

**C++实现**：
```cpp
#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

const int MAXN = 100010;

class FHQTreap {
private:
    int head, cnt;
    int key[MAXN], size_[MAXN], left_[MAXN], right_[MAXN];
    double priority[MAXN];
    bool reverse_[MAXN];
    
    void updateSize(int i) {
        if (i != 0) {
            size_[i] = size_[left_[i]] + 1 + size_[right_[i]];
        }
    }
    
    void pushDown(int i) {
        if (i != 0 && reverse_[i]) {
            swap(left_[i], right_[i]);
            if (left_[i] != 0) reverse_[left_[i]] ^= 1;
            if (right_[i] != 0) reverse_[right_[i]] ^= 1;
            reverse_[i] = false;
        }
    }
    
    int build(int l, int r) {
        if (l > r) return 0;
        int mid = (l + r) >> 1;
        int i = ++cnt;
        key[i] = mid;
        size_[i] = r - l + 1;
        priority[i] = (double)rand() / RAND_MAX;
        left_[i] = build(l, mid - 1);
        right_[i] = build(mid + 1, r);
        return i;
    }
    
    void split(int l, int r, int i, int k) {
        pushDown(i);
        if (i == 0) {
            right_[l] = left_[r] = 0;
        } else {
            int leftSize = size_[left_[i]] + 1;
            if (leftSize <= k) {
                right_[l] = i;
                split(i, r, right_[i], k - leftSize);
            } else {
                left_[r] = i;
                split(l, i, left_[i], k);
            }
            updateSize(i);
        }
    }
    
    int merge(int l, int r) {
        if (l == 0 || r == 0) return l + r;
        pushDown(l);
        pushDown(r);
        if (priority[l] >= priority[r]) {
            right_[l] = merge(right_[l], r);
            updateSize(l);
            return l;
        } else {
            left_[r] = merge(l, left_[r]);
            updateSize(r);
            return r;
        }
    }
    
    void inorder(int i, int* arr, int& ptr) {
        if (i == 0) return;
        pushDown(i);
        inorder(left_[i], arr, ptr);
        arr[ptr++] = key[i];
        inorder(right_[i], arr, ptr);
    }
    
public:
    FHQTreap(int n) {
        head = cnt = 0;
        memset(reverse_, 0, sizeof(reverse_));
        srand(42);
        head = build(1, n);
    }
    
    void reverseRange(int l, int r) {
        split(0, 0, head, r);
        int a = right_[0];
        int b = left_[0];
        
        split(0, 0, a, l - 1);
        int c = right_[0];
        int d = left_[0];
        
        reverse_[c] ^= 1;
        
        head = merge(merge(d, c), b);
    }
    
    void output(int* arr, int n) {
        int ptr = 0;
        inorder(head, arr, ptr);
        for (int i = 0; i < n; i++) {
            cout << arr[i] << (i == n-1 ? "\n" : " ");
        }
    }
};

int main() {
    int n, m;
    cin >> n >> m;
    FHQTreap treap(n);
    
    for (int i = 0; i < m; i++) {
        int l, r;
        cin >> l >> r;
        treap.reverseRange(l, r);
    }
    
    int* arr = new int[n];
    treap.output(arr, n);
    delete[] arr;
    
    return 0;
}
```

**时间复杂度**：每个操作的期望时间复杂度为 O(log n)
**空间复杂度**：O(n)，存储树节点

### 三、可持久化平衡树应用

#### 题目1：洛谷 P3835 可持久化平衡树

**题目链接**：https://www.luogu.com.cn/problem/P3835

**题目描述**：实现一个可持久化的平衡树，支持插入、删除、查询排名、查询第k小、前驱、后继等操作，并保存每个版本的树结构。

**题解**：FHQ-Treap非常适合实现可持久化，通过在修改操作时复制路径上的节点，保留历史版本。

**C++实现**：
```cpp
#include <iostream>
#include <cstdlib>
#include <ctime>
#include <algorithm>
using namespace std;

const int MAXN = 500010;
const int MAX_VERSIONS = 500010;

struct Node {
    int key, size, cnt, priority, left, right;
} tree[MAXN];

int root[MAX_VERSIONS], node_cnt, version_cnt;

int newNode(int key) {
    node_cnt++;
    tree[node_cnt].key = key;
    tree[node_cnt].size = 1;
    tree[node_cnt].cnt = 1;
    tree[node_cnt].priority = rand();
    tree[node_cnt].left = tree[node_cnt].right = 0;
    return node_cnt;
}

void updateSize(int p) {
    if (p) {
        tree[p].size = tree[tree[p].left].size + tree[tree[p].right].size + tree[p].cnt;
    }
}

void split(int p, int key, int &a, int &b) {
    if (!p) {
        a = b = 0;
        return;
    }
    if (tree[p].key <= key) {
        a = newNode(tree[p].key); // 复制当前节点
        tree[a] = tree[p]; // 复制节点信息
        split(tree[a].right, key, tree[a].right, b);
        updateSize(a);
    } else {
        b = newNode(tree[p].key); // 复制当前节点
        tree[b] = tree[p]; // 复制节点信息
        split(tree[b].left, key, a, tree[b].left);
        updateSize(b);
    }
}

int merge(int a, int b) {
    if (!a || !b) return a + b;
    if (tree[a].priority > tree[b].priority) {
        tree[a].right = merge(tree[a].right, b);
        updateSize(a);
        return a;
    } else {
        tree[b].left = merge(a, tree[b].left);
        updateSize(b);
        return b;
    }
}

void insert(int &now, int old, int key) {
    int a, b;
    split(old, key, a, b);
    int c = newNode(key);
    now = merge(merge(a, c), b);
}

void remove(int &now, int old, int key) {
    int a, b, c;
    split(old, key, a, b);
    split(a, key - 1, a, c);
    if (c) {
        if (tree[c].cnt > 1) {
            // 复制节点并减少计数
            int new_c = newNode(tree[c].key);
            tree[new_c] = tree[c];
            tree[new_c].cnt--;
            updateSize(new_c);
            c = new_c;
        } else {
            c = 0;
        }
    }
    now = merge(merge(a, c), b);
}

int findRank(int p, int key) {
    if (!p) return 0;
    if (tree[p].key == key) return tree[tree[p].left].size + 1;
    if (tree[p].key > key) return findRank(tree[p].left, key);
    return tree[tree[p].left].size + tree[p].cnt + findRank(tree[p].right, key);
}

int findKth(int p, int k) {
    if (!p) return 0;
    if (tree[tree[p].left].size >= k) return findKth(tree[p].left, k);
    if (tree[tree[p].left].size + tree[p].cnt >= k) return tree[p].key;
    return findKth(tree[p].right, k - tree[tree[p].left].size - tree[p].cnt);
}

int findPre(int p, int key) {
    if (!p) return -1e9;
    if (tree[p].key >= key) return findPre(tree[p].left, key);
    return max(tree[p].key, findPre(tree[p].right, key));
}

int findSuc(int p, int key) {
    if (!p) return 1e9;
    if (tree[p].key <= key) return findSuc(tree[p].right, key);
    return min(tree[p].key, findSuc(tree[p].left, key));
}

int main() {
    srand(time(0));
    int n;
    cin >> n;
    root[0] = 0;
    version_cnt = 0;
    
    for (int i = 1; i <= n; i++) {
        int v, op, x;
        cin >> v >> op >> x;
        version_cnt++;
        switch (op) {
            case 1:
                insert(root[version_cnt], root[v], x);
                break;
            case 2:
                remove(root[version_cnt], root[v], x);
                break;
            case 3:
                cout << findRank(root[v], x) << endl;
                root[version_cnt] = root[v]; // 不修改树，直接复制版本
                break;
            case 4:
                cout << findKth(root[v], x) << endl;
                root[version_cnt] = root[v];
                break;
            case 5:
                cout << findPre(root[v], x) << endl;
                root[version_cnt] = root[v];
                break;
            case 6:
                cout << findSuc(root[v], x) << endl;
                root[version_cnt] = root[v];
                break;
        }
    }
    
    return 0;
}
```

**时间复杂度**：每个操作的期望时间复杂度为 O(log n)
**空间复杂度**：O(n log n)，保存所有版本的树结构

### 四、其他平台经典题目

#### 题目1：Codeforces Round #600 (Div. 2) F. Animal Observation

**题目链接**：https://codeforces.com/contest/1253/problem/F

**题目描述**：这道题结合了动态规划和FHQ-Treap，用于优化区间查询和更新操作。

**题解**：使用FHQ-Treap维护区间最大值，加速动态规划转移。

#### 题目2：SPOJ COT - Count on a tree

**题目链接**：https://www.spoj.com/problems/COT/

**题目描述**：给定一棵树，多次查询路径u到v上的第k小元素。

**题解**：结合可持久化FHQ-Treap和树链剖分，实现高效的树上路径第k小查询。

#### 题目3：HDU 4006 The k-th great number

**题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4006

**题目描述**：维护一个动态集合，支持插入元素和查询第k大元素。

**题解**：使用FHQ-Treap的第k小查询功能，通过转化为第(n-k+1)小来实现第k大查询。

### 五、FHQ-Treap在工程中的应用

FHQ-Treap不仅在算法竞赛中有广泛应用，在实际工程中也有重要价值：

1. **数据库索引**：FHQ-Treap的平衡性和高效的查询性能使其适用于实现某些类型的数据库索引
2. **缓存管理**：通过维护访问顺序，实现LRU缓存策略
3. **区间管理系统**：如区间调度、资源分配等场景
4. **实时数据处理**：需要高效插入、删除和查询的数据处理场景

## 三种语言实现比较

| 语言 | 优势 | 劣势 | 性能特点 |
|------|------|------|----------|
| Python | 语法简洁，易读性高 | 递归深度限制，常数较大 | 适合小数据量，竞赛中可能会超时 |
| Java | 面向对象，线程安全，JVM优化 | 内存占用较大 | 中等性能，适合工程应用 |
| C++ | 性能最高，内存控制灵活 | 代码量较大，需要手动内存管理 | 竞赛首选，高性能工程应用 |

## 工程化考量

### 1. 异常处理
- **输入验证**：确保所有操作的输入参数在有效范围内
- **空树处理**：处理树为空时的各种边界情况
- **错误日志**：记录操作过程中的异常情况

### 2. 性能优化
- **内存池**：预分配节点空间，减少动态内存分配开销
- **非递归实现**：避免大递归深度导致的栈溢出问题
- **常数优化**：内联函数、减少不必要的计算等

### 3. 线程安全
- **锁机制**：在多线程环境下保护共享的FHQ-Treap数据结构
- **无锁算法**：设计线程安全的操作实现

### 4. 可扩展性
- **模板/泛型**：支持多种数据类型
- **复合操作**：封装常见的复合操作，提高使用便捷性

### 5. 测试与验证
- **单元测试**：覆盖各种边界情况和常见操作
- **性能测试**：在不同规模数据下测试性能表现
- **正确性证明**：通过数学归纳法证明算法的正确性

## 从算法到工程的迁移

将FHQ-Treap从算法竞赛应用到实际工程时，需要考虑以下几点：

1. **数据规模适应性**：竞赛算法通常针对特定规模优化，工程中需要处理更广泛的数据规模
2. **鲁棒性提升**：增加更多的错误处理和异常情况的考虑
3. **接口设计**：设计清晰、易用的API，隐藏内部实现细节
4. **文档完善**：提供详细的使用说明和性能特性文档
5. **持续维护**：建立测试用例库，确保后续修改不会破坏现有功能

## 时间复杂度

所有操作的期望时间复杂度均为 O(log n)。

## 空间复杂度

空间复杂度为 O(n)，其中 n 为节点数量。对于可持久化版本，空间复杂度为 O(n log n)。

## 算法优势

1. **实现简单**：相比于其他平衡树，FHQ-Treap的实现更加简洁，只需要掌握分裂和合并两个核心操作
2. **无需旋转**：避免了复杂的旋转操作，代码更容易编写和调试
3. **支持可持久化**：由于分裂和合并操作的特性，FHQ-Treap天然支持可持久化
4. **支持区间操作**：通过维护子树大小，可以方便地进行区间操作
5. **随机平衡**：通过随机优先级保证树的平衡性，避免最坏情况

## 学习要点

1. **分裂和合并操作的正确实现**：这是FHQ-Treap的核心
2. **懒惰标记的应用**：用于处理区间操作
3. **可持久化技巧**：在修改时复制路径上的节点
4. **词频压缩技术**：优化相同值节点的存储
5. **边界条件处理**：如空树的情况，分裂点的选择等
6. **性能优化**：如非递归实现、内存池等

## 常见题型总结

1. **普通平衡树问题**：直接应用FHQ-Treap的基本操作
2. **区间操作问题**：结合懒惰标记实现
3. **可持久化问题**：实现版本保存和回溯
4. **动态维护问题**：如动态维护第k小、前驱后继等
5. **复杂数据结构组合**：与其他数据结构结合解决问题

## 注意事项

1. **随机种子的选择**：为了保证树的平衡性，需要使用合适的随机数生成器
2. **内存管理**：对于大规模数据，需要考虑内存分配效率
3. **操作顺序**：分裂和合并的顺序会影响最终结果
4. **边界条件**：需要仔细处理各种边界情况
5. **常数优化**：在时间敏感的场景下，需要进行常数优化

## 与其他平衡树的比较

1. **相比于Splay树**：FHQ-Treap常数略大，但实现更简单，支持可持久化
2. **相比于AVL树**：FHQ-Treap不需要维护平衡因子，实现更简单
3. **相比于红黑树**：FHQ-Treap逻辑更清晰，代码量更小
4. **相比于SGT**：FHQ-Treap在某些动态问题上更有优势

FHQ-Treap作为一种强大而灵活的数据结构，在算法竞赛和实际工程中都有广泛的应用。掌握它的实现和应用，对于解决各种复杂的数据结构问题非常有帮助。