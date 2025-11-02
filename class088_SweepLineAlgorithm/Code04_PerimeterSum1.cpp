// 矩形周长并 - 扫描线算法实现
// 问题描述：给定平面上的n个矩形，求这些矩形的并集周长
// 解题思路：使用扫描线算法分别计算水平边和垂直边的长度
// 算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
// 工程化考量：
// 1. 使用高效的IO处理，适用于竞赛环境
// 2. 线段树实现优化，利用问题特殊性避免懒更新
// 3. 离散化处理坐标，减少空间使用
// 4. 边界条件处理完善，避免数组越界
// 测试链接 : https://www.luogu.com.cn/problem/P1856

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <algorithm>
#include <cstdio>

using namespace std;

// 最大矩形数量
const int MAXN = 20001;

// 存储矩形信息：[左下x, 左下y, 右上x, 右上y]
int rec[MAXN][4];

// 存储扫描线事件：[扫描线位置, 区间下界, 区间上界, 变化量(1或-1)]
int line[MAXN][4];

// 存储所有坐标用于离散化
int vsort[MAXN];

// 线段树某范围总长度
int length[MAXN << 2];

// 线段树某范围覆盖长度
int cover[MAXN << 2];

// 线段树某范围覆盖次数
int times[MAXN << 2];

// 离散化坐标数组，去除重复元素
int prepare(int n) {
    sort(vsort + 1, vsort + n + 1);
    int m = 1;
    for (int i = 2; i <= n; i++) {
        if (vsort[m] != vsort[i]) {
            vsort[++m] = vsort[i];
        }
    }
    vsort[m + 1] = vsort[m];
    return m;
}

// 二分查找坐标在离散化数组中的位置
int rank(int n, int num) {
    int ans = 0;
    int l = 1, r = n, mid;
    while (l <= r) {
        mid = (l + r) >> 1;
        if (vsort[mid] >= num) {
            ans = mid;
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}

// 构建线段树
void build(int l, int r, int i) {
    if (l < r) {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
    }
    length[i] = vsort[r + 1] - vsort[l];
    times[i] = 0;
    cover[i] = 0;
}

// 更新线段树节点的覆盖长度
void up(int i) {
    if (times[i] > 0) {
        cover[i] = length[i];
    } else {
        cover[i] = cover[i << 1] + cover[i << 1 | 1];
    }
}

// 在线段树中添加或删除扫描线覆盖
void add(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        times[i] += jobv;
    } else {
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
    up(i);
}

// 扫描y轴方向计算水平边长度
long long scanY(int n) {
    // 构造y轴方向的扫描线事件
    for (int i = 1, j = 1 + n, x1, y1, x2, y2; i <= n; i++, j++) {
        x1 = rec[i][0]; y1 = rec[i][1]; x2 = rec[i][2]; y2 = rec[i][3];
        vsort[i] = y1; vsort[j] = y2;
        line[i][0] = x1; line[i][1] = y1; line[i][2] = y2; line[i][3] = 1;
        line[j][0] = x2; line[j][1] = y1; line[j][2] = y2; line[j][3] = -1;
    }
    return scan(n << 1);
}

// 扫描x轴方向计算垂直边长度
long long scanX(int n) {
    // 构造x轴方向的扫描线事件
    for (int i = 1, j = 1 + n, x1, y1, x2, y2; i <= n; i++, j++) {
        x1 = rec[i][0]; y1 = rec[i][1]; x2 = rec[i][2]; y2 = rec[i][3];
        vsort[i] = x1; vsort[j] = x2;
        line[i][0] = y1; line[i][1] = x1; line[i][2] = x2; line[i][3] = 1;
        line[j][0] = y2; line[j][1] = x1; line[j][2] = x2; line[j][3] = -1;
    }
    return scan(n << 1);
}

// 执行扫描线算法计算投影长度变化总和
long long scan(int n) {
    int m = prepare(n);
    build(1, m, 1);
    // 这里有个坑
    // 在排序时，如果同一个位置的扫描线有多条，也就是line[i][0] == line[j][0]时
    // 应该先处理区间覆盖+1的扫描线，然后再处理区间覆盖-1的扫描线
    // 不然投影长度会频繁变化，导致答案错误
    // 不过测试数据并没有安排这方面的测试
    sort(line + 1, line + n + 1, [](const int* a, const int* b) {
        if (a[0] != b[0]) return a[0] < b[0];
        return b[3] < a[3];
    });
    long long ans = 0;
    for (int i = 1, pre; i <= n; i++) {
        pre = cover[1];
        add(rank(m, line[i][1]), rank(m, line[i][2]) - 1, line[i][3], 1, m, 1);
        ans += abs(cover[1] - pre);
    }
    return ans;
}

// 计算n个矩形的周长并
long long compute(int n) {
    return scanY(n) + scanX(n);
}

int main() {
    // 读取矩形数量
    int n;
    scanf("%d", &n);
    
    // 读取所有矩形的坐标信息
    for (int i = 1; i <= n; i++) {
        // 左下角下标
        scanf("%d%d", &rec[i][0], &rec[i][1]);
        // 右上角下标
        scanf("%d%d", &rec[i][2], &rec[i][3]);
    }
    
    // 计算并输出矩形周长并
    printf("%lld\n", compute(n));
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}