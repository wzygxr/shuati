#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * LOJ 6284. 数列分块入门 8 - C++实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间乘法，区间加法，单点询问。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护乘法标记和加法标记，但不需要维护块内元素和，因为只需要单点查询。
 * 当进行区间乘法操作时：
 * 1. 更新块的乘法标记和加法标记
 * 当进行区间加法操作时：
 * 1. 更新块的加法标记
 * 单点查询时：
 * 1. 找到元素所在块
 * 2. 根据块的标记计算元素的实际值
 * 
 * 时间复杂度：
 * - 区间操作：O(√n)
 * - 单点查询：O(1)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：相比区间求和，单点查询更高效
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 取模操作：注意溢出问题
 */

const int MAXN = 100010;
const long long MOD = 10007;

class LOJ6284 {
private:
    long long arr[MAXN];     // 原数组
    int belong[MAXN];        // 每个元素所属的块
    long long mul[MAXN];     // 每个块的乘法标记
    long long add[MAXN];     // 每个块的加法标记
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
        this->n = size;
        // 设置块大小为sqrt(n)
        this->blockSize = sqrt(n);
        // 计算块数量
        this->blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化每个元素所属的块
        for (int i = 1; i <= n; i++) {
            belong[i] = (i - 1) / blockSize + 1;
        }
        
        // 初始化每个块的边界
        for (int i = 1; i <= blockNum; i++) {
            blockLeft[i] = (i - 1) * blockSize + 1;
            blockRight[i] = min(i * blockSize, n);
        }
        
        // 初始化标记，乘法标记初始化为1，加法标记初始化为0
        for (int i = 1; i <= blockNum; i++) {
            mul[i] = 1;
            add[i] = 0;
        }
    }
    
    /**
     * 向下传递标记（将块的标记应用到每个元素）
     * 
     * @param block 块号
     */
    void pushDown(int block) {
        // 如果该块有标记
        if (mul[block] != 1 || add[block] != 0) {
            // 对块内所有元素应用标记
            for (int i = blockLeft[block]; i <= blockRight[block]; i++) {
                arr[i] = (arr[i] * mul[block] + add[block]) % MOD;
                // 确保结果为非负数
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            // 重置标记
            mul[block] = 1;
            add[block] = 0;
        }
    }
    
    /**
     * 区间乘法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要乘的值
     */
    void multiply(int l, int r, long long val) {
        val %= MOD;
        // 确保val为正数
        if (val < 0) {
            val += MOD;
        }
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 下传标记
            pushDown(leftBlock);
            // 更新元素值
            for (int i = l; i <= r; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
        } else {
            // 处理左边不完整块
            pushDown(leftBlock);
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            
            // 处理右边不完整块
            pushDown(rightBlock);
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] = (arr[i] * val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                // 更新乘法标记
                mul[i] = (mul[i] * val) % MOD;
                if (mul[i] < 0) {
                    mul[i] += MOD;
                }
                // 更新加法标记
                add[i] = (add[i] * val) % MOD;
                if (add[i] < 0) {
                    add[i] += MOD;
                }
            }
        }
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要加的值
     */
    void add(int l, int r, long long val) {
        val %= MOD;
        // 确保val为正数
        if (val < 0) {
            val += MOD;
        }
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            // 下传标记
            pushDown(leftBlock);
            // 更新元素值
            for (int i = l; i <= r; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
        } else {
            // 处理左边不完整块
            pushDown(leftBlock);
            for (int i = l; i <= blockRight[leftBlock]; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            
            // 处理右边不完整块
            pushDown(rightBlock);
            for (int i = blockLeft[rightBlock]; i <= r; i++) {
                arr[i] = (arr[i] + val) % MOD;
                if (arr[i] < 0) {
                    arr[i] += MOD;
                }
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                // 更新加法标记
                add[i] = (add[i] + val) % MOD;
                if (add[i] < 0) {
                    add[i] += MOD;
                }
            }
        }
    }
    
    /**
     * 单点查询操作
     * 
     * @param pos 查询位置
     * @return 查询结果
     */
    long long query(int pos) {
        int block = belong[pos];
        // 计算实际值：原数组值 * 乘法标记 + 加法标记
        long long result = (arr[pos] * mul[block] + add[block]) % MOD;
        // 确保结果为非负数
        if (result < 0) {
            result += MOD;
        }
        return result;
    }
    
    /**
     * 设置数组元素值
     * 
     * @param index 索引（从1开始）
     * @param value 值
     */
    void setValue(int index, long long value) {
        arr[index] = value % MOD;
        // 确保值为非负数
        if (arr[index] < 0) {
            arr[index] += MOD;
        }
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    LOJ6284 solution;
    solution.init(n);
    
    // 读取初始数组
    for (int i = 1; i <= n; i++) {
        long long value;
        cin >> value;
        solution.setValue(i, value);
    }
    
    // 处理操作
    for (int i = 0; i < n; i++) {
        int op, l, r;
        long long c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 区间乘法
            solution.multiply(l, r, c);
        } else if (op == 1) {
            // 区间加法
            solution.add(l, r, c);
        } else {
            // 单点查询
            cout << solution.query(r) % MOD << '\n';
        }
    }
    
    return 0;
}