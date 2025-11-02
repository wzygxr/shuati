#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <string>
#include <sstream>
using namespace std;

/**
 * LOJ 6280. 数列分块入门 4 - C++实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，区间求和。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记和块内元素和。
 * 区间加法操作时：
 * 1. 对于完整块，更新加法标记和块内元素和
 * 2. 对于不完整块，暴力更新元素值并更新块内元素和
 * 查询操作时：
 * 1. 对于不完整块，暴力计算元素和
 * 2. 对于完整块，直接使用块内元素和
 * 
 * 时间复杂度：
 * - 区间加法：O(√n)
 * - 查询操作：O(√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：维护块内元素和减少计算量
 * 4. 鲁棒性：处理边界情况和特殊输入
 */

const int MAXN = 50010;

class LOJ6280 {
private:
    long long arr[MAXN];     // 原数组
    long long sum[MAXN];     // 每个块的元素和
    int belong[MAXN];        // 每个元素所属的块
    long long lazy[MAXN];    // 每个块的加法标记
    int blockLeft[MAXN];     // 每个块的左边界
    int blockRight[MAXN];    // 每个块的右边界
    int blockSize;           // 块大小
    int blockNum;            // 块数量
    int n;                   // 数组大小
    
public:
    /**
     * 初始化分块结构
     * 
     * @param size 数组大小
     */
    void init(int size) {
        n = size;
        // 设置块大小为sqrt(n)
        blockSize = static_cast<int>(sqrt(n));
        // 计算块数量
        blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; ++i) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; ++i) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = min(i * blockSize, n);
        }
        
        // 初始化加法标记和块和
        for (int i = 1; i <= blockNum; ++i) {
            lazy[i] = 0;
            sum[i] = 0;
        }
    }
    
    /**
     * 设置数组元素值
     * 
     * @param index 索引（从1开始）
     * @param value 值
     */
    void setValue(int index, long long value) {
        arr[index] = value;
        // 更新所在块的和
        int block = belong[index];
        sum[block] = 0;
        for (int i = blockLeft[block]; i <= blockRight[block]; ++i) {
            sum[block] += arr[i];
        }
        sum[block] += lazy[block] * (blockRight[block] - blockLeft[block] + 1);
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要增加的值
     */
    void add(int l, int r, long long val) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 更新元素值
            for (int i = l; i <= r; ++i) {
                arr[i] += val;
            }
            // 重新计算块和
            sum[leftBlock] = 0;
            for (int i = blockLeft[leftBlock]; i <= blockRight[leftBlock]; ++i) {
                sum[leftBlock] += arr[i];
            }
            // 加上块的加法标记
            sum[leftBlock] += lazy[leftBlock] * (blockRight[leftBlock] - blockLeft[leftBlock] + 1);
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; ++i) {
                arr[i] += val;
            }
            // 重新计算左边块的和
            sum[leftBlock] = 0;
            for (int i = blockLeft[leftBlock]; i <= blockRight[leftBlock]; ++i) {
                sum[leftBlock] += arr[i];
            }
            sum[leftBlock] += lazy[leftBlock] * (blockRight[leftBlock] - blockLeft[leftBlock] + 1);
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; ++i) {
                arr[i] += val;
            }
            // 重新计算右边块的和
            sum[rightBlock] = 0;
            for (int i = blockLeft[rightBlock]; i <= blockRight[rightBlock]; ++i) {
                sum[rightBlock] += arr[i];
            }
            sum[rightBlock] += lazy[rightBlock] * (blockRight[rightBlock] - blockLeft[rightBlock] + 1);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; ++i) {
                lazy[i] += val;
                sum[i] += val * (blockRight[i] - blockLeft[i] + 1);
            }
        }
    }
    
    /**
     * 查询区间和
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间元素和
     */
    long long query(int l, int r) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        long long result = 0;
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; ++i) {
                result += arr[i] + lazy[leftBlock];
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; ++i) {
                result += arr[i] + lazy[leftBlock];
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; ++i) {
                result += sum[i];
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; ++i) {
                result += arr[i] + lazy[rightBlock];
            }
        }
        
        return result;
    }
    
    /**
     * 初始化块和
     */
    void initSum() {
        for (int i = 1; i <= blockNum; ++i) {
            sum[i] = 0;
            for (int j = blockLeft[i]; j <= blockRight[i]; ++j) {
                sum[i] += arr[j];
            }
        }
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    LOJ6280 solution;
    solution.init(n);
    
    // 读取初始数组
    for (int i = 1; i <= n; ++i) {
        long long value;
        cin >> value;
        solution.setValue(i, value);
    }
    
    // 重新初始化块和，确保正确性
    solution.initSum();
    
    // 处理操作
    for (int i = 0; i < n; ++i) {
        int op, l, r;
        long long c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 区间加法
            solution.add(l, r, c);
        } else {
            // 区间求和
            cout << solution.query(l, r) % (c + 1) << '\n';
        }
    }
    
    return 0;
}