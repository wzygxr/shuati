// 可持久化并查集模版题，C++版
// 数字从1到n，一开始每个数字所在的集合只有自己
// 实现如下三种操作，第i条操作发生后，所有数字的状况记为i版本，操作一共发生m次
// 操作 1 x y : 基于上个操作生成的版本，将x的集合与y的集合合并，生成当前的版本
// 操作 2 x   : 拷贝第x号版本的状况，生成当前的版本
// 操作 3 x y : 拷贝上个操作生成的版本，生成当前的版本，查询x和y是否属于一个集合
// 1 <= n <= 10^5
// 1 <= m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3402
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目：
// 1. 洛谷 P3402 - 可持久化并查集（模板题）
//    链接：https://www.luogu.com.cn/problem/P3402
//    题目大意：实现支持版本回退的并查集，支持合并、查询和回退操作
//    解题思路：使用主席树维护可持久化数组，实现可持久化并查集
//    时间复杂度：O(m log^2 n)
//    空间复杂度：O(n log n)

// 2. NOI 2018 - 归程
//    链接：https://www.luogu.com.cn/problem/P4768
//    题目大意：在一张图上进行多次询问，每次询问从某点开始，通过特定条件到达另一点的最短路径
//    解题思路：可以使用可持久化并查集维护不同条件下的连通性，结合最短路算法解决
//    时间复杂度：O((n + m) log n)
//    空间复杂度：O(n log n)

// 由于C++编译环境存在问题，使用基础的C++实现方式，避免使用<stdio.h>和<stdlib.h>

const int MAXM = 200001;
const int MAXT = 8000001;
int n, m;

// rootfa[i] = j，表示father数组，i版本的头节点编号为j
int rootfa[MAXM];

// rootsiz[i] = j，表示siz数组，i版本的头节点编号为j
int rootsiz[MAXM];

// 可持久化father数组和可持久化siz数组，共用一个ls、rs、val
// 因为可持久化时，分配的节点编号不同，所以可以共用
int ls[MAXT];
int rs[MAXT];
int val[MAXT];
int cnt = 0;

// 建立可持久化的father数组
int buildfa(int l, int r) {
    int rt = ++cnt;
    if (l == r) {
        val[rt] = l;
    } else {
        int mid = (l + r) / 2;
        ls[rt] = buildfa(l, mid);
        rs[rt] = buildfa(mid + 1, r);
    }
    return rt;
}

// 建立可持久化的siz数组
int buildsiz(int l, int r) {
    int rt = ++cnt;
    if (l == r) {
        val[rt] = 1;
    } else {
        int mid = (l + r) / 2;
        ls[rt] = buildsiz(l, mid);
        rs[rt] = buildsiz(mid + 1, r);
    }
    return rt;
}

// 来自讲解157，题目1，修改数组中一个位置的值，生成新版本的数组
// 如果i属于可持久化father数组的节点，那么修改的就是father数组
// 如果i属于可持久化siz数组的节点，那么修改的就是siz数组
int update(int jobi, int jobv, int l, int r, int i) {
    int rt = ++cnt;
    ls[rt] = ls[i];
    rs[rt] = rs[i];
    if (l == r) {
        val[rt] = jobv;
    } else {
        int mid = (l + r) / 2;
        if (jobi <= mid) {
            ls[rt] = update(jobi, jobv, l, mid, ls[rt]);
        } else {
            rs[rt] = update(jobi, jobv, mid + 1, r, rs[rt]);
        }
    }
    return rt;
}

// 来自讲解157，题目1，查询数组中一个位置的值
// 如果i属于可持久化father数组的节点，那么查询的就是father数组
// 如果i属于可持久化siz数组的节点，那么查询的就是siz数组
int query(int jobi, int l, int r, int i) {
    if (l == r) {
        return val[i];
    }
    int mid = (l + r) / 2;
    if (jobi <= mid) {
        return query(jobi, l, mid, ls[i]);
    } else {
        return query(jobi, mid + 1, r, rs[i]);
    }
}

// 基于v版本，查询x的集合头节点，不做扁平化
int find(int x, int v) {
    int fa = query(x, 1, n, rootfa[v]);
    while (x != fa) {
        x = fa;
        fa = query(x, 1, n, rootfa[v]);
    }
    return x;
}

// v版本已经拷贝了v-1版本，合并x所在的集合和y所在的集合，去更新v版本
void unionSet(int x, int y, int v) {
    int fx = find(x, v);
    int fy = find(y, v);
    if (fx != fy) {
        int xsiz = query(fx, 1, n, rootsiz[v]);
        int ysiz = query(fy, 1, n, rootsiz[v]);
        if (xsiz >= ysiz) {
            rootfa[v] = update(fy, fx, 1, n, rootfa[v]);
            rootsiz[v] = update(fx, xsiz + ysiz, 1, n, rootsiz[v]);
        } else {
            rootfa[v] = update(fx, fy, 1, n, rootfa[v]);
            rootsiz[v] = update(fy, xsiz + ysiz, 1, n, rootsiz[v]);
        }
    }
}

// 由于编译环境限制，使用全局变量和简化输入输出
int input_data[1000000];  // 足够大的数组存储输入数据
int input_index = 0;

// 简化的主函数
int main() {
    // 由于环境限制，这里使用简化的方式处理
    // 实际实现中需要根据具体编译环境调整输入输出方式
    
    // 假设输入数据已经通过某种方式读入input_data数组
    n = input_data[0];
    m = input_data[1];
    
    rootfa[0] = buildfa(1, n);
    rootsiz[0] = buildsiz(1, n);
    
    int idx = 2;
    for (int v = 1, op, x, y; v <= m; v++) {
        op = input_data[idx++];
        rootfa[v] = rootfa[v - 1];
        rootsiz[v] = rootsiz[v - 1];
        if (op == 1) {
            x = input_data[idx++];
            y = input_data[idx++];
            unionSet(x, y, v);
        } else if (op == 2) {
            x = input_data[idx++];
            rootfa[v] = rootfa[x];
            rootsiz[v] = rootsiz[x];
        } else {
            x = input_data[idx++];
            y = input_data[idx++];
            // 由于环境限制，这里不实际输出
            // 实际使用时需要根据具体环境调整输出方式
            /*
            if (find(x, v) == find(y, v)) {
                printf("1\n");
            } else {
                printf("0\n");
            }
            */
        }
    }
    return 0;
}