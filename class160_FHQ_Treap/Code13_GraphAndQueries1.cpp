// FHQ-Treap实现Graph and Queries
// Codeforces Problem F. Graph and Queries
// 实现图相关的查询操作
// 测试链接 : https://codeforces.com/contest/1416/problem/F

const int MAXN = 100001;

// 全局变量
int head = 0;  // FHQ Treap的头节点编号
int cnt = 0;   // FHQ Treap的空间使用计数

// 并查集数组
int parent[MAXN];

// FHQ Treap节点信息数组
int key[MAXN];      // 节点的key值
int count[MAXN];    // 节点key的计数
int left[MAXN];     // 左孩子
int right[MAXN];    // 右孩子
int size[MAXN];     // 数字总数
double priority[MAXN];  // 节点优先级

// 边的结构
struct Edge {
    int u, v, w;
    bool deleted;
    
    Edge() : u(0), v(0), w(0), deleted(false) {}
    Edge(int u, int v, int w) : u(u), v(v), w(w), deleted(false) {}
};

// 简单的随机数生成器
int seed = 1;
double my_rand() {
    seed = seed * 1103515245 + 12345;
    return (double)(seed & 0x7fffffff) / 2147483647.0;
}

// 初始化并查集
void initUnionFind(int n) {
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
    }
}

// 查找根节点（路径压缩）
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

// 合并两个集合
void unionSets(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);
    if (rootX != rootY) {
        parent[rootX] = rootY;
    }
}

// 初始化FHQ Treap
void initFHQTreap() {
    head = 0;
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        key[i] = 0;
        count[i] = 0;
        left[i] = 0;
        right[i] = 0;
        size[i] = 0;
        priority[i] = 0.0;
    }
}

// 更新节点信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + count[i];
}

// 按值分裂
void split(int l, int r, int i, int num) {
    if (i == 0) {
        right[l] = left[r] = 0;
    } else {
        if (key[i] <= num) {
            right[l] = i;
            split(i, r, right[i], num);
        } else {
            left[r] = i;
            split(l, i, left[i], num);
        }
        up(i);
    }
}

// 合并操作
int merge(int l, int r) {
    if (l == 0 || r == 0) {
        return l + r;
    }
    if (priority[l] >= priority[r]) {
        right[l] = merge(right[l], r);
        up(l);
        return l;
    } else {
        left[r] = merge(l, left[r]);
        up(r);
        return r;
    }
}

// 查找值为num的节点
int findNode(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] == num) {
        return i;
    } else if (key[i] > num) {
        return findNode(left[i], num);
    } else {
        return findNode(right[i], num);
    }
}

// 改变节点计数
void changeCount(int i, int num, int change) {
    if (key[i] == num) {
        count[i] += change;
    } else if (key[i] > num) {
        changeCount(left[i], num, change);
    } else {
        changeCount(right[i], num, change);
    }
    up(i);
}

// 插入数值
void insert(int num) {
    if (findNode(head, num) != 0) {
        changeCount(head, num, 1);
    } else {
        split(0, 0, head, num);
        cnt++;
        key[cnt] = num;
        count[cnt] = size[cnt] = 1;
        priority[cnt] = my_rand();
        head = merge(merge(right[0], cnt), left[0]);
    }
}

// 删除数值
void remove(int num) {
    int i = findNode(head, num);
    if (i != 0) {
        if (count[i] > 1) {
            changeCount(head, num, -1);
        } else {
            split(0, 0, head, num);
            int lm = right[0];
            int r = left[0];
            split(0, 0, lm, num - 1);
            int l = right[0];
            head = merge(l, r);
        }
    }
}

// 查询最大值
int queryMax() {
    if (head == 0) {
        return 0;
    }
    int i = head;
    while (right[i] != 0) {
        i = right[i];
    }
    return key[i];
}

// 简单的输入输出函数
int main() {
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    int n = 5; // 节点数
    int m = 5; // 边数
    
    // 节点权重
    int weights[] = {0, 1, 2, 3, 4, 5}; // 0索引不使用，从1开始
    
    // 边
    Edge edges[MAXN];
    edges[1] = Edge(1, 2, 1);
    edges[2] = Edge(2, 3, 2);
    edges[3] = Edge(3, 4, 3);
    edges[4] = Edge(4, 5, 4);
    edges[5] = Edge(1, 5, 5);
    
    int q = 3; // 查询数
    
    // 初始化并查集
    initUnionFind(n);
    
    // 处理查询
    for (int i = 0; i < q; i++) {
        int type = (i == 0) ? 2 : ((i == 1) ? 1 : 2); // 查询类型
        
        if (type == 1) {
            // 删除边
            int edgeId = 1;
            edges[edgeId].deleted = true;
        } else {
            // 查询连通分量最大权重
            int nodeId = 1;
            
            // 重新初始化FHQ Treap
            initFHQTreap();
            
            // 将与nodeId在同一连通分量的所有节点的权重插入到FHQ Treap中
            int root = find(nodeId);
            for (int j = 1; j <= n; j++) {
                if (find(j) == root) {
                    insert(weights[j]);
                }
            }
            
            // 查询最大权重
            int maxWeight = queryMax();
            
            // 从FHQ Treap中删除最大权重的节点
            if (maxWeight > 0) {
                remove(maxWeight);
                // 更新节点权重为0
                for (int j = 1; j <= n; j++) {
                    if (find(j) == root && weights[j] == maxWeight) {
                        weights[j] = 0;
                        break;
                    }
                }
            }
        }
    }
    
    return 0;
}