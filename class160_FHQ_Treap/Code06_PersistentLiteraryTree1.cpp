// 可持久化文艺平衡树，FHQ-Treap实现，C++版
// 一开始序列为空，实现如下操作，操作一共发生n次
// v 1 x y : 基于v版本的序列，在第x个数后插入y，生成新版本的序列
// v 2 x   : 基于v版本的序列，删除第x个数，生成新版本的序列
// v 3 x y : 基于v版本的序列，范围[x,y]所有数字翻转，生成新版本的序列
// v 4 x y : 基于v版本的序列，查询范围[x,y]所有数字的和，生成新版本的序列状况=v版本状况
// 不管什么操作，都基于某个v版本，操作完成后得到新版本的序列，但v版本不会变化
// 每种操作给定的参数都是有效的，插入数字的范围[-10^6, +10^6]
// 1 <= n <= 2 * 10^5
// 本题目要求强制在线，具体规则可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P5055
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <algorithm>
using namespace std;

const int MAXN = 200001;
const int MAXM = MAXN * 100;

int cnt = 0;
int head[MAXN];
int key[MAXM];
int left[MAXM];
int right[MAXM];
int size[MAXM];
bool reverse[MAXM];
long long sum[MAXM];
double priority[MAXM];

int copy(int i) {
    if (i == 0) return 0;
    cnt++;
    key[cnt] = key[i];
    left[cnt] = left[i];
    right[cnt] = right[i];
    size[cnt] = size[i];
    reverse[cnt] = reverse[i];
    sum[cnt] = sum[i];
    priority[cnt] = priority[i];
    return cnt;
}

void update_size(int i) {
    if (i == 0) return;
    size[i] = size[left[i]] + size[right[i]] + 1;
    sum[i] = sum[left[i]] + sum[right[i]] + key[i];
}

void push_down(int i) {
    if (reverse[i]) {
        if (left[i] != 0) {
            left[i] = copy(left[i]);
            reverse[left[i]] = !reverse[left[i]];
        }
        if (right[i] != 0) {
            right[i] = copy(right[i]);
            reverse[right[i]] = !reverse[right[i]];
        }
        swap(left[i], right[i]);
        reverse[i] = false;
    }
}

void split(int i, int rank, int &l, int &r) {
    if (i == 0) {
        l = r = 0;
        return;
    }
    
    int new_i = copy(i);
    push_down(new_i);
    
    int left_size = size[left[new_i]];
    
    if (left_size + 1 <= rank) {
        l = new_i;
        split(right[new_i], rank - left_size - 1, right[new_i], r);
    } else {
        r = new_i;
        split(left[new_i], rank, l, left[new_i]);
    }
    
    update_size(new_i);
}

int merge(int l, int r) {
    if (l == 0 || r == 0) {
        return l + r;
    }
    
    if (priority[l] >= priority[r]) {
        int new_l = copy(l);
        push_down(new_l);
        right[new_l] = merge(right[new_l], r);
        update_size(new_l);
        return new_l;
    } else {
        int new_r = copy(r);
        push_down(new_r);
        left[new_r] = merge(l, left[new_r]);
        update_size(new_r);
        return new_r;
    }
}

int main() {
    srand(time(0));
    
    int n;
    scanf("%d", &n);
    
    head[0] = 0; // 初始版本为空树
    long long last_ans = 0;
    
    for (int i = 1; i <= n; i++) {
        int v, op;
        long long x, y = 0;
        scanf("%d%d%lld", &v, &op, &x);
        x ^= last_ans;
        
        if (op != 2) {
            scanf("%lld", &y);
            y ^= last_ans;
        }
        
        int l, m, lm, r;
        
        if (op == 1) { // 插入
            split(head[v], x, l, r);
            
            cnt++;
            key[cnt] = (int)y;
            size[cnt] = 1;
            sum[cnt] = y;
            priority[cnt] = (double)rand() / RAND_MAX;
            
            head[i] = merge(merge(l, cnt), r);
        } else if (op == 2) { // 删除
            split(head[v], x, lm, r);
            split(lm, x - 1, l, m);
            
            head[i] = merge(l, r);
        } else if (op == 3) { // 翻转
            split(head[v], y, lm, r);
            split(lm, x - 1, l, m);
            
            reverse[m] = !reverse[m];
            
            head[i] = merge(merge(l, m), r);
        } else { // 查询和
            split(head[v], y, lm, r);
            split(lm, x - 1, l, m);
            
            last_ans = sum[m];
            printf("%lld\n", last_ans);
            
            head[i] = merge(merge(l, m), r);
        }
    }
    
    return 0;
}