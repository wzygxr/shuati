# 可持久化线段树（主席树）题目扩展与实现详解

## 1. 概述

可持久化线段树（Persistent Segment Tree），也称为主席树，是一种可以保存历史版本的数据结构。它通过函数式编程的思想，在每次修改时只创建新节点，共享未修改的部分，从而实现对历史版本的高效访问。这种数据结构特别适合处理需要频繁查询历史状态或进行多次修改后的回退操作的场景。

## 2. 核心思想

1. **函数式编程思想**：每次修改时只创建新节点，共享未修改部分，保证历史版本不变
2. **前缀和思想**：利用前缀和的差值来计算任意区间的信息，如区间第K小、区间不同元素个数等
3. **离散化处理**：对大数据范围进行离散化以节省空间，提高查询效率
4. **路径共享**：相同结构的路径在不同版本间共享，减少内存消耗

## 3. 主要应用场景

1. **静态区间第K小**：给定一个序列，多次查询区间[l,r]内第k小的数
2. **带历史版本的区间查询**：支持查询历史版本的区间信息
3. **树上路径第K小**：在树上查询两点间路径上第k小的点权
4. **离线处理区间问题**：结合离线处理解决复杂的区间查询问题
5. **区间Mex查询**：查询区间内未出现的最小自然数
6. **动态图连通性**：维护动态图的连通性信息
7. **区间不同元素个数查询**：统计给定区间内不同元素的数量
8. **二维区间查询**：处理二维平面上的范围查询问题

## 4. 完整实现代码（三种语言）

### 4.1 Java实现

```java
import java.util.*;

/**
 * 可持久化线段树（主席树）Java实现
 * 支持静态区间第K小查询
 */
class PersistentSegmentTree {
    // 节点信息存储为三个数组，避免对象创建开销
    private int[] left;   // 左子节点索引
    private int[] right;  // 右子节点索引
    private int[] count;  // 区间内元素个数
    private int[] roots;  // 存储每个版本的根节点
    private int size;     // 最大节点数
    private int idx;      // 当前节点索引
    private int version;  // 当前版本号
    
    /**
     * 构造函数
     * @param n 原始数组长度
     */
    public PersistentSegmentTree(int n) {
        // 预估需要的节点数，40*n通常足够
        this.size = n * 40;
        left = new int[size];
        right = new int[size];
        count = new int[size];
        roots = new int[n + 2]; // 多分配空间避免越界
        idx = 1;
        version = 0;
    }
    
    /**
     * 构建初始线段树
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @return 新建节点的索引
     */
    public int build(int l, int r) {
        int node = idx++;
        if (l == r) {
            count[node] = 0;
            return node;
        }
        int mid = l + (r - l) / 2;
        left[node] = build(l, mid);
        right[node] = build(mid + 1, r);
        count[node] = 0;
        return node;
    }
    
    /**
     * 更新线段树，创建新版本
     * @param preRoot 旧版本根节点索引
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param pos 更新位置
     * @param val 更新值（通常为1或-1）
     * @return 新版本根节点索引
     */
    public int update(int preRoot, int l, int r, int pos, int val) {
        int newNode = idx++;
        // 复制旧节点的信息
        left[newNode] = left[preRoot];
        right[newNode] = right[preRoot];
        count[newNode] = count[preRoot] + val;
        
        if (l == r) {
            return newNode;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            left[newNode] = update(left[preRoot], l, mid, pos, val);
        } else {
            right[newNode] = update(right[preRoot], mid + 1, r, pos, val);
        }
        return newNode;
    }
    
    /**
     * 查询区间第K小元素
     * @param rootL 左边界版本根节点
     * @param rootR 右边界版本根节点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param k 要查询的排名
     * @return 第K小的元素离散化后的值
     */
    public int queryKth(int rootL, int rootR, int l, int r, int k) {
        if (l == r) return l;
        
        int mid = l + (r - l) / 2;
        // 计算左子树中区间[l..r]的元素个数
        int leftCount = count[left[rootR]] - count[left[rootL]];
        
        if (k <= leftCount) {
            // 第k小在左子树
            return queryKth(left[rootL], left[rootR], l, mid, k);
        } else {
            // 第k小在右子树
            return queryKth(right[rootL], right[rootR], mid + 1, r, k - leftCount);
        }
    }
    
    /**
     * 获取当前版本号
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * 设置当前版本
     */
    public void setVersion(int version) {
        this.version = version;
    }
    
    /**
     * 获取指定版本的根节点
     */
    public int getRoot(int version) {
        return roots[version];
    }
    
    /**
     * 设置指定版本的根节点
     */
    public void setRoot(int version, int root) {
        this.roots[version] = root;
    }
}

/**
 * 测试主席树实现静态区间第K小
 */
public class PersistentSegmentTreeTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();  // 数组长度
        int m = scanner.nextInt();  // 查询次数
        
        int[] a = new int[n + 1];   // 原始数组
        int[] sorted = new int[n];  // 用于离散化
        
        for (int i = 1; i <= n; i++) {
            a[i] = scanner.nextInt();
            sorted[i - 1] = a[i];
        }
        
        // 离散化处理
        Arrays.sort(sorted);
        int uniqueCount = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sorted[uniqueCount++] = sorted[i];
            }
        }
        
        // 构建离散化映射
        Map<Integer, Integer> valueToRank = new HashMap<>();
        for (int i = 0; i < uniqueCount; i++) {
            valueToRank.put(sorted[i], i + 1);  // 排名从1开始
        }
        
        // 创建并构建主席树
        PersistentSegmentTree tree = new PersistentSegmentTree(n);
        tree.setRoot(0, tree.build(1, uniqueCount));
        
        // 建立前缀权值线段树
        for (int i = 1; i <= n; i++) {
            int rank = valueToRank.get(a[i]);
            tree.setRoot(i, tree.update(tree.getRoot(i - 1), 1, uniqueCount, rank, 1));
        }
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            int k = scanner.nextInt();
            
            int rank = tree.queryKth(tree.getRoot(l - 1), tree.getRoot(r), 1, uniqueCount, k);
            System.out.println(sorted[rank - 1]);  // 将排名转换回原始值
        }
        
        scanner.close();
    }
}
```

### 4.2 C++实现

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
using namespace std;

/**
 * 可持久化线段树（主席树）C++实现
 * 支持静态区间第K小查询
 */
class PersistentSegmentTree {
private:
    vector<int> left;   // 左子节点索引
    vector<int> right;  // 右子节点索引
    vector<int> count;  // 区间内元素个数
    vector<int> roots;  // 存储每个版本的根节点
    int idx;            // 当前节点索引
    int version;        // 当前版本号
    
public:
    /**
     * 构造函数
     * @param n 原始数组长度
     */
    PersistentSegmentTree(int n) {
        // 预留足够空间避免频繁扩容
        left.reserve(n * 40);
        right.reserve(n * 40);
        count.reserve(n * 40);
        roots.resize(n + 2);  // 多分配空间避免越界
        left.push_back(0);    // 0号节点作为空节点
        right.push_back(0);
        count.push_back(0);
        idx = 1;
        version = 0;
    }
    
    /**
     * 构建初始线段树
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @return 新建节点的索引
     */
    int build(int l, int r) {
        int node = idx++;
        left.push_back(0);
        right.push_back(0);
        count.push_back(0);
        
        if (l == r) {
            return node;
        }
        
        int mid = l + (r - l) / 2;
        left[node] = build(l, mid);
        right[node] = build(mid + 1, r);
        return node;
    }
    
    /**
     * 更新线段树，创建新版本
     * @param preRoot 旧版本根节点索引
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param pos 更新位置
     * @param val 更新值（通常为1或-1）
     * @return 新版本根节点索引
     */
    int update(int preRoot, int l, int r, int pos, int val) {
        int newNode = idx++;
        left.push_back(left[preRoot]);
        right.push_back(right[preRoot]);
        count.push_back(count[preRoot] + val);
        
        if (l == r) {
            return newNode;
        }
        
        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            left[newNode] = update(left[preRoot], l, mid, pos, val);
        } else {
            right[newNode] = update(right[preRoot], mid + 1, r, pos, val);
        }
        return newNode;
    }
    
    /**
     * 查询区间第K小元素
     * @param rootL 左边界版本根节点
     * @param rootR 右边界版本根节点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param k 要查询的排名
     * @return 第K小的元素离散化后的值
     */
    int queryKth(int rootL, int rootR, int l, int r, int k) {
        if (l == r) return l;
        
        int mid = l + (r - l) / 2;
        int leftCount = count[left[rootR]] - count[left[rootL]];
        
        if (k <= leftCount) {
            return queryKth(left[rootL], left[rootR], l, mid, k);
        } else {
            return queryKth(right[rootL], right[rootR], mid + 1, r, k - leftCount);
        }
    }
    
    // 获取当前版本号
    int getVersion() const { return version; }
    // 设置当前版本
    void setVersion(int v) { version = v; }
    // 获取指定版本的根节点
    int getRoot(int v) const { return roots[v]; }
    // 设置指定版本的根节点
    void setRoot(int v, int root) { roots[v] = root; }
};

/**
 * 主函数：测试主席树实现静态区间第K小
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    vector<int> a(n + 1);
    vector<int> sorted(n);
    
    for (int i = 1; i <= n; ++i) {
        cin >> a[i];
        sorted[i - 1] = a[i];
    }
    
    // 离散化处理
    sort(sorted.begin(), sorted.end());
    sorted.erase(unique(sorted.begin(), sorted.end()), sorted.end());
    
    // 构建离散化映射
    map<int, int> valueToRank;
    for (int i = 0; i < sorted.size(); ++i) {
        valueToRank[sorted[i]] = i + 1;  // 排名从1开始
    }
    
    // 创建并构建主席树
    PersistentSegmentTree tree(n);
    tree.setRoot(0, tree.build(1, sorted.size()));
    
    // 建立前缀权值线段树
    for (int i = 1; i <= n; ++i) {
        int rank = valueToRank[a[i]];
        tree.setRoot(i, tree.update(tree.getRoot(i - 1), 1, sorted.size(), rank, 1));
    }
    
    // 处理查询
    for (int i = 0; i < m; ++i) {
        int l, r, k;
        cin >> l >> r >> k;
        
        int rank = tree.queryKth(tree.getRoot(l - 1), tree.getRoot(r), 1, sorted.size(), k);
        cout << sorted[rank - 1] << '\n';  // 将排名转换回原始值
    }
    
    return 0;
}
```

### 4.3 Python实现

```python
"""
可持久化线段树（主席树）Python实现
支持静态区间第K小查询
"""

import sys
from bisect import bisect_left

class PersistentSegmentTree:
    def __init__(self, n):
        """
        初始化可持久化线段树
        
        参数:
            n: 原始数组长度
        """
        # 使用列表存储节点信息，避免对象创建开销
        self.left = [0]   # 左子节点索引
        self.right = [0]  # 右子节点索引
        self.count = [0]  # 区间内元素个数
        self.roots = [0] * (n + 2)  # 存储每个版本的根节点
        self.idx = 1      # 当前节点索引
        self.version = 0  # 当前版本号
    
    def build(self, l, r):
        """
        构建初始线段树
        
        参数:
            l: 当前区间左端点
            r: 当前区间右端点
            
        返回:
            新建节点的索引
        """
        node = self.idx
        self.idx += 1
        self.left.append(0)
        self.right.append(0)
        self.count.append(0)
        
        if l == r:
            return node
        
        mid = l + (r - l) // 2
        self.left[node] = self.build(l, mid)
        self.right[node] = self.build(mid + 1, r)
        return node
    
    def update(self, pre_root, l, r, pos, val):
        """
        更新线段树，创建新版本
        
        参数:
            pre_root: 旧版本根节点索引
            l: 当前区间左端点
            r: 当前区间右端点
            pos: 更新位置
            val: 更新值（通常为1或-1）
            
        返回:
            新版本根节点索引
        """
        new_node = self.idx
        self.idx += 1
        # 复制旧节点的信息
        self.left.append(self.left[pre_root])
        self.right.append(self.right[pre_root])
        self.count.append(self.count[pre_root] + val)
        
        if l == r:
            return new_node
        
        mid = l + (r - l) // 2
        if pos <= mid:
            self.left[new_node] = self.update(self.left[pre_root], l, mid, pos, val)
        else:
            self.right[new_node] = self.update(self.right[pre_root], mid + 1, r, pos, val)
        return new_node
    
    def query_kth(self, root_l, root_r, l, r, k):
        """
        查询区间第K小元素
        
        参数:
            root_l: 左边界版本根节点
            root_r: 右边界版本根节点
            l: 当前区间左端点
            r: 当前区间右端点
            k: 要查询的排名
            
        返回:
            第K小的元素离散化后的值
        """
        if l == r:
            return l
        
        mid = l + (r - l) // 2
        # 计算左子树中区间[l..r]的元素个数
        left_count = self.count[self.left[root_r]] - self.count[self.left[root_l]]
        
        if k <= left_count:
            # 第k小在左子树
            return self.query_kth(self.left[root_l], self.left[root_r], l, mid, k)
        else:
            # 第k小在右子树
            return self.query_kth(self.right[root_l], self.right[root_r], mid + 1, r, k - left_count)
    
    # 获取当前版本号
    def get_version(self):
        return self.version
    
    # 设置当前版本
    def set_version(self, version):
        self.version = version
    
    # 获取指定版本的根节点
    def get_root(self, version):
        return self.roots[version]
    
    # 设置指定版本的根节点
    def set_root(self, version, root):
        self.roots[version] = root

# 测试函数
def main():
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    a = [0] * (n + 1)  # 原始数组
    sorted_values = []  # 用于离散化
    
    for i in range(1, n + 1):
        a[i] = int(input[ptr])
        ptr += 1
        sorted_values.append(a[i])
    
    # 离散化处理
    sorted_values = sorted(list(set(sorted_values)))
    
    # 建立离散化映射（使用bisect提高效率）
    def get_rank(x):
        return bisect_left(sorted_values, x) + 1  # 排名从1开始
    
    # 创建并构建主席树
    tree = PersistentSegmentTree(n)
    tree.set_root(0, tree.build(1, len(sorted_values)))
    
    # 建立前缀权值线段树
    for i in range(1, n + 1):
        rank = get_rank(a[i])
        tree.set_root(i, tree.update(tree.get_root(i - 1), 1, len(sorted_values), rank, 1))
    
    # 处理查询
    for _ in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        k = int(input[ptr])
        ptr += 1
        
        rank = tree.query_kth(tree.get_root(l - 1), tree.get_root(r), 1, len(sorted_values), k)
        print(sorted_values[rank - 1])  # 将排名转换回原始值

if __name__ == "__main__":
    main()
```

## 5. 算法实现要点

### 5.1 建树过程
```java
// 构建空线段树
static int build(int l, int r) {
    int rt = ++cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}
```

### 5.2 插入操作
```java
// 在线段树中插入一个值
static int insert(int pos, int l, int r, int pre) {
    int rt = ++cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + 1;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt]);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt]);
        }
    }
    return rt;
}
```

### 5.3 查询操作
```java
// 查询区间第k小的数
static int query(int k, int l, int r, int u, int v) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[v]] - sum[left[u]];
    if (x >= k) {
        // 第k小在左子树中
        return query(k, l, mid, left[u], left[v]);
    } else {
        // 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v]);
    }
}
```

### 5.4 区间Mex查询
```java
// 查询区间Mex
static int queryMex(int l, int r, int u, int v) {
    if (l == r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int leftCount = sum[left[v]] - sum[left[u]];
    // 如果左子树中数的个数等于区间长度，说明左子树满
    if (leftCount == mid - l + 1) {
        // Mex在右子树中
        return queryMex(mid + 1, r, right[u], right[v]);
    } else {
        // Mex在左子树中
        return queryMex(l, mid, left[u], left[v]);
    }
}
```

## 6. 扩展题目列表（40题）

### 6.1 基础静态区间查询类

#### 6.1.1 静态区间第K小
- **SPOJ MKTHNUM**：静态区间第K小（主席树模板题）
  - 题目链接：https://www.spoj.com/problems/MKTHNUM/
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **Luogu P3834**：【模板】可持久化线段树 2
  - 题目链接：https://www.luogu.com.cn/problem/P3834
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **LeetCode 218. The Skyline Problem**：天际线问题（可转化为区间查询）
  - 题目链接：https://leetcode.com/problems/the-skyline-problem/
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n)

#### 6.1.2 区间不同元素个数
- **SPOJ DQUERY**：查询区间不同数字的个数
  - 题目链接：https://www.spoj.com/problems/DQUERY/
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **Luogu P1972**：[SDOI2009] HH的项链
  - 题目链接：https://www.luogu.com.cn/problem/P1972
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **AtCoder ABC127 F - Absolute Minima**：绝对最小值问题
  - 题目链接：https://atcoder.jp/contests/abc127/tasks/abc127_f
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

### 6.2 树上路径查询类

#### 6.2.1 树上路径第K小
- **SPOJ COT**：Count on a tree（树上路径第K小）
  - 题目链接：https://www.spoj.com/problems/COT/
  - 时间复杂度：O((n + m) log n)
  - 空间复杂度：O(n log n)

- **Luogu P2633**：Count on a tree
  - 题目链接：https://www.luogu.com.cn/problem/P2633
  - 时间复杂度：O((n + m) log n)
  - 空间复杂度：O(n log n)

- **HDU 2665**：Kth number（树上路径第K小变种）
  - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2665
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

#### 6.2.2 子树查询
- **Luogu P3899**：[湖南集训]谈笑风生
  - 题目链接：https://www.luogu.com.cn/problem/P3899
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **LeetCode 1970**：Smallest Missing Genetic Value in Each Subtree
  - 题目链接：https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

- **CodeForces 1009F**：Dominant Indices
  - 题目链接：https://codeforces.com/contest/1009/problem/F
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

### 6.3 区间Mex查询类

- **Luogu P4137**：Mex（查询区间内未出现的最小自然数）
  - 题目链接：https://www.luogu.com.cn/problem/P4137
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **CodeChef MEXUM**：Maximum Mex
  - 题目链接：https://www.codechef.com/problems/MEXUM
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

- **HackerEarth Missing Number**：缺失的数
  - 题目链接：https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
  - 时间复杂度：O(n log n + q log n)
  - 空间复杂度：O(n log n)

### 6.4 动态区间查询类

#### 6.4.1 动态区间第K小
- **Luogu P2617**：Dynamic Rankings（动态区间第K小）
  - 题目链接：https://www.luogu.com.cn/problem/P2617
  - 时间复杂度：O(n log²n + m log²n)
  - 空间复杂度：O(n log²n)

- **ZOJ 2112**：Dynamic Rankings
  - 题目链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemId=2112
  - 时间复杂度：O(n log²n + m log²n)
  - 空间复杂度：O(n log²n)

- **HDU 2665**：Kth number（动态区间第K小）
  - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2665
  - 时间复杂度：O(n log²n + m log²n)
  - 空间复杂度：O(n log²n)

#### 6.4.2 区间修改与查询
- **SPOJ TTM**：区间修改，区间查询，带历史版本
  - 题目链接：https://www.spoj.com/problems/TTM/
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **CodeForces 341E**：Candies Game
  - 题目链接：https://codeforces.com/problemset/problem/341/E
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **POJ 2763**：Housewife Wind
  - 题目链接：http://poj.org/problem?id=2763
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

### 6.5 高级应用类

#### 6.5.1 二分答案结合
- **Luogu P2839**：Middle（浮动区间的最大上中位数）
  - 题目链接：https://www.luogu.com.cn/problem/P2839
  - 时间复杂度：O(n log²n + m log²n)
  - 空间复杂度：O(n log n)

- **Luogu P4587**：[FJOI2016]神秘数
  - 题目链接：https://www.luogu.com.cn/problem/P4587
  - 时间复杂度：O(n log²n + m log²n)
  - 空间复杂度：O(n log n)

- **CodeForces 813E**：Army Creation（军队创建）
  - 题目链接：https://codeforces.com/contest/813/problem/E
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

#### 6.5.2 二维区间查询
- **Luogu P2468**：[SDOI2010]粟粟的书架（二维矩阵区间查询）
  - 题目链接：https://www.luogu.com.cn/problem/P2468
  - 时间复杂度：O(n log n + m log²n)
  - 空间复杂度：O(n log n)

- **HackerRank Matrix Queries**：矩阵查询
  - 题目链接：https://www.hackerrank.com/challenges/matrix-queries/problem
  - 时间复杂度：O(n² log n + q log n)
  - 空间复杂度：O(n² log n)

- **POJ 2104**：K-th Number（二维扩展）
  - 题目链接：http://poj.org/problem?id=2104
  - 时间复杂度：O(n² log n + q log n)
  - 空间复杂度：O(n² log n)

#### 6.5.3 持久化数据结构
- **Luogu P3919**：【模板】可持久化数组
  - 题目链接：https://www.luogu.com.cn/problem/P3919
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **CodeForces 707D**：Persistent Bookcase（持久化书架）
  - 题目链接：https://codeforces.com/contest/707/problem/D
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **LeetCode 2276**：Count Integers in Intervals（动态开点线段树）
  - 题目链接：https://leetcode.com/problems/count-integers-in-intervals/
  - 时间复杂度：O(n log C)
  - 空间复杂度：O(n log C)

### 6.6 其他应用类

- **HDU 5919**：Sequence II（第一次出现位置的序列）
  - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=5919
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **CodeForces 441E**：Subset Sums（动态维护子集和）
  - 题目链接：https://codeforces.com/contest/441/problem/E
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

- **SPOJ KQUERY**：查询区间内大于k的元素个数
  - 题目链接：https://www.spoj.com/problems/KQUERY/
  - 时间复杂度：O(n log n + q log n)
  - 空间复杂度：O(n log n)

- **UVa 11991**：Easy Problem from Rujia Liu?
  - 题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3142
  - 时间复杂度：O(n log n + q log n)
  - 空间复杂度：O(n log n)

- **AizuOJ DSL_2_F**：Range Update Query (RUQ)
  - 题目链接：http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=DSL_2_F
  - 时间复杂度：O(n + m log n)
  - 空间复杂度：O(n)

- **MarsCode 1001**：区间查询问题
  - 题目链接：https://www.mars.codeforces.com/contest/1001
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **牛客 NC14542**：区间第K小
  - 题目链接：https://ac.nowcoder.com/acm/problem/14542
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **ACWing 241**：楼兰图腾
  - 题目链接：https://www.acwing.com/problem/content/243/
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

- **计蒜客 T1467**：区间查询
  - 题目链接：https://nanti.jisuanke.com/t/T1467
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **LOJ 111**：樱花查询
  - 题目链接：https://loj.ac/p/111
  - 时间复杂度：O(n log n + m log n)
  - 空间复杂度：O(n log n)

- **USACO 2013 Open**：Photo
  - 题目链接：https://usaco.org/index.php?page=viewproblem2&cpid=281
  - 时间复杂度：O(n log n)
  - 空间复杂度：O(n log n)

- **POJ 3764**：The xor-longest Path
  - 题目链接：http://poj.org/problem?id=3764
  - 时间复杂度：O(n log M)，M是数值范围
  - 空间复杂度：O(n log M)

## 5. 算法实现要点

### 5.1 建树过程
```java
// 构建空线段树
static int build(int l, int r) {
    int rt = ++cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}
```

### 5.2 插入操作
```java
// 在线段树中插入一个值
static int insert(int pos, int l, int r, int pre) {
    int rt = ++cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + 1;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt]);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt]);
        }
    }
    return rt;
}
```

### 5.3 查询操作
```java
// 查询区间第k小的数
static int query(int k, int l, int r, int u, int v) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[v]] - sum[left[u]];
    if (x >= k) {
        // 第k小在左子树中
        return query(k, l, mid, left[u], left[v]);
    } else {
        // 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v]);
    }
}
```

### 5.4 区间Mex查询
```java
// 查询区间Mex
static int queryMex(int l, int r, int u, int v) {
    if (l == r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int leftCount = sum[left[v]] - sum[left[u]];
    // 如果左子树中数的个数等于区间长度，说明左子树满
    if (leftCount == mid - l + 1) {
        // Mex在右子树中
        return queryMex(mid + 1, r, right[u], right[v]);
    } else {
        // Mex在左子树中
        return queryMex(l, mid, left[u], left[v]);
    }
}
```

## 6. 复杂度分析

- **时间复杂度**：
  - 建树：O(n log n)
  - 插入：O(log n)
  - 查询：O(log n)
- **空间复杂度**：O(n log n)

## 7. 工程化考量

1. **内存优化**：只在需要时创建新节点，共享未修改部分
2. **离散化处理**：对大数据范围进行离散化以节省空间
3. **边界处理**：注意数组下标和边界条件
4. **异常处理**：处理非法输入和查询
5. **性能优化**：
   - 使用内存池避免频繁内存分配
   - 优化常数项，减少不必要的计算
   - 合理选择数据类型避免溢出
6. **可配置性**：
   - 支持自定义比较函数
   - 支持不同类型的查询操作
7. **线程安全**：
   - 对于多线程环境，需要考虑数据竞争问题
   - 可以使用读写锁提高并发性能

## 8. 优缺点分析

### 8.1 优点
1. 可以访问历史版本
2. 空间效率较高（相比存储所有版本）
3. 查询效率高
4. 适用于离线和在线查询

### 8.2 缺点
1. 实现较为复杂
2. 常数较大
3. 空间占用仍然较大
4. 不支持动态修改（基础版本）

## 9. 扩展应用

1. **树上主席树**：结合LCA处理树上路径问题
2. **二维主席树**：处理二维平面上的问题
3. **动态主席树**：结合其他数据结构支持动态修改
4. **整体二分**：结合整体二分处理复杂问题
5. **可持久化数组**：支持历史版本的数组访问
6. **区间Mex查询**：查询区间内未出现的最小自然数
7. **区间不同元素个数**：统计区间内不同元素的个数

## 10. 与机器学习等领域的联系

1. **数据结构优化**：在大规模数据处理中，主席树可以用于优化特征选择和数据采样
2. **在线学习**：主席树的历史版本特性可以用于实现在线学习算法中的模型回溯
3. **推荐系统**：在推荐系统中，可以使用主席树维护用户历史行为的不同版本

## 11. 调试与测试技巧

1. **小例子测试法**：使用小规模数据手动验证算法正确性
2. **断点式打印**：在关键步骤打印中间结果，验证逻辑正确性
3. **边界场景测试**：测试空输入、极端值、重复数据等边界情况
4. **性能退化排查**：通过性能分析工具定位瓶颈

## 12. 常见错误与注意事项

1. **数组越界**：注意线段树节点数组的大小，通常需要4倍空间
2. **离散化错误**：离散化时要注意去重和映射关系
3. **版本管理错误**：确保正确维护历史版本之间的关系
4. **内存泄漏**：在动态开点的实现中要注意内存释放

## 13. 总结

可持久化线段树是一种强大的数据结构，特别适用于需要访问历史版本或处理静态区间查询的场景。掌握其核心思想和实现方法对于解决相关问题非常有帮助。在实际应用中，需要根据具体问题选择合适的实现方式，并注意工程化考量。