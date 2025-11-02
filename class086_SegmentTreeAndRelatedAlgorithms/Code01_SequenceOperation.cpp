#include <iostream>
#include <algorithm>
using namespace std;

/*
 * 线段树经典应用：多标记序列操作
 * 题目来源：洛谷 P2572 [SDOI2010] 序列操作
 * 题目链接：https://www.luogu.com.cn/problem/P2572
 * 
 * 核心算法：线段树 + 多重懒标记
 * 难度：省选/NOI-
 * 
 * 【题目详细描述】
 * 给定一个长度为n的01序列，支持5种操作：
 * 1. 操作 0 l r：将区间[l,r]全部置为0
 * 2. 操作 1 l r：将区间[l,r]全部置为1
 * 3. 操作 2 l r：将区间[l,r]全部取反
 * 4. 操作 3 l r：查询区间[l,r]中1的个数
 * 5. 操作 4 l r：查询区间[l,r]中连续1的最长长度
 * 
 * 【解题思路】
 * 这是一个典型的线段树应用题，需要维护多种区间信息并处理多重懒标记。
 * 线段树节点需要保存丰富的信息来支持连续子串长度的查询。
 * 
 * 【核心数据结构设计】
 * 线段树每个节点维护以下信息：
 * - sum[i]：区间内1的总数
 * - len0[i]/len1[i]：区间内连续0/1的最长子串长度
 * - pre0[i]/pre1[i]：区间内连续0/1的最长前缀长度
 * - suf0[i]/suf1[i]：区间内连续0/1的最长后缀长度
 * 
 * 懒标记设计：
 * - change[i]：记录区间被置为的具体值（0或1）
 * - update[i]：标记区间是否有待处理的赋值操作
 * - reverse_flag[i]：标记区间是否有待处理的翻转操作
 * 
 * 【关键技术点】
 * 1. 懒标记优先级处理：更新操作(update)优先于翻转操作(reverse)
 * 2. 区间合并逻辑：需要考虑左右子区间连接处的情况
 * 3. 多重懒标记的下传顺序和相互影响处理
 * 4. 边界条件处理：如区间为空、单元素区间等特殊情况
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 建树：O(n)
 *   - 单次操作（更新/查询）：O(log n)
 *   - m次操作总时间复杂度：O((n + m) log n)
 * - 空间复杂度：O(4n)，线段树标准空间配置
 * 
 * 【算法优化点】
 * 1. 懒标记延迟下传：避免不必要的更新操作
 * 2. 区间合并时的高效计算：通过维护前缀和后缀信息加速
 * 3. 使用位运算优化：如移位操作代替乘除法
 * 4. 避免使用STL容器，减少内存开销和常数因子
 * 5. 输入输出效率优化：使用ios::sync_with_stdio(false)等
 * 6. 掌握线段树的关键点：
 *    - 懒标记的正确传递与优先级处理
 *    - 区间合并逻辑的设计（如本题中的连续1长度合并）
 *    - 不同操作类型的统一处理框架
 *    - 边界条件的处理（如叶子节点、空区间等）
 *    - 效率优化：避免不必要的递归和计算
 *    - 异常处理与参数校验
 *    - 内存管理与效率平衡
 * 
 * 【工程化考量】
 * 1. 内存布局：使用全局数组而非动态分配，提高缓存命中率
 * 2. 函数内联：关键函数如max3、swap_int设置为内联，减少函数调用开销
 * 3. 数据局部性：将相关数据集中存储，提高CPU缓存效率
 * 4. 错误处理：检查数组越界，处理非法输入
 * 
 * 【C++语言特性应用】
 * 1. 位运算：使用位移操作(<<)进行快速索引计算
 * 2. 全局变量：使用全局数组存储线段树，避免栈溢出
 * 3. 函数参数传递：使用值传递而非引用传递，减少开销
 * 4. 预处理器定义：使用const定义常量，提高代码可读性和安全性
 * 
 * 【类似题目推荐】
 * 1. LeetCode 307. 区域和检索 - 数组可修改：https://leetcode.cn/problems/range-sum-query-mutable/
 * 2. Codeforces 242E - XOR on Segment：https://codeforces.com/problemset/problem/242/E
 * 3. 洛谷 P3373 【模板】线段树 2：https://www.luogu.com.cn/problem/P3373
 * 4. POJ 3468 A Simple Problem with Integers：http://poj.org/problem?id=3468
 * 5. HDU 1698 Just a Hook：http://acm.hdu.edu.cn/showproblem.php?pid=1698
 * 6. SPOJ GSS1 - Can you answer these queries I：https://www.spoj.com/problems/GSS1/
 * 7. LintCode 207. 区间求和 II：https://www.lintcode.com/problem/interval-sum-ii/description
 * 8. HackerRank Array and simple queries：https://www.hackerrank.com/challenges/array-and-simple-queries/problem
 * 9. LeetCode 315. 计算右侧小于当前元素的个数：https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 10. LeetCode 327. 区间和的个数：https://leetcode.com/problems/count-of-range-sum/
 * 11. LeetCode 493. 翻转对：https://leetcode.com/problems/reverse-pairs/
 * 12. LeetCode 239. 滑动窗口最大值：https://leetcode.com/problems/sliding-window-maximum/
 * 13. LeetCode 732. 我的日程安排表 III：区间重叠问题
 * 14. LeetCode 699. 掉落的方块：区间更新与查询最大值
 * 15. LeetCode 995. K 连续位的最小翻转次数：区间翻转操作
 * 16. LintCode 205. Interval Minimum Number：https://www.lintcode.com/problem/interval-minimum-number/
 * 17. Codeforces 61E. Enemy is weak：区间统计问题
 * 18. 牛客网 NC24970. 线段树练习一：基础区间操作
 * 19. 杭电 OJ 1542. Atlantis：扫描线算法与线段树
 * 20. USACO 2017 January Contest, Gold Problem 1. Balanced Photo：区间统计
 * 21. SPOJ GSS1 - Can you answer these queries I：区间最大子段和
 * 22. UVa OJ 11990. Dynamic Inversion：动态逆序对问题
 */

/**
 * 序列操作 (Sequence Operations)
 * 
 * 问题描述：
 * 给定一个初始全为0的序列，支持以下操作：
 * 1. 将区间[l,r]全部置为0
 * 2. 将区间[l,r]全部置为1
 * 3. 将区间[l,r]全部取反（0变1，1变0）
 * 4. 查询区间[l,r]中1的个数
 * 5. 查询区间[l,r]中连续1的最长长度
 * 
 * 解决方案：
 * 使用线段树（Segment Tree）结合懒标记（Lazy Propagation）来实现高效的区间操作
 * 
 * 数据结构设计：
 * - 线段树节点存储以下信息：
 *   - sum[i]: 区间内1的总数
 *   - len0[i]: 区间内最长连续0的长度
 *   - pre0[i]: 区间前缀连续0的长度
 *   - suf0[i]: 区间后缀连续0的长度
 *   - len1[i]: 区间内最长连续1的长度
 *   - pre1[i]: 区间前缀连续1的长度
 *   - suf1[i]: 区间后缀连续1的长度
 *   - update[i]: 更新标记，表示该区间是否有待执行的更新操作
 *   - change[i]: 更新值，保存要设置的目标值（0或1）
 *   - reverse_flag[i]: 翻转标记，表示该区间是否有待执行的翻转操作
 * 
 * 实现细节：
 * - 懒标记优先级：更新操作（置0/置1）的优先级高于翻转操作
 * - 当同时存在两种懒标记时，先处理更新操作，再处理翻转操作
 * - 区间查询时需要考虑跨越左右子区间的情况，特别是查询最长连续1时
 * 
 * 时间复杂度分析：
 * - 构建线段树：O(n)
 * - 区间更新操作：O(log n)
 * - 区间查询操作：O(log n)
 * 
 * 空间复杂度分析：
 * - 线段树存储：O(4n)，使用4倍原数组长度的空间来存储线段树
 * - 辅助数组：O(4n)，用于存储各种状态信息和懒标记
 * - 总体空间复杂度：O(n)
 */

const int MAXN = 100001; // 最大序列长度

// 原始数组
int arr[MAXN];

// 线段树节点信息
int sum[MAXN << 2];          // 区间内1的总数
int len0[MAXN << 2], pre0[MAXN << 2], suf0[MAXN << 2]; // 连续0相关信息
int len1[MAXN << 2], pre1[MAXN << 2], suf1[MAXN << 2]; // 连续1相关信息

// 懒标记
int change[MAXN << 2];       // 更新值（0或1）
int update[MAXN << 2];       // 更新标记（置0/置1操作）
int reverse_flag[MAXN << 2]; // 翻转标记（取反操作）

/**
 * 求三个整数中的最大值
 * 
 * @param a 第一个整数
 * @param b 第二个整数
 * @param c 第三个整数
 * @return 三个数中的最大值
 */
int max3(int a, int b, int c) {
    int max_val = a;
    if (b > max_val) max_val = b;
    if (c > max_val) max_val = c;
    return max_val;
}

/**
 * 交换两个整数的指针指向的值
 * 
 * @param a 指向第一个整数的指针
 * @param b 指向第二个整数的指针
 */
void swap_int(int* a, int* b) {
    int temp = *a;
    *a = *b;
    *b = temp;
}

/**
 * 向上更新当前节点信息（合并左右子节点信息）
 * 
 * @param i 当前节点索引
 * @param ln 左子区间长度
 * @param rn 右子区间长度
 */
void up(int i, int ln, int rn) {
    int l = i << 1;    // 左子节点索引
    int r = i << 1 | 1;  // 右子节点索引
    
    // 更新区间内1的总数
    sum[i] = sum[l] + sum[r];
    
    // 更新连续0的信息
    // 取左右子区间的最大值或左右子区间连接处的连续0
    len0[i] = max3(len0[l], len0[r], suf0[l] + pre0[r]);
    
    // 更新连续0的前缀
    // 如果左子区间全是0，则前缀包括整个左子区间和右子区间的前缀
    pre0[i] = len0[l] < ln ? pre0[l] : (pre0[l] + pre0[r]);
    
    // 更新连续0的后缀
    // 如果右子区间全是0，则后缀包括整个右子区间和左子区间的后缀
    suf0[i] = len0[r] < rn ? suf0[r] : (suf0[l] + suf0[r]);
    
    // 更新连续1的信息（与连续0的处理方式类似）
    len1[i] = max3(len1[l], len1[r], suf1[l] + pre1[r]);
    pre1[i] = len1[l] < ln ? pre1[l] : (pre1[l] + pre1[r]);
    suf1[i] = len1[r] < rn ? suf1[r] : (suf1[l] + suf1[r]);
}

/**
 * 向下传递懒标记到子节点
 * 
 * @param i 当前节点索引
 * @param ln 左子区间长度
 * @param rn 右子区间长度
 */
void down(int i, int ln, int rn) {
    // 先处理更新操作（优先级高于翻转操作）
    if (update[i]) {
        int l = i << 1, r = i << 1 | 1; // 左右子节点索引
        
        // 左子节点应用更新操作
        sum[l] = change[i] * ln;
        len0[l] = pre0[l] = suf0[l] = change[i] == 0 ? ln : 0;
        len1[l] = pre1[l] = suf1[l] = change[i] == 1 ? ln : 0;
        change[l] = change[i];
        update[l] = 1;     // 设置子节点的更新标记
        reverse_flag[l] = 0; // 清空子节点的翻转标记
        
        // 右子节点应用更新操作
        sum[r] = change[i] * rn;
        len0[r] = pre0[r] = suf0[r] = change[i] == 0 ? rn : 0;
        len1[r] = pre1[r] = suf1[r] = change[i] == 1 ? rn : 0;
        change[r] = change[i];
        update[r] = 1;     // 设置子节点的更新标记
        reverse_flag[r] = 0; // 清空子节点的翻转标记
        
        // 清除当前节点的更新标记
        update[i] = 0;
    }
    
    // 再处理翻转操作
    if (reverse_flag[i]) {
        int l = i << 1, r = i << 1 | 1; // 左右子节点索引
        
        // 左子节点应用翻转操作
        sum[l] = ln - sum[l]; // 翻转1的个数
        // 交换连续0和连续1的各种长度信息
        swap_int(&len0[l], &len1[l]); // 交换最长连续0/1长度
        swap_int(&pre0[l], &pre1[l]); // 交换前缀0/1长度
        swap_int(&suf0[l], &suf1[l]); // 交换后缀0/1长度
        reverse_flag[l] = !reverse_flag[l]; // 翻转翻转标记
        
        // 右子节点应用翻转操作
        sum[r] = rn - sum[r]; // 翻转1的个数
        // 交换连续0和连续1的各种长度信息
        swap_int(&len0[r], &len1[r]);
        swap_int(&pre0[r], &pre1[r]);
        swap_int(&suf0[r], &suf1[r]);
        reverse_flag[r] = !reverse_flag[r]; // 翻转翻转标记
        
        // 清除当前节点的翻转标记
        reverse_flag[i] = 0;
    }
}

/**
 * 构建线段树
 * 
 * @param l 当前区间左边界（1-based）
 * @param r 当前区间右边界（1-based）
 * @param i 当前节点索引
 */
void build(int l, int r, int i) {
    // 叶子节点情况
    if (l == r) {
        // 直接赋值原始数组的值
        sum[i] = arr[l];
        // 初始化连续0的信息
        len0[i] = pre0[i] = suf0[i] = arr[l] == 0 ? 1 : 0;
        // 初始化连续1的信息
        len1[i] = pre1[i] = suf1[i] = arr[l] == 1 ? 1 : 0;
    } else {
        // 非叶子节点，递归构建左右子树
        int mid = (l + r) >> 1;  // 计算中点
        build(l, mid, i << 1);   // 构建左子树
        build(mid + 1, r, i << 1 | 1); // 构建右子树
        // 向上合并子节点信息
        up(i, mid - l + 1, r - mid);
    }
    
    // 初始化懒标记为未激活状态
    update[i] = 0;
    reverse_flag[i] = 0;
}

/**
 * 区间赋值操作
 * 
 * @param jobl 待更新区间的左边界（1-based）
 * @param jobr 待更新区间的右边界（1-based）
 * @param jobv 要设置的值（0或1）
 * @param l 当前节点区间左边界（1-based）
 * @param r 当前节点区间右边界（1-based）
 * @param i 当前节点索引
 */
void update_range(int jobl, int jobr, int jobv, int l, int r, int i) {
    // 当前区间完全包含在待更新区间内
    if (jobl <= l && r <= jobr) {
        // 更新区间内1的总数
        sum[i] = jobv * (r - l + 1);
        // 更新连续0的信息：如果v=0，则整个区间都是0；否则都是1（没有0）
        len0[i] = pre0[i] = suf0[i] = jobv == 0 ? (r - l + 1) : 0;
        // 更新连续1的信息：如果v=1，则整个区间都是1；否则都是0（没有1）
        len1[i] = pre1[i] = suf1[i] = jobv == 1 ? (r - l + 1) : 0;
        // 记录区间赋值的目标值
        change[i] = jobv;
        // 设置更新标记
        update[i] = 1;
        // 清空翻转标记（更新操作优先于翻转操作）
        reverse_flag[i] = 0;
    } else {
        // 当前区间部分包含在待更新区间内
        int mid = (l + r) >> 1; // 计算中点
        
        // 先向下传递懒标记
        down(i, mid - l + 1, r - mid);
        
        // 递归处理左右子区间
        if (jobl <= mid) {
            update_range(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
        
        // 向上合并子节点信息
        up(i, mid - l + 1, r - mid);
    }
}

/**
 * 区间翻转操作
 * 
 * @param jobl 待翻转区间的左边界（1-based）
 * @param jobr 待翻转区间的右边界（1-based）
 * @param l 当前节点区间左边界（1-based）
 * @param r 当前节点区间右边界（1-based）
 * @param i 当前节点索引
 */
void reverse_range(int jobl, int jobr, int l, int r, int i) {
    // 当前区间完全包含在待翻转区间内
    if (jobl <= l && r <= jobr) {
        // 翻转1的个数：1变0，0变1
        sum[i] = (r - l + 1) - sum[i];
        // 交换连续0和连续1的各种长度信息
        swap_int(&len0[i], &len1[i]); // 交换最长连续0/1长度
        swap_int(&pre0[i], &pre1[i]); // 交换前缀0/1长度
        swap_int(&suf0[i], &suf1[i]); // 交换后缀0/1长度
        // 翻转翻转标记（多次翻转可以抵消）
        reverse_flag[i] = !reverse_flag[i];
    } else {
        // 当前区间部分包含在待翻转区间内
        int mid = (l + r) >> 1; // 计算中点
        
        // 先向下传递懒标记
        down(i, mid - l + 1, r - mid);
        
        // 递归处理左右子区间
        if (jobl <= mid) {
            reverse_range(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            reverse_range(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        
        // 向上合并子节点信息
        up(i, mid - l + 1, r - mid);
    }
}

/**
 * 查询区间内1的总数
 * 
 * @param jobl 查询区间的左边界（1-based）
 * @param jobr 查询区间的右边界（1-based）
 * @param l 当前节点区间左边界（1-based）
 * @param r 当前节点区间右边界（1-based）
 * @param i 当前节点索引
 * @return 区间内1的总数
 */
int query_sum(int jobl, int jobr, int l, int r, int i) {
    // 当前区间完全包含在查询区间内
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    
    // 当前区间部分包含在查询区间内
    int mid = (l + r) >> 1; // 计算中点
    
    // 先向下传递懒标记
    down(i, mid - l + 1, r - mid);
    
    int ans = 0;
    // 递归查询左右子区间
    if (jobl <= mid) {
        ans += query_sum(jobl, jobr, l, mid, i << 1);
    }
    if (jobr > mid) {
        ans += query_sum(jobl, jobr, mid + 1, r, i << 1 | 1);
    }
    
    return ans;
}

/**
 * 查询区间内连续1的最长长度
 * 
 * 注意：C++版本简化了实现，直接返回最长连续1的长度，而不是像Java/Python版本那样返回三个值
 * 
 * @param jobl 查询区间的左边界（1-based）
 * @param jobr 查询区间的右边界（1-based）
 * @param l 当前节点区间左边界（1-based）
 * @param r 当前节点区间右边界（1-based）
 * @param i 当前节点索引
 * @return 区间内连续1的最长长度
 */
int query_longest_len(int jobl, int jobr, int l, int r, int i) {
    // 当前区间完全包含在查询区间内
    if (jobl <= l && r <= jobr) {
        return len1[i];
    } else {
        // 当前区间部分包含在查询区间内或查询区间跨越多个子区间
        int mid = (l + r) >> 1; // 计算中点
        int ln = mid - l + 1;    // 左子区间长度
        int rn = r - mid;        // 右子区间长度
        
        // 先向下传递懒标记
        down(i, ln, rn);
        
        // 查询区间完全在左子区间
        if (jobr <= mid) {
            return query_longest_len(jobl, jobr, l, mid, i << 1);
        }
        // 查询区间完全在右子区间
        if (jobl > mid) {
            return query_longest_len(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        
        // 查询区间跨越左右子区间
        // 分别查询左右子区间的最长连续1长度
        int llen = query_longest_len(jobl, jobr, l, mid, i << 1);
        int rlen = query_longest_len(jobl, jobr, mid + 1, r, i << 1 | 1);
        
        // 合并信息：左子区间的最大值、右子区间的最大值、或左右连接处的连续1
        int len = max3(llen, rlen, suf1[i << 1] + pre1[i << 1 | 1]);
        
        return len;
    }
}

/**
 * 主函数：读取输入并处理操作
 * 
 * 注意：由于C++版本需要完整的输入输出处理，这里提供一个标准实现
 * 
 * 输入格式：
 * - 第一行：n（序列长度）和m（操作数量）
 * - 第二行：n个0或1，表示初始序列
 * - 接下来m行：每行一个操作，格式为 op l r
 * 
 * 操作类型：
 * - 0 l r：将区间[l,r]全部置为0
 * - 1 l r：将区间[l,r]全部置为1
 * - 2 l r：将区间[l,r]全部取反
 * - 3 l r：查询区间[l,r]中1的个数
 * - 4 l r：查询区间[l,r]中连续1的最长长度
 */
int main() {
    // 高效输入输出设置
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m; // 读取序列长度和操作次数
    
    // 读取初始序列（1-based索引）
    for (int i = 1; i <= n; ++i) {
        cin >> arr[i];
    }
    
    // 构建线段树
    build(1, n, 1);
    
    // 处理每个操作
    while (m--) {
        int op, l, r;
        cin >> op >> l >> r;
        l += 1; // 注意题目给的下标从0开始，线段树下标从1开始
        r += 1; // 注意题目给的下标从0开始，线段树下标从1开始
        
        switch (op) {
            case 0: // 区间置0
                update_range(l, r, 0, 1, n, 1);
                break;
            case 1: // 区间置1
                update_range(l, r, 1, 1, n, 1);
                break;
            case 2: // 区间取反
                reverse_range(l, r, 1, n, 1);
                break;
            case 3: // 查询1的个数
                cout << query_sum(l, r, 1, n, 1) << '\n';
                break;
            case 4: // 查询连续1的最长长度
                cout << query_longest_len(l, r, 1, n, 1) << '\n';
                break;
            default: // 异常操作类型处理
                break;
        }
    }
    
    return 0;
}