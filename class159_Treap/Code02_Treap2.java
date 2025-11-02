package class151;

// Treap树的实现(C++版)
// 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
// 1，增加x，重复加入算多个词频
// 2，删除x，如果有多个，只删掉一个
// 3，查询x的排名，x的排名为，比x小的数的个数+1
// 4，查询数据中排名为x的数
// 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
// 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
// 所有操作的次数 <= 10^5
// -10^7 <= x <= +10^7
// 测试链接 : https://www.luogu.com.cn/problem/P3369
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//
//// 全局变量
//int cnt = 0;          // 空间使用计数
//int head = 0;         // 整棵树的头节点编号
//int key[MAXN];        // 节点的key值
//int key_count[MAXN];  // 节点key的计数
//int ls[MAXN];         // 左孩子
//int rs[MAXN];         // 右孩子
//int siz[MAXN];        // 数字总数
//double priority[MAXN]; // 节点优先级
//
///**
// * 更新节点信息
// * 计算以节点i为根的子树大小
// * @param i 节点索引
// */
//void up(int i) {
//    siz[i] = siz[ls[i]] + siz[rs[i]] + key_count[i];
//}
//
///**
// * 左旋操作
// * 当右子节点的优先级大于当前节点时执行
// * @param i 当前节点
// * @return 旋转后的新根节点
// */
//int leftRotate(int i) {
//    int r = rs[i];
//    rs[i] = ls[r];
//    ls[r] = i;
//    up(i);
//    up(r);
//    return r;
//}
//
///**
// * 右旋操作
// * 当左子节点的优先级大于当前节点时执行
// * @param i 当前节点
// * @return 旋转后的新根节点
// */
//int rightRotate(int i) {
//    int l = ls[i];
//    ls[i] = rs[l];
//    rs[l] = i;
//    up(i);
//    up(l);
//    return l;
//}
//
///**
// * 添加节点的递归实现
// * @param i 当前节点索引
// * @param num 要插入的数值
// * @return 插入后的新节点索引
// */
//int add(int i, int num) {
//    if (i == 0) {
//        key[++cnt] = num;
//        key_count[cnt] = siz[cnt] = 1;
//        priority[cnt] = static_cast<double>(rand()) / RAND_MAX;
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
//    if (ls[i] != 0 && priority[ls[i]] > priority[i]) {
//        return rightRotate(i);
//    }
//    if (rs[i] != 0 && priority[rs[i]] > priority[i]) {
//        return leftRotate(i);
//    }
//    return i;
//}
//
///**
// * 添加元素的公共接口
// * @param num 要添加的数值
// */
//void add(int num) {
//    head = add(head, num);
//}
//
///**
// * 计算小于num的元素个数
// * @param i 当前节点索引
// * @param num 目标数值
// * @return 小于num的元素个数
// */
//int small(int i, int num) {
//    if (i == 0) {
//        return 0;
//    }
//    if (key[i] >= num) {
//        return small(ls[i], num);
//    } else {
//        return siz[ls[i]] + key_count[i] + small(rs[i], num);
//    }
//}
//
///**
// * 查询x的排名
// * @param num 目标数值
// * @return num的排名（比num小的数的个数+1）
// */
//int getRank(int num) {
//    return small(head, num) + 1;
//}
//
///**
// * 查询排名为x的数
// * @param i 当前节点索引
// * @param x 排名
// * @return 排名为x的数值
// */
//int index(int i, int x) {
//    if (siz[ls[i]] >= x) {
//        return index(ls[i], x);
//    } else if (siz[ls[i]] + key_count[i] < x) {
//        return index(rs[i], x - siz[ls[i]] - key_count[i]);
//    }
//    return key[i];
//}
//
///**
// * 查询排名为x的数的公共接口
// * @param x 排名
// * @return 排名为x的数值
// */
//int index(int x) {
//    return index(head, x);
//}
//
///**
// * 查询x的前驱
// * @param i 当前节点索引
// * @param num 目标数值
// * @return x的前驱（小于x的最大数）
// */
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
///**
// * 查询x的前驱的公共接口
// * @param num 目标数值
// * @return x的前驱
// */
//int pre(int num) {
//    return pre(head, num);
//}
//
///**
// * 查询x的后继
// * @param i 当前节点索引
// * @param num 目标数值
// * @return x的后继（大于x的最小数）
// */
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
///**
// * 查询x的后继的公共接口
// * @param num 目标数值
// * @return x的后继
// */
//int post(int num) {
//    return post(head, num);
//}
//
///**
// * 删除节点的递归实现
// * @param i 当前节点索引
// * @param num 要删除的数值
// * @return 删除后的新节点索引
// */
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
//                if (priority[ls[i]] >= priority[rs[i]]) {
//                    i = rightRotate(i);
//                    rs[i] = remove(rs[i], num);
//                } else {
//                    i = leftRotate(i);
//                    ls[i] = remove(ls[i], num);
//                }
//            }
//        }
//    }
//    up(i);
//    return i;
//}
//
///**
// * 删除元素的公共接口
// * @param num 要删除的数值
// */
//void remove(int num) {
//    if (getRank(num) != getRank(num + 1)) {
//        head = remove(head, num);
//    }
//}
//
///**
// * 清空数据结构，重置所有数组
// */
//void clear() {
//    memset(key + 1, 0, cnt * sizeof(int));
//    memset(key_count + 1, 0, cnt * sizeof(int));
//    memset(ls + 1, 0, cnt * sizeof(int));
//    memset(rs + 1, 0, cnt * sizeof(int));
//    memset(siz + 1, 0, cnt * sizeof(int));
//    memset(priority + 1, 0, cnt * sizeof(int));
//    cnt = 0;
//    head = 0;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    srand(time(0));
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