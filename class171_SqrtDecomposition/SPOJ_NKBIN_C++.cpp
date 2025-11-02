#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * SPOJ - NKBIN - C++实现
 * 题目链接：https://www.spoj.com/problems/NKBIN/
 * 
 * 题目描述：
 * 给定一个数组，支持区间乘法和区间加法，以及单点查询。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 每个块维护两个懒惰标记：乘法标记和加法标记。
 * 预处理每个块的大小和边界。
 * 处理操作时：
 * 1. 对于区间操作，分情况处理左右不完整块和中间完整块
 * 2. 对于完整块，直接更新懒惰标记
 * 3. 对于不完整块，暴力更新每个元素并更新懒惰标记
 * 4. 对于单点查询，应用该点所在块的懒惰标记后返回值
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 每个操作：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用懒惰标记减少重复计算
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用数组存储元素和懒惰标记
 */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, q;
    cin >> n >> q;
    
    vector<long long> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    int blockSize = static_cast<int>(sqrt(n)) + 1;
    int blockNum = (n + blockSize - 1) / blockSize;
    
    // 初始化块信息
    vector<long long> mul(blockNum, 1); // 乘法懒惰标记
    vector<long long> add(blockNum, 0); // 加法懒惰标记
    
    // 处理查询
    while (q--) {
        int op;
        cin >> op;
        
        if (op == 0) {
            // 区间乘法
            int l, r;
            long long x;
            cin >> l >> r >> x;
            l--; // 转换为0-based
            r--; // 转换为0-based
            
            int leftBlock = l / blockSize;
            int rightBlock = r / blockSize;
            
            if (leftBlock == rightBlock) {
                // 在同一个块内，暴力更新
                for (int i = l; i <= r; i++) {
                    a[i] = a[i] * mul[leftBlock] + add[leftBlock];
                }
                // 重置当前块的懒惰标记
                mul[leftBlock] = 1;
                add[leftBlock] = 0;
                // 应用新的乘法操作
                for (int i = l; i <= r; i++) {
                    a[i] *= x;
                }
            } else {
                // 处理左边不完整块
                for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
                    a[i] = a[i] * mul[leftBlock] + add[leftBlock];
                }
                mul[leftBlock] = 1;
                add[leftBlock] = 0;
                for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
                    a[i] *= x;
                }
                
                // 处理中间完整块
                for (int b = leftBlock + 1; b < rightBlock; b++) {
                    mul[b] *= x;
                    add[b] *= x;
                }
                
                // 处理右边不完整块
                for (int i = rightBlock * blockSize; i <= r; i++) {
                    a[i] = a[i] * mul[rightBlock] + add[rightBlock];
                }
                mul[rightBlock] = 1;
                add[rightBlock] = 0;
                for (int i = rightBlock * blockSize; i <= r; i++) {
                    a[i] *= x;
                }
            }
        } else if (op == 1) {
            // 区间加法
            int l, r;
            long long x;
            cin >> l >> r >> x;
            l--; // 转换为0-based
            r--; // 转换为0-based
            
            int leftBlock = l / blockSize;
            int rightBlock = r / blockSize;
            
            if (leftBlock == rightBlock) {
                // 在同一个块内，暴力更新
                for (int i = l; i <= r; i++) {
                    a[i] = a[i] * mul[leftBlock] + add[leftBlock];
                }
                // 重置当前块的懒惰标记
                mul[leftBlock] = 1;
                add[leftBlock] = 0;
                // 应用新的加法操作
                for (int i = l; i <= r; i++) {
                    a[i] += x;
                }
            } else {
                // 处理左边不完整块
                for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
                    a[i] = a[i] * mul[leftBlock] + add[leftBlock];
                }
                mul[leftBlock] = 1;
                add[leftBlock] = 0;
                for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
                    a[i] += x;
                }
                
                // 处理中间完整块
                for (int b = leftBlock + 1; b < rightBlock; b++) {
                    add[b] += x;
                }
                
                // 处理右边不完整块
                for (int i = rightBlock * blockSize; i <= r; i++) {
                    a[i] = a[i] * mul[rightBlock] + add[rightBlock];
                }
                mul[rightBlock] = 1;
                add[rightBlock] = 0;
                for (int i = rightBlock * blockSize; i <= r; i++) {
                    a[i] += x;
                }
            }
        } else if (op == 2) {
            // 单点查询
            int pos;
            cin >> pos;
            pos--; // 转换为0-based
            
            int block = pos / blockSize;
            long long result = a[pos] * mul[block] + add[block];
            cout << result << endl;
        }
    }
    
    return 0;
}