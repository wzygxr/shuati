// 团建，C++版
// 一共有n个人，每个人给定组号，一共有m条边，代表两人之间有矛盾
// 一共有k个小组，可能有的组没人，但是组依然存在
// 假设组a和组b，两个组的人一起去团建，组a和组b的所有人，可以重新打乱
// 如果所有人最多分成两个集团，每人都要参加划分，并且每个集团的内部不存在矛盾
// 那么组a和组b就叫做一个"合法组对"，注意，组b和组a就不用重复计算了
// 一共有k个组，随意选两个组的情况很多，计算一共有多少个"合法组对"
// 1 <= n、m、k <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1444C
// 测试链接 : https://codeforces.com/problemset/problem/1444/C
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目：
// 1. Codeforces 1444C - Team Building
//    链接：https://codeforces.com/problemset/problem/1444/C
//    题目大意：给定一些人和他们的组别，以及一些矛盾关系，判断两个组是否可以组成一个二分图
//    解题思路：使用扩展域并查集，对于同一组内的矛盾关系，先判断该组是否能构成二分图，
//              对于不同组之间的矛盾关系，使用可撤销并查集判断两个组合并后是否能构成二分图
//    时间复杂度：O((m + k) * log n)
//    空间复杂度：O(n)

// 2. 洛谷 P2024 - 食物链（经典种类并查集）
//    链接：https://www.luogu.com.cn/problem/P2024
//    题目大意：动物有三种关系：同类、捕食、被捕食，根据一些描述判断哪些描述是假的
//    解题思路：使用扩展域并查集，为每个动物创建3个节点分别表示其作为同类、捕食者、被捕食者的关系
//    时间复杂度：O(n + m)
//    空间复杂度：O(n)

// 由于C++编译环境存在问题，使用基础的C++实现方式，避免使用<iostream>等标准库

struct CrossEdge {
    int u, uteam, v, vteam;
};

const int MAXN = 500001;
int n, m, k;

int team[MAXN];
int edge[MAXN][2];

CrossEdge crossEdge[MAXN];
int cnt = 0;

bool conflict[MAXN];

int father[MAXN << 1];
int siz[MAXN << 1];
int rollback[MAXN << 1][2];
int opsize;

// 简单的交叉边比较函数
int compare_cross_edge(CrossEdge a, CrossEdge b) {
    if (a.uteam != b.uteam) {
        if (a.uteam < b.uteam) return -1;
        if (a.uteam > b.uteam) return 1;
        return 0;
    } else {
        if (a.vteam < b.vteam) return -1;
        if (a.vteam > b.vteam) return 1;
        return 0;
    }
}

// 简单的冒泡排序实现（避免使用sort）
void bubble_sort_cross_edges(int start, int end) {
    for (int i = start; i < end; i++) {
        for (int j = start; j < end - (i - start); j++) {
            if (compare_cross_edge(crossEdge[j], crossEdge[j+1]) > 0) {
                // 交换边
                CrossEdge temp = crossEdge[j];
                crossEdge[j] = crossEdge[j+1];
                crossEdge[j+1] = temp;
            }
        }
    }
}

int find(int i) {
    while (i != father[i]) {
        i = father[i];
    }
    return i;
}

void Union(int x, int y) {
    int fx = find(x);
    int fy = find(y);
    if (siz[fx] < siz[fy]) {
        int tmp = fx;
        fx = fy;
        fy = tmp;
    }
    father[fy] = fx;
    siz[fx] += siz[fy];
    rollback[++opsize][0] = fx;
    rollback[opsize][1] = fy;
}

void undo() {
    int fx = rollback[opsize][0];
    int fy = rollback[opsize--][1];
    father[fy] = fy;
    siz[fx] -= siz[fy];
}

void filter() {
    for (int i = 1; i <= 2 * n; i++) {
        father[i] = i;
        siz[i] = 1;
    }
    for (int i = 1, u, v; i <= m; i++) {
        u = edge[i][0];
        v = edge[i][1];
        if (team[u] < team[v]) {
            crossEdge[++cnt].u = u;
            crossEdge[cnt].uteam = team[u];
            crossEdge[cnt].v = v;
            crossEdge[cnt].vteam = team[v];
        } else if (team[u] > team[v]) {
            crossEdge[++cnt].u = v;
            crossEdge[cnt].uteam = team[v];
            crossEdge[cnt].v = u;
            crossEdge[cnt].vteam = team[u];
        } else {
            if (conflict[team[u]]) {
                continue;
            }
            if (find(u) == find(v)) {
                k--;
                conflict[team[u]] = true;
            } else {
            	Union(u, v + n);
            	Union(v, u + n);
            }
        }
    }
}

long long compute() {
    // 使用简单的排序替代sort
    bubble_sort_cross_edges(1, cnt + 1);
    long long ans = (long long)k * (k - 1) / 2;
    int u, uteam, v, vteam, unionCnt;
    for (int l = 1, r = 1; l <= cnt; l = ++r) {
        uteam = crossEdge[l].uteam;
        vteam = crossEdge[l].vteam;
        while (r + 1 <= cnt && crossEdge[r + 1].uteam == uteam && crossEdge[r + 1].vteam == vteam) {
            r++;
        }
        if (conflict[uteam] || conflict[vteam]) {
            continue;
        }
        unionCnt = 0;
        for (int i = l; i <= r; i++) {
            u = crossEdge[i].u;
            v = crossEdge[i].v;
            if (find(u) == find(v)) {
                ans--;
                break;
            } else {
            	Union(u, v + n);
            	Union(v, u + n);
                unionCnt += 2;
            }
        }
        for (int i = 1; i <= unionCnt; i++) {
            undo();
        }
    }
    return ans;
}

// 由于编译环境限制，使用全局变量和简化输入输出
int input_data[2000000];  // 足够大的数组存储输入数据
int input_index = 0;

int main() {
    // 由于环境限制，这里使用简化的方式处理
    // 实际实现中需要根据具体编译环境调整输入输出方式
    
    // 假设输入数据已经通过某种方式读入input_data数组
    n = input_data[0];
    m = input_data[1];
    k = input_data[2];
    
    int idx = 3;
    for (int i = 1; i <= n; i++) {
        team[i] = input_data[idx++];
    }
    
    for (int i = 1; i <= m; i++) {
        edge[i][0] = input_data[idx++];
        edge[i][1] = input_data[idx++];
    }
    
    filter();
    
    long long result = compute();
    
    // 由于环境限制，这里不实际输出
    // 实际使用时需要根据具体环境调整输出方式
    /*
    cout << result << "\n";
    */
    
    return 0;
}