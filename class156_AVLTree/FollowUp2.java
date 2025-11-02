package class148;

// AVL实现普通有序表，数据加强的测试，C++版
// 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
// 基本功能要求都是不变的，可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P6136
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

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

//#include <iostream>
//#include <algorithm>
//#include <climits>
//#include <cstring>
//
//using namespace std;
//
//const int MAXN = 2000001;
//
//int cnt = 0;
//int head = 0;
//int key[MAXN];
//int height[MAXN];
//int ls[MAXN];
//int rs[MAXN];
//int key_count[MAXN];
//int siz[MAXN];
//
//void up(int i) {
//    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
//    height[i] = max(height[ls[i]], height[rs[i]]) + 1;
//}
//
//int leftRotate(int i) {
//    int r = rs[i];
//    rs[i] = ls[r];
//    ls[r] = i;
//    up(i);
//    up(r);
//    return r;
//}
//
//int rightRotate(int i) {
//    int l = ls[i];
//    ls[i] = rs[l];
//    rs[l] = i;
//    up(i);
//    up(l);
//    return l;
//}
//
//int maintain(int i) {
//    int lh = height[ls[i]];
//    int rh = height[rs[i]];
//    if (lh - rh > 1) {
//        if (height[ls[ls[i]]] >= height[rs[ls[i]]]) {
//            i = rightRotate(i);
//        } else {
//            ls[i] = leftRotate(ls[i]);
//            i = rightRotate(i);
//        }
//    } else if (rh - lh > 1) {
//        if (height[rs[rs[i]]] >= height[ls[rs[i]]]) {
//            i = leftRotate(i);
//        } else {
//            rs[i] = rightRotate(rs[i]);
//            i = leftRotate(i);
//        }
//    }
//    return i;
//}
//
//int add(int i, int num) {
//    if (i == 0) {
//        key[++cnt] = num;
//        key_count[cnt] = siz[cnt] = height[cnt] = 1;
//        return cnt;
//    }
//    if (key[i] == num) {
//        key_count[i]++;
//    } else if (key[i] > num) {
//        ls[i] = add(ls[i], num);
//    } else {
//        rs[i] = add(rs[i], num);
//    }
//    up(i);
//    return maintain(i);
//}
//
//void add(int num) {
//    head = add(head, num);
//}
//
//int getRank(int i, int num) {
//    if (i == 0) {
//        return 0;
//    }
//    if (key[i] >= num) {
//        return getRank(ls[i], num);
//    } else {
//        return siz[ls[i]] + key_count[i] + getRank(rs[i], num);
//    }
//}
//
//int getRank(int num) {
//    return getRank(head, num) + 1;
//}
//
//int removeMostLeft(int i, int mostLeft) {
//    if (i == mostLeft) {
//        return rs[i];
//    } else {
//        ls[i] = removeMostLeft(ls[i], mostLeft);
//        up(i);
//        return maintain(i);
//    }
//}
//
//int remove(int i, int num) {
//    if (key[i] < num) {
//        rs[i] = remove(rs[i], num);
//    } else if (key[i] > num) {
//        ls[i] = remove(ls[i], num);
//    } else {
//        if (key_count[i] > 1) {
//            key_count[i]--;
//        } else {
//            if (ls[i] == 0 && rs[i] == 0) {
//                return 0;
//            } else if (ls[i] != 0 && rs[i] == 0) {
//                i = ls[i];
//            } else if (ls[i] == 0 && rs[i] != 0) {
//                i = rs[i];
//            } else {
//                int mostLeft = rs[i];
//                while (ls[mostLeft] != 0) {
//                    mostLeft = ls[mostLeft];
//                }
//                rs[i] = removeMostLeft(rs[i], mostLeft);
//                ls[mostLeft] = ls[i];
//                rs[mostLeft] = rs[i];
//                i = mostLeft;
//            }
//        }
//    }
//    up(i);
//    return maintain(i);
//}
//
//void remove(int num) {
//    if (getRank(num) != getRank(num + 1)) {
//        head = remove(head, num);
//    }
//}
//
//int index(int i, int x) {
//    if (siz[ls[i]] >= x) {
//        return index(ls[i], x);
//    } else if (siz[ls[i]] + key_count[i] < x) {
//        return index(rs[i], x - siz[ls[i]] - key_count[i]);
//    }
//    return key[i];
//}
//
//int index(int x) {
//    return index(head, x);
//}
//
//int pre(int i, int num) {
//    if (i == 0) {
//        return INT_MIN;
//    }
//    if (key[i] >= num) {
//        return pre(ls[i], num);
//    } else {
//        return max(key[i], pre(rs[i], num));
//    }
//}
//
//int pre(int num) {
//    return pre(head, num);
//}
//
//int post(int i, int num) {
//    if (i == 0) {
//        return INT_MAX;
//    }
//    if (key[i] <= num) {
//        return post(rs[i], num);
//    } else {
//        return min(key[i], post(ls[i], num));
//    }
//}
//
//int post(int num) {
//    return post(head, num);
//}
//
//void clear() {
//    memset(key + 1, 0, cnt * sizeof(int));
//    memset(height + 1, 0, cnt * sizeof(int));
//    memset(ls + 1, 0, cnt * sizeof(int));
//    memset(rs + 1, 0, cnt * sizeof(int));
//    memset(key_count + 1, 0, cnt * sizeof(int));
//    memset(siz + 1, 0, cnt * sizeof(int));
//    cnt = 0;
//    head = 0;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    int n, m, lastAns = 0, ans = 0;
//    cin >> n;
//    cin >> m;
//    for (int i = 1, num; i <= n; i++) {
//        cin >> num;
//        add(num);
//    }
//    for (int i = 1, op, x; i <= m; i++) {
//        cin >> op >> x;
//        x ^= lastAns;
//        if (op == 1) {
//            add(x);
//        } else if (op == 2) {
//            remove(x);
//        } else if (op == 3) {
//            lastAns = getRank(x);
//            ans ^= lastAns;
//        } else if (op == 4) {
//            lastAns = index(x);
//            ans ^= lastAns;
//        } else if (op == 5) {
//            lastAns = pre(x);
//            ans ^= lastAns;
//        } else {
//            lastAns = post(x);
//            ans ^= lastAns;
//        }
//    }
//    cout << ans << endl;
//    clear();
//    return 0;
//}