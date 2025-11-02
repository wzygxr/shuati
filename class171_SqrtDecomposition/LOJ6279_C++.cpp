#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <string>
#include <sstream>
using namespace std;

/**
 * LOJ 6279. 数列分块入门 3 - C++实现
 * 
 * 题目描述：
 * 给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，询问区间内小于某个值 x 的前驱（比x小的最大元素）。
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 对于每个块维护一个加法标记和排序后的数组。
 * 区间加法操作时：
 * 1. 对于完整块，直接更新加法标记
 * 2. 对于不完整块，暴力更新元素值并重新排序
 * 查询操作时：
 * 1. 对于不完整块，暴力查找前驱
 * 2. 对于完整块，使用二分查找寻找前驱
 * 
 * 时间复杂度：
 * - 区间加法：O(√n * log√n)
 * - 查询操作：O(√n * log√n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性，处理没有前驱的情况
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：使用二分查找减少查询时间
 * 4. 鲁棒性：处理边界情况和特殊输入
 */

const int MAXN = 50010;

class LOJ6279 {
private:
    int arr[MAXN];           // 原数组
    int sorted[MAXN];        // 排序后的数组
    int belong[MAXN];        // 每个元素所属的块
    int lazy[MAXN];          // 每个块的加法标记
    int blockLeft[MAXN];     // 每个块的左边界
    int blockRight[MAXN];    // 每个块的右边界
    int blockSize;           // 块大小
    int blockNum;            // 块数量
    int n;                   // 数组大小
    
    /**
     * 重构指定块的排序数组
     * 
     * @param block 块号
     */
    void rebuild(int block) {
        int left = blockLeft[block];
        int right = blockRight[block];
        
        // 复制原数组到排序数组
        for (int i = left; i <= right; ++i) {
            sorted[i] = arr[i];
        }
        
        // 对块内元素排序
        sort(sorted + left, sorted + right + 1);
    }
    
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
        
        // 初始化加法标记
        for (int i = 1; i <= blockNum; ++i) {
            lazy[i] = 0;
        }
    }
    
    /**
     * 设置数组元素值
     * 
     * @param index 索引（从1开始）
     * @param value 值
     */
    void setValue(int index, int value) {
        arr[index] = value;
    }
    
    /**
     * 区间加法操作
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param val 要增加的值
     */
    void add(int l, int r, int val) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; ++i) {
                arr[i] += val;
            }
            // 重构排序数组
            rebuild(leftBlock);
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; ++i) {
                arr[i] += val;
            }
            rebuild(leftBlock);
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; ++i) {
                arr[i] += val;
            }
            rebuild(rightBlock);
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; ++i) {
                lazy[i] += val;
            }
        }
    }
    
    /**
     * 查询区间内小于x的前驱
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param x 查询值
     * @return 区间内小于x的最大元素，不存在则返回-1
     */
    int query(int l, int r, int x) {
        int leftBlock = belong[l];
        int rightBlock = belong[r];
        int result = -1;
        
        // 如果在同一个块内，暴力处理
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; ++i) {
                int actualValue = arr[i] + lazy[leftBlock];
                if (actualValue < x && actualValue > result) {
                    result = actualValue;
                }
            }
        } else {
            // 处理左边不完整块
            for (int i = l; i <= blockRight[leftBlock]; ++i) {
                int actualValue = arr[i] + lazy[leftBlock];
                if (actualValue < x && actualValue > result) {
                    result = actualValue;
                }
            }
            
            // 处理中间完整块
            for (int i = leftBlock + 1; i < rightBlock; ++i) {
                int currentBlockValue = x - lazy[i];
                // 在排序数组中二分查找前驱
                int left = blockLeft[i];
                int right = blockRight[i];
                int currentMax = -1;
                
                // 二分查找小于currentBlockValue的最大元素
                int low = left;
                int high = right;
                while (low <= high) {
                    int mid = (low + high) / 2;
                    if (sorted[mid] < currentBlockValue) {
                        currentMax = sorted[mid];
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
                
                if (currentMax != -1) {
                    currentMax += lazy[i];
                    if (currentMax > result) {
                        result = currentMax;
                    }
                }
            }
            
            // 处理右边不完整块
            for (int i = blockLeft[rightBlock]; i <= r; ++i) {
                int actualValue = arr[i] + lazy[rightBlock];
                if (actualValue < x && actualValue > result) {
                    result = actualValue;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 初始化所有块的排序数组
     */
    void initSortedArrays() {
        for (int i = 1; i <= blockNum; ++i) {
            rebuild(i);
        }
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    LOJ6279 solution;
    solution.init(n);
    
    // 读取初始数组
    for (int i = 1; i <= n; ++i) {
        int value;
        cin >> value;
        solution.setValue(i, value);
    }
    
    // 初始化排序数组
    solution.initSortedArrays();
    
    // 处理操作
    for (int i = 0; i < n; ++i) {
        int op, l, r, c;
        cin >> op >> l >> r >> c;
        
        if (op == 0) {
            // 区间加法
            solution.add(l, r, c);
        } else {
            // 查询区间内小于x的前驱
            cout << solution.query(l, r, c) << '\n';
        }
    }
    
    return 0;
}