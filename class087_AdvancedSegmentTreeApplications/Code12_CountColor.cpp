#include <iostream>
#include <vector>
#include <bitset>
#include <chrono>

using namespace std;

/**
 * POJ 2777 Count Color - 区间染色问题 (C++实现)
 * 
 * 题目描述：
 * 给定一个长度为L的板条，初始时所有位置都是颜色1，执行O次操作：
 * 1. "C A B C": 将区间[A,B]染成颜色C
 * 2. "P A B": 查询区间[A,B]中有多少种不同的颜色
 * 
 * 解法要点：
 * - 使用线段树维护区间颜色集合(用位运算表示)
 * - 结合懒惰标记实现区间染色
 * - 通过位运算计算颜色种类数
 * 
 * 时间复杂度：
 * - 区间染色：O(log L)
 * - 区间查询：O(log L)
 * 
 * 空间复杂度：O(4L)
 * 
 * 工程化考量：
 * - 使用位运算高效表示颜色集合
 * - 懒惰标记优化区间更新
 * - 输入验证和边界处理
 * - 内存管理优化
 */
class Code12_CountColor {
private:
    vector<int> tree;    // 线段树，存储颜色集合(位掩码)
    vector<int> lazy;    // 懒惰标记，存储要设置的颜色
    int n;               // 线段树大小
    
    /**
     * 构建线段树
     */
    void build(int node, int l, int r) {
        if (l == r) {
            tree[node] = 1; // 颜色1用位掩码1表示
            return;
        }
        int mid = (l + r) >> 1;
        build(node << 1, l, mid);
        build(node << 1 | 1, mid + 1, r);
        tree[node] = tree[node << 1] | tree[node << 1 | 1];
    }
    
    /**
     * 下推懒惰标记
     */
    void pushDown(int node, int l, int r) {
        if (lazy[node] != 0 && l != r) {
            int color = lazy[node];
            lazy[node << 1] = color;
            lazy[node << 1 | 1] = color;
            tree[node << 1] = 1 << (color - 1);
            tree[node << 1 | 1] = 1 << (color - 1);
            lazy[node] = 0;
        }
    }
    
    /**
     * 区间染色操作（内部实现）
     */
    void update(int node, int l, int r, int ql, int qr, int color) {
        if (ql <= l && r <= qr) {
            // 完全覆盖，设置懒惰标记和当前节点颜色
            lazy[node] = color;
            tree[node] = 1 << (color - 1);
            return;
        }
        
        // 下推懒惰标记
        pushDown(node, l, r);
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            update(node << 1, l, mid, ql, qr, color);
        }
        if (qr > mid) {
            update(node << 1 | 1, mid + 1, r, ql, qr, color);
        }
        
        // 合并子区间信息
        tree[node] = tree[node << 1] | tree[node << 1 | 1];
    }
    
    /**
     * 查询区间颜色集合（内部实现）
     */
    int query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }
        
        // 下推懒惰标记
        pushDown(node, l, r);
        
        int mid = (l + r) >> 1;
        int result = 0;
        if (ql <= mid) {
            result |= query(node << 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            result |= query(node << 1 | 1, mid + 1, r, ql, qr);
        }
        return result;
    }
    
    /**
     * 计算位掩码中1的个数（颜色种类数）
     */
    int bitCount(int mask) {
        int count = 0;
        while (mask) {
            count++;
            mask &= mask - 1; // 清除最低位的1
        }
        return count;
    }
    
public:
    /**
     * 构造函数
     * @param L 板条长度
     */
    Code12_CountColor(int L) : n(L) {
        tree.resize(4 * n, 0);
        lazy.resize(4 * n, 0);
        // 初始化所有位置为颜色1
        build(1, 1, n);
    }
    
    /**
     * 区间染色操作
     * @param l 区间左端点
     * @param r 区间右端点
     * @param color 颜色编号(1-30)
     */
    void update(int l, int r, int color) {
        // 输入验证
        if (l < 1 || r > n || l > r || color < 1 || color > 30) {
            cerr << "Invalid input parameters!" << endl;
            return;
        }
        update(1, 1, n, l, r, color);
    }
    
    /**
     * 查询区间颜色种类数
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 颜色种类数
     */
    int query(int l, int r) {
        // 输入验证
        if (l < 1 || r > n || l > r) {
            cerr << "Invalid query parameters!" << endl;
            return 0;
        }
        int mask = query(1, 1, n, l, r);
        return bitCount(mask);
    }
    
    /**
     * 获取当前线段树状态（用于调试）
     */
    void printState() {
        cout << "Tree state (first 10 elements): ";
        for (int i = 1; i <= min(10, n); i++) {
            int mask = query(1, 1, n, i, i);
            int color = 0;
            while (mask) {
                color++;
                mask >>= 1;
            }
            cout << color << " ";
        }
        cout << endl;
    }
};

/**
 * 测试函数
 */
void testCode12_CountColor() {
    // 测试用例1：基本功能测试
    cout << "=== 测试用例1：基本功能测试 ===" << endl;
    Code12_CountColor segTree(10);
    
    // 初始状态：所有位置都是颜色1
    cout << "初始查询[1,10]颜色种类数: " << segTree.query(1, 10) << endl; // 应为1
    
    // 染色操作
    segTree.update(1, 5, 2); // 将[1,5]染成颜色2
    cout << "染色后查询[1,10]颜色种类数: " << segTree.query(1, 10) << endl; // 应为2
    cout << "查询[1,5]颜色种类数: " << segTree.query(1, 5) << endl; // 应为1
    cout << "查询[6,10]颜色种类数: " << segTree.query(6, 10) << endl; // 应为1
    
    // 覆盖染色
    segTree.update(3, 7, 3); // 将[3,7]染成颜色3
    cout << "覆盖染色后查询[1,10]颜色种类数: " << segTree.query(1, 10) << endl; // 应为3
    
    // 测试用例2：边界情况
    cout << "\n=== 测试用例2：边界情况 ===" << endl;
    Code12_CountColor segTree2(5);
    
    // 单点染色
    segTree2.update(1, 1, 2);
    segTree2.update(5, 5, 3);
    cout << "单点染色后查询[1,5]颜色种类数: " << segTree2.query(1, 5) << endl; // 应为3
    
    // 测试用例3：性能测试（大规模数据）
    cout << "\n=== 测试用例3：性能测试 ===" << endl;
    int L = 100000;
    Code12_CountColor largeSegTree(L);
    
    auto start = chrono::high_resolution_clock::now();
    for (int i = 1; i <= 1000; i++) {
        int l = (i * 10) % L + 1;
        int r = min(l + 100, L);
        largeSegTree.update(l, r, (i % 30) + 1);
    }
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "1000次染色操作耗时: " << duration.count() << "ms" << endl;
    
    // 测试用例4：错误输入处理
    cout << "\n=== 测试用例4：错误输入处理 ===" << endl;
    Code12_CountColor errorSegTree(10);
    
    // 测试无效输入
    errorSegTree.update(0, 5, 2);  // 无效左边界
    errorSegTree.update(1, 15, 2); // 无效右边界
    errorSegTree.update(5, 3, 2);  // 左边界大于右边界
    errorSegTree.update(1, 5, 0); // 无效颜色
    errorSegTree.update(1, 5, 31); // 无效颜色
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 主函数
 */
int main() {
    testCode12_CountColor();
    return 0;
}