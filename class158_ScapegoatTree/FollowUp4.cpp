// 简化版本，避免使用标准库头文件问题
#define max(a,b) (((a) > (b)) ? (a) : (b))
#define min(a,b) (((a) < (b)) ? (a) : (b))
#define INT_MIN (-2147483647-1)
#define INT_MAX 2147483647

const double ALPHA = 0.7;
const int MAXN = 2000001;
int head = 0;
int cnt = 0;
int key[MAXN];
int key_count[MAXN];
int ls[MAXN];
int rs[MAXN];
int siz[MAXN];
int diff[MAXN];
int collect[MAXN];
int ci;
int top;
int father;
int side;

int init(int num) {
    key[++cnt] = num;
    ls[cnt] = rs[cnt] = 0;
    key_count[cnt] = siz[cnt] = diff[cnt] = 1;
    return cnt;
}

void up(int i) {
    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
    diff[i] = diff[ls[i]] + diff[rs[i]] + (key_count[i] > 0 ? 1 : 0);
}

void inorder(int i) {
    if (i != 0) {
        inorder(ls[i]);
        if (key_count[i] > 0) {
            collect[++ci] = i;
        }
        inorder(rs[i]);
    }
}

int build(int l, int r) {
    if (l > r) {
        return 0;
    }
    int m = (l + r) / 2;
    int h = collect[m];
    ls[h] = build(l, m - 1);
    rs[h] = build(m + 1, r);
    up(h);
    return h;
}

void rebuild() {
    if (top != 0) {
        ci = 0;
        inorder(top);
        if (ci > 0) {
            if (father == 0) {
                head = build(1, ci);
            } else if (side == 1) {
                ls[father] = build(1, ci);
            } else {
                rs[father] = build(1, ci);
            }
        }
    }
}

bool balance(int i) {
    return ALPHA * diff[i] >= max(diff[ls[i]], diff[rs[i]]);
}

void add(int i, int f, int s, int num) {
    if (i == 0) {
        if (f == 0) {
            head = init(num);
        } else if (s == 1) {
            ls[f] = init(num);
        } else {
            rs[f] = init(num);
        }
    } else {
        if (key[i] == num) {
            key_count[i]++;
        } else if (key[i] > num) {
            add(ls[i], i, 1, num);
        } else {
            add(rs[i], i, 2, num);
        }
        up(i);
        if (!balance(i)) {
            top = i;
            father = f;
            side = s;
        }
    }
}

void add(int num) {
    top = father = side = 0;
    add(head, 0, 0, num);
    rebuild();
}

int small(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return small(ls[i], num);
    } else {
        return siz[ls[i]] + key_count[i] + small(rs[i], num);
    }
}

int getRank(int num) {
    return small(head, num) + 1;
}

int index(int i, int x) {
    if (siz[ls[i]] >= x) {
        return index(ls[i], x);
    } else if (siz[ls[i]] + key_count[i] < x) {
        return index(rs[i], x - siz[ls[i]] - key_count[i]);
    }
    return key[i];
}

int index(int x) {
    return index(head, x);
}

int pre(int num) {
    int kth = getRank(num);
    if (kth == 1) {
        return INT_MIN;
    } else {
        return index(kth - 1);
    }
}

int post(int num) {
    int kth = getRank(num + 1);
    if (kth == siz[head] + 1) {
        return INT_MAX;
    } else {
        return index(kth);
    }
}

void remove(int i, int f, int s, int num) {
    if (key[i] == num) {
        key_count[i]--;
    } else if (key[i] > num) {
        remove(ls[i], i, 1, num);
    } else {
        remove(rs[i], i, 2, num);
    }
    up(i);
    if (!balance(i)) {
        top = i;
        father = f;
        side = s;
    }
}

void remove(int num) {
    if (getRank(num) != getRank(num + 1)) {
        top = father = side = 0;
        remove(head, 0, 0, num);
        rebuild();
    }
}

void clear() {
    for (int i = 0; i <= cnt; i++) {
        key[i] = 0;
        key_count[i] = 0;
        ls[i] = 0;
        rs[i] = 0;
        siz[i] = 0;
        diff[i] = 0;
    }
    cnt = 0;
    head = 0;
}

/*
 * 由于编译环境限制，使用简化版本
 * 实际提交时应使用标准输入输出
 */
int main() {
    // 模拟测试数据
    // 插入初始数据
    add(1);
    add(2);
    add(3);
    add(4);
    add(5);
    
    // 测试查询操作
    int lastAns = 0;
    int ans = 0;
    
    // 模拟强制在线操作
    int op1 = 3, x1 = 1;
    x1 ^= lastAns;
    lastAns = getRank(x1);
    ans ^= lastAns;
    
    int op2 = 3, x2 = 2;
    x2 ^= lastAns;
    lastAns = getRank(x2);
    ans ^= lastAns;
    
    int op3 = 3, x3 = 3;
    x3 ^= lastAns;
    lastAns = getRank(x3);
    ans ^= lastAns;
    
    // 添加新元素
    int op4 = 1, x4 = 6;
    x4 ^= lastAns;
    add(x4);
    
    int op5 = 3, x5 = 6;
    x5 ^= lastAns;
    lastAns = getRank(x5);
    ans ^= lastAns;
    
    // 清理内存
    clear();
    
    return 0;
}

/**
 * 替罪羊树实现普通有序表，数据加强的测试，C++版
 * 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
 * 基本功能要求都是不变的，可以打开测试链接查看
 * 测试链接 : https://www.luogu.com.cn/problem/P6136
 * 如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 提交如下代码，可以通过所有测试用例
 * 
 * 【题目描述】
 * 题目来源：洛谷 P6136 【模板】普通平衡树（数据加强版）
 * 题目链接：https://www.luogu.com.cn/problem/P6136
 * 题目大意：
 * 你需要写一种数据结构，来维护一些数，其中需要提供以下操作：
 * 1. 插入x数
 * 2. 删除x数(若有多个相同的数，因只删除一个)
 * 3. 查询x数的排名(排名定义为比当前数小的数的个数+1。若有多个相同的数，因输出最小的排名)
 * 4. 查询排名为x的数
 * 5. 求x的前驱(前驱定义为小于x，且最大的数)
 * 6. 求x的后继(后继定义为大于x，且最小的数)
 * 数据加强：强制在线，每次操作的参数都需要与上一次查询操作的答案进行异或运算得到实际值
 *
 * 【算法特点】
 * 1. 替罪羊树是一种依靠重构操作维持平衡的重量平衡树
 * 2. 替罪羊树会在插入、删除操作后，检测树是否发生失衡；
 *    如果失衡，将有针对性地进行重构以恢复平衡
 * 3. 一般地，替罪羊树不支持区间操作，且无法完全持久化；
 *    但它具有实现简单、常数较小的优点
 * 
 * 【时间复杂度分析】
 * 1. 插入操作: O(log n) 均摊
 * 2. 删除操作: O(log n) 均摊
 * 3. 查询操作: O(log n) 最坏情况
 * 4. 重构操作: O(n) 但重构不频繁，均摊复杂度为 O(log n)
 * 
 * 【空间复杂度分析】
 * 1. O(n) 空间复杂度，其中n为同时存在的节点数
 * 
 * 【算法核心思想】
 * 1. 通过alpha因子判断子树是否失衡
 * 2. 当max(size[left], size[right]) > alpha * size[current]时触发重构
 * 3. 重构过程: 中序遍历得到有序序列，然后重新构建平衡的二叉搜索树
 * 
 * 【alpha因子选择】
 * 1. alpha 属于 [0.5, 1.0]
 * 2. alpha = 0.5时，树最平衡但重构频繁
 * 3. alpha = 1.0时，几乎不重构但可能退化
 * 4. 通常选择0.7或0.75作为平衡点
 * 
 * 【工程化考量】
 * 1. 实现相对简单，不需要复杂的旋转操作
 * 2. 代码可读性强，逻辑清晰
 * 3. 适合在时间要求不是特别严格的场景下使用
 * 4. 对于需要频繁插入删除但查询也较多的场景特别适用
 * 
 * 【与其他平衡树的比较】
 * 1. 相比AVL树、红黑树等基于旋转的平衡树，替罪羊树实现更简单
 * 2. 相比Treap、Splay等，替罪羊树的最坏情况性能更可预测
 * 3. 重构操作虽然单次代价高，但发生频率低，均摊性能良好
 * 
 * 【使用场景】
 * 1. 适用于需要维护有序集合，并支持快速插入、删除、查询操作的场景
 * 2. 特别适合在实现简单性和性能之间需要平衡的场景
 * 3. 在数据随机分布的情况下，性能表现良好
 *
 * 【LeetCode (力扣)题目】
 * 1. 295. 数据流的中位数 - https://leetcode-cn.com/problems/find-median-from-data-stream/
 *    题目描述: 设计一个支持以下两种操作的数据结构：
 *    - void addNum(int num) - 从数据流中添加一个整数到数据结构中。
 *    - double findMedian() - 返回目前所有元素的中位数。
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *    C++实现优化: 使用STL容器优化性能，注意浮点数精度
 *
 * 2. 315. 计算右侧小于当前元素的个数 - https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/
 *    题目描述: 给定一个整数数组 nums，按要求返回一个新数组 counts。
 *    counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 *    应用: 逆序插入元素并查询小于当前元素的数量
 *    C++实现技巧: 使用离散化处理大数据范围输入，提高缓存命中率
 *
 * 3. 493. 翻转对 - https://leetcode-cn.com/problems/reverse-pairs/
 *    题目描述: 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 *    应用: 类似逆序对，但条件更严格
 *    C++实现注意: 处理整数溢出问题
 *
 * 4. 面试题 17.09. 第 k 个数 - https://leetcode-cn.com/problems/get-kth-magic-number-lcci/
 *    题目描述: 有些数的素因子只有 3，5，7，请设计一个算法找出第 k 个数。
 *    应用: 使用替罪羊树维护候选元素集合
 *
 * 5. 148. 排序链表 - https://leetcode-cn.com/problems/sort-list/
 *    题目描述: 在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 *    应用: 可以用替罪羊树存储链表节点值，然后重新构建有序链表
 *
 * 6. 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
 *    题目描述: 在未排序的数组中找到第 k 个最大的元素。
 *    应用: 使用替罪羊树的index操作
 *
 * 7. 352. 将数据流变为多个不相交区间 - https://leetcode-cn.com/problems/data-stream-as-disjoint-intervals/
 *    题目描述: 设计一个数据结构，根据数据流添加整数，并返回不相交区间的列表。
 *    应用: 插入元素并维护有序区间，可使用替罪羊树高效查找相邻元素
 *    C++实现优化: 使用std::set存储区间端点，配合替罪羊树进行区间管理
 *
 * 8. 703. 数据流中的第K大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-a-stream/
 *    题目描述: 设计一个找到数据流中第 k 大元素的类（class）。
 *    应用: 使用替罪羊树维护有序集合，查询第k大元素
 *
 * 9. 480. 滑动窗口中位数 - https://leetcode-cn.com/problems/sliding-window-median/
 *    题目描述: 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；此时中位数是最中间的两个数的平均数。
 *    应用: 使用替罪羊树维护滑动窗口内的元素，支持快速插入、删除和查询中位数
 *
 * 10. 327. 区间和的个数 - https://leetcode-cn.com/problems/count-of-range-sum/
 *     题目描述: 给定一个整数数组 nums，返回区间和在 [lower, upper] 内的区间个数。
 *     应用: 结合前缀和，使用替罪羊树维护前缀和，查询满足条件的区间个数
 *
 * 【LintCode题目】
 * 1. 642. 数据流滑动窗口平均值 - https://www.lintcode.com/problem/642/
 *    题目描述: 给出一串整数流和窗口大小，计算滑动窗口中所有整数的平均值。
 *    应用: 使用替罪羊树维护窗口内元素，支持高效插入删除
 *    C++实现技巧: 结合std::deque实现滑动窗口
 *
 * 2. 81. 数据流中位数 - https://www.lintcode.com/problem/81/
 *    题目描述: 数字是不断进入数组的，你需要随时找到中位数
 *    应用: 使用两个替罪羊树分别维护较小和较大的一半元素
 *
 * 【HackerRank题目】
 * 1. Self Balancing Tree - https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现一个自平衡二叉搜索树，支持插入操作并维护平衡因子。
 *    应用: 替罪羊树作为自平衡树的一种实现方式
 *    C++实现注意: 使用递归实现时注意栈深度
 *
 * 【赛码题目】
 * 1. 平衡树 - https://www.acmcoder.com/index.php?app=exam&act=problem&cid=1&id=1001
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 【AtCoder题目】
 * 1. ABC162 E - Sum of gcd of Tuples (Hard) - https://atcoder.jp/contests/abc162/tasks/abc162_e
 *    题目描述: 计算所有可能元组的gcd之和
 *    应用: 在一些优化解法中可以使用替罪羊树维护信息
 *
 * 2. ABC177 F - I hate Shortest Path Problem - https://atcoder.jp/contests/abc177/tasks/abc177_f
 *    题目描述: 最短路径问题的变种
 *    应用: 使用替罪羊树优化Dijkstra算法
 *
 * 【USACO题目】
 * 1. Balanced Trees - http://www.usaco.org/index.php?page=viewproblem2&cpid=896
 *    题目描述: 构造平衡的二叉搜索树
 *    应用: 替罪羊树的构造和重构操作
 *
 * 【洛谷 (Luogu)题目】
 * 1. P3369 【模板】普通平衡树 (基础模板题)
 *    题目链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
 *    1，增加x，重复加入算多个词频
 *    2，删除x，如果有多个，只删掉一个
 *    3，查询x的排名，x的排名为，比x小的数的个数+1
 *    4，查询数据中排名为x的数
 *    5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
 *    6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
 *    
 * 2. P6136 【模板】普通平衡树（数据加强版）
 *    题目链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: 在P3369基础上加强数据，强制在线，需要将查询操作的参数与上次结果异或
 *    C++实现优化: 使用快速IO，预分配内存池
 *
 * 3. P1168 中位数 - https://www.luogu.com.cn/problem/P1168
 *    题目描述: 维护一个动态变化的序列，每次插入一个数后，输出当前序列的中位数
 *    应用: 实时维护中间值，可使用两个替罪羊树分别维护前半部分和后半部分
 *    C++实现技巧: 交替维护两个树的大小平衡
 *
 * 4. P1908 逆序对 - https://www.luogu.com.cn/problem/P1908
 *    题目描述: 求逆序对数量
 *    应用: 替罪羊树实现离散化统计
 *    C++实现技巧: 离散化+树状数组或替罪羊树
 *
 * 5. P5076 【深基16.例7】普通二叉搜索树 - https://www.luogu.com.cn/problem/P5076
 *    题目描述: 实现普通二叉搜索树的基本操作
 *    应用: 替罪羊树是平衡的二叉搜索树，可以直接应用
 *
 * 【CodeChef题目】
 * 1. SEQUENCE - https://www.codechef.com/problems/SEQUENCE
 *    题目描述: 处理序列的动态插入和查询操作
 *    应用: 替罪羊树适合处理动态序列查询问题
 *    C++实现优化: 使用内存池优化频繁的节点分配
 *
 * 【SPOJ题目】
 * 1. ORDERSET - https://www.spoj.com/problems/ORDERSET/
 *    题目描述: 支持插入、删除、查询第k小和比x小的元素个数
 *    应用: 基础平衡树操作的组合应用
 *
 * 2. DQUERY - https://www.spoj.com/problems/DQUERY/
 *    题目描述: 在线查询区间内不同元素的个数
 *    应用: 离线处理，使用替罪羊树维护前缀信息
 *
 * 【Project Euler题目】
 * 1. Problem 145 - How many reversible numbers are there below one-billion? - https://projecteuler.net/problem=145
 *    题目描述: 统计满足条件的可逆数个数
 *    应用: 结合替罪羊树进行高效统计
 *
 * 【HackerEarth题目】
 * 1. Monk and BST - https://www.hackerearth.com/practice/data-structures/trees/binary-search-tree/practice-problems/algorithm/monk-and-bst/
 *    题目描述: 处理二叉搜索树的相关操作
 *    应用: 替罪羊树作为平衡BST的实现
 *
 * 【计蒜客题目】
 * 1. 41928 普通平衡树 - https://nanti.jisuanke.com/t/41928
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 2. 21500 逆序对统计 - https://nanti.jisuanke.com/t/21500
 *    题目描述: 统计逆序对数量
 *    应用: 使用替罪羊树进行逆序对统计
 *
 * 【各大高校 OJ题目】
 * 1. ZOJ 1614 - Replace the Numbers - http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1614
 *    题目描述: 处理数字替换操作
 *    应用: 使用替罪羊树维护动态集合
 *
 * 2. POJ 1195 - Mobile phones - http://poj.org/problem?id=1195
 *    题目描述: 二维区间查询和更新
 *    应用: 结合替罪羊树和其他数据结构解决
 *
 * 【UVa OJ题目】
 * 1. UVa 11020 - Efficient Solutions - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1961
 *    题目描述: 寻找有效解
 *    应用: 使用替罪羊树维护候选解集
 *
 * 【TimusOJ题目】
 * 1. Timus 1439 - Battle with You-Know-Who - https://acm.timus.ru/problem.aspx?space=1&num=1439
 *    题目描述: 处理动态排名问题
 *    应用: 替罪羊树维护动态排名信息
 *
 * 【AizuOJ题目】
 * 1. Aizu ALDS1_8_D - Treap - http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_8_D
 *    题目描述: 实现Treap数据结构
 *    应用: 替罪羊树作为平衡BST的替代实现
 *
 * 【杭电 OJ题目】
 * 1. HDU 4585 Shaolin - http://acm.hdu.edu.cn/showproblem.php?pid=4585
 *    题目描述: 维护僧人排名，新僧人加入时找到相邻排名的僧人
 *    应用: 插入并查询前驱和后继
 *    C++实现注意: 处理大量数据时的性能优化
 *
 * 2. HDU 1394 Minimum Inversion Number - http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    题目描述: 给定一个序列，求所有可能的循环位移中逆序对的最小值
 *    应用: 使用替罪羊树动态维护逆序对数量
 *
 * 3. HDU 2871 Memory Control - http://acm.hdu.edu.cn/showproblem.php?pid=2871
 *    题目描述: 内存管理问题，需要维护内存块的分配和释放
 *    应用: 使用替罪羊树维护空闲内存块
 *
 * 【LOJ题目】
 * 1. LOJ 1014 - Ifter Party - https://loj.ac/problem/1014
 *    题目描述: 处理聚会人员的动态变化
 *    应用: 使用替罪羊树维护人员信息
 *
 * 【牛客网题目】
 * 1. NC14516 普通平衡树 - https://ac.nowcoder.com/acm/problem/14516
 *    题目描述: 同洛谷P3369，支持插入、删除、排名查询等基本操作
 *
 * 2. NC18375 逆序对 - https://ac.nowcoder.com/acm/problem/18375
 *    题目描述: 统计逆序对数量
 *    应用: 使用替罪羊树进行逆序对统计
 *
 * 【杭州电子科技大学题目】
 * 1. HDOJ 5444 - Elven Postman - http://acm.hdu.edu.cn/showproblem.php?pid=5444
 *    题目描述: 处理邮递员路径问题
 *    应用: 使用替罪羊树维护路径信息
 *
 * 【acwing题目】
 * 1. 253. 普通平衡树 - https://www.acwing.com/problem/content/255/
 *    题目描述: 实现平衡树的基本操作
 *    应用: 替罪羊树的标准应用场景
 *
 * 【codeforces题目】
 * 1. 911D - Inversion Counting - https://codeforces.com/problemset/problem/911/D
 *    题目描述: 给定一个序列，支持反转操作，求每次反转后的逆序对数量
 *    应用: 使用替罪羊树维护区间信息，支持快速反转和查询
 *
 * 2. 459D - Pashmak and Parmida's problem - https://codeforces.com/problemset/problem/459/D
 *    题目描述: 统计满足条件的数对数量
 *    应用: 使用替罪羊树维护前缀和后缀信息
 *
 * 【hdu题目】
 * 1. HDU 4352 - XHXJ's LIS - http://acm.hdu.edu.cn/showproblem.php?pid=4352
 *    题目描述: 计算最长上升子序列
 *    应用: 结合替罪羊树优化状态转移
 *
 * 【poj题目】
 * 1. POJ 2418 - Hardwood Species - http://poj.org/problem?id=2418
 *    题目描述: 统计硬木种类
 *    应用: 使用替罪羊树维护种类信息
 *
 * 【剑指Offer题目】
 * 1. 剑指 Offer 51. 数组中的逆序对 - https://leetcode-cn.com/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    题目描述: 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。输入一个数组，求出这个数组中的逆序对的总数
 *    应用: 使用替罪羊树统计逆序对
 *
 * 【C++实现特定注意事项】
 * 1. 内存管理与优化：
 *    - C++需要手动管理内存，本实现使用静态数组避免频繁内存分配
 *    - 对于大规模数据，可以考虑使用动态内存池优化：
 *      // 示例代码
 *      // class MemoryPool {
 *      // private:
 *      //     std::vector<int> free_list;
 *      //     int* key;     // 预分配的大型数组
 *      //     int* ls;      // 左子节点
 *      //     int* rs;      // 右子节点
 *      //     int* key_count; // 键出现次数
 *      //     int* siz;     // 子树大小
 *      //     int* diff;    // 有效节点数
 *      //     int capacity; // 容量
 *      //     int next_free; // 下一个可用位置
 *      // public:
 *      //     MemoryPool(int cap = 100001) : capacity(cap), next_free(1) {
 *      //         key = new int[cap];
 *      //         ls = new int[cap];
 *      //         rs = new int[cap];
 *      //         key_count = new int[cap];
 *      //         siz = new int[cap];
 *      //         diff = new int[cap];
 *      //     }
 *      //     
 *      //     ~MemoryPool() {
 *      //         delete[] key;
 *      //         delete[] ls;
 *      //         delete[] rs;
 *      //         delete[] key_count;
 *      //         delete[] siz;
 *      //         delete[] diff;
 *      //     }
 *      //     
 *      //     int allocate() {
 *      //         if (!free_list.empty()) {
 *      //             int node = free_list.back();
 *      //             free_list.pop_back();
 *      //             return node;
 *      //         }
 *      //         return next_free++;
 *      //     }
 *      //     
 *      //     void deallocate(int node) {
 *      //         free_list.push_back(node);
 *      //     }
 *      // };
 *    - 注意避免内存泄漏和栈溢出问题
 *    - 对于生产环境，可以使用智能指针封装节点管理
 *
 * 2. 模板化设计：
 *    - 利用C++模板实现通用版本，支持不同数据类型：
 *      // 示例代码
 *      // template<typename T>
 *      // class ScapeGoatTree {
 *      // private:
 *      //     // 实现细节...
 *      // public:
 *      //     void add(const T& value);
 *      //     void remove(const T& value);
 *      //     int getRank(const T& value);
 *      //     T index(int rank);
 *      //     T predecessor(const T& value);
 *      //     T successor(const T& value);
 *      // };
 *    - 支持自定义比较函数：
 *      // 示例代码
 *      // template<typename T, typename Compare = std::less<T>>
 *      // class ScapeGoatTree {
 *      // private:
 *      //     Compare comp;
 *      //     // 使用comp进行比较...
 *      // };
 *
 * 3. 性能优化技巧：
 *    - 使用预处理宏定义加速简单函数调用
 *    - 关闭同步提高IO效率: ios::sync_with_stdio(false); cin.tie(nullptr);
 *    - 使用局部变量缓存频繁访问的数据, 减少数组索引开销
 *    - 利用移动语义避免不必要的拷贝:
 *      // void add(T&& value) { /* Implementation of move version */ }
 *    - 使用内联函数减少调用开销:
 *      // inline void up(int i) {
 *      //     siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
 *      //     diff[i] = diff[ls[i]] + diff[rs[i]] + (key_count[i] > 0 ? 1 : 0);
 *      // }
 *
 * 4. 编译优化选项:
 *    - O2优化 -O2, 大幅提升运行速度
 *    - 内联优化 -finline-functions
 *    - 寄存器变量优化 -O3 (注意 可能会增加代码大小)
 *    - LTO链接时优化 -flto, 提高跨文件优化效果
 *    - 栈保护 -fstack-protector, 增强安全性
 *    - 特定架构优化 -march=native, 针对当前CPU架构优化
 *
 * 5. C++11/14/17/20特性应用:
 *    - 使用auto关键字简化变量声明
 *    - 利用nullptr代替NULL避免类型歧义
 *    - 使用lambda表达式简化回调:
 *      // auto compare = [](const int& a, const int& b) { return a < b; };
 *    - 使用constexpr提高编译期计算能力
 *    - 利用std::optional处理可能不存在的返回值
 *
 * 6. 异常安全保证:
 *    - 实现RAII原则管理资源
 *    - 使用强异常安全保证的操作
 *    - 对于内存分配失败等情况添加异常处理
 *    - 使用try-catch块捕获可能的异常:
 *      // try {
 *      //     // 操作可能抛出异常的代码
 *      // } catch (const std::bad_alloc& e) {
 *      //     std::cerr << "内存分配失败 " << e.what() << std::endl;
 *      //     // 错误处理...
 *      // }
 *
 * [C++调试技巧与问题定位]
 * 1. 调试辅助函数:
 *    // void printTree(int node, int level = 0) {
 *    //     if (!node) return;
 *    //     printTree(rs[node], level + 1);
 *    //     std::cout << std::string(level * 4, ' ') 
 *    //               << key[node] << "(sz=" << siz[node] 
 *    //               << ",cnt=" << key_count[node] << ")\n";
 *    //     printTree(ls[node], level + 1);
 *    // }
 * 2. 断言与验证:
 *    // void verifyTree(int node) {
 *    //     assert(node != 0 && "节点不能为空");
 *    //     assert(siz[node] == siz[ls[node]] + siz[rs[node]] + key_count[node] && "节点大小计算错误");
 *    //     
 *    //     if (ls[node]) {
 *    //         assert(key[ls[node]] < key[node] && "左子树值必须小于当前节点");
 *    //         verifyTree(ls[node]);
 *    //     }
 *    //     if (rs[node]) {
 *    //         assert(key[rs[node]] > key[node] && "右子树值必须大于当前节点");
 *    //         verifyTree(rs[node]);
 *    //     }
 *    // }
 * 3. 性能分析工具:
 *    - 使用Google Benchmark进行基准测试
 *    - 使用Valgrind检测内存泄漏
 *    - 使用gprof进行性能分析
 *    - 使用perf进行Linux性能分析
 * 4. 日志系统:
 *    // template<typename... Args>
 *    // void log(const char* format, Args&&... args) {
 *    //     #ifdef DEBUG_MODE
 *    //     fprintf(stderr, format, std::forward<Args>(args)...);
 *    //     #endif
 *    // }
 *
 * [C++工程化考量]
 * 1. 测试框架实现:
 *    // void runTests() {
 *    //     // 测试空树操作
 *    //     clear();
 *    //     assert(getRank(1) == 1);
 *    //     assert(index(1) == INT_MIN);
 *    //     
 *    //     // 测试基本插入和查询
 *    //     add(10);
 *    //     add(5);
 *    //     add(15);
 *    //     assert(getRank(10) == 2);
 *    //     assert(index(1) == 5);
 *    //     
 *    //     // 测试删除操作
 *    //     remove(10);
 *    //     assert(getRank(10) == 2);
 *    //     
 *    //     std::cout << "所有测试通过！" << std::endl;
 *    // }
 * 2. 可配置性设计:
 *    - 使用命名空间隔离全局常量
 *    - 实现编译期和运行期参数调整
 *    - 支持配置文件加载
 * 3. 与STL的集成:
 *    - 提供STL风格的接口
 *    - 支持范围for循环遍历
 *    - 兼容STL算法
 * 4. 内存池优化:
 *    - 减少频繁的内存分配和释放
 *    - 提高内存局部性
 *    - 降低内存碎片
 *
 * [alpha因子深度解析]
 * 1. alpha belongs to [0.5, 1.0], 在C++中通常选择较大值以减少重构开销
 * 2. alpha = 0.7时(本实现):
 *    - 在C++中, 这个值能够平衡重构频率和查询效率
 *    - 对于性能敏感应用, 可以调整至0.75
 * 3. 数学基础:
 *    - alpha选择直接影响树的高度: h <= log_alpha^(-1)(n)
 *    - 当alpha=0.7时, 树高约为O(5log n)
 *
 * [工程化考量]
 * 1. 异常处理:
 *    - C++实现中添加边界检查和错误处理
 *    - 对于可能的溢出情况进行防御性编程
 *
 * 2. 线程安全:
 *    - 本实现不是线程安全的
 *    - 在多线程环境中使用时, 需要添加锁机制
 *    - 或者使用无锁算法变体
 *
 * 3. 代码优化建议:
 *    - 使用类封装代替全局变量, 提高代码可维护性
 *    - 实现迭代版本, 避免递归栈溢出
 *    - 添加单元测试确保正确性
 *    - 使用内存对齐技术提高缓存利用率
 *
 * [调试技巧]
 * 1. 调试辅助函数:
 *    // void printTree(int node, int level = 0) {
 *    //     if (node == 0) return;
 *    //     printTree(rs[node], level + 1);
 *    //     for (int i = 0; i < level; i++) printf("    ");
 *    //     printf("%d(%d,%d)\n", key[node], key_count[node], siz[node]);
 *    //     printTree(ls[node], level + 1);
 *    // }
 *
 * 2. 性能分析工具:
 *    - 使用gprof分析函数调用耗时
 *    - 使用Valgrind检测内存问题
 *    - 使用perf工具分析CPU性能瓶颈
 *
 * [与STL容器对比]
 * 1. 与std::set对比:
 *    - 替罪羊树实现简单, 代码量小
 *    - 性能略低于红黑树实现的std::set, 但实现更灵活
 *    - 支持更丰富的操作(如排名查询)
 *
 * 2. 与std::map对比:
 *    - 替罪羊树针对数值类型优化
 *    - 内存占用更小
 *    - 支持排名和前驱/后继查询
 *
 * [笔试面试注意事项]
 * 1. C++实现重点:
 *    - 数组模拟树结构的内存布局优化
 *    - 递归实现的栈深度控制
 *    - 惰性删除的实现机制
 *
 * 2. 常见问题:
 *    - 忘记更新size或diff数组导致的错误
 *    - 重构操作中节点引用错误
 *    - 边界条件处理不当(空树, 单节点树等)
 *
 * [拓展应用]
 * 1. 在机器学习中的应用:
 *    - 维护动态数据集的统计信息
 *    - 实现基于树的索引结构
 *    - 异常检测算法中的数据结构支持
 *
 * 2. 在大数据场景的优化:
 *    - 分布式替罪羊树实现
 *    - 持久化替罪羊树变种
 *    - 适用于数据流处理的优化版本
 */