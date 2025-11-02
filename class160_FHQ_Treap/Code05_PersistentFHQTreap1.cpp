// 可持久化平衡树，FHQ-Treap实现，不用词频压缩，C++版
// 认为一开始是0版本的树，为空树，实现如下操作，操作一共发生n次
// v 1 x : 基于v版本的树，增加一个x，生成新版本的树
// v 2 x : 基于v版本的树，删除一个x，生成新版本的树
// v 3 x : 基于v版本的树，查询x的排名，生成新版本的树状况=v版本状况
// v 4 x : 基于v版本的树，查询数据中排名为x的数，生成新版本的树状况=v版本状况
// v 5 x : 基于v版本的树，查询x的前驱，生成新版本的树状况=v版本状况
// v 6 x : 基于v版本的树，查询x的后继，生成新版本的树状况=v版本状况
// 不管什么操作，都基于某个v版本，操作完成后得到新版本的树，但v版本不会变化
// 如果x的前驱不存在，返回-2^31 + 1，如果x的后继不存在，返回+2^31 - 1
// 1 <= n <= 5 * 10^5
// -10^9 <= x <= +10^9
// 测试链接 : https://www.luogu.com.cn/problem/P3835
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <vector>
using namespace std;

const int MAXN = 500001;
const int MAXM = MAXN * 50;

int cnt = 0;
int head[MAXN];
int key[MAXM];
int left[MAXM];
int right[MAXM];
int size[MAXM];
double priority[MAXM];

// 复制节点
int copyNode(int i) {
    if (i == 0) return 0;
    cnt++;
    key[cnt] = key[i];
    left[cnt] = left[i];
    right[cnt] = right[i];
    size[cnt] = size[i];
    priority[cnt] = priority[i];
    return cnt;
}

// 更新节点大小
void updateSize(int i) {
    if (i == 0) return;
    size[i] = size[left[i]] + size[right[i]] + 1;
}

// 分裂操作
void split(int i, int k, int &l, int &r) {
    if (i == 0) {
        l = r = 0;
        return;
    }
    
    int newI = copyNode(i);
    
    if (key[newI] <= k) {
        l = newI;
        split(right[newI], k, right[newI], r);
    } else {
        r = newI;
        split(left[newI], k, l, left[newI]);
    }
    
    updateSize(newI);
}

// 合并操作
int merge(int l, int r) {
    if (l == 0 || r == 0) {
        return l + r;
    }
    
    if (priority[l] >= priority[r]) {
        int newL = copyNode(l);
        right[newL] = merge(right[newL], r);
        updateSize(newL);
        return newL;
    } else {
        int newR = copyNode(r);
        left[newR] = merge(l, left[newR]);
        updateSize(newR);
        return newR;
    }
}

// 插入操作
int insert(int root, int x) {
    int l, r;
    split(root, x, l, r);
    
    cnt++;
    key[cnt] = x;
    size[cnt] = 1;
    priority[cnt] = (double)rand() / RAND_MAX;
    
    return merge(merge(l, cnt), r);
}

// 删除操作
int remove(int root, int x) {
    int l, m, r;
    split(root, x - 1, l, m);
    split(m, x, m, r);
    
    if (m != 0) {
        // 删除一个x
        m = merge(left[m], right[m]);
    }
    
    return merge(merge(l, m), r);
}

// 查询排名
int getRank(int root, int x) {
    int l, r;
    split(root, x - 1, l, r);
    int rank = size[l] + 1;
    merge(l, r);
    return rank;
}

// 查询第k小的数
int getKth(int root, int k) {
    int i = root;
    while (i != 0) {
        int leftSize = size[left[i]];
        if (leftSize + 1 == k) {
            return key[i];
        } else if (leftSize >= k) {
            i = left[i];
        } else {
            k -= leftSize + 1;
            i = right[i];
        }
    }
    return 0;
}

// 查询前驱
int getPredecessor(int root, int x) {
    int l, r;
    split(root, x - 1, l, r);
    
    if (l == 0) {
        return -2147483647; // -2^31 + 1
    }
    
    int pred = getKth(l, size[l]);
    merge(l, r);
    return pred;
}

// 查询后继
int getSuccessor(int root, int x) {
    int l, r;
    split(root, x, l, r);
    
    if (r == 0) {
        return 2147483647; // 2^31 - 1
    }
    
    int succ = getKth(r, 1);
    merge(l, r);
    return succ;
}

int main() {
    srand(time(0));
    
    int n;
    scanf("%d", &n);
    
    head[0] = 0; // 初始版本为空树
    
    for (int i = 1; i <= n; i++) {
        int v, op, x;
        scanf("%d%d%d", &v, &op, &x);
        
        switch (op) {
            case 1: // 插入
                head[i] = insert(head[v], x);
                break;
            case 2: // 删除
                head[i] = remove(head[v], x);
                break;
            case 3: // 查询排名
                head[i] = head[v];
                printf("%d\n", getRank(head[v], x));
                break;
            case 4: // 查询第k小
                head[i] = head[v];
                printf("%d\n", getKth(head[v], x));
                break;
            case 5: // 查询前驱
                head[i] = head[v];
                printf("%d\n", getPredecessor(head[v], x));
                break;
            case 6: // 查询后继
                head[i] = head[v];
                printf("%d\n", getSuccessor(head[v], x));
                break;
        }
    }
    
    return 0;
}