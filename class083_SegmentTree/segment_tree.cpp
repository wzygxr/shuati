/**
 * C++ 线段树实现 - 支持区间加法和区间求和
 * 适用于 LeetCode 307. Range Sum Query - Mutable 类问题
 * 
 * 线段树是一种强大的树形数据结构，专门用于高效处理区间查询和更新操作。
 * 本实现包含两个版本：
 * 1. 基础线段树：支持单点更新和区间求和查询
 * 2. 带懒标记的线段树：支持区间加法更新和区间求和查询
 * 
 * 时间复杂度分析：
 * - 建树：O(n)
 * - 单点更新：O(log n)
 * - 区间更新（使用懒标记）：O(log n)
 * - 区间查询：O(log n)
 * 空间复杂度：O(n) - 使用4*n大小的数组存储线段树节点和懒标记
 * 
 * 线段树适用场景：
 * - 频繁的区间查询操作（如区间和、最大值、最小值等）
 * - 需要高效更新的动态数组查询
 * - 离线和在线区间数据处理
 * 
 * 题目来源：
 * - LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
 * - HDU 1166. 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
 * - HDU 1754. I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
 * - Luogu P3372. 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
 * - Codeforces 339D. Xor - https://codeforces.com/problemset/problem/339/D
 * - SPOJ GSS1. Can you answer these queries I - https://www.spoj.com/problems/GSS1/
 * - Codeforces 52C. Circular RMQ - https://codeforces.com/problemset/problem/52/C
 * - AtCoder ABC183E. Queen on Grid - https://atcoder.jp/contests/abc183/tasks/abc183_e
 * - USACO 2016 Jan Silver. Subsequences Summing to Sevens - http://www.usaco.org/index.php?page=viewproblem2&cpid=595
 * - 牛客 NC14417. 线段树练习 - https://ac.nowcoder.com/acm/problem/14417
 * - 杭电多校 HDU6514. Monitor - http://acm.hdu.edu.cn/showproblem.php?pid=6514
 * - AizuOJ ALDS1_9_C. Segment Tree - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_9_C
 */

#include <iostream>
#include <vector>
using namespace std;

/**
 * 线段树类
 * 线段树是一种基于分治思想的二叉树数据结构，非常适合处理区间查询和更新操作。
 * 本实现支持区间加法和区间求和查询，是线段树最基础也是最常用的形式。
 */
class SegmentTree {
private:
    vector<int> tree;  // 线段树数组，存储每个区间的和
    vector<int> arr;   // 原始数组的副本
    int n;             // 数组长度

    /**
     * 向上更新节点值
     * 时间复杂度：O(1)
     * @param i 当前节点索引
     * 功能：将左右子节点的值合并到当前节点，是线段树自底向上传递信息的核心操作
     * 在每次递归调用返回时执行，确保父节点保存了子节点的正确聚合信息
     */
    void pushUp(int i) {
        tree[i] = tree[i << 1] + tree[i << 1 | 1];
    }

    /**
     * 构建线段树
     * 时间复杂度：O(n)
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     * 构建过程：
     * 1. 递归地将区间分割为左右两部分，直到叶子节点（区间长度为1）
     * 2. 叶子节点直接存储对应数组元素的值
     * 3. 自底向上合并子节点信息，构建父节点
     */
    void build(int l, int r, int i) {
        if (l == r) {
            // 叶节点，直接赋值
            tree[i] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        // 递归构建左右子树
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        // 向上合并信息
        pushUp(i);
    }

    /**
     * 更新单点
     * 时间复杂度：O(log n)
     * @param index 要更新的数组索引
     * @param val 新的值
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     * 更新过程：
     * 1. 递归向下查找目标叶子节点
     * 2. 更新叶子节点的值
     * 3. 递归返回时，自底向上更新受影响的所有祖先节点
     */
    void update(int index, int val, int l, int r, int i) {
        if (l == r) {
            // 找到目标叶节点，直接更新
            tree[i] = val;
            return;
        }
        int mid = (l + r) >> 1;
        // 根据索引决定更新左子树还是右子树
        if (index <= mid)
            update(index, val, l, mid, i << 1);
        else
            update(index, val, mid + 1, r, i << 1 | 1);
        // 更新当前节点的值
        pushUp(i);
    }

    /**
     * 查询区间和
     * 时间复杂度：O(log n)
     * @param jobl 查询区间左边界
     * @param jobr 查询区间右边界
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     * @return 查询区间的和
     * 查询过程：
     * 1. 如果当前区间完全包含在查询区间内，直接返回当前节点的值
     * 2. 否则，将查询分解到左右子树，并合并结果
     * 这种分治策略确保了查询操作的高效性
     */
    int query(int jobl, int jobr, int l, int r, int i) {
        // 如果当前区间完全包含在查询区间内
        if (jobl <= l && r <= jobr) {
            return tree[i];
        }
        int mid = (l + r) >> 1;
        int ans = 0;
        // 递归查询左子区间
        if (jobl <= mid)
            ans += query(jobl, jobr, l, mid, i << 1);
        // 递归查询右子区间
        if (jobr > mid)
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        return ans;
    }

public:
    /**
     * 构造函数
     * @param nums 输入数组
     */
    SegmentTree(vector<int>& nums) {
        n = nums.size();
        arr = nums;
        tree.resize(4 * n, 0);  // 分配4倍空间以保证足够
        build(0, n - 1, 1);     // 构建线段树，根节点索引为1
    }

    /**
     * 更新操作 - 对外接口
     * @param index 要更新的数组索引
     * @param val 新的值
     */
    void update(int index, int val) {
        arr[index] = val;  // 同时更新原始数组副本
        update(index, val, 0, n - 1, 1);
    }

    /**
     * 查询操作 - 对外接口
     * @param left 查询区间左边界
     * @param right 查询区间右边界
     * @return 查询区间的和
     */
    int sumRange(int left, int right) {
        return query(left, right, 0, n - 1, 1);
    }
};

/**
 * 扩展：支持区间加法和区间查询的线段树实现
 * 这个扩展版本使用懒标记技术，实现区间加法和区间查询
 * 
 * 懒标记（Lazy Propagation）原理：
 * 1. 当需要对某个区间进行更新时，不是立即递归地更新所有相关节点
 * 2. 而是将更新操作记录在当前节点的懒标记中，延迟到需要访问子节点时再传递
 * 3. 这样可以避免不必要的递归操作，将区间更新的时间复杂度优化到O(log n)
 * 
 * 懒标记策略：
 * - 当当前节点代表的区间完全包含在目标更新区间内时，直接更新当前节点并设置懒标记
 * - 当需要访问子节点时（查询或部分更新），先将懒标记传递给子节点，然后清空当前节点的懒标记
 * - 这种"延迟传递"的策略是线段树能够高效处理区间更新的关键
 */
class SegmentTreeWithLazy {
private:
    vector<long long> tree;  // 线段树数组
    vector<long long> lazy;  // 懒标记数组
    vector<int> arr;         // 原始数组
    int n;                   // 数组长度

    /**
     * 向上更新节点值
     * 时间复杂度：O(1)
     * 功能：将左右子节点的值合并到当前节点，确保父节点保存了子节点的正确聚合信息
     */
    void pushUp(int i) {
        tree[i] = tree[i << 1] + tree[i << 1 | 1];
    }

    /**
     * 向下传递懒标记（核心操作）
     * 时间复杂度：O(1)
     * @param i 当前节点索引
     * @param ln 左子树的节点数
     * @param rn 右子树的节点数
     * 懒标记传递原理：
     * 1. 只有当存在未处理的懒标记时才需要传递
     * 2. 将当前节点的懒标记值传递给左右子节点
     * 3. 根据子树的大小更新子节点的值
     * 4. 清空当前节点的懒标记，表示更新操作已传递
     * 这种延迟传递机制确保了只有在必要时才会执行子节点的更新
     */
    void pushDown(int i, int ln, int rn) {
        if (lazy[i] != 0) {
            // 更新左子节点
            tree[i << 1] += lazy[i] * ln;
            lazy[i << 1] += lazy[i];
            // 更新右子节点
            tree[i << 1 | 1] += lazy[i] * rn;
            lazy[i << 1 | 1] += lazy[i];
            // 清空当前节点的懒标记
            lazy[i] = 0;
        }
    }

    /**
     * 构建线段树
     * 时间复杂度：O(n)
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点在数组中的索引
     * 构建过程与基础线段树相同，但增加了懒标记数组的初始化
     */
    void build(int l, int r, int i) {
        if (l == r) {
            tree[i] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        pushUp(i);
    }

    /**
     * 区间加法更新
     * 时间复杂度：O(log n)
     * @param L 更新区间左边界
     * @param R 更新区间右边界
     * @param val 要增加的值
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点索引
     * 区间更新策略：
     * 1. 当前区间完全包含在目标更新区间内：使用懒标记延迟更新
     *   - 更新当前节点的值（加上val乘以区间长度）
     *   - 更新懒标记，表示子节点需要进行相同的更新
     * 2. 部分重叠：先下发懒标记，再递归处理左右子树
     *   - 计算中点，将当前区间分为左右两部分
     *   - 下发懒标记到子节点
     *   - 根据需要递归处理左子树和/或右子树
     *   - 最后更新当前节点的值
     */
    void updateRange(int L, int R, int val, int l, int r, int i) {
        if (L <= l && r <= R) {
            tree[i] += (long long)val * (r - l + 1);
            lazy[i] += val;
            return;
        }
        int mid = (l + r) >> 1;
        pushDown(i, mid - l + 1, r - mid);
        if (L <= mid) updateRange(L, R, val, l, mid, i << 1);
        if (R > mid) updateRange(L, R, val, mid + 1, r, i << 1 | 1);
        pushUp(i);
    }

    /**
     * 区间查询
     * 时间复杂度：O(log n)
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @param l 当前节点代表的区间左边界
     * @param r 当前节点代表的区间右边界
     * @param i 当前节点索引
     * @return 查询区间的和
     * 区间查询策略：
     * 1. 当前区间完全包含在查询区间内：直接返回当前节点的值
     * 2. 部分重叠：先下发懒标记（确保子节点的数据正确性），再递归查询左右子树
     *   - 计算中点
     *   - 下发懒标记到子节点
     *   - 递归查询与查询区间有交集的子树
     *   - 合并查询结果
     */
    long long queryRange(int L, int R, int l, int r, int i) {
        if (L <= l && r <= R) {
            return tree[i];
        }
        int mid = (l + r) >> 1;
        pushDown(i, mid - l + 1, r - mid);
        long long ans = 0;
        if (L <= mid) ans += queryRange(L, R, l, mid, i << 1);
        if (R > mid) ans += queryRange(L, R, mid + 1, r, i << 1 | 1);
        return ans;
    }

public:
    /**
     * 带懒标记的线段树构造函数
     * @param nums 输入数组
     * 初始化线段树和懒标记数组，并构建线段树
     * 使用long long类型避免整数溢出问题
     */

    /**
     * 区间加法更新接口
     * @param L 更新区间左边界
     * @param R 更新区间右边界
     * @param val 要增加的值
     */

    /**
     * 区间查询接口
     * @param L 查询区间左边界
     * @param R 查询区间右边界
     * @return 查询区间的和
     */
};

// 测试函数
int main() {
    // 测试基础线段树
    vector<int> nums = {1, 3, 5};
    SegmentTree st(nums);
    
    cout << "sumRange(0, 2): " << st.sumRange(0, 2) << endl; // 输出: 9
    
    st.update(1, 2);
    
    cout << "sumRange(0, 2): " << st.sumRange(0, 2) << endl; // 输出: 8
    
    // 测试带懒标记的线段树
    vector<int> nums2 = {1, 3, 5, 7, 9, 11};
    SegmentTreeWithLazy stl(nums2);
    
    cout << "sumRange(0, 5): " << stl.queryRange(0, 5) << endl; // 输出: 36
    stl.updateRange(1, 3, 2);  // 区间[1,3]每个元素加2
    cout << "sumRange(0, 5): " << stl.queryRange(0, 5) << endl; // 输出: 42
    
    return 0;
}

/*
 * C++线段树实现的工程化考量：
 * 1. 数据类型选择：
 *    - 使用long long类型避免整数溢出问题，特别是在区间求和场景
 *    - 根据问题需求选择合适的数据类型，平衡空间占用和计算精度
 * 
 * 2. 索引处理：
 *    - 本实现使用0-based索引（与C++标准库习惯一致）
 *    - 需要注意与1-based索引实现的转换
 *    - 线段树内部使用1-based索引存储节点，简化父子关系计算
 * 
 * 3. 性能优化技巧：
 *    - 位运算优化：使用i << 1代替i * 2，i << 1 | 1代替i * 2 + 1
 *    - 预分配空间：使用resize一次性分配足够空间，避免动态扩容开销
 *    - 使用引用传递避免不必要的复制操作
 *    - 内联关键函数减少函数调用开销
 * 
 * 4. 错误处理与边界检查：
 *    - 在实际应用中应添加参数验证，确保查询和更新区间有效
 *    - 考虑数组为空或单元素的特殊情况
 *    - 对于大规模数据，考虑使用动态开点线段树节省空间
 * 
 * 5. 线段树变体与扩展：
 *    - 区间最大值/最小值线段树：修改pushUp和查询逻辑
 *    - 区间异或线段树：修改pushUp和pushDown逻辑
 *    - 区间赋值线段树：需要处理懒标记的覆盖问题
 *    - 二维线段树：适用于二维区间查询
 *    - 主席树（可持久化线段树）：支持历史版本查询
 * 
 * 6. 调试与测试：
 *    - 使用小例子验证算法正确性
 *    - 添加日志输出关键变量，辅助调试
 *    - 使用单元测试覆盖不同场景
 * 
 * 7. 与其他数据结构的对比：
 *    - 树状数组：实现更简单，常数更小，但功能相对有限
 *    - ST表：适用于静态数组的区间查询，查询O(1)，但不支持更新
 *    - 块状数组：某些场景下可以平衡时间和实现复杂度
 * 
 * 8. 工程应用场景：
 *    - 金融数据分析：区间统计、趋势分析
 *    - 图像处理：区域操作、卷积计算
 *    - 游戏开发：范围效果计算、碰撞检测
 *    - 网络安全：流量分析、异常检测
 *    - 数据库系统：区间查询优化
 * 
 * 9. 高级优化技巧：
 *    - 离散化：当数据范围很大但稀疏时，进行离散化处理
 *    - 剪枝：在查询过程中跳过不可能包含答案的子树
 *    - 内存池：动态开点线段树的内存管理优化
 *    - SIMD指令：利用现代CPU的向量化指令加速计算
 */

/*
 * 补充题目列表：
 * 1. LeetCode 307. Range Sum Query - Mutable - https://leetcode.cn/problems/range-sum-query-mutable/
 * 2. LeetCode 315. Count of Smaller Numbers After Self - https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 3. LeetCode 699. Falling Squares - https://leetcode.cn/problems/falling-squares/
 * 4. HDU 1166. 敌兵布阵 - http://acm.hdu.edu.cn/showproblem.php?pid=1166
 * 5. HDU 1754. I Hate It - http://acm.hdu.edu.cn/showproblem.php?pid=1754
 * 6. HDU 6514. Monitor - http://acm.hdu.edu.cn/showproblem.php?pid=6514
 * 7. Luogu P3372. 【模板】线段树 1 - https://www.luogu.com.cn/problem/P3372
 * 8. Luogu P3373. 【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
 * 9. Codeforces 52C. Circular RMQ - https://codeforces.com/problemset/problem/52/C
 * 10. Codeforces 339D. Xor - https://codeforces.com/problemset/problem/339/D
 * 11. SPOJ GSS1. Can you answer these queries I - https://www.spoj.com/problems/GSS1/
 * 12. AtCoder ABC183E. Queen on Grid - https://atcoder.jp/contests/abc183/tasks/abc183_e
 * 13. USACO 2016 Jan Silver. Subsequences Summing to Sevens - http://www.usaco.org/index.php?page=viewproblem2&cpid=595
 * 14. AizuOJ ALDS1_9_C. Segment Tree - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_9_C
 * 15. 牛客 NC14417. 线段树练习 - https://ac.nowcoder.com/acm/problem/14417
 * 16. 计蒜客 T1250. 线段树练习 - https://nanti.jisuanke.com/t/T1250
 * 17. CodeChef SEGPROD. Segment Product - https://www.codechef.com/problems/SEGPROD
 * 18. SPOJ MKTHNUM. K-th number - https://www.spoj.com/problems/MKTHNUM/
 * 19. MarsCode MCS04E. Array Queries - https://acm.marscode.com/problem.php?id=61
 * 20. TimusOJ 1547. Cipher Grille - https://acm.timus.ru/problem.aspx?space=1&num=1547
 */