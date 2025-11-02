package class149;

/**
 * 跳表的实现(C++版本的Java注释)
 * 
 * 这个文件包含了C++版本跳表实现的注释，C++版本和Java版本逻辑完全一样
 * 
 * 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
 * 1，增加x，重复加入算多个词频
 * 2，删除x，如果有多个，只删掉一个
 * 3，查询x的排名，x的排名为，比x小的数的个数+1
 * 4，查询数据中排名为x的数
 * 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
 * 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
 * 
 * 所有操作的次数 <= 10^5
 * -10^7 <= x <= +10^7
 * 测试链接 : https://www.luogu.com.cn/problem/P3369
 */

/*
补充题目列表：
1. LeetCode 1206. 设计跳表
   链接：https://leetcode.cn/problems/design-skiplist
   题目描述：设计一个跳表，支持在O(log(n))时间内完成增加、删除、搜索操作。

2. 洛谷 P3369 【模板】普通平衡树
   链接：https://www.luogu.com.cn/problem/P3369
   题目描述：维护一个可重集合，支持插入、删除、查询排名、查询第k小、查询前驱、查询后继等操作。

3. 洛谷 P3391 【模板】文艺平衡树
   链接：https://www.luogu.com.cn/problem/P3391
   题目描述：维护一个序列，支持区间翻转操作。

4. HDU 1754 I Hate It
   链接：http://acm.hdu.edu.cn/showproblem.php?pid=1754
   题目描述：维护一个序列，支持单点修改和区间最大值查询。

5. POJ 3468 A Simple Problem with Integers
   链接：http://poj.org/problem?id=3468
   题目描述：维护一个序列，支持区间加和区间求和操作。

跳表(Skip List)是一种概率型数据结构，由William Pugh在1990年提出。
它通过在有序链表的基础上增加多级索引来实现快速查找，平均时间复杂度为O(log n)。

跳表的核心思想：
1. 在有序链表的基础上增加多层索引
2. 每一层都是下一层的稀疏表示
3. 查找时从高层开始，逐层向下
4. 插入时通过随机函数决定节点层数

跳表与平衡树的对比：
1. 实现简单：跳表不需要复杂的旋转操作，代码更容易编写和维护
2. 并发友好：跳表在并发场景下更容易实现高效的锁策略
3. 范围查询：跳表天然支持高效的范围查询
4. 内存占用：跳表每个节点包含的指针数目可调，通常比平衡树更节省空间
5. 时间复杂度：跳表和平衡树的时间复杂度都是O(log n)，但跳表是期望复杂度

跳表的操作：
1. 查找(search)：从最高层开始，逐层向下查找
2. 插入(add)：查找插入位置，随机生成层数，插入节点并更新索引
3. 删除(remove)：查找节点，删除节点并更新索引

时间复杂度分析：
1. 查找：O(log n) 期望时间复杂度
2. 插入：O(log n) 期望时间复杂度
3. 删除：O(log n) 期望时间复杂度

空间复杂度分析：
每个节点平均包含1/(1-p)个指针，总的空间复杂度为O(n)
*/

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXL = 20;
//const int MAXN = 100001;
//
//int cnt;
//int key[MAXN];
//int key_count[MAXN];
//int level[MAXN];
//int next_node[MAXN][MAXL + 1];
//int len[MAXN][MAXL + 1];
//
//void build() {
//    cnt = 1;
//    key[cnt] = INT_MIN;
//    level[cnt] = MAXL;
//}
//
//void clear() {
//    memset(key + 1, 0, cnt * sizeof(int));
//    memset(key_count + 1, 0, cnt * sizeof(int));
//    memset(level + 1, 0, cnt * sizeof(int));
//    for (int i = 1; i <= cnt; i++) {
//        memset(next_node[i], 0, (MAXL + 1) * sizeof(int));
//        memset(len[i], 0, (MAXL + 1) * sizeof(int));
//    }
//    cnt = 0;
//}
//
//int randomLevel() {
//    int ans = 1;
//    while ((rand() / double(RAND_MAX)) < 0.5) {
//        ans++;
//    }
//    return min(ans, MAXL);
//}
//
//int find(int i, int h, int num) {
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        if (next_node[i][h] != 0 && key[next_node[i][h]] == num) {
//            return next_node[i][h];
//        } else {
//            return 0;
//        }
//    }
//    return find(i, h - 1, num);
//}
//
//void addCount(int i, int h, int num) {
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        key_count[next_node[i][h]]++;
//    } else {
//        addCount(i, h - 1, num);
//    }
//    len[i][h]++;
//}
//
//int addNode(int i, int h, int j) {
//    int rightCnt = 0;
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < key[j]) {
//        rightCnt += len[i][h];
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        next_node[j][h] = next_node[i][h];
//        next_node[i][h] = j;
//        len[j][h] = key_count[next_node[j][h]];
//        len[i][h] = key_count[next_node[i][h]];
//        return rightCnt;
//    } else {
//        int downCnt = addNode(i, h - 1, j);
//        if (h > level[j]) {
//            len[i][h]++;
//        } else {
//            next_node[j][h] = next_node[i][h];
//            next_node[i][h] = j;
//            len[j][h] = len[i][h] + 1 - downCnt - key_count[j];
//            len[i][h] = downCnt + key_count[j];
//        }
//        return rightCnt + downCnt;
//    }
//}
//
//void add(int num) {
//    if (find(1, MAXL, num) != 0) {
//        addCount(1, MAXL, num);
//    } else {
//        key[++cnt] = num;
//        key_count[cnt] = 1;
//        level[cnt] = randomLevel();
//        addNode(1, MAXL, cnt);
//    }
//}
//
//void removeCount(int i, int h, int num) {
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        key_count[next_node[i][h]]--;
//    } else {
//        removeCount(i, h - 1, num);
//    }
//    len[i][h]--;
//}
//
//void removeNode(int i, int h, int j) {
//    if (h < 1) {
//        return;
//    }
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < key[j]) {
//        i = next_node[i][h];
//    }
//    if (h > level[j]) {
//        len[i][h]--;
//    } else {
//        next_node[i][h] = next_node[j][h];
//        len[i][h] += len[j][h] - 1;
//    }
//    removeNode(i, h - 1, j);
//}
//
//void remove(int num) {
//    int j = find(1, MAXL, num);
//    if (j != 0) {
//        if (key_count[j] > 1) {
//            removeCount(1, MAXL, num);
//        } else {
//            removeNode(1, MAXL, j);
//        }
//    }
//}
//
//int small(int i, int h, int num) {
//    int rightCnt = 0;
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        rightCnt += len[i][h];
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        return rightCnt;
//    } else {
//        return rightCnt + small(i, h - 1, num);
//    }
//}
//
//int getRank(int num) {
//    return small(1, MAXL, num) + 1;
//}
//
//int index(int i, int h, int x) {
//    int c = 0;
//    while (next_node[i][h] != 0 && c + len[i][h] < x) {
//        c += len[i][h];
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        return key[next_node[i][h]];
//    } else {
//        return index(i, h - 1, x - c);
//    }
//}
//
//int index(int x) {
//    return index(1, MAXL, x);
//}
//
//int pre(int i, int h, int num) {
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        return i == 1 ? INT_MIN : key[i];
//    } else {
//        return pre(i, h - 1, num);
//    }
//}
//
//int pre(int num) {
//    return pre(1, MAXL, num);
//}
//
//int post(int i, int h, int num) {
//    while (next_node[i][h] != 0 && key[next_node[i][h]] < num) {
//        i = next_node[i][h];
//    }
//    if (h == 1) {
//        if (next_node[i][h] == 0) {
//            return INT_MAX;
//        }
//        if (key[next_node[i][h]] > num) {
//            return key[next_node[i][h]];
//        }
//        i = next_node[i][h];
//        if (next_node[i][h] == 0) {
//            return INT_MAX;
//        } else {
//            return key[next_node[i][h]];
//        }
//    } else {
//        return post(i, h - 1, num);
//    }
//}
//
//int post(int num) {
//    return post(1, MAXL, num);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    srand(time(0));
//    build();
//    int n;
//    cin >> n;
//    for (int i = 1, op, x; i <= n; i++) {
//        cin >> op >> x;
//        if (op == 1) {
//            add(x);
//        } else if (op == 2) {
//            remove(x);
//        } else if (op == 3) {
//            cout << getRank(x) << endl;
//        } else if (op == 4) {
//            cout << index(x) << endl;
//        } else if (op == 5) {
//            cout << pre(x) << endl;
//        } else {
//            cout << post(x) << endl;
//        }
//    }
//    clear();
//    return 0;
//}