// Serega and Fun问题 - 分块算法实现 (C++版本)
// 题目来源: https://codeforces.com/problemset/problem/455/D
// 题目大意: 给定一个长度为n的数组arr，有q次操作，操作分为两种类型：
// 类型1: l r - 计算区间[l,r]内值为k的元素个数，其中k是该区间内出现次数最多的数字
// 类型2: l r - 将arr[l]移动到位置r
// 约束条件: 
// 1 <= n, q <= 10^5
// 1 <= arr[i] <= n

#include <iostream>
#include <vector>
#include <deque>
#include <cmath>
#include <algorithm>
using namespace std;

const int MAXN = 100005;
int n, q;
int blen; // 块大小
vector<deque<int>> blocks; // 每个块存储元素
vector<vector<int>> cnt; // 每个块中各个数值的出现次数
int bcnt; // 块的数量

/**
 * 重构分块结构
 * 当块的数量远超sqrt(n)时，重新分块以保持效率
 */
void reBuild() {
    // 当块的数量远超sqrt(n)时，重新分块
    if (bcnt > 2 * blen) {
        // 将所有元素收集到一个临时向量中
        vector<int> tmp;
        for (int i = 0; i < bcnt; i++) {
            for (int x : blocks[i]) {
                tmp.push_back(x);
            }
        }
        
        // 清空原有的分块结构
        blocks.clear();
        cnt.clear();
        bcnt = 0;
        
        // 重新分块
        for (int i = 0; i < (int)tmp.size(); i++) {
            // 每blen个元素作为一个块
            if (i % blen == 0) {
                blocks.push_back(deque<int>());
                cnt.push_back(vector<int>(MAXN, 0));
                bcnt++;
            }
            // 将元素添加到对应的块中
            int val = tmp[i];
            blocks[bcnt - 1].push_back(val);
            cnt[bcnt - 1][val]++;
        }
    }
}

/**
 * 查询区间[l,r]内出现最多的数字的出现次数
 * @param l 左边界(1-indexed)
 * @param r 右边界(1-indexed)
 * @return 出现最多的数字的出现次数
 */
int query(int l, int r) {
    // 转换为0-indexed
    l--;
    r--;
    
    // 计算左右边界所在的块
    int lb = l / blen;
    int rb = r / blen;
    int ans = 0;

    // 如果在同一个块内
    if (lb == rb) {
        // 直接遍历计算
        vector<int> count(MAXN, 0);
        for (int i = l; i <= r; i++) {
            int blockId = i / blen;
            int indexInBlock = i % blen;
            int val = blocks[blockId][indexInBlock];
            count[val]++;
            ans = max(ans, count[val]);
        }
    } else {
        // 跨多个块
        vector<int> count(MAXN, 0);
        
        // 处理左端不完整块
        int lEnd = (lb + 1) * blen - 1;
        for (int i = l; i <= min(lEnd, n - 1); i++) {
            int blockId = i / blen;
            int indexInBlock = i % blen;
            int val = blocks[blockId][indexInBlock];
            count[val]++;
            ans = max(ans, count[val]);
        }

        // 处理中间完整块
        for (int i = lb + 1; i <= rb - 1; i++) {
            for (int j = 1; j < MAXN; j++) {
                count[j] += cnt[i][j];
                ans = max(ans, count[j]);
            }
        }

        // 处理右端不完整块
        int rStart = rb * blen;
        for (int i = rStart; i <= r; i++) {
            int blockId = i / blen;
            int indexInBlock = i % blen;
            int val = blocks[blockId][indexInBlock];
            count[val]++;
            ans = max(ans, count[val]);
        }
    }

    return ans;
}

/**
 * 将位置l的元素移动到位置r
 * @param l 源位置(1-indexed)
 * @param r 目标位置(1-indexed)
 */
void move(int l, int r) {
    // 转换为0-indexed
    l--;
    r--;
    
    // 计算左右位置所在的块
    int lb = l / blen;
    int rb = r / blen;
    int lIndexInBlock = l % blen;
    
    // 从源块中移除元素
    int val = blocks[lb][lIndexInBlock];
    blocks[lb].erase(blocks[lb].begin() + lIndexInBlock);
    cnt[lb][val]--;
    
    // 计算在新位置的索引
    int newIndexInBlock = r % blen;
    if (lb < rb) {
        // 如果从前面的块移动到后面的块
        newIndexInBlock = newIndexInBlock - (lb + 1) * blen + (lb * blen) + blocks[lb].size();
    } else if (lb > rb) {
        // 如果从后面的块移动到前面的块
        newIndexInBlock = newIndexInBlock + (lb * blen) - (rb + 1) * blen;
    }

    // 将元素插入到目标块中
    blocks[rb].insert(blocks[rb].begin() + newIndexInBlock, val);
    cnt[rb][val]++;
}

/**
 * 初始化分块
 */
void prepare() {
    // 计算块大小，通常选择sqrt(n)
    blen = sqrt(n);
    bcnt = 0;
    blocks.clear();
    cnt.clear();

    // 初始化分块结构
    for (int i = 0; i < n; i++) {
        if (i % blen == 0) {
            blocks.push_back(deque<int>());
            cnt.push_back(vector<int>(MAXN, 0));
            bcnt++;
        }
    }
}

int main() {
    // 优化输入输出速度
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取数组长度n
    cin >> n;
    
    // 初始化分块
    prepare();

    // 读取初始数组
    for (int i = 0; i < n; i++) {
        int blockId = i / blen;
        int val;
        cin >> val;
        blocks[blockId].push_back(val);
        cnt[blockId][val]++;
    }

    // 读取操作次数q
    cin >> q;
    
    // 处理q次操作
    for (int i = 1, op, l, r; i <= q; i++) {
        cin >> op >> l >> r;
        if (op == 1) {
            // 查询操作
            cout << query(l, r) << "\n";
        } else {
            // 移动操作
            move(l, r);
            // 定期重构以保持效率
            if (i % 5000 == 0) {
                reBuild();
            }
        }
    }

    return 0;
}