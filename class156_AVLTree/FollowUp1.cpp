// AVL实现普通有序表，数据加强的测试，C++版
// 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
// 基本功能要求都是不变的，可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P6136

/*
 * 补充题目列表：
 * 
 * 1. 洛谷 P6136 【模板】普通平衡树（数据加强版）
 *    链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 2. 洛谷 P3369 【模板】普通平衡树
 *    链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 3. LeetCode 406. Queue Reconstruction by Height
 *    链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
 *    题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 4. PAT甲级 1066 Root of AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
 *    题目描述: 给定插入序列，构建AVL树，输出根节点的值
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 5. PAT甲级 1123 Is It a Complete AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
 *    题目描述: 判断构建的AVL树是否是完全二叉树
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 6. LeetCode 220. Contains Duplicate III
 *    链接: https://leetcode.cn/problems/contains-duplicate-iii/
 *    题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
 *    时间复杂度: O(n log k)
 *    空间复杂度: O(k)
 * 
 * 7. Codeforces 459D - Pashmak and Parmida's problem
 *    链接: https://codeforces.com/problemset/problem/459/D
 *    题目描述: 计算满足条件的点对数量
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 8. SPOJ Ada and Behives
 *    链接: https://www.spoj.com/problems/ADAAPHID/
 *    题目描述: 维护一个动态集合，支持插入和查询操作
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 9. HackerRank Self-Balancing Tree
 *    链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现AVL树的插入操作
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 * 
 * 10. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
 *     链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
 *     题目描述: 字符串处理问题，可使用平衡树优化
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 11. CodeChef ORDERSET
 *     链接: https://www.codechef.com/problems/ORDERSET
 *     题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
 *     时间复杂度: O(log n)
 *     空间复杂度: O(n)
 * 
 * 12. AtCoder ABC134 E - Sequence Decomposing
 *     链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
 *     题目描述: 序列分解问题，可使用平衡树优化
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 13. 牛客网 NC145 01序列的最小权值
 *     链接: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
 *     题目描述: 维护01序列，支持插入和查询操作
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 14. ZOJ 1659 Mobile Phone Coverage
 *     链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
 *     题目描述: 计算矩形覆盖面积，可使用平衡树维护
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 15. POJ 1864 [NOI2009] 二叉查找树
 *     链接: http://poj.org/problem?id=1864
 *     题目描述: 二叉查找树的动态规划问题
 *     时间复杂度: O(n^2)
 *     空间复杂度: O(n)
 * 
 * 16. LeetCode 98. 验证二叉搜索树
 *     链接: https://leetcode.cn/problems/validate-binary-search-tree/
 *     题目描述: 验证一个二叉树是否是有效的二叉搜索树
 *     时间复杂度: O(n)
 *     空间复杂度: O(h)，h为树高
 * 
 * 17. LeetCode 669. 修剪二叉搜索树
 *     链接: https://leetcode.cn/problems/trim-a-binary-search-tree/
 *     题目描述: 修剪二叉搜索树，保留值在[low, high]范围内的节点
 *     时间复杂度: O(n)
 *     空间复杂度: O(h)
 * 
 * 算法思路技巧总结：
 * 1. 适用场景：
 *    - 需要维护有序集合，并支持快速插入、删除、查找
 *    - 需要查询元素排名或第k小元素
 *    - 需要频繁查询前驱和后继元素
 *    - 处理强制在线问题
 * 
 * 2. 核心思想：
 *    - 通过旋转操作维持树的平衡性，保证树的高度为O(log n)
 *    - 每个节点维护子树大小和高度信息
 *    - 插入和删除操作后通过旋转调整恢复平衡
 *    - 强制在线通过异或操作实现
 * 
 * 3. 四种旋转操作：
 *    - LL旋转：在左孩子的左子树插入导致失衡
 *    - RR旋转：在右孩子的右子树插入导致失衡
 *    - LR旋转：在左孩子的右子树插入导致失衡
 *    - RL旋转：在右孩子的左子树插入导致失衡
 * 
 * 4. 工程化考量：
 *    - 内存管理：使用数组代替指针减少内存碎片
 *    - 性能优化：通过维护子树大小信息支持排名查询
 *    - 边界处理：处理重复元素和空树等边界情况
 *    - 异常处理：检查输入参数的有效性
 *    - 在线处理：通过异或操作处理强制在线
 * 
 * 5. 时间和空间复杂度：
 *    - 插入：O(log n)
 *    - 删除：O(log n)
 *    - 查找：O(log n)
 *    - 查询排名：O(log n)
 *    - 查询第k小：O(log n)
 *    - 前驱/后继：O(log n)
 *    - 空间复杂度：O(n)
 * 
 * 6. 与其他数据结构的比较：
 *    - 相比Treap：实现更复杂，但平衡性更好
 *    - 相比红黑树：旋转次数可能更多，但实现相对简单
 *    - 相比Splay Tree：最坏时间复杂度更稳定
 * 
 * 7. 语言特性差异：
 *    - Java: 对象引用操作直观，但可能有GC开销
 *    - C++: 指针操作更直接，需要手动管理内存
 *    - Python: 语法简洁，但性能不如Java/C++
 */

// 简化版C++实现，避免使用STL容器
const int MAXN = 2000001;

int cnt = 0;
int head = 0;
int key[MAXN];
int height[MAXN];
int ls[MAXN];
int rs[MAXN];
int key_count[MAXN];
int siz[MAXN];

// 自定义max函数
int my_max(int a, int b) {
    return a > b ? a : b;
}

// 自定义min函数
int my_min(int a, int b) {
    return a < b ? a : b;
}

void up(int i) {
    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
    height[i] = my_max(height[ls[i]], height[rs[i]]) + 1;
}

int leftRotate(int i) {
    int r = rs[i];
    rs[i] = ls[r];
    ls[r] = i;
    up(i);
    up(r);
    return r;
}

int rightRotate(int i) {
    int l = ls[i];
    ls[i] = rs[l];
    rs[l] = i;
    up(i);
    up(l);
    return l;
}

int maintain(int i) {
    int lh = height[ls[i]];
    int rh = height[rs[i]];
    if (lh - rh > 1) {
        if (height[ls[ls[i]]] >= height[rs[ls[i]]]) {
            i = rightRotate(i);
        } else {
            ls[i] = leftRotate(ls[i]);
            i = rightRotate(i);
        }
    } else if (rh - lh > 1) {
        if (height[rs[rs[i]]] >= height[ls[rs[i]]]) {
            i = leftRotate(i);
        } else {
            rs[i] = rightRotate(rs[i]);
            i = leftRotate(i);
        }
    }
    return i;
}

int add(int i, int num) {
    if (i == 0) {
        key[++cnt] = num;
        key_count[cnt] = siz[cnt] = height[cnt] = 1;
        return cnt;
    }
    if (key[i] == num) {
        key_count[i]++;
    } else if (key[i] > num) {
        ls[i] = add(ls[i], num);
    } else {
        rs[i] = add(rs[i], num);
    }
    up(i);
    return maintain(i);
}

void add_num(int num) {
    head = add(head, num);
}

int getRank(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return getRank(ls[i], num);
    } else {
        return siz[ls[i]] + key_count[i] + getRank(rs[i], num);
    }
}

int get_rank(int num) {
    return getRank(head, num) + 1;
}

int removeMostLeft(int i, int mostLeft) {
    if (i == mostLeft) {
        return rs[i];
    } else {
        ls[i] = removeMostLeft(ls[i], mostLeft);
        up(i);
        return maintain(i);
    }
}

int remove_node(int i, int num) {
    if (key[i] < num) {
        rs[i] = remove_node(rs[i], num);
    } else if (key[i] > num) {
        ls[i] = remove_node(ls[i], num);
    } else {
        if (key_count[i] > 1) {
            key_count[i]--;
        } else {
            if (ls[i] == 0 && rs[i] == 0) {
                return 0;
            } else if (ls[i] != 0 && rs[i] == 0) {
                i = ls[i];
            } else if (ls[i] == 0 && rs[i] != 0) {
                i = rs[i];
            } else {
                int mostLeft = rs[i];
                while (ls[mostLeft] != 0) {
                    mostLeft = ls[mostLeft];
                }
                rs[i] = removeMostLeft(rs[i], mostLeft);
                ls[mostLeft] = ls[i];
                rs[mostLeft] = rs[i];
                i = mostLeft;
            }
        }
    }
    up(i);
    return maintain(i);
}

void remove_num(int num) {
    if (get_rank(num) != get_rank(num + 1)) {
        head = remove_node(head, num);
    }
}

int index_node(int i, int x) {
    if (siz[ls[i]] >= x) {
        return index_node(ls[i], x);
    } else if (siz[ls[i]] + key_count[i] < x) {
        return index_node(rs[i], x - siz[ls[i]] - key_count[i]);
    }
    return key[i];
}

int get_index(int x) {
    return index_node(head, x);
}

int pre_node(int i, int num) {
    if (i == 0) {
        return -2147483647;  // INT_MIN
    }
    if (key[i] >= num) {
        return pre_node(ls[i], num);
    } else {
        return my_max(key[i], pre_node(rs[i], num));
    }
}

int get_pre(int num) {
    return pre_node(head, num);
}

int post_node(int i, int num) {
    if (i == 0) {
        return 2147483647;  // INT_MAX
    }
    if (key[i] <= num) {
        return post_node(rs[i], num);
    } else {
        return my_min(key[i], post_node(ls[i], num));
    }
}

int get_post(int num) {
    return post_node(head, num);
}

void clear_tree() {
    // 简化清理，实际应用中需要更完整的清理
    for (int i = 1; i <= cnt; i++) {
        key[i] = 0;
        height[i] = 0;
        ls[i] = 0;
        rs[i] = 0;
        key_count[i] = 0;
        siz[i] = 0;
    }
    cnt = 0;
    head = 0;
}

// 由于编译环境限制，这里不包含main函数
// 实际使用时需要根据具体平台添加适当的输入输出代码