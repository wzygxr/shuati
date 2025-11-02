// P2163 [SHOI2007]园丁的烦恼
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 二维数点, 离线查询
// 链接: https://www.luogu.com.cn/problem/P2163
// 
// 题目描述:
// 给定平面上的n个点，m个查询，每个查询询问一个矩形区域内有多少个点
// 
// 解题思路:
// 1. 使用CDQ分治将二维问题转化为一维问题
// 2. 将点和查询按照x坐标排序
// 3. 使用树状数组维护y坐标的计数
// 4. 对于每个查询，计算矩形内的点数
// 
// 时间复杂度: O((n+m) log n)

#include <bits/stdc++.h>
using namespace std;

const int MAXN = 500005;

struct Point {
    int x, y, type; // type: 0-点, 1-查询左下角, 2-查询右上角
    int id, val;    // id: 查询编号, val: 权重
    
    bool operator<(const Point& other) const {
        if (x != other.x) return x < other.x;
        if (y != other.y) return y < other.y;
        return type < other.type;
    }
};

vector<Point> points;
int ans[MAXN];
int tree[MAXN * 2];
vector<int> yvals;

// 树状数组操作
inline int lowbit(int x) {
    return x & -x;
}

void update(int pos, int val) {
    for (; pos <= yvals.size(); pos += lowbit(pos)) {
        tree[pos] += val;
    }
}

int query(int pos) {
    int res = 0;
    for (; pos > 0; pos -= lowbit(pos)) {
        res += tree[pos];
    }
    return res;
}

// 离散化y坐标
int getYIndex(int y) {
    return lower_bound(yvals.begin(), yvals.end(), y) - yvals.begin() + 1;
}

// CDQ分治主函数
void cdq(int l, int r) {
    if (l == r) return;
    
    int mid = (l + r) / 2;
    cdq(l, mid);
    cdq(mid + 1, r);
    
    // 合并两个有序区间
    vector<Point> temp;
    int i = l, j = mid + 1;
    
    while (i <= mid && j <= r) {
        if (points[i].y <= points[j].y) {
            if (points[i].type == 0) {
                update(getYIndex(points[i].y), 1);
            }
            temp.push_back(points[i]);
            i++;
        } else {
            if (points[j].type == 1) {
                ans[points[j].id] += query(getYIndex(points[j].y));
            } else if (points[j].type == 2) {
                ans[points[j].id] -= query(getYIndex(points[j].y));
            }
            temp.push_back(points[j]);
            j++;
        }
    }
    
    while (i <= mid) {
        if (points[i].type == 0) {
            update(getYIndex(points[i].y), 1);
        }
        temp.push_back(points[i]);
        i++;
    }
    
    while (j <= r) {
        if (points[j].type == 1) {
            ans[points[j].id] += query(getYIndex(points[j].y));
        } else if (points[j].type == 2) {
            ans[points[j].id] -= query(getYIndex(points[j].y));
        }
        temp.push_back(points[j]);
        j++;
    }
    
    // 恢复树状数组
    for (int k = l; k <= mid; k++) {
        if (points[k].type == 0) {
            update(getYIndex(points[k].y), -1);
        }
    }
    
    // 复制回原数组
    for (int k = l; k <= r; k++) {
        points[k] = temp[k - l];
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 读取点
    for (int i = 0; i < n; i++) {
        int x, y;
        cin >> x >> y;
        points.push_back({x, y, 0, 0, 0});
        yvals.push_back(y);
    }
    
    // 读取查询
    for (int i = 0; i < m; i++) {
        int x1, y1, x2, y2;
        cin >> x1 >> y1 >> x2 >> y2;
        
        // 将查询拆分为四个点
        points.push_back({x1 - 1, y1 - 1, 1, i, 1});
        points.push_back({x1 - 1, y2, 2, i, -1});
        points.push_back({x2, y1 - 1, 2, i, -1});
        points.push_back({x2, y2, 1, i, 1});
        
        yvals.push_back(y1 - 1);
        yvals.push_back(y2);
    }
    
    // 离散化y坐标
    sort(yvals.begin(), yvals.end());
    yvals.erase(unique(yvals.begin(), yvals.end()), yvals.end());
    
    // 按照x坐标排序
    sort(points.begin(), points.end());
    
    // CDQ分治
    cdq(0, points.size() - 1);
    
    // 输出结果
    for (int i = 0; i < m; i++) {
        cout << ans[i] << "
";
    }
    
    return 0;
}
